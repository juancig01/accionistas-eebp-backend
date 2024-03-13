package com.eebp.accionistas.backend.acciones.services;

import com.eebp.accionistas.backend.acciones.entities.EstadoTitulo;
import com.eebp.accionistas.backend.acciones.entities.Titulo;
import com.eebp.accionistas.backend.acciones.entities.TitulosPersona;
import com.eebp.accionistas.backend.acciones.entities.TomadorTitulo;
import com.eebp.accionistas.backend.acciones.repositories.TituloPersonaRepository;
import com.eebp.accionistas.backend.acciones.repositories.TituloRepository;
import com.eebp.accionistas.backend.accionistas.entities.Persona;
import com.eebp.accionistas.backend.accionistas.repositories.PersonaRepository;
import com.eebp.accionistas.backend.transacciones.entities.*;
import com.eebp.accionistas.backend.transacciones.repositories.TransaccionEstadoRepository;
import com.eebp.accionistas.backend.transacciones.repositories.TransaccionRepository;
import com.eebp.accionistas.backend.transacciones.repositories.TransaccionTituloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;


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

    public Optional<Titulo> findTituloById(Integer id) {
        return tituloRepository.findById(id);
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


    public void comprarAcciones(TransaccionDatos transaccionCompra) {
        LocalDateTime fechaHoraActual = LocalDateTime.now();

        int cantAccionesAComprar = transaccionCompra.getCantAcciones();
        int accionesCompradas = 0;
        Titulo tituloCompradas = null;
        Titulo tituloDisponibles = null;

        Optional<Persona> personaCorrespondienteOptional = null;
        List<Persona> personasAsociadas = null;
        List<Persona> personasA = new ArrayList<>();

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

                        // Obtener todas las personas asociadas a ese título
                        personasA = personaRepository.findByTitulos(tituloDB);

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
        for (Persona persona : personasA) {
            final Titulo tituloDisponible = tituloDisponibles;
            Optional<Persona> personaOptionalA = personaRepository.findById(persona.getCodUsuario());
            personaOptionalA.ifPresent(persona1 -> {
                persona1.getTitulos().add(tituloDisponible); // Usar la variable final dentro de la expresión lambda
            });
        }

        // Asociar el nuevo título a los tomadores
        for (TomadorTitulo tomador : transaccionCompra.getTomadores()) {
            final Titulo tituloCompradasFinal = tituloCompradas; // Declarar la variable final aquí
            Optional<Persona> personaOptional = personaRepository.findById(tomador.getIdePer());
            personaOptional.ifPresent(persona -> {
                persona.getTitulos().add(tituloCompradasFinal); // Usar la variable final dentro de la expresión lambda
            });
        }

        //Crear la transaccion
        TomadorTitulo primerTomador = transaccionCompra.getTomadores().get(0);

        Titulo tituloTrasanccion = tituloRepository.save(tituloCompradas);

        TipoTransaccion tipoTransaccion = new TipoTransaccion();
        tipoTransaccion.setCodTipTran(2);

        TransaccionEstado transaccionEstado = new TransaccionEstado();
        transaccionEstado.setIdeEstado(4);

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

    }

    public void donarAcciones(TransaccionDatos transaccionDonacion) {
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
    }

    public void endosarTitulos(TransaccionDatos transaccionEndoso) {

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
    }

    public void  sucesionTitulos(TransaccionDatos transaccionSucesion) {

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

    }

    public void embargarTitulo(Transaccion transaccionEmbargo) {

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
    }

}
