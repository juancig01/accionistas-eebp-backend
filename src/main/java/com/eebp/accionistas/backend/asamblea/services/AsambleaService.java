package com.eebp.accionistas.backend.asamblea.services;

import com.eebp.accionistas.backend.acciones.entities.Titulo;
import com.eebp.accionistas.backend.acciones.entities.TitulosPersona;
import com.eebp.accionistas.backend.accionistas.entities.Persona;
import com.eebp.accionistas.backend.accionistas.repositories.PersonaRepository;
import com.eebp.accionistas.backend.accionistas.services.AccionistaService;
import com.eebp.accionistas.backend.accionistas.services.PersonaService;
import com.eebp.accionistas.backend.asamblea.entities.Asamblea;
import com.eebp.accionistas.backend.asamblea.repositories.AsambleaRepository;
import com.eebp.accionistas.backend.financiero.services.UtilidadService;
import com.eebp.accionistas.backend.geo.MunicipioRepository;
import com.eebp.accionistas.backend.seguridad.entities.Asset;
import com.eebp.accionistas.backend.seguridad.entities.EmailDetails;
import com.eebp.accionistas.backend.seguridad.services.EmailServiceImpl;
import com.eebp.accionistas.backend.seguridad.utils.FileUploadUtil;
import com.openhtmltopdf.outputdevice.helper.BaseRendererBuilder;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AsambleaService {

    @Autowired
    AsambleaRepository asambleaRepository;
    @Autowired
    PersonaRepository personaRepository;
    @Autowired
    AccionistaService accionistaService;
    @Autowired
    PersonaService personaService;
    @Autowired
    MunicipioRepository municipioRepository;

   /* @Autowired
    UtilidadService utilidadService;*/

    @Autowired
    private EmailServiceImpl emailService;

    public Asamblea addAsamblea(Asamblea asamblea) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate fechaAsamblea = LocalDate.parse(asamblea.getFechaAsamblea(), formatter);
        String currentYear = String.valueOf(fechaAsamblea.getYear());

        if ("ORDINARIA".equals(asamblea.getTipoAsamblea())) {
            Optional<Asamblea> existingAsamblea = asambleaRepository.findByTipoAsambleaAndYear("ORDINARIA", currentYear);
            if (existingAsamblea.isPresent()) {
                throw new IllegalArgumentException("Ya existe una asamblea de tipo ORDINARIA en el año " + currentYear);
            }
        }
        asamblea.setEstado("ACTIVA");
        return asambleaRepository.save(asamblea);
    }

    public List<Asamblea> getAsambleas() {
        List<Asamblea> asambleas = asambleaRepository.findAll();
        for (Asamblea asamblea : asambleas) {
            asamblea.setFiles(getFilesAsamblea(asamblea.getConsecutivo()));
        }
        Collections.reverse(asambleas);
        return asambleas;
    }

    public Asamblea updateAsamblea(Asamblea asamblea) {
        return asambleaRepository.save(asamblea);
    }

    public Optional<Asamblea> findAsambleaById(Integer id) {
        return asambleaRepository.findById(id);
    }

    public Integer getConsecutivoAsamblea() {
        List<Asamblea> asambleas = asambleaRepository.findAll();

        if (!asambleas.isEmpty()) {
            Asamblea ultimaAsamblea = asambleas.get(asambleas.size() - 1);
            return ultimaAsamblea.getConsecutivo();
        } else {
            return null;
        }
    }

    public Integer getUltimoConsecutivoAsamblea() {
        List<Asamblea> asambleas = asambleaRepository.findAll();

        if (!asambleas.isEmpty()) {
            Asamblea ultimaAsamblea = asambleas.get(asambleas.size() - 1);
            return ultimaAsamblea.getConsecutivo() + 1;
        } else {
            return null;
        }
    }

    public List<Asset> getFilesAsamblea(@PathVariable Integer consecutivo) {
        return FileUploadUtil.files(String.valueOf(consecutivo), "asamblea").stream().map(file -> {
            file.setUrl("/assets/images/avatars/" + file.getFileName());
            return file;
        }).collect(Collectors.toList());
    }

    public Map<String, List<Asset>> getReportesAsamblea(@PathVariable Integer consecutivo) {
        List<Asset> files = FileUploadUtil.files(String.valueOf(consecutivo), "asamblea").stream().map(file -> {
            file.setUrl("/assets/images/avatars/" + file.getFileName());
            return file;
        }).collect(Collectors.toList());

        Map<String, List<Asset>> result = new LinkedHashMap<>();
        result.put("actaCierrePostulaciones", new LinkedList<>());
        result.put("actaReforma", new LinkedList<>());
        result.put("actaRevisoriaFiscal", new LinkedList<>());
        result.put("actaPoderes", new LinkedList<>());
        result.put("actaEscrutinio", new LinkedList<>());
        result.put("otroAnexo", new LinkedList<>());

        for (Asset file : files) {
            String fileName = file.getFileName();
            if (fileName.contains("actaCierrePostulaciones")) {
                result.get("actaCierrePostulaciones").add(file);
            } else if (fileName.contains("actaReforma")) {
                result.get("actaReforma").add(file);
            } else if (fileName.contains("actaRevisoriaFiscal")) {
                result.get("actaRevisoriaFiscal").add(file);
            } else if (fileName.contains("actaPoderes")) {
                result.get("actaPoderes").add(file);
            } else if (fileName.contains("actaEscrutinio")) {
                result.get("actaEscrutinio").add(file);
            } else {
                result.get("otroAnexo").add(file);
            }
        }

        return result;
    }

    public Asamblea sendEmailAccionistas(Integer id) {
        Optional<Asamblea> asamblea = asambleaRepository.findById(id);

        if (asamblea.isPresent()) {
            List<Persona> personas = personaService.getPersonas();

            // Filtrar solo los accionistas activos
            List<Persona> accionistasActivos = personas.stream()
                    .filter(a -> "S".equals(a.getEsAccionista()))
                    .collect(Collectors.toList());

            System.out.println("Total de accionistas activos: " + accionistasActivos.size());

            int batchSize = 50; // tamaño del bloque

            // Dividir la lista en lotes de 50
            for (int i = 0; i < accionistasActivos.size(); i += batchSize) {
                List<Persona> lote = accionistasActivos.subList(i, Math.min(i + batchSize, accionistasActivos.size()));

                String[] correosLote = lote.stream()
                        .map(Persona::getCorreoPersona)
                        .filter(Objects::nonNull)
                        .toArray(String[]::new);

                System.out.println("Enviando correo a los siguientes destinatarios: " + Arrays.toString(correosLote));

                // Construir el cuerpo del correo
                String cuerpoHtml = "<table border=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse; height:147px; width:600px\">\n" +
                        "\t<tbody>\n" +
                        "\t\t<tr>\n" +
                        "\t\t\t<td style=\"height:91px; text-align:center; width:23.5796%\"><img src=\"https://eebpsa.com.co/wp-content/uploads/2020/08/lOGO-2.1.png\" /></td>\n" +
                        "\t\t\t<td style=\"height:91px; width:67.4766%\">\n" +
                        "\t\t\t<h3 style=\"text-align:center\"><strong>BIENVENIDO AL SISTEMA DE ACCIONISTAS </strong></h3>\n" +
                        "\t\t\t<h3 style=\"text-align:center\"><strong>Empresa de Energ&iacute;a del Bajo Putumayo S.A. E.S.P.</strong></h3>\n" +
                        "\t\t\t</td>\n" +
                        "\t\t</tr>\n" +
                        "\t\t<tr>\n" +
                        "\t\t\t<td colspan=\"2\" style=\"height:10px; text-align:center; width:91.0562%\">\n" +
                        "\t\t\t<p>&nbsp;</p>\n" +
                        "\t\t\t<p style=\"text-align:left\">Queremos informarle que se programó una asamblea de tipo " + asamblea.get().getTipoAsamblea() + ", la cual se realizará el día " + asamblea.get().getFechaAsamblea() + " a las " + asamblea.get().getHoraAsamblea() + ".</p>\n" +
                        "\t\t\t</td>\n" +
                        "\t\t</tr>\n" +
                        "\t\t<tr>\n" +
                        "\t\t\t<td colspan=\"2\" style=\"text-align:center; width:91.0562%\">\n" +
                        "\t\t\t<p style=\"text-align:left\"><u>En caso de alguna duda, favor contactarse con servicio al cliente.</u></p>\n" +
                        "\t\t\t<p style=\"text-align:left\">Acceso al sistema: <a href=\"http://localhost:4200\">http://localhost:4200</a></p>\n" +
                        "\t\t\t</td>\n" +
                        "\t\t</tr>\n" +
                        "\t</tbody>\n" +
                        "</table>";

                EmailDetails emailDetails = EmailDetails.builder()
                        .recipient(String.join(", ", correosLote))
                        .subject("Asamblea " + asamblea.get().getTipoAsamblea())
                        .msgBody(cuerpoHtml)
                        .build();

                // Enviar el correo a este bloque
                emailService.sendSimpleMailArray(emailDetails, correosLote);

                // Esperar 2 segundos entre lotes (opcional, ayuda a no saturar Gmail)
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            return asamblea.get();
        }

        return null;
    }


    public Map<String, List<Asset>> getFormatosActas() {
        Integer consecutivo = 0;
        List<Asset> files = FileUploadUtil.files(String.valueOf(consecutivo), "formatoActa").stream()
                .map(file -> {
                    file.setUrl("/assets/images/avatars/" + file.getFileName());
                    return file;
                })
                .collect(Collectors.toList());
        Map<String, List<Asset>> result = new LinkedHashMap<>();
        result.put("actaCierrePostulacionesJuntaDirectiva", new ArrayList<>());
        result.put("actaCierreReformaEstatutos", new ArrayList<>());
        result.put("actaCierreRevisorFiscal", new ArrayList<>());
        result.put("actaPoderes", new ArrayList<>());
        result.put("actaEscutinio", new ArrayList<>());

        for (Asset file : files) {
            String fileName = file.getFileName();
            if (fileName.contains("actaCierrePostulacionesJuntaDirectiva")) {
                result.get("actaCierrePostulacionesJuntaDirectiva").add(file);
            } else if (fileName.contains("actaCierreReformaEstatutos")) {
                result.get("actaCierreReformaEstatutos").add(file);
            } else if (fileName.contains("actaCierreRevisorFiscal")) {
                result.get("actaCierreRevisorFiscal").add(file);
            } else if (fileName.contains("actaPoderes")) {
                result.get("actaPoderes").add(file);
            } else if (fileName.contains("actaEscutinio")) {
                result.get("actaEscutinio").add(file);
            }
        }

        return result;
    }

    public byte[] getCertificado(Integer codUsuario) throws IOException {
        // Buscar el título del cual sobraron las acciones

        Optional<Persona> datosPersona = personaRepository.findById(String.valueOf(codUsuario));

        File inputHTML = new File("src/main/resources/certificadoAccionesDeAnio.html"); //quitar la ruta cunado subna a server va sin backend/
        Document document = Jsoup.parse(inputHTML, "UTF-8");

        //document.selectFirst("#codUsuario").text(datosPersona.getCodUsuario());
        document.selectFirst("#nomAccionista").text(
                datosPersona.get().getNomPri().toUpperCase() + " " +
                        datosPersona.get().getNomSeg().toUpperCase() + " " +
                        datosPersona.get().getApePri().toUpperCase() + " " +
                        datosPersona.get().getApeSeg().toUpperCase());
        document.selectFirst("#codUsuario").text(datosPersona.get().getCodUsuario());

        if(datosPersona.get().getMunicipioExp() != null) {
            document.selectFirst("#munExpedicion").text(municipioRepository.findById(Integer.parseInt(datosPersona.get().getMunicipioExp())).get().getNombreMunicipio().toUpperCase());
        }

        document.selectFirst("#numAcciones").text(String.valueOf(Integer.valueOf(String.valueOf(asambleaRepository.totalAccionesPorPersona(Integer.valueOf(String.valueOf((datosPersona.get().getCodUsuario()))))))));

        //document.selectFirst("#valorNominal").text(String.valueOf(utilidadService.getUltimaUtilidad().getValNomAccion()));

        document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PdfRendererBuilder builder = new PdfRendererBuilder();
        builder.useDefaultPageSize(Float.valueOf(210), Float.valueOf(297), BaseRendererBuilder.PageSizeUnits.MM);
        builder.toStream(os);
        builder.withW3cDocument(new W3CDom().fromJsoup(document), "/");
        builder.run();
        return os.toByteArray();
    }
}
