package com.eebp.accionistas.backend.accionistas.services;

import com.eebp.accionistas.backend.accionistas.entities.Accionista;
import com.eebp.accionistas.backend.accionistas.entities.LogRegistroAccionistas;
import com.eebp.accionistas.backend.accionistas.entities.Persona;
import com.eebp.accionistas.backend.accionistas.entities.request.ActualizarRepresentanteRequest;
import com.eebp.accionistas.backend.accionistas.entities.request.AprobarAccionistaRequest;
import com.eebp.accionistas.backend.accionistas.entities.request.RechazarAccionistaRequest;
import com.eebp.accionistas.backend.accionistas.entities.response.AccionistaRepresentanteResponse;
import com.eebp.accionistas.backend.accionistas.exceptions.AccionistaExistsException;
import com.eebp.accionistas.backend.accionistas.repositories.AccionistaRepository;
import com.eebp.accionistas.backend.accionistas.entities.request.ActualizarTipoAccionistaRequest;
import com.eebp.accionistas.backend.seguridad.entities.EmailDetails;
import com.eebp.accionistas.backend.seguridad.entities.User;
import com.eebp.accionistas.backend.seguridad.exceptions.NewUserException;
import com.eebp.accionistas.backend.seguridad.exceptions.UserNotFoundException;
import com.eebp.accionistas.backend.seguridad.services.EmailServiceImpl;
import com.eebp.accionistas.backend.seguridad.services.UserServiceImpl;
import com.eebp.accionistas.backend.seguridad.utils.PasswordGenerator;
/*import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;*/
import lombok.extern.log4j.Log4j2;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Log4j2
@Service
public class AccionistaService {

    @Autowired
    private AccionistaRepository accionistaRepository;

    @Autowired
    private PersonaService personaService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private EmailServiceImpl emailService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    PasswordGenerator passwordGenerator;

    @Autowired
    LogRegistroAccionistaService logRegistroAccionistaService;

    public Optional<Accionista> getAccionista(String codUsuario) {
        return accionistaRepository.findById(codUsuario);
    }

