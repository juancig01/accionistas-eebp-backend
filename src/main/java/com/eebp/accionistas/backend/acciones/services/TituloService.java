package com.eebp.accionistas.backend.acciones.services;

import com.eebp.accionistas.backend.acciones.entities.*;
import com.eebp.accionistas.backend.acciones.repositories.EstadoTituloRepository;
import com.eebp.accionistas.backend.acciones.repositories.TituloPersonaRepository;
import com.eebp.accionistas.backend.acciones.repositories.TituloRepository;
import com.eebp.accionistas.backend.accionistas.entities.Persona;
import com.eebp.accionistas.backend.accionistas.repositories.PersonaRepository;
import com.eebp.accionistas.backend.seguridad.entities.Asset;
import com.eebp.accionistas.backend.seguridad.entities.EmailDetails;
import com.eebp.accionistas.backend.seguridad.services.EmailServiceImpl;
import com.eebp.accionistas.backend.seguridad.utils.FileUploadUtil;
import com.eebp.accionistas.backend.transacciones.entities.*;
import com.eebp.accionistas.backend.transacciones.repositories.TransaccionEstadoRepository;
import com.eebp.accionistas.backend.transacciones.repositories.TransaccionRepository;
import com.eebp.accionistas.backend.transacciones.repositories.TransaccionTituloRepository;
import com.openhtmltopdf.outputdevice.helper.BaseRendererBuilder;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class TituloService {

    @Autowired
    TituloRepository tituloRepository;

    @Autowired
    PersonaRepository personaRepository;

    @Autowired
    TituloPersonaRepository tituloPersonaRepository;

    @Autowired
    TransaccionEstadoRepository transaccionEstadoRepository;

    @Autowired
    TransaccionRepository transaccionRepository;

    @Autowired
    TransaccionTituloRepository transaccionTituloRepository;

    @Autowired
    EstadoTituloRepository estadoTituloRepository;

    @Autowired
    private EmailServiceImpl emailService;

    public Optional<Titulo> findTituloById(Integer id) {
        return tituloRepository.findById(id);
    }

    public Titulo updateTitulo(Titulo titulo) {
        return tituloRepository.save(titulo);
    }

    public Map<String, List<Asset>> getFilesTitulos() {
        Integer consecutivo = 0;
        List<Asset> files = FileUploadUtil.files(String.valueOf(consecutivo), "formatoTitulo").stream()
                .map(file -> {
                    file.setUrl("/assets/images/avatars/" + file.getFileName());
                    return file;
                })
                .collect(Collectors.toList());

        Map<String, List<Asset>> result = new LinkedHashMap<>();
        result.put("formatoDeEndosoIndividual", new ArrayList<>());
        result.put("formatoDeCompraVentaNatural", new ArrayList<>());
        result.put("formatoDeCompraVentaMenores", new ArrayList<>());
        result.put("formatoDeCompraVentaJuridicas", new ArrayList<>());
        result.put("formatoDeDonacionDeAcciones", new ArrayList<>());
        result.put("formatoDeSucesionDeAcciones", new ArrayList<>());

        for (Asset file : files) {
            String fileName = file.getFileName();
            if (fileName.contains("formatoDeEndosoIndividual")) {
                result.get("formatoDeEndosoIndividual").add(file);
            } else if (fileName.contains("formatoCompraVentaNatural")) {
                result.get("formatoDeCompraVentaNatural").add(file);
            } else if (fileName.contains("formatoCompraVentaMenores")) {
                result.get("formatoDeCompraVentaMenores").add(file);
            } else if (fileName.contains("formatoCompraVentaJuridicas")) {
                result.get("formatoDeCompraVentaJuridicas").add(file);
            } else if (fileName.contains("formatoDonacionDeAcciones")) {
                result.get("formatoDeDonacionDeAcciones").add(file);
            } else if (fileName.contains("formatoDeSucesionDeAcciones")) {
                result.get("formatoDeSucesionDeAcciones").add(file);
            }
        }

        return result;
    }

   /* public void comprarAcciones(List<TransaccionDatos> transaccionesCompra) {
        LocalDateTime fechaHoraActual = LocalDateTime.now();

        for (TransaccionDatos transaccionCompra : transaccionesCompra) {
            int cantAccionesAComprar = transaccionCompra.getCantAccionesAComprar();
            int accionesCompradas = 0;
            Titulo tituloCompradas = null;
            Titulo tituloDisponibles = null;

            Optional<Persona> personaCorrespondienteOptional = null;
            for (TransaccionTitulo titulo : transaccionCompra.getTitulos()) {
                if (accionesCompradas >= cantAccionesAComprar) {
                    break; // Ya se compraron todas las acciones necesarias
                }

                Optional<Titulo> tituloOptional = tituloRepository.findById(titulo.getConseTitulo());
                if (tituloOptional.isPresent()) {
                    Titulo tituloDB = tituloOptional.get();
                    int accionesDisponibles = titulo.getNumAcciones();

                    if (accionesDisponibles > 0) {
                        int accionesATomar = Math.min(accionesDisponibles, cantAccionesAComprar - accionesCompradas);
                        accionesCompradas += accionesATomar;

                        // Anular el título utilizado
                        EstadoTitulo estadoTituloAnulado = new EstadoTitulo();
                        estadoTituloAnulado.setIdeEstadoTitulo(3); // Anula el título
                        tituloDB.setEstadoTitulo(estadoTituloAnulado);
                        tituloRepository.save(tituloDB);

                        // Asignar las acciones compradas al título correspondiente
                        if (tituloCompradas == null) {
                            tituloCompradas = new Titulo();
                            tituloCompradas.setCanAccTit(accionesATomar);
                            tituloCompradas.setValAccTit(tituloDB.getValAccTit());
                            tituloCompradas.setClaAccTit(tituloDB.getClaAccTit());
                            tituloCompradas.setTipAccTit(tituloDB.getTipAccTit());
                            tituloCompradas.setFecCreTit(LocalDate.from(fechaHoraActual));
                            tituloCompradas.setFecFinTit(tituloDB.getFecFinTit());
                            tituloCompradas.setObsAccTit("NINGUNA");

                            EstadoTitulo estadoTituloCompradas = new EstadoTitulo();
                            estadoTituloCompradas.setIdeEstadoTitulo(1); // Título con estado activo
                            tituloCompradas.setEstadoTitulo(estadoTituloCompradas);
                        } else {
                            tituloCompradas.setCanAccTit(tituloCompradas.getCanAccTit() + accionesATomar);
                        }

                        // Asignar las acciones disponibles al título correspondiente
                        if (accionesDisponibles - accionesATomar > 0) {

                            // Buscar el título del cual sobraron las acciones
                            Optional<TitulosPersona> tituloSobranteOptional = Optional.ofNullable(tituloPersonaRepository.findByConseTitulo(titulo.getConseTitulo()));

                            TitulosPersona titulosPersona = tituloSobranteOptional.get();

                            // Obtener la persona a la que corresponde ese título
                            Integer idePer = titulosPersona.getIdePer();
                            personaCorrespondienteOptional = personaRepository.findById(String.valueOf(idePer));

                            Persona personaCorrespondiente = personaCorrespondienteOptional.get();

                            if (tituloDisponibles == null) {
                                tituloDisponibles = new Titulo();
                                tituloDisponibles.setCanAccTit(accionesDisponibles - accionesATomar);
                                tituloDisponibles.setValAccTit(tituloDB.getValAccTit());
                                tituloDisponibles.setClaAccTit(tituloDB.getClaAccTit());
                                tituloDisponibles.setTipAccTit(tituloDB.getTipAccTit());
                                tituloDisponibles.setFecCreTit(LocalDate.from(fechaHoraActual));
                                tituloDisponibles.setFecFinTit(tituloDB.getFecFinTit());
                                tituloDisponibles.setObsAccTit(tituloDB.getObsAccTit());

                                EstadoTitulo estadoTituloDisponibles = new EstadoTitulo();
                                estadoTituloDisponibles.setIdeEstadoTitulo(1); // Título con estado activo
                                tituloDisponibles.setEstadoTitulo(estadoTituloDisponibles);
                            } else {
                                tituloDisponibles.setCanAccTit(tituloDisponibles.getCanAccTit() + (accionesDisponibles - accionesATomar));
                            }
                        }
                    }
                }
            }
            // Guardar los títulos en la base de datos
            if (tituloCompradas != null) {
                tituloRepository.save(tituloCompradas);
            }
            if (tituloDisponibles != null) {
                tituloRepository.save(tituloDisponibles);
            }

            //Asociar el titulo sobrante a la persona encontrada
            Persona personaCorrespondiente = personaCorrespondienteOptional.get();
            Optional<Persona> personaC = personaRepository.findById(personaCorrespondienteOptional.get().getCodUsuario());
            final Titulo tituloDisponibles1 = tituloDisponibles;
            personaC.ifPresent(persona -> {
                persona.getTitulos().add(tituloDisponibles1); // Usar la variable final dentro de la expresión lambda
            });

            // Asociar el nuevo título a los tomadores
            for (TomadorTitulo tomador : transaccionCompra.getTomadores()) {
                final Titulo tituloCompradasFinal = tituloCompradas; // Declarar la variable final aquí
                Optional<Persona> personaOptional = personaRepository.findById(tomador.getIdePer());
                personaOptional.ifPresent(persona -> {
                    persona.getTitulos().add(tituloCompradasFinal); // Usar la variable final dentro de la expresión lambda
                });
            }
            tituloRepository.save(tituloCompradas);
        }
    }
*/

    public AccionesInfoDTO getAccionesInfo() {
        List<Object[]> resultados = tituloRepository.obtenerAccionesInfo();
        if (!resultados.isEmpty()) {
            Object[] resultado = resultados.get(0);
            int numeroDeAccionesEnElMercado = ((Number) resultado[0]).intValue();
            int numeroDeAccionesConDerechoAUtilidades = ((Number) resultado[1]).intValue();
            return new AccionesInfoDTO(numeroDeAccionesEnElMercado, numeroDeAccionesConDerechoAUtilidades);
        }
        return new AccionesInfoDTO(0, 0);
    }
    @Transactional
    public Transaccion comprarAcciones(TransaccionDatos transaccionCompra) {
        LocalDateTime fechaHoraActual = LocalDateTime.now();

        int cantAccionesAComprar = transaccionCompra.getCantAcciones();
        int accionesCompradas = 0;
        Titulo tituloCompradas = null;
        Set<Integer> titulosModificados = new HashSet<>(); // Titulos usados en la compra

        // NUEVO: Obtener el ID de la transacción original para actualizarla
        Integer transaccionOriginalId = null;
        if (!transaccionCompra.getTitulos().isEmpty()) {
            // Obtener el conseTrans del primer título para identificar la transacción original
            transaccionOriginalId = transaccionCompra.getTitulos().get(0).getConseTrans();
        }

        for (TransaccionTitulo titulo : transaccionCompra.getTitulos()) {
            Optional<Titulo> tituloOptional = tituloRepository.findById(titulo.getConseTitulo());
            if (tituloOptional.isPresent()) {
                Titulo tituloOriginal = tituloOptional.get();
                int accionesDisponibles = titulo.getNumAcciones();

                if (accionesDisponibles > 0) {
                    int accionesATomar = Math.min(accionesDisponibles, cantAccionesAComprar - accionesCompradas);
                    accionesCompradas += accionesATomar;

                    // MODIFICAR EL TÍTULO ORIGINAL: restar las acciones compradas
                    //int accionesRestantes = tituloOriginal.getCanAccTit() - accionesATomar;
                    //tituloOriginal.setCanAccTit(accionesRestantes);

                    // Establecer el estado ACTIVO directamente en el objeto original
                    EstadoTitulo estadoActivo = new EstadoTitulo();
                    estadoActivo.setIdeEstadoTitulo(1);
                    tituloOriginal.setEstadoTitulo(estadoActivo);

                    // Guardar cambios en el título original con estado 1
                    updateTitulo(tituloOriginal);

                    System.out.println("Estado actualizado a 1 para título ID: " + tituloOriginal.getConseTitulo());
                    titulosModificados.add(tituloOriginal.getConseTitulo());

                    // CREAR NUEVO TÍTULO con las acciones compradas
                    if (tituloCompradas == null) {
                        tituloCompradas = new Titulo();
                        tituloCompradas.setCanAccTit(accionesATomar);
                        tituloCompradas.setValAccTit(tituloOriginal.getValAccTit());
                        tituloCompradas.setClaAccTit(tituloOriginal.getClaAccTit());
                        tituloCompradas.setTipAccTit(tituloOriginal.getTipAccTit());
                        tituloCompradas.setFecCreTit(LocalDate.from(fechaHoraActual));
                        tituloCompradas.setFecFinTit(tituloOriginal.getFecFinTit());
                        tituloCompradas.setObsAccTit("ACCIONES COMPRADAS - TÍTULO ORIGEN: " + tituloOriginal.getConseTitulo());

                        EstadoTitulo estadoTituloCompradas = new EstadoTitulo();
                        estadoTituloCompradas.setIdeEstadoTitulo(1); // Estado ACTIVO
                        tituloCompradas.setEstadoTitulo(estadoTituloCompradas);
                    } else {
                        // Si ya existe un título de compra, sumar las acciones
                        tituloCompradas.setCanAccTit(tituloCompradas.getCanAccTit() + accionesATomar);
                        // Actualizar observaciones para incluir múltiples orígenes
                        String obsActual = tituloCompradas.getObsAccTit();
                        if (!obsActual.contains("TÍTULO ORIGEN: " + tituloOriginal.getConseTitulo())) {
                            tituloCompradas.setObsAccTit(obsActual + " - TÍTULO ORIGEN: " + tituloOriginal.getConseTitulo());
                        }
                    }
                }
            }
        }

        // Guardar el nuevo título de compra
        if (tituloCompradas != null) {
            tituloRepository.save(tituloCompradas);
        }

        // NUEVO: Actualizar el estado de la transacción ORIGINAL a 6 (Ejecutado/Completado)
        if (transaccionOriginalId != null) {
            Optional<Transaccion> transaccionOriginalOptional = transaccionRepository.findById(transaccionOriginalId);
            if (transaccionOriginalOptional.isPresent()) {
                Transaccion transaccionOriginal = transaccionOriginalOptional.get();

                // Crear y asignar el nuevo estado
                TransaccionEstado estadoEjecutado = new TransaccionEstado();
                estadoEjecutado.setIdeEstado(6); // Estado 6 = Ejecutado/Completado
                transaccionOriginal.setEstadoTransaccion(estadoEjecutado);

                // Guardar la transacción original con el nuevo estado
                transaccionRepository.save(transaccionOriginal);

                System.out.println("Estado actualizado a 6 para transacción original ID: " + transaccionOriginalId);
            }
        }

        // Asociar el nuevo título a los tomadores
        for (TomadorTitulo tomador : transaccionCompra.getTomadores()) {
            final Titulo tituloCompradasFinal = tituloCompradas;
            Optional<Persona> personaOptional = personaRepository.findById(tomador.getIdePer());
            personaOptional.ifPresent(persona -> {
                persona.getTitulos().add(tituloCompradasFinal);
            });

            // Envío de email de notificación
            EmailDetails emailDetails = EmailDetails.builder()
                    .recipient(personaOptional.get().getCorreoPersona())
                    .subject("Notificación de adquisición de acciones")
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
                            "\t\t\t<p style=\"text-align:left\">Se&ntilde;or(a) " + personaOptional.get().getNomPri() + " " + personaOptional.get().getNomSeg() + " " + personaOptional.get().getApePri() +  " " + personaOptional.get().getApeSeg()  + ",</p>\n" +
                            "\n" +
                            "\t\t\t<p style=\"text-align:left\">Le informamos que se han adquirido acciones en su nombre y se ha generado el Titulo" + " " + tituloCompradasFinal.getConseTitulo() + " " + "con" + " " +tituloCompradasFinal.getCanAccTit() + " " + "acciones.\n" +
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

        //Crear la transaccion NUEVA (registro de la compra)
        TomadorTitulo primerTomador = transaccionCompra.getTomadores().get(0);

        TipoTransaccion tipoTransaccion = new TipoTransaccion();
        tipoTransaccion.setCodTipTran(2);

        TransaccionEstado transaccionEstado = new TransaccionEstado();
        transaccionEstado.setIdeEstado(6);

        TransaccionTitulo transaccionTitulo = new TransaccionTitulo();
        transaccionTitulo.setConseTitulo(tituloCompradas.getConseTitulo());
        transaccionTitulo.setNumAcciones(tituloCompradas.getCanAccTit());

        LocalDateTime fechaTrans = LocalDateTime.now();
        Instant instant = fechaTrans.atZone(ZoneId.systemDefault()).toInstant();

        Transaccion transaccion =
                transaccionRepository.save(
                        Transaccion.builder()
                                .fecTrans(Date.from(instant))
                                .idePer(primerTomador.getIdePer())
                                .intencionCompra(false)
                                .valTran(tituloCompradas.getValAccTit())
                                .tipoTransaccion(tipoTransaccion)
                                .estadoTransaccion(transaccionEstado)
                                .build()
                );
        transaccionTitulo.setConseTrans(transaccion.getConseTrans());

        transaccionTituloRepository.save(transaccionTitulo);

        for (Integer idTitulo : titulosModificados) {
            tituloRepository.updateEstadoTitulo(idTitulo, 1);
            System.out.println("Estado actualizado a 1 para título ID: ESTADO FINAL DEL METODO ");
        }

        return transaccion;
    }

    public TransaccionDatos donarAcciones(TransaccionDatos transaccionDonacion) {
        LocalDateTime fechaHoraActual = LocalDateTime.now();

            for (TransaccionTitulo titulo : transaccionDonacion.getTitulos()) {
                Optional<Titulo> tituloOptional = tituloRepository.findById(titulo.getConseTitulo());
                if (tituloOptional.isPresent()) {
                    Titulo tituloDB = tituloOptional.get();
                    int numAcciones = titulo.getNumAcciones(); // Cantidad de acciones a donar para este título

                    // Anular el título utilizado
                    EstadoTitulo estadoTituloAnulado = new EstadoTitulo();
                    estadoTituloAnulado.setIdeEstadoTitulo(3); // Anula el título
                    tituloDB.setEstadoTitulo(estadoTituloAnulado);
                    tituloRepository.save(tituloDB);

                    // Crear un nuevo título con las acciones donadas
                    Titulo nuevoTituloDonado = new Titulo();
                    nuevoTituloDonado.setCanAccTit(numAcciones);
                    nuevoTituloDonado.setValAccTit(tituloDB.getValAccTit());
                    nuevoTituloDonado.setClaAccTit(tituloDB.getClaAccTit());
                    nuevoTituloDonado.setTipAccTit(tituloDB.getTipAccTit());
                    nuevoTituloDonado.setFecCreTit(LocalDate.from(fechaHoraActual));
                    nuevoTituloDonado.setFecFinTit(tituloDB.getFecFinTit());
                    nuevoTituloDonado.setObsAccTit(tituloDB.getObsAccTit());

                    EstadoTitulo estadoTituloDonado = new EstadoTitulo();
                    estadoTituloDonado.setIdeEstadoTitulo(1); // Título con estado activo
                    nuevoTituloDonado.setEstadoTitulo(estadoTituloDonado);

                    tituloRepository.save(nuevoTituloDonado);

                    // Asociar el título donado con los tomadores
                    for (TomadorTitulo tomador : transaccionDonacion.getTomadores()) {
                        Optional<Persona> personaOptional = personaRepository.findById(tomador.getIdePer());
                        personaOptional.ifPresent(persona -> {
                            persona.getTitulos().add(nuevoTituloDonado);
                            personaRepository.save(persona);
                        });
                    }

                    // Crear un nuevo título con las acciones restantes
                    int accionesRestantes = tituloDB.getCanAccTit() - numAcciones;
                    if (accionesRestantes > 0) {
                        Titulo nuevoTituloRestante = new Titulo();
                        nuevoTituloRestante.setCanAccTit(accionesRestantes);
                        nuevoTituloRestante.setValAccTit(tituloDB.getValAccTit());
                        nuevoTituloRestante.setClaAccTit(tituloDB.getClaAccTit());
                        nuevoTituloRestante.setTipAccTit(tituloDB.getTipAccTit());
                        nuevoTituloRestante.setFecCreTit(LocalDate.from(fechaHoraActual));
                        nuevoTituloRestante.setFecFinTit(tituloDB.getFecFinTit());
                        nuevoTituloRestante.setObsAccTit(tituloDB.getObsAccTit());

                        EstadoTitulo estadoTituloRestante = new EstadoTitulo();
                        estadoTituloRestante.setIdeEstadoTitulo(1); // Título con estado activo
                        nuevoTituloRestante.setEstadoTitulo(estadoTituloRestante);

                        tituloRepository.save(nuevoTituloRestante);

                        // Asociar el título restante a las personas asociadas al título original
                        List<Persona> personasAsociadas = personaRepository.findByTitulos(tituloDB);
                        for (Persona persona : personasAsociadas) {
                            persona.getTitulos().add(nuevoTituloRestante);
                            personaRepository.save(persona);
                        }
                    }
                }
            }

            return transaccionDonacion;
    }

    public TransaccionDatos endosarTitulos(TransaccionDatos transaccionEndoso) {

        LocalDateTime fechaHoraActual = LocalDateTime.now();


        for (TransaccionTitulo titulo : transaccionEndoso.getTitulos()) {
            Optional<Titulo> tituloOptional = tituloRepository.findById(titulo.getConseTitulo());
            if (tituloOptional.isPresent()) {
                Titulo tituloDB = tituloOptional.get();
                int numAcciones = titulo.getNumAcciones(); // Cantidad de acciones a donar para este título

                // Anular el título utilizado
                EstadoTitulo estadoTituloAnulado = new EstadoTitulo();
                estadoTituloAnulado.setIdeEstadoTitulo(3); // Anula el título
                tituloDB.setEstadoTitulo(estadoTituloAnulado);
                tituloRepository.save(tituloDB);

                // Crear un nuevo título con las acciones donadas
                Titulo nuevoTituloDonado = new Titulo();
                nuevoTituloDonado.setCanAccTit(numAcciones);
                nuevoTituloDonado.setValAccTit(tituloDB.getValAccTit());
                nuevoTituloDonado.setClaAccTit(tituloDB.getClaAccTit());
                nuevoTituloDonado.setTipAccTit(tituloDB.getTipAccTit());
                nuevoTituloDonado.setFecCreTit(LocalDate.from(fechaHoraActual));
                nuevoTituloDonado.setFecFinTit(tituloDB.getFecFinTit());
                nuevoTituloDonado.setObsAccTit(tituloDB.getObsAccTit());

                EstadoTitulo estadoTituloDonado = new EstadoTitulo();
                estadoTituloDonado.setIdeEstadoTitulo(1); // Título con estado activo
                nuevoTituloDonado.setEstadoTitulo(estadoTituloDonado);

                tituloRepository.save(nuevoTituloDonado);

                // Asociar el título donado con los tomadores
                for (TomadorTitulo tomador : transaccionEndoso.getTomadores()) {
                    Optional<Persona> personaOptional = personaRepository.findById(tomador.getIdePer());
                    personaOptional.ifPresent(persona -> {
                        persona.getTitulos().add(nuevoTituloDonado);
                        personaRepository.save(persona);
                    });
                }

                // Crear un nuevo título con las acciones restantes
                int accionesRestantes = tituloDB.getCanAccTit() - numAcciones;
                if (accionesRestantes > 0) {
                    Titulo nuevoTituloRestante = new Titulo();
                    nuevoTituloRestante.setCanAccTit(accionesRestantes);
                    nuevoTituloRestante.setValAccTit(tituloDB.getValAccTit());
                    nuevoTituloRestante.setClaAccTit(tituloDB.getClaAccTit());
                    nuevoTituloRestante.setTipAccTit(tituloDB.getTipAccTit());
                    nuevoTituloRestante.setFecCreTit(LocalDate.from(fechaHoraActual));
                    nuevoTituloRestante.setFecFinTit(tituloDB.getFecFinTit());
                    nuevoTituloRestante.setObsAccTit(tituloDB.getObsAccTit());

                    EstadoTitulo estadoTituloRestante = new EstadoTitulo();
                    estadoTituloRestante.setIdeEstadoTitulo(1); // Título con estado activo
                    nuevoTituloRestante.setEstadoTitulo(estadoTituloRestante);

                    tituloRepository.save(nuevoTituloRestante);

                    // Asociar el título restante a las personas asociadas al título original
                    List<Persona> personasAsociadas = personaRepository.findByTitulos(tituloDB);
                    for (Persona persona : personasAsociadas) {
                        persona.getTitulos().add(nuevoTituloRestante);
                        personaRepository.save(persona);
                    }
                }
            }
        }
        return transaccionEndoso;
    }

    public TransaccionDatos  sucesionTitulos(TransaccionDatos transaccionSucesion) {

        LocalDateTime fechaHoraActual = LocalDateTime.now();

        for (TransaccionTitulo titulo : transaccionSucesion.getTitulos()) {
            Optional<Titulo> tituloOptional = tituloRepository.findById(titulo.getConseTitulo());
            if (tituloOptional.isPresent()) {
                Titulo tituloDB = tituloOptional.get();
                int numAcciones = titulo.getNumAcciones(); // Cantidad de acciones a donar para este título

                // Anular el título utilizado
                EstadoTitulo estadoTituloAnulado = new EstadoTitulo();
                estadoTituloAnulado.setIdeEstadoTitulo(3); // Anula el título
                tituloDB.setEstadoTitulo(estadoTituloAnulado);
                tituloRepository.save(tituloDB);

                // Crear un nuevo título con las acciones donadas
                Titulo nuevoTituloDonado = new Titulo();
                nuevoTituloDonado.setCanAccTit(numAcciones);
                nuevoTituloDonado.setValAccTit(tituloDB.getValAccTit());
                nuevoTituloDonado.setClaAccTit(tituloDB.getClaAccTit());
                nuevoTituloDonado.setTipAccTit(tituloDB.getTipAccTit());
                nuevoTituloDonado.setFecCreTit(LocalDate.from(fechaHoraActual));
                nuevoTituloDonado.setFecFinTit(tituloDB.getFecFinTit());
                nuevoTituloDonado.setObsAccTit(tituloDB.getObsAccTit());

                EstadoTitulo estadoTituloDonado = new EstadoTitulo();
                estadoTituloDonado.setIdeEstadoTitulo(1); // Título con estado activo
                nuevoTituloDonado.setEstadoTitulo(estadoTituloDonado);

                tituloRepository.save(nuevoTituloDonado);

                // Asociar el título donado con los tomadores
                for (TomadorTitulo tomador : transaccionSucesion.getTomadores()) {
                    Optional<Persona> personaOptional = personaRepository.findById(tomador.getIdePer());
                    personaOptional.ifPresent(persona -> {
                        persona.getTitulos().add(nuevoTituloDonado);
                        personaRepository.save(persona);
                    });
                }

                // Crear un nuevo título con las acciones restantes
                int accionesRestantes = tituloDB.getCanAccTit() - numAcciones;
                if (accionesRestantes > 0) {
                    Titulo nuevoTituloRestante = new Titulo();
                    nuevoTituloRestante.setCanAccTit(accionesRestantes);
                    nuevoTituloRestante.setValAccTit(tituloDB.getValAccTit());
                    nuevoTituloRestante.setClaAccTit(tituloDB.getClaAccTit());
                    nuevoTituloRestante.setTipAccTit(tituloDB.getTipAccTit());
                    nuevoTituloRestante.setFecCreTit(LocalDate.from(fechaHoraActual));
                    nuevoTituloRestante.setFecFinTit(tituloDB.getFecFinTit());
                    nuevoTituloRestante.setObsAccTit(tituloDB.getObsAccTit());

                    EstadoTitulo estadoTituloRestante = new EstadoTitulo();
                    estadoTituloRestante.setIdeEstadoTitulo(1); // Título con estado activo
                    nuevoTituloRestante.setEstadoTitulo(estadoTituloRestante);

                    tituloRepository.save(nuevoTituloRestante);

                    // Asociar el título restante a las personas asociadas al título original
                    List<Persona> personasAsociadas = personaRepository.findByTitulos(tituloDB);
                    for (Persona persona : personasAsociadas) {
                        persona.getTitulos().add(nuevoTituloRestante);
                        personaRepository.save(persona);
                    }
                }
            }
        }
        return transaccionSucesion;
    }

    public Transaccion embargarTitulo(Transaccion transaccionEmbargo) {

        List<TransaccionTitulo> titulos = transaccionEmbargo.getTransaccionTitulo();

        for (TransaccionTitulo titulo : titulos) {
            Optional<Titulo> tituloEncontrado = tituloRepository.findById(titulo.getConseTitulo());
            tituloEncontrado.ifPresent(t -> {
                EstadoTitulo estadoTitulo = new EstadoTitulo();
                estadoTitulo.setIdeEstadoTitulo(4);
                t.setEstadoTitulo(estadoTitulo);
                tituloRepository.save(t);
            });
        }
        TransaccionEstado transaccionEstado = transaccionEstadoRepository.findByDescEstado("inactivo");
        transaccionEmbargo.setEstadoTransaccion(transaccionEstado);
        Transaccion t = transaccionRepository.save(transaccionEmbargo);

        return transaccionEmbargo;
    }

    public class NumeroALetras {
        private static final String[] UNIDADES = {
                "", "uno", "dos", "tres", "cuatro", "cinco", "seis", "siete", "ocho", "nueve"
        };
        private static final String[] DIEZ_A_DIECINUEVE = {
                "diez", "once", "doce", "trece", "catorce", "quince",
                "dieciséis", "diecisiete", "dieciocho", "diecinueve"
        };
        private static final String[] DECENAS = {
                "", "", "veinte", "treinta", "cuarenta", "cincuenta",
                "sesenta", "setenta", "ochenta", "noventa"
        };
        private static final String[] CENTENAS = {
                "", "ciento", "doscientos", "trescientos", "cuatrocientos",
                "quinientos", "seiscientos", "setecientos", "ochocientos", "novecientos"
        };

        public static String convertir(long numero) {
            if (numero == 0) return "cero";
            if (numero == 100) return "cien";
            return convertirNumero(numero).trim();
        }

        private static String convertirNumero(long numero) {
            StringBuilder resultado = new StringBuilder();

            // Billones
            if (numero >= 1_000_000_000_000L) {
                long billones = numero / 1_000_000_000_000L;
                resultado.append(convertirNumero(billones)).append(" billón");
                if (billones > 1) resultado.append("es");
                numero %= 1_000_000_000_000L;
                if (numero > 0) resultado.append(" ");
            }

            // Miles de millones
            if (numero >= 1_000_000_000L) {
                long milesMillones = numero / 1_000_000_000L;
                resultado.append(convertirNumero(milesMillones)).append(" mil millones");
                numero %= 1_000_000_000L;
                if (numero > 0) resultado.append(" ");
            }

            // Millones
            if (numero >= 1_000_000) {
                long millones = numero / 1_000_000;
                resultado.append(convertirNumero(millones)).append(" millón");
                if (millones > 1) resultado.append("es");
                numero %= 1_000_000;
                if (numero > 0) resultado.append(" ");
            }

            // Miles
            if (numero >= 1000) {
                long miles = numero / 1000;
                if (miles == 1) {
                    resultado.append("mil");
                } else {
                    resultado.append(convertirNumero(miles)).append(" mil");
                }
                numero %= 1000;
                if (numero > 0) resultado.append(" ");
            }

            // Centenas
            if (numero >= 100) {
                int centenas = (int)(numero / 100);
                resultado.append(CENTENAS[centenas]);
                numero %= 100;
                if (numero > 0) resultado.append(" ");
            }

            // Decenas y unidades
            if (numero >= 20) {
                int decenas = (int)(numero / 10);
                resultado.append(DECENAS[decenas]);
                numero %= 10;
                if (numero > 0) {
                    if (decenas == 2) {
                        resultado.append("i").append(UNIDADES[(int)numero]); // veintiuno, veintidós...
                    } else {
                        resultado.append(" y ").append(UNIDADES[(int)numero]);
                    }
                }
            } else if (numero >= 10) {
                resultado.append(DIEZ_A_DIECINUEVE[(int)numero - 10]);
            } else if (numero > 0) {
                resultado.append(UNIDADES[(int)numero]);
            }

            return resultado.toString();
        }

        // Método específico para pesos colombianos
        public static String convertirAPesos(long numero) {
            if (numero == 0) return "cero pesos";

            String numeroEnLetras = convertir(numero);

            // Capitalizar primera letra
            numeroEnLetras = numeroEnLetras.substring(0, 1).toUpperCase() + numeroEnLetras.substring(1);

            return numeroEnLetras + " pesos";
        }
    }

    public byte[] getFormatoTitulo(Integer conseTitulo) throws IOException {
        // Buscar el título del cual sobraron las acciones

        Titulo datosTitulo = tituloRepository.findById(conseTitulo).get();
        Optional<TitulosPersona> tituloPersona = Optional.ofNullable(tituloPersonaRepository.findByConseTitulo(datosTitulo.getConseTitulo()));
        Optional<Persona> datosPersona = personaRepository.findById(String.valueOf(tituloPersona.get().getIdePer()));

        File inputHTML = new File("src/main/resources/formatoTituloAcciones.html");
        Document document = Jsoup.parse(inputHTML, "UTF-8");

        //document.selectFirst("#codUsuario").text(datosPersona.getCodUsuario());
        document.selectFirst("#numAcciones").text(String.valueOf(datosTitulo.getCanAccTit()));
        long valorTotal = datosTitulo.getValAccTit() * datosTitulo.getCanAccTit();
        document.selectFirst("#valAccTit").text(String.format("$%,d", valorTotal));
        //document.selectFirst("#valAccTit").text(String.valueOf(datosTitulo.getValAccTit() * datosTitulo.getCanAccTit()));
        document.selectFirst("#conseTitulo").text(String.valueOf(datosTitulo.getConseTitulo()));

        if (datosTitulo.getClaAccTit().equalsIgnoreCase("A")) {
            document.selectFirst("#claseA").text("x");
        }else {
            document.selectFirst("#claseB").text("x");
        }

        if (datosTitulo.getTipAccTit().equalsIgnoreCase("O")) {
            document.selectFirst("#tipAccTitO").text("x");
        }else {
            document.selectFirst("#tipAccTitP").text("x");
        }

        if (datosPersona.get().getNomPri() != null && !datosPersona.get().getNomPri().equalsIgnoreCase("")) {
            document.selectFirst("#nomAccionista").text(
                    datosPersona.get().getNomPri().toUpperCase() + " " +
                            datosPersona.get().getNomSeg().toUpperCase() + " " +
                            datosPersona.get().getApePri().toUpperCase() + " " +
                            datosPersona.get().getApeSeg().toUpperCase());
        } else {
            document.selectFirst("#nomAccionista").text(datosPersona.get().getRazonSocial().toUpperCase());
        }

        document.selectFirst("#codUsuario").text(datosPersona.get().getCodUsuario());
        // Para el campo POR: (formato en letras) - AGREGAR ESTA LÍNEA
        document.selectFirst("#valorEnLetras").text(NumeroALetras.convertirAPesos(valorTotal));
        document.selectFirst("#firmaRepresentanteRepresentanteLegal").html("<img width=\"150\" src=\"data:image/png;base64, " + "<img width=\"150\" src=\"data:image/png;base64, " + Base64.getEncoder().encodeToString(datosPersona.get().getFirma()) + "\">");


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
