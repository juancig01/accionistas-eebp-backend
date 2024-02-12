package com.eebp.accionistas.backend.accionistas.services;

import com.eebp.accionistas.backend.accionistas.entities.Accionista;
import com.eebp.accionistas.backend.accionistas.entities.LogRegistroAccionistas;
import com.eebp.accionistas.backend.accionistas.entities.Persona;
import com.eebp.accionistas.backend.accionistas.entities.response.AccionistaRepresentanteResponse;
import com.eebp.accionistas.backend.accionistas.repositories.AccionistaRepository;
import com.eebp.accionistas.backend.accionistas.repositories.PersonaRepository;
import com.eebp.accionistas.backend.geo.MunicipioRepository;
import com.eebp.accionistas.backend.seguridad.exceptions.UserNotFoundException;
import com.openhtmltopdf.outputdevice.helper.BaseRendererBuilder;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonaService {

    @Autowired
    PersonaRepository personaRepository;

    @Autowired
    MunicipioRepository municipioRepository;

    @Autowired
    LogRegistroAccionistaService logRegistroAccionistaService;

    @Autowired
    AccionistaRepository accionistaRepository;

    public List<Persona> getPersonas() {
        return personaRepository.findAll().stream().map(persona -> {
            if(accionistaRepository.findById(persona.getCodUsuario()).isPresent()) {
                persona.setEsAccionista(accionistaRepository.findById(persona.getCodUsuario()).get().getAprobado());
            } else {
                persona.setEsAccionista("N");
            }
            return persona;
        }).collect(Collectors.toList());
    }

    public Persona addPersona(Persona persona) {
        logRegistroAccionistaService.add(LogRegistroAccionistas.builder()
                .codUsuario(persona.getCodUsuario())
                .tipo("AGREGAR")
                .accion(persona.getNomPri() + " " + persona.getNomSeg() + " " + persona.getApePri() +  " " + persona.getApeSeg()  + " se agregó exitosamente como PERSONA en el sistema.")
                .fecha(LocalDateTime.now())
                .build());
        return personaRepository.save(persona);
    }

    public Optional<Persona> getPersona(String codUsuario) throws UserNotFoundException {
        Optional<Persona> response = personaRepository.findById(codUsuario);
        if (response.isPresent()) {
            if(accionistaRepository.findById(codUsuario).isPresent()) {
                response.get().setEsAccionista("S");
            } else {
                response.get().setEsAccionista("N");
            }
            return response;
        } else {
            throw new UserNotFoundException();
        }
    }

    public Persona addDeclaracionPersona(Persona persona) {
        Persona per = personaRepository.findById(persona.getCodUsuario()).get();
        per.setIngresos(persona.getIngresos());
        per.setRecursos(persona.getRecursos());
        return personaRepository.save(per);
    }

    public Persona addAutorizacionPersona(Persona persona) {
        Persona per = personaRepository.findById(persona.getCodUsuario()).get();
        per.setNumSuscripcion(persona.getNumSuscripcion());
        per.setNumPersonas(persona.getNumPersonas());
        per.setAutorizaCorreo(persona.getAutorizaCorreo());
        per.setAutorizaLlamada(persona.getAutorizaLlamada());
        per.setAutorizaMensaje(persona.getAutorizaMensaje());
        per.setAutorizaFisico(persona.getAutorizaFisico());
        per.setAutorizaTodas(persona.getAutorizaTodas());
        per.setTipoVivienda(persona.getTipoVivienda());
        return personaRepository.save(per);
    }

    public byte[] getPDFDatosPersonales(String codUsuario) throws IOException {
        Persona datosPersona = personaRepository.findById(codUsuario).get();

        File inputHTML = new File("src/main/resources/registro.html");
        Document document = Jsoup.parse(inputHTML, "UTF-8");

        if (datosPersona.getNomPri() != null && !datosPersona.getNomPri().equalsIgnoreCase("")) {
            document.selectFirst("#nombre").text(
                    datosPersona.getNomPri().toUpperCase() + " " +
                            datosPersona.getNomSeg().toUpperCase() + " " +
                            datosPersona.getApePri().toUpperCase() + " " +
                            datosPersona.getApeSeg().toUpperCase());
        } else {
            document.selectFirst("#nombre").text(datosPersona.getRazonSocial().toUpperCase());
        }
        document.selectFirst("#" + datosPersona.getTipDocumento()).text("X");
        document.selectFirst("#codUsuario").text(datosPersona.getCodUsuario());
        if(datosPersona.getMunicipioExp() != null) {
            document.selectFirst("#municipioExp").text(municipioRepository.findById(Integer.parseInt(datosPersona.getMunicipioExp())).get().getNombreMunicipio().toUpperCase());
            //document.selectFirst("#departamento").text(municipioRepository.findById(Integer.parseInt(datosPersona.getMunicipioExp())).get().getDepartamento().getNombreDepartamento().toUpperCase());
            document.selectFirst("#fecNacimiento").text(datosPersona.getFecNacimiento().split("T")[0]);
            document.selectFirst("#lugNacimiento").text(municipioRepository.findById(Integer.parseInt(datosPersona.getLugNacimiento())).get().getNombreMunicipio().toUpperCase());

            if (datosPersona.getGenPersona().equalsIgnoreCase("M")) {
                document.selectFirst("#genPersonaM").text("X");
            } else {
                document.selectFirst("#genPersonaM").text("X");
            }
        }

        //document.selectFirst("#estCivPersona").text(datosPersona.getEstCivPersona().toUpperCase());
        document.selectFirst("#celPersona").text(datosPersona.getCelPersona());
        //document.selectFirst("#profPersona").text(datosPersona.getProfPersona().toUpperCase());
        if(datosPersona.getActEcoPersona() != null) {
            //document.selectFirst("#actEcoPersona").text(datosPersona.getActEcoPersona().toUpperCase());
        }
        document.selectFirst("#correoPersona").text(datosPersona.getCorreoPersona().toUpperCase());
        document.selectFirst("#dirDomicilio").text(datosPersona.getDirDomicilio().toUpperCase());
        document.selectFirst("#munDomicilio").text(municipioRepository.findById(Integer.parseInt(datosPersona.getMunicipioDomicilio())).get().getNombreMunicipio().toUpperCase());
        document.selectFirst("#departamentoDomicilio").text(municipioRepository.findById(Integer.parseInt(datosPersona.getMunicipioDomicilio())).get().getDepartamento().getNombreDepartamento().toUpperCase());
        document.selectFirst("#paisDomicilio").text(datosPersona.getPaisDomicilio().toUpperCase());
        document.selectFirst("#telfDomicilio").text(datosPersona.getTelfDomicilio());
        //document.selectFirst("#indDomicilio").text(datosPersona.getIndTelDomicilio());
        if (!datosPersona.getTipDocumento().equalsIgnoreCase("TI")) {

            document.selectFirst("#dirLaboral").text(datosPersona.getDirLaboral());
            if(!datosPersona.getMunicipioLaboral().isEmpty()) {
                document.selectFirst("#munLaboral").text(municipioRepository.findById(Integer.parseInt(datosPersona.getMunicipioLaboral())).get().getNombreMunicipio().toUpperCase());
                document.selectFirst("#depLaboral").text(municipioRepository.findById(Integer.parseInt(datosPersona.getMunicipioLaboral())).get().getDepartamento().getNombreDepartamento().toUpperCase());
            }

            document.selectFirst("#paisLaboral").text(datosPersona.getPaisLaboral());
            document.selectFirst("#telLaboral").text(datosPersona.getTelfLaboral());
        }

        if(datosPersona.getDirCorrespondencia() != null) {
            if (datosPersona.getDirCorrespondencia().equalsIgnoreCase("laboral")) {
                document.selectFirst("#dirCorrespondenciaLaboral").text("X");
            }
            if (datosPersona.getDirCorrespondencia().equalsIgnoreCase("domicilio")) {
                document.selectFirst("#dirCorrespondenciaDomicilio").text("X");
            }
            if (datosPersona.getDirCorrespondencia().equalsIgnoreCase("otra")) {
                //document.selectFirst("#dirCorrespondenciaOtra").text("X");
                //document.selectFirst("#cualDirCorrespondencia").text(datosPersona.getOtraDirLaboral().toUpperCase());
            }
        }

        if (datosPersona.getTipDocumento().equalsIgnoreCase("TI")) {
            /*if (datosPersona.getOpcPotestad().equalsIgnoreCase("S")) {
                document.selectFirst("#opcPotestadSi").text("X");
            } else {
                document.selectFirst("#opcPotestadNo").text("X");
            }
            document.selectFirst("#nomRepresentante").text(datosPersona.getNomRepresentante().toUpperCase());
            document.selectFirst("#tipoDocRepresentante" + datosPersona.getTipoDocRepresentante()).text("X");
            document.selectFirst("#codRepresentante").text(datosPersona.getCodRepresentante());
            document.selectFirst("#munRepresentante").text(municipioRepository.findById(Integer.parseInt(datosPersona.getMunicipioRepresentante())).get().getNombreMunicipio().toUpperCase());
            document.selectFirst("#depRepresentante").text(municipioRepository.findById(Integer.parseInt(datosPersona.getMunicipioRepresentante())).get().getDepartamento().getNombreDepartamento().toUpperCase());
            document.selectFirst("#fecNacRepresentante").text(datosPersona.getFecNacRepresentante().split("T")[0]);
            document.selectFirst("#lugNacRepresentante").text(municipioRepository.findById(Integer.parseInt(datosPersona.getLugNacRepresentante())).get().getNombreMunicipio().toUpperCase());
            if (datosPersona.getGenRepresentante().equalsIgnoreCase("M")) {
                document.selectFirst("#genRepresentanteM").text("X");
            } else {
                document.selectFirst("#genRepresentanteF").text("X");
            }
            document.selectFirst("#estCivRepresentante").text(datosPersona.getEstCivPersona().toUpperCase());
            document.selectFirst("#numCelRepresentante").text(datosPersona.getCelRepresentante());
            document.selectFirst("#actEcoRepresentante").text(datosPersona.getProfActRepresentante().toUpperCase());
            document.selectFirst("#correoRepresentante").text(datosPersona.getCorreoRepresentante().toUpperCase());*/
        }

        document.selectFirst("#firma").html("<img width=\"150\" src=\"data:image/png;base64, " + "<img width=\"150\" src=\"data:image/png;base64, " + Base64.getEncoder().encodeToString(datosPersona.getFirma()) + "\">");

        try {
            byte[] bytes = datosPersona.getHuella();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
            BufferedImage originalImage = ImageIO.read(inputStream);
            BufferedImage rotatedImage = rotateImage(originalImage, 270);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(rotatedImage, "jpg", outputStream);
            byte[] rotatedBytes = outputStream.toByteArray();
            String imagenRotada = Base64.getEncoder().encodeToString(rotatedBytes);

            document.selectFirst("#huella").html("<img style=\"border: 1px black solid;\" width=\"70\" src=\"data:image/png;base64, " + imagenRotada + "\">");
        } catch (Exception e) {

        }

        document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PdfRendererBuilder builder = new PdfRendererBuilder();
        builder.useDefaultPageSize(Float.valueOf(210), Float.valueOf(297), BaseRendererBuilder.PageSizeUnits.MM);
        builder.toStream(os);
        builder.withW3cDocument(new W3CDom().fromJsoup(document), "/");
        builder.run();
        return os.toByteArray();
    }

    public byte[] getPDFAutorizacion(String codUsuario) throws IOException {
        Persona datosPersona = personaRepository.findById(codUsuario).get();

        File inputHTML = new File("src/main/resources/pdfAutorizacion.html");
        Document document = Jsoup.parse(inputHTML, "UTF-8");

        document.selectFirst("#codUsuario").text(datosPersona.getCodUsuario());

        if (datosPersona.getNomPri() != null && !datosPersona.getNomPri().equalsIgnoreCase("")) {
            document.selectFirst("#nombre").text(
                    datosPersona.getNomPri().toUpperCase() + " " +
                            datosPersona.getNomSeg().toUpperCase() + " " +
                            datosPersona.getApePri().toUpperCase() + " " +
                            datosPersona.getApeSeg().toUpperCase());
        } else {
            document.selectFirst("#nombre").text(datosPersona.getRazonSocial().toUpperCase());
        }

        document.selectFirst("#dirDomicilio").text(datosPersona.getDirDomicilio());
        document.selectFirst("#correoPersona").text(datosPersona.getCorreoPersona());
        document.selectFirst("#celPersona").text(datosPersona.getCelPersona());
        document.selectFirst("#telDomicilio").text(datosPersona.getTelfDomicilio());

        if (datosPersona.getTipoVivienda().equalsIgnoreCase("Propia")) {
            document.selectFirst("#tipoViviendaPropia").text("x");
        } else {
            document.selectFirst("#tipoViviendaArrendada").text("x");
        }

        document.selectFirst("#numPersonas").text(datosPersona.getNumPersonas().toString());

        if (datosPersona.getAutorizaCorreo()) {
            document.selectFirst("#autorizaCorreoSi").text("x");
        } else {
            document.selectFirst("#autorizaCorreoNo").text("x");
        }

        if (datosPersona.getAutorizaMensaje()) {
            document.selectFirst("#autorizaMensajeSi").text("x");
        } else {
            document.selectFirst("#autorizaMensajeNo").text("x");
        }

        if (datosPersona.getAutorizaFisico()) {
            document.selectFirst("#autorizaFisicoSi").text("x");
        } else {
            document.selectFirst("#autorizaFisicoNo").text("x");
        }

        if (datosPersona.getAutorizaLlamada()) {
            document.selectFirst("#autorizaLlamadaSi").text("x");
        } else {
            document.selectFirst("#autorizaLlamadaNo").text("x");
        }

        if (datosPersona.getAutorizaTodas()) {
            document.selectFirst("#autorizaTodasSi").text("x");
        } else {
            document.selectFirst("#autorizaTodasNo").text("x");
        }
        document.selectFirst("#firma").html("<img width=\"150\" src=\"data:image/png;base64, " + "<img width=\"150\" src=\"data:image/png;base64, " + Base64.getEncoder().encodeToString(datosPersona.getFirma()) + "\">");
        document.selectFirst("#identificacion").text(datosPersona.getCodUsuario());
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        document.selectFirst("#fecha").text("Fecha                                 " + formatter.format(date));

        document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PdfRendererBuilder builder = new PdfRendererBuilder();
        builder.useDefaultPageSize(Float.valueOf(210), Float.valueOf(297), BaseRendererBuilder.PageSizeUnits.MM);
        builder.toStream(os);
        builder.withW3cDocument(new W3CDom().fromJsoup(document), "/");
        builder.run();
        return os.toByteArray();
    }

    public byte[] getPDFDeclaracion(String codUsuario) throws IOException, UserNotFoundException {
        Persona datosPersona = personaRepository.findById(codUsuario).get();
        File inputHTML = new File("src/main/resources/pdfDeclaracion.html");
        Document document = Jsoup.parse(inputHTML, "UTF-8");

        if (datosPersona.getNomPri() != null && !datosPersona.getNomPri().equalsIgnoreCase("")) {


            Accionista accionista = accionistaRepository.findById(codUsuario).get();
            Persona pRepresentante;
            if (accionista.getCodRepresentante() == null) {
                pRepresentante = getPersona(accionista.getCodUsuario()).get();
            } else {
                pRepresentante = getPersona(accionista.getCodRepresentante()).get();
            }
            Persona pAccionista = getPersona(accionista.getCodUsuario()).get();

            AccionistaRepresentanteResponse accionistaRepresentante = AccionistaRepresentanteResponse.builder()
                    .nomAccionista(pAccionista.getNomPri() + " " + pAccionista.getNomSeg() + " " + pAccionista.getApePri() +  " " + pAccionista.getApeSeg())
                    .nomRepresentante(pRepresentante.getNomPri() + " " + pRepresentante.getNomSeg() + " " + pRepresentante.getApePri() +  " " + pRepresentante.getApeSeg())
                    .codAccionista(accionista.getCodUsuario())
                    .codRepresentante(pRepresentante.getCodUsuario())
                    .esAccionista("S")
                    .tipoDocAccionista(pAccionista.getTipDocumento())
                    .tipoDocRepresentante(pRepresentante.getTipDocumento())
                    .build();

            document.selectFirst("#nombre").text(accionistaRepresentante.getNomRepresentante());

            document.selectFirst("#representacion").text(accionistaRepresentante.getNomAccionista());
            document.selectFirst("#codUsuario").text(accionistaRepresentante.getCodRepresentante());
            document.selectFirst("#nit").text(accionistaRepresentante.getCodAccionista());

        } else {
            document.selectFirst("#nombre").text(datosPersona.getRazonSocial().toUpperCase());
            document.selectFirst("#representacion").text(datosPersona.getRazonSocial().toUpperCase());
        }

        if(datosPersona.getMunicipioExp() != null) {
            document.selectFirst("#municipio").text(municipioRepository.findById(Integer.parseInt(datosPersona.getMunicipioExp())).get().getNombreMunicipio().toUpperCase());
            document.selectFirst("#expedicion").text(municipioRepository.findById(Integer.parseInt(datosPersona.getMunicipioExp())).get().getNombreMunicipio().toUpperCase());
        }
        if (datosPersona.getRecursos() != null) {
            document.selectFirst("#recursos").text(datosPersona.getRecursos());
            document.selectFirst("#ingresos").text(datosPersona.getIngresos());
        }
        LocalDate fecha = LocalDate.now();
        Map<Integer, String> meses = new HashMap<>();
        meses.put(1, "Enero");
        meses.put(2, "Febrero");
        meses.put(3, "Marzo");
        meses.put(4, "Abril");
        meses.put(5, "Mayo");
        meses.put(6, "Junio");
        meses.put(7, "Julio");
        meses.put(8, "Agosto");
        meses.put(9, "Septiembre");
        meses.put(10, "Octubre");
        meses.put(11, "Noviembre");
        meses.put(12, "Diciembre");

        document.selectFirst("#dia").text(String.valueOf(fecha.getDayOfMonth()));
        document.selectFirst("#mes").text(meses.get(fecha.getMonth().getValue()));
        document.selectFirst("#anio").text(String.valueOf(fecha.getYear()));

        document.selectFirst("#firma").html("<img width=\"150\" src=\"data:image/png;base64, " + "<img width=\"150\" src=\"data:image/png;base64, " + Base64.getEncoder().encodeToString(datosPersona.getFirma()) + "\">");
        try {
            byte[] bytes = datosPersona.getHuella();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
            BufferedImage originalImage = ImageIO.read(inputStream);
            BufferedImage rotatedImage = rotateImage(originalImage, 270);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(rotatedImage, "jpg", outputStream);
            byte[] rotatedBytes = outputStream.toByteArray();
            String imagenRotada = Base64.getEncoder().encodeToString(rotatedBytes);
            document.selectFirst("#huella").html("<img width=\"70\" src=\"data:image/png;base64, " + "<img width=\"150\" src=\"data:image/png;base64, " + imagenRotada + "\">");
        } catch (Exception e) {

        }
        document.selectFirst("#cc").text(datosPersona.getCodUsuario());

        document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PdfRendererBuilder builder = new PdfRendererBuilder();
        builder.useDefaultPageSize(Float.valueOf(210), Float.valueOf(297), BaseRendererBuilder.PageSizeUnits.MM);
        builder.toStream(os);
        builder.withW3cDocument(new W3CDom().fromJsoup(document), "/");
        builder.run();
        return os.toByteArray();
    }

    public static BufferedImage rotateImage(BufferedImage image, double angleDegrees) {
        double angleRadians = Math.toRadians(angleDegrees);
        double sin = Math.abs(Math.sin(angleRadians));
        double cos = Math.abs(Math.cos(angleRadians));

        int originalWidth = image.getWidth();
        int originalHeight = image.getHeight();

        int rotatedWidth = (int) Math.floor(originalWidth * cos + originalHeight * sin);
        int rotatedHeight = (int) Math.floor(originalHeight * cos + originalWidth * sin);

        BufferedImage rotatedImage = new BufferedImage(rotatedWidth, rotatedHeight, image.getType());

        Graphics2D g2d = rotatedImage.createGraphics();
        g2d.translate((rotatedWidth - originalWidth) / 2, (rotatedHeight - originalHeight) / 2);
        g2d.rotate(angleRadians, originalWidth / 2, originalHeight / 2);
        g2d.drawRenderedImage(image, null);
        g2d.dispose();

        return rotatedImage;
    }
}