    public Accionista addAccionista(Accionista accionista) throws AccionistaExistsException, UserNotFoundException {
        if(!accionistaRepository.findById(accionista.getCodUsuario()).isPresent()) {

            accionista.setAprobado("N");
            Persona persona = personaService.getPersona(accionista.getCodUsuario()).get();
            emailService.sendSimpleMail(
                    EmailDetails.builder()
                            .recipient(persona.getCorreoPersona())
                            .subject("Registro como accionista de EEBP")
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
                                    "\t\t\t<p style=\"text-align:left\">Su registro como accionista ha sido exitoso y se encuentra en estado de REVISI&Oacute;N, una vez sea aprobado, se le notificar&aacute; por &eacute;ste mismo medio.</p>\n" +
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
                                    "\t\t\t</td>\n" +
                                    "\t\t</tr>\n" +
                                    "\t</tbody>\n" +
                                    "</table>\n" +
                                    "\n" +
                                    "<p><strong>&nbsp;</strong></p>")
                            .build()
            );
            log.info("Accionista registrado correctamente.");

            if (persona.getNomPri() == null) {
                persona.setNomPri(persona.getRazonSocial());
                persona.setNomSeg("");
                persona.setApePri("");
                persona.setApeSeg("");
            }

            logRegistroAccionistaService.add(LogRegistroAccionistas.builder()
                    .codUsuario(accionista.getCodUsuario())
                    .tipo("AGREGAR")
                    .accion(persona.getNomPri() + " " + persona.getNomSeg() + " " + persona.getApePri() +  " " + persona.getApeSeg()  + " se agregó exitosamente como ACCIONISTA en el sistema.")
                    .fecha(LocalDateTime.now())
                    .build());

            if(accionista.getCodRepresentante() != null) {
                Persona representante = personaService.getPersona(accionista.getCodRepresentante()).get();
                logRegistroAccionistaService.add(LogRegistroAccionistas.builder()
                        .codUsuario(accionista.getCodUsuario())
                        .tipo("AGREGAR")
                        .accion(persona.getNomPri() + " " + persona.getNomSeg() + " " + persona.getApePri() +  " " + persona.getApeSeg()  + " es menor de edad, por lo cual se agregó como representante a " + representante.getNomPri() + " " + representante.getNomSeg() + " " + representante.getApePri() +  " " + representante.getApeSeg())
                        .fecha(LocalDateTime.now())
                        .build());
            }

            logRegistroAccionistaService.add(LogRegistroAccionistas.builder()
                    .codUsuario(accionista.getCodUsuario())
                    .tipo("EMAIL")
                    .accion("Se envió un email a " + persona.getCorreoPersona()  + " confirmando el registro como accionista y su estado en REVISIÓN.")
                    .fecha(LocalDateTime.now())
                    .build());


            return accionistaRepository.save(accionista);
        } else {
            throw new AccionistaExistsException();
        }
    }

    public void aprobarAccionista(AprobarAccionistaRequest aprobarAccionistaRequest) throws NewUserException, UserNotFoundException {
        accionistaRepository.aprobarAccionista(aprobarAccionistaRequest.getCodUsuario());
        Persona persona = personaService.getPersona(aprobarAccionistaRequest.getCodUsuario()).get();
        if (persona.getNomPri() == null) {
            persona.setNomPri(persona.getRazonSocial());
            persona.setNomSeg("");
            persona.setApePri("");
            persona.setApeSeg("");
        }
        logRegistroAccionistaService.add(LogRegistroAccionistas.builder()
                .codUsuario(aprobarAccionistaRequest.getCodUsuario())
                .tipo("APROBAR")
                .accion(persona.getNomPri() + " " + persona.getNomSeg() + " " + persona.getApePri() +  " " + persona.getApeSeg()  + " fue APROBADO como accionista en el sistema.")
                .fecha(LocalDateTime.now())
                .build());
        emailService.sendSimpleMail(
                EmailDetails.builder()
                        .recipient(persona.getCorreoPersona())
                        .subject("Registro como accionista de EEBP")
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
                                "\t\t\t<p style=\"text-align:left\">Su registro como accionista ha sido APROBADO, en instantes recibirá un correo electrónico con las credenciales de inicio de sesión.</p>\n" +
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
                        .build()
        );
        String tempPassword = passwordGenerator.generatePassword();

        userService.crearUsuario(User.builder()
                .codUsuario(persona.getCodUsuario())
                .nombreUsuario(persona.getNomPri() + " " + persona.getNomSeg())
                .apellidoUsuario(persona.getApePri() +  " " + persona.getApeSeg())
                .email(persona.getCorreoPersona())
                .perfil(2).password(passwordEncoder.encode(tempPassword)).build(), tempPassword);

        logRegistroAccionistaService.add(LogRegistroAccionistas.builder()
                .codUsuario(aprobarAccionistaRequest.getCodUsuario())
                .tipo("AGREGAR")
                .accion("Usuario de sistema creado exitosamente para el accionista " + persona.getNomPri() + " " + persona.getNomSeg() + " " + persona.getApePri() +  " " + persona.getApeSeg())
                .fecha(LocalDateTime.now())
                .build());
        logRegistroAccionistaService.add(LogRegistroAccionistas.builder()
                .codUsuario(aprobarAccionistaRequest.getCodUsuario())
                .tipo("EMAIL")
                .accion("Se envió un email a " + persona.getCorreoPersona()  + " con las credenciales de acceso al sistema.")
                .fecha(LocalDateTime.now())
                .build());
        log.info("Accionista aprobado correctamente");
    }

    public void rechazarAccionista(RechazarAccionistaRequest request) throws UserNotFoundException {
        accionistaRepository.rechazarAccionista(request.getCodUsuario(), request.getDescripcionRechazo());
        Persona persona = personaService.getPersona(request.getCodUsuario()).get();
        if (persona.getNomPri() == null) {
            persona.setNomPri(persona.getRazonSocial());
            persona.setNomSeg("");
            persona.setApePri("");
            persona.setApeSeg("");
        }
        logRegistroAccionistaService.add(LogRegistroAccionistas.builder()
                .codUsuario(request.getCodUsuario())
                .tipo("RECHAZAR")
                .accion(persona.getNomPri() + " " + persona.getNomSeg() + " " + persona.getApePri() +  " " + persona.getApeSeg()  + " fue RECHAZADO como accionista en el sistema.")
                .razon(request.getDescripcionRechazo())
                .fecha(LocalDateTime.now())
                .build());
        emailService.sendSimpleMail(
                EmailDetails.builder()
                        .recipient(persona.getCorreoPersona())
                        .subject("Registro rechazado como accionista de EEBP")
                        .msgBody("<table border=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse; height:147px; width:600px\">\n" +
                                "\t<tbody>\n" +
                                "\t\t<tr>\n" +
                                "\t\t\t<td style=\"height:91px; text-align:center; width:23.5796%\"><img src=\"https://eebpsa.com.co/wp-content/uploads/2020/08/lOGO-2.1.png\" /></td>\n" +
                                "\t\t\t<td style=\"height:91px; width:67.4766%\">\n" +
                                "\t\t\t<h3 style=\"text-align:center\"><strong>SISTEMA DE ACCIONISTAS </strong></h3>\n" +
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
                                "\t\t\t<p style=\"text-align:left\">Su registro como accionista ha sido RECHAZADO por el siguiente motivo: " + request.getDescripcionRechazo() + "</p>\n" +
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
                        .build()
        );
        logRegistroAccionistaService.add(LogRegistroAccionistas.builder()
                .codUsuario(request.getCodUsuario())
                .tipo("EMAIL")
                .accion("Se envió un email a " + persona.getCorreoPersona()  + " con la información del RECHAZO como accionista.")
                .fecha(LocalDateTime.now())
                .build());

    }

    public void actualizarRepresentante(ActualizarRepresentanteRequest request) throws UserNotFoundException {
        accionistaRepository.actualizarRepresentante(request.getCodUsuario(), request.getCodRepresentante());
        Persona persona = personaService.getPersona(request.getCodRepresentante()).get();
        if (persona.getNomPri() == null) {
            persona.setNomPri(persona.getRazonSocial());
            persona.setNomSeg("");
            persona.setApePri("");
            persona.setApeSeg("");
        }
        logRegistroAccionistaService.add(LogRegistroAccionistas.builder()
                .codUsuario(request.getCodUsuario())
                .tipo("AGREGAR")
                .accion(persona.getNomPri() + " " + persona.getNomSeg() + " " + persona.getApePri() +  " " + persona.getApeSeg()  + " fue actualizado como representante.")
                .fecha(LocalDateTime.now())
                .build());
    }

    public void actualizarTipoAccionista(ActualizarTipoAccionistaRequest request) {
        accionistaRepository.actualizarTipoAccionista(request.getCodUsuario(), request.getTipoAccionista(), request.getAprobado());
    }

    public AccionistaRepresentanteResponse getAccionistaRepresentante(String codUsuario) throws UserNotFoundException {
        Accionista accionista = accionistaRepository.findById(codUsuario).get();
        Persona pRepresentante = Persona.builder().build();
        if (accionista.getCodRepresentante() == null) {
            pRepresentante = personaService.getPersona(accionista.getCodUsuario()).get();
        } else {
            pRepresentante = personaService.getPersona(accionista.getCodRepresentante()).get();
        }
        Persona pAccionista = personaService.getPersona(accionista.getCodUsuario()).get();

        return AccionistaRepresentanteResponse.builder()
                .nomAccionista(pAccionista.getNomPri() + " " + pAccionista.getNomSeg() + " " + pAccionista.getApePri() +  " " + pAccionista.getApeSeg())
                .nomRepresentante(pRepresentante.getNomPri() + " " + pRepresentante.getNomSeg() + " " + pRepresentante.getApePri() +  " " + pRepresentante.getApeSeg())
                .codAccionista(accionista.getCodUsuario())
                .codRepresentante(pRepresentante.getCodUsuario())
                .esAccionista("S")
                .tipoDocAccionista(pAccionista.getTipDocumento())
                .tipoDocRepresentante(pRepresentante.getTipDocumento())
                .build();
    }

    public List<AccionistaRepresentanteResponse> getAccionistas() throws UserNotFoundException {
        List<AccionistaRepresentanteResponse> lista = new ArrayList<>();
        List<Accionista> accionistas = accionistaRepository.findAll();
        for (Accionista accionista : accionistas) {
            Persona pRepresentante = Persona.builder().build();
            if (accionista.getCodRepresentante() == null) {
                pRepresentante = personaService.getPersona(accionista.getCodUsuario()).get();
            } else {
                pRepresentante = personaService.getPersona(accionista.getCodRepresentante()).get();
            }
            Persona pAccionista = personaService.getPersona(accionista.getCodUsuario()).get();
            if (pAccionista.getNomPri() == null) {
                pAccionista.setNomPri(pAccionista.getRazonSocial());
                pAccionista.setNomSeg("");
                pAccionista.setApePri("");
                pAccionista.setApeSeg("");
            }

            // Agregar esta condición
            if ("S".equals(accionista.getAprobado())) {
                lista.add(AccionistaRepresentanteResponse.builder()
                        .nomAccionista(pAccionista.getNomPri() + " " + pAccionista.getNomSeg() + " " + pAccionista.getApePri() + " " + pAccionista.getApeSeg())
                        .nomRepresentante(pRepresentante.getNomPri() + " " + pRepresentante.getNomSeg() + " " + pRepresentante.getApePri() + " " + pRepresentante.getApeSeg())
                        .codAccionista(accionista.getCodUsuario())
                        .tipoAccionista(accionista.getTipoAccionista())
                        .codRepresentante(pRepresentante.getCodUsuario())
                        .esAccionista("S")
                        .tipoDocAccionista(pAccionista.getTipDocumento())
                        .tipoDocRepresentante(pRepresentante.getTipDocumento())
                        .build());
            }
        }
        return lista;
    }

    public List<AccionistaRepresentanteResponse> getAccionistasTipoTres() throws UserNotFoundException {
        List<AccionistaRepresentanteResponse> lista = new ArrayList<>();
        List<Accionista> accionistas = accionistaRepository.findAll();
        for (Accionista accionista : accionistas) {
            Persona pRepresentante = Persona.builder().build();
            if (accionista.getCodRepresentante() == null) {
                pRepresentante = personaService.getPersona(accionista.getCodUsuario()).get();
            } else {
                pRepresentante = personaService.getPersona(accionista.getCodRepresentante()).get();
            }
            Persona pAccionista = personaService.getPersona(accionista.getCodUsuario()).get();
            if (pAccionista.getNomPri() == null) {
                pAccionista.setNomPri(pAccionista.getRazonSocial());
                pAccionista.setNomSeg("");
                pAccionista.setApePri("");
                pAccionista.setApeSeg("");
            }

            // Agregar esta condición
            if ("S".equals(accionista.getAprobado()) && accionista.getTipoAccionista() == 3) {
                lista.add(AccionistaRepresentanteResponse.builder()
                        .nomAccionista(pAccionista.getNomPri() + " " + pAccionista.getNomSeg() + " " + pAccionista.getApePri() + " " + pAccionista.getApeSeg())
                        .nomRepresentante(pRepresentante.getNomPri() + " " + pRepresentante.getNomSeg() + " " + pRepresentante.getApePri() + " " + pRepresentante.getApeSeg())
                        .codAccionista(accionista.getCodUsuario())
                        .tipoAccionista(accionista.getTipoAccionista())
                        .codRepresentante(pRepresentante.getCodUsuario())
                        .esAccionista("S")
                        .tipoDocAccionista(pAccionista.getTipDocumento())
                        .tipoDocRepresentante(pRepresentante.getTipDocumento())
                        .build());
            }
        }
        return lista;
    }

    public List<AccionistaRepresentanteResponse> getListaAccionistasPendientesAprobar() throws UserNotFoundException {
        List<AccionistaRepresentanteResponse> lista = new ArrayList<>();
        List<Accionista> accionistas = accionistaRepository.findAll();
        for (Accionista accionista : accionistas) {
            Persona pRepresentante = Persona.builder().build();
            if (accionista.getCodRepresentante() == null) {
                pRepresentante = personaService.getPersona(accionista.getCodUsuario()).get();
            } else {
                pRepresentante = personaService.getPersona(accionista.getCodRepresentante()).get();
            }
            Persona pAccionista = personaService.getPersona(accionista.getCodUsuario()).get();
            if (pAccionista.getNomPri() == null) {
                pAccionista.setNomPri(pAccionista.getRazonSocial());
                pAccionista.setNomSeg("");
                pAccionista.setApePri("");
                pAccionista.setApeSeg("");
            }
            lista.add(AccionistaRepresentanteResponse.builder()
                    .nomAccionista(pAccionista.getNomPri() + " " + pAccionista.getNomSeg() + " " + pAccionista.getApePri() + " " + pAccionista.getApeSeg())
                    .nomRepresentante(pRepresentante.getNomPri() + " " + pRepresentante.getNomSeg() + " " + pRepresentante.getApePri() + " " + pRepresentante.getApeSeg())
                    .codAccionista(accionista.getCodUsuario())
                    .codRepresentante(pRepresentante.getCodUsuario())
                    .esAccionista(accionista.getAprobado())
                    .tipoDocAccionista(pAccionista.getTipDocumento())
                    .tipoDocRepresentante(pRepresentante.getTipDocumento())
                    .descripcionRechazo(accionista.getDescripcionRechazo())
                    .build());
        }
        return lista;
    }

    public ByteArrayInputStream excelPendientesAprobar() throws UserNotFoundException, IOException {
        String [] columns = {"Identificación", "Nombre"};

        Workbook workbook = new HSSFWorkbook();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        Sheet sheet = workbook.createSheet("Pendietes por Aprobar");
        Row row = sheet.createRow(0);

        for (int i = 0; i<columns.length; i ++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(columns[i]);
        }

        List<AccionistaRepresentanteResponse> lista = getListaAccionistasPendientesAprobar();
        int initRow= 1;
        for (AccionistaRepresentanteResponse listas : lista) {
            if (listas.getEsAccionista().equalsIgnoreCase("N") || listas.getEsAccionista().equalsIgnoreCase("P")) {
                row = sheet.createRow(initRow);
                row.createCell(0).setCellValue(listas.getCodAccionista());
                row.createCell(1).setCellValue(listas.getNomAccionista());

                initRow++;
            }
        }
        workbook.write(stream);
        workbook.close();
        return new ByteArrayInputStream(stream.toByteArray());
    }

    /*public byte[] getAccionistasPendientesPorAprobar() throws DocumentException, UserNotFoundException {
        /*List<AccionistaRepresentanteResponse> lista = getListaAccionistasPendientesAprobar();
        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, outputStream);
        document.open();
        PdfPTable table = new PdfPTable(2);

        Font boldFont = new Font(Font.FontFamily.HELVETICA, 15, Font.BOLD);
        Paragraph paragraph = new Paragraph("ACCIONISTAS PENDIENTES POR APROBAR - EEBP\n\n", boldFont);
        document.add(paragraph);
        // Add header row
        table.addCell("IDENTIFICACION");
        table.addCell("NOMBRE");
        lista.stream().filter(accionista -> accionista.getEsAccionista().equalsIgnoreCase("N") && accionista.getDescripcionRechazo() == null)
                .forEach(accionista -> {
                    table.addCell(accionista.getTipoDocAccionista() + " " + accionista.getCodAccionista());
                    table.addCell(accionista.getNomAccionista());
                });
        document.add(table);
        document.close();
        return outputStream.toByteArray();
        return null;
    }*/
}
