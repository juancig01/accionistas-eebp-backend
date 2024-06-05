package com.eebp.accionistas.backend.asamblea.services;

import com.eebp.accionistas.backend.accionistas.entities.Persona;
import com.eebp.accionistas.backend.accionistas.services.AccionistaService;
import com.eebp.accionistas.backend.accionistas.services.PersonaService;
import com.eebp.accionistas.backend.asamblea.entities.Asamblea;
import com.eebp.accionistas.backend.asamblea.repositories.AsambleaRepository;
import com.eebp.accionistas.backend.seguridad.entities.Asset;
import com.eebp.accionistas.backend.seguridad.entities.EmailDetails;
import com.eebp.accionistas.backend.seguridad.services.EmailServiceImpl;
import com.eebp.accionistas.backend.seguridad.utils.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AsambleaService {

    @Autowired
    AsambleaRepository asambleaRepository;
    @Autowired
    AccionistaService accionistaService;
    @Autowired
    PersonaService personaService;

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

            List<Persona> accionistasActivos = personas.stream()
                    .filter(a -> a.getEsAccionista().equals("S"))
                    .collect(Collectors.toList());

            String[] correosAccionistas = accionistasActivos.stream()
                    .map(Persona::getCorreoPersona)
                    .toArray(String[]::new);

            System.out.println("Correos electrónicos de los accionistas: " + Arrays.toString(correosAccionistas));

            EmailDetails emailDetails = EmailDetails.builder()
                    .recipient(String.join(", ", correosAccionistas))
                    .subject("Asamblea" + " " + asamblea.get().getTipoAsamblea())
                    .msgBody("<table border=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse; height:147px; width:600px\">\n" +
                            "\t<tbody>\n" +
                            "\t\t<tr>\n" +
                            "\t\t\t<td style=\"height:91px; text-align:center; width:23.5796%\"><img src=\"https://eebpsa.com.co/wp-content/uploads/2020/08/lOGO-2.1.png\" /></td>\n" +
                            "\t\t\t<td style=\"height:91px; width:67.4766%\">\n" +
                            "\t\t\t<h3 style=\"text-align:center\"><strong>BIENVENIDO AL SISTEMA DE ACCIONISTAS </strong></h3>\n" +
                            "\n" +
                            "\t\t\t<h3 style=\"text-align:center\"><strong>Empresa de Energ&iacute;a del Bajo Putumayo S.A. E.S.P.</strong></h3>\n" +
                            "\t\t\t</td>\n" +
                            "\t\t</tr>\n" +
                            "\t\t<tr>\n" +
                            "\t\t\t<td colspan=\"2\" style=\"height:10px; text-align:center; width:91.0562%\">\n" +
                            "\t\t\t<p>&nbsp;</p>\n" +
                            "\n" +
                            //"\t\t\t<p style=\"text-align:left\">Se&ntilde;or(a) " + accionistasActivos.get().getNomPri() + " " + accionistasActivos.get().getNomSeg() + " " + accionistasActivos.get().getApePri() +  " " + accionistasActivos.get().getApeSeg()  + ",</p>\n" +
                            "\n" +
                            "\t\t\t<p style=\"text-align:left\">Queremos informarle que se programó una asamblea de tipo" + " " + asamblea.get().getTipoAsamblea() +"," + "la cual se realizará el día" + " " + asamblea.get().getFechaAsamblea()  + " " + "a las " + " " + asamblea.get().getHoraAsamblea() +
                            "\t\t\t</td>\n" +
                            "\t\t</tr>\n" +
                            "\t\t<tr>\n" +
                            "\t\t\t<td colspan=\"2\" style=\"text-align:center; width:91.0562%\">\n" +
                            "\t\t\t<p style=\"text-align:left\">&nbsp;</p>\n" +
                            "\n" +
                            "\t\t\t<p style=\"text-align:left\"><u>En caso de alguna duda, favor contactarse con servicio al cliente.</u></p>\n" +
                            "\n" +
                            "\t\t\t<p style=\"text-align:left\">&nbsp;</p>\n" +
                            "\n" +
                            "\t\t\t<p style=\"text-align:left\">Acceso al sistema: <a href=\"http://localhost:4200\">http://localhost:4200</a></p>\n" +
                            "\t\t\t</td>\n" +
                            "\t\t</tr>\n" +
                            "\t</tbody>\n" +
                            "</table>\n" +
                            "\n" +
                            "<p><strong>&nbsp;</strong></p>")
                    .build();
            emailService.sendSimpleMailArray(emailDetails, correosAccionistas);

            // Aquí puedes realizar las operaciones necesarias con la lista filtrada de accionistas activos
            // ...

            return asamblea.get();
        }


        return null;
    }
}
