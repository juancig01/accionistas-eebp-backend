package com.eebp.accionistas.backend.transacciones.services;

import com.eebp.accionistas.backend.acciones.entities.EstadoTitulo;
import com.eebp.accionistas.backend.acciones.entities.Titulo;
import com.eebp.accionistas.backend.acciones.repositories.TituloRepository;
import com.eebp.accionistas.backend.accionistas.entities.Persona;
import com.eebp.accionistas.backend.accionistas.repositories.PersonaRepository;
import com.eebp.accionistas.backend.accionistas.services.PersonaService;
import com.eebp.accionistas.backend.seguridad.entities.Asset;
import com.eebp.accionistas.backend.seguridad.entities.EmailDetails;
import com.eebp.accionistas.backend.seguridad.services.EmailServiceImpl;
import com.eebp.accionistas.backend.seguridad.utils.FileUploadUtil;
import com.eebp.accionistas.backend.transacciones.entities.Transaccion;
import com.eebp.accionistas.backend.transacciones.entities.TransaccionEstado;
import com.eebp.accionistas.backend.transacciones.entities.TransaccionTitulo;
import com.eebp.accionistas.backend.transacciones.repositories.TransaccionEstadoRepository;
import com.eebp.accionistas.backend.transacciones.repositories.TransaccionRepository;
import com.eebp.accionistas.backend.transacciones.repositories.TransaccionTituloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TransaccionService {

    @Autowired
    TransaccionRepository transaccionRepository;

    @Autowired
    TransaccionTituloRepository transaccionTituloRepository;

    @Autowired
    TransaccionEstadoRepository transaccionEstadoRepository;

    @Autowired
    TituloRepository tituloRepository;

    @Autowired
    PersonaRepository personaRepository;

    @Autowired
    private EmailServiceImpl emailService;

    @Autowired
    private PersonaService personaService;

    public List<Asset> getFilesTransaccion(@PathVariable String conseTrans) {
        return FileUploadUtil.files(conseTrans, "transaccion").stream().map(file -> {
            file.setUrl("/assets/images/avatars/" + file.getFileName());
            return file;
        }).collect(Collectors.toList());
    }

    public List<Transaccion> getTransacciones() {
        List<Transaccion> transacciones = transaccionRepository.findAll();
        for (Transaccion transaccion : transacciones) {
            List<TransaccionTitulo> transaccionTitulos = transaccionTituloRepository.findTransaccionesPorConseTrans(transaccion.getConseTrans());
            transaccion.setTransaccionTitulo(transaccionTitulos);

            transaccion.setFiles(getFilesTransaccion(String.valueOf(transaccion.getConseTrans())));
        }
        return transacciones;
    }

    public List<Transaccion> getTransaccionesTramite() {
        List<Transaccion> transacciones = transaccionRepository.findAll();
        List<Transaccion> transaccionesTramite = new ArrayList<>();

        for (Transaccion transaccion : transacciones) {
            TransaccionEstado estadoTransaccion = transaccion.getEstadoTransaccion();
            if (estadoTransaccion != null && estadoTransaccion.getDescEstado().equals("En tramite")) {
                List<TransaccionTitulo> transaccionTitulos = transaccionTituloRepository.findTransaccionesPorConseTrans(transaccion.getConseTrans());
                transaccion.setTransaccionTitulo(transaccionTitulos);
                transaccionesTramite.add(transaccion);
            }
            transaccion.setFiles(getFilesTransaccion(String.valueOf(transaccion.getConseTrans())));
        }
        return transaccionesTramite;
    }

    public List<Transaccion> getTransaccionesAprobado() {
        List<Transaccion> transacciones = transaccionRepository.findAll();
        List<Transaccion> transaccionesAprobado = new ArrayList<>();

        for (Transaccion transaccion : transacciones) {
            TransaccionEstado estadoTransaccion = transaccion.getEstadoTransaccion();
            if (estadoTransaccion != null && estadoTransaccion.getDescEstado().equals("Aprobado")) {
                List<TransaccionTitulo> transaccionTitulos = transaccionTituloRepository.findTransaccionesPorConseTrans(transaccion.getConseTrans());
                transaccion.setTransaccionTitulo(transaccionTitulos);
                transaccionesAprobado.add(transaccion);
            }
            transaccion.setFiles(getFilesTransaccion(String.valueOf(transaccion.getConseTrans())));
        }
        return transaccionesAprobado;
    }

    public Transaccion addTransaccion(Transaccion transaccion) {
        TransaccionEstado transaccionEstado = transaccionEstadoRepository.findByDescEstado("En tramite");
        transaccion.setEstadoTransaccion(transaccionEstado);
        Transaccion t = transaccionRepository.save(transaccion);
        t.getTransaccionTitulo().stream()
                .forEach(transaccionTitulo -> {
                    transaccionTitulo.setConseTrans(t.getConseTrans());
                    Titulo tempTitulo = tituloRepository.findById(transaccionTitulo.getConseTitulo()).get();
                    tempTitulo.setEstadoTitulo(EstadoTitulo.builder().ideEstadoTitulo(2).build());
                    tituloRepository.save(tempTitulo);
                });
        transaccionTituloRepository.saveAll(t.getTransaccionTitulo());
        return t;
    }

    public Optional<Transaccion> findTransaccionById(Integer id) {
        Transaccion t = transaccionRepository.findById(id).get();
        t.setTransaccionTitulo(transaccionTituloRepository.findTransaccionesPorConseTrans(t.getConseTrans()));
        return transaccionRepository.findById(id);
    }

    public Transaccion updateTransaccion(Transaccion transaccion) {
        Optional<TransaccionEstado> estadoTransaccionOptional = transaccionEstadoRepository
                .findById(transaccion.getEstadoTransaccion().getIdeEstado());
        TransaccionEstado estadoTransaccion = estadoTransaccionOptional.get();
        transaccion.setEstadoTransaccion(estadoTransaccion);

        if (estadoTransaccion.getIdeEstado() == 3) {
            List<TransaccionTitulo> transaccionTitulos = transaccion.getTransaccionTitulo();
            for (TransaccionTitulo transaccionTitulo : transaccionTitulos) {
                Titulo tempTitulo = tituloRepository.findById(transaccionTitulo.getConseTitulo()).get();
                tempTitulo.setEstadoTitulo(EstadoTitulo.builder().ideEstadoTitulo(1).build()); // Estado activo
                tituloRepository.save(tempTitulo);
            }

            String idePer = transaccion.getIdePer();

            Persona persona = personaRepository.findById(idePer).orElse(null);

            EmailDetails emailDetails = EmailDetails.builder()
                    .recipient(persona.getCorreoPersona())
                    .subject("Notificación de rechazo de transacción")
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
                            "\t\t\t<p style=\"text-align:left\">Se&ntilde;or(a) " + persona.getNomPri() + " " + persona.getNomSeg() + " " + persona.getApePri() +  " " + persona.getApeSeg()  + ",</p>\n" +
                            "\n" +
                            "\t\t\t<p style=\"text-align:left\">Queremos informarle que lamentablemente la transacción que intentó realizar ha sido RECHAZADA.</p>\n" +
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

            emailService.sendSimpleMail(emailDetails);
        } else if (estadoTransaccion.getIdeEstado() == 2) {

            String idePer = transaccion.getIdePer();

            Persona persona = personaRepository.findById(idePer).orElse(null);

            EmailDetails emailDetails = EmailDetails.builder()
                    .recipient(persona.getCorreoPersona())
                    .subject("Notificación de aprobación de transacción")
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
                            "\t\t\t<p style=\"text-align:left\">Se&ntilde;or(a) " + persona.getNomPri() + " " + persona.getNomSeg() + " " + persona.getApePri() +  " " + persona.getApeSeg()  + ",</p>\n" +
                            "\n" +
                            "\t\t\t<p style=\"text-align:left\">Queremos informarle que la transacción ha sido APROBADA.</p>\n" +
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

            emailService.sendSimpleMail(emailDetails);

        }

        return transaccionRepository.save(transaccion);
    }

}
