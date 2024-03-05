package com.eebp.accionistas.backend.acciones.services;

import com.eebp.accionistas.backend.acciones.entities.EstadoTitulo;
import com.eebp.accionistas.backend.acciones.entities.Titulo;

import com.eebp.accionistas.backend.acciones.repositories.TituloRepository;
import com.eebp.accionistas.backend.accionistas.entities.Persona;
import com.eebp.accionistas.backend.accionistas.repositories.PersonaRepository;
import com.eebp.accionistas.backend.transacciones.entities.TransaccionDatos;
import com.eebp.accionistas.backend.transacciones.entities.TransaccionTitulo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class TituloService {

    @Autowired
    TituloRepository tituloRepository;

    @Autowired
    PersonaRepository personaRepository;

    public Optional<Titulo> findTituloById(Integer id) {
        return tituloRepository.findById(id);
    }

    public void comprarAcciones(List<TransaccionDatos> transacciones) {
        LocalDateTime fechaHoraActual = LocalDateTime.now();

        for (TransaccionDatos transaccionDatos : transacciones) {
            Optional<Titulo> titulo = tituloRepository.findById(transaccionDatos.getConseTitulo());
            Optional<Persona> personaOptional = personaRepository.findById(transaccionDatos.getIdePer());

            titulo.ifPresent(t -> {
                if (t.getCanAccTit() >= transaccionDatos.getNumAcciones()) {
                    Integer accionesRestantes = t.getCanAccTit() - transaccionDatos.getNumAcciones();

                    if (accionesRestantes > 0) {
                        EstadoTitulo estadoTituloAnulado = new EstadoTitulo();
                        estadoTituloAnulado.setIdeEstadoTitulo(3); // Anula el titulo
                        titulo.get().setEstadoTitulo(estadoTituloAnulado);

                        Titulo tituloNuevo = new Titulo();
                        tituloNuevo.setCanAccTit(accionesRestantes);
                        tituloNuevo.setValAccTit(t.getValAccTit());
                        tituloNuevo.setClaAccTit(t.getClaAccTit());
                        tituloNuevo.setTipAccTit(t.getTipAccTit());
                        tituloNuevo.setFecCreTit(LocalDate.from(fechaHoraActual));
                        tituloNuevo.setFecFinTit(t.getFecFinTit());
                        tituloNuevo.setObsAccTit("NINGUNA");

                        EstadoTitulo estadoTitulo = new EstadoTitulo();
                        estadoTitulo.setIdeEstadoTitulo(1); //titulo con estado activo
                        tituloNuevo.setEstadoTitulo(estadoTitulo);

                        personaOptional.ifPresent(persona -> {
                            persona.getTitulos().add(tituloNuevo);
                        });

                        tituloRepository.save(tituloNuevo);
                    }
                }
            });
        }
    }

    public void donarTitulos(List<TransaccionDatos> transacciones) {

        for (TransaccionDatos transaccionDatos: transacciones) {

            LocalDateTime fechaHoraActual = LocalDateTime.now();
            Optional<Titulo> titulo = tituloRepository.findById(transaccionDatos.getConseTitulo());
            Optional<Persona> personaOptional = personaRepository.findById(transaccionDatos.getIdePer());

            titulo.ifPresent(t -> {

                EstadoTitulo estadoTituloAnulado = new EstadoTitulo();
                estadoTituloAnulado.setIdeEstadoTitulo(3); // Anula el titulo
                titulo.get().setEstadoTitulo(estadoTituloAnulado);

                Titulo tituloNuevo = new Titulo();
                tituloNuevo.setCanAccTit(t.getCanAccTit());
                tituloNuevo.setValAccTit(t.getValAccTit());
                tituloNuevo.setClaAccTit(t.getClaAccTit());
                tituloNuevo.setTipAccTit(t.getTipAccTit());
                tituloNuevo.setFecCreTit(LocalDate.from(fechaHoraActual));
                tituloNuevo.setFecFinTit(t.getFecFinTit());
                tituloNuevo.setObsAccTit("NINGUNA");

                EstadoTitulo estadoTitulo = new EstadoTitulo();
                estadoTitulo.setIdeEstadoTitulo(1); //titulo con estado activo
                tituloNuevo.setEstadoTitulo(estadoTitulo);

                personaOptional.ifPresent(persona -> {
                    persona.getTitulos().add(tituloNuevo);
                });

                tituloRepository.save(tituloNuevo);
            });
        }

    }

    public void endosarTitulos(List<TransaccionDatos> transacciones) {

        for (TransaccionDatos transaccionDatos: transacciones) {

            LocalDateTime fechaHoraActual = LocalDateTime.now();
            Optional<Titulo> titulo = tituloRepository.findById(transaccionDatos.getConseTitulo());
            Optional<Persona> personaOptional = personaRepository.findById(transaccionDatos.getIdePer());

            titulo.ifPresent(t -> {

                EstadoTitulo estadoTituloAnulado = new EstadoTitulo();
                estadoTituloAnulado.setIdeEstadoTitulo(3); // Anula el titulo
                titulo.get().setEstadoTitulo(estadoTituloAnulado);

                Titulo tituloNuevo = new Titulo();
                tituloNuevo.setCanAccTit(t.getCanAccTit());
                tituloNuevo.setValAccTit(t.getValAccTit());
                tituloNuevo.setClaAccTit(t.getClaAccTit());
                tituloNuevo.setTipAccTit(t.getTipAccTit());
                tituloNuevo.setFecCreTit(LocalDate.from(fechaHoraActual));
                tituloNuevo.setFecFinTit(t.getFecFinTit());
                tituloNuevo.setObsAccTit("NINGUNA");

                EstadoTitulo estadoTitulo = new EstadoTitulo();
                estadoTitulo.setIdeEstadoTitulo(1); //titulo con estado activo
                tituloNuevo.setEstadoTitulo(estadoTitulo);

                personaOptional.ifPresent(persona -> {
                    persona.getTitulos().add(tituloNuevo);
                });

                tituloRepository.save(tituloNuevo);
            });
        }

    }

    public void  sucesionTitulos(List<TransaccionDatos> transacciones) {

        for (TransaccionDatos transaccionDatos: transacciones) {

            LocalDateTime fechaHoraActual = LocalDateTime.now();
            Optional<Titulo> titulo = tituloRepository.findById(transaccionDatos.getConseTitulo());
            Optional<Persona> personaOptional = personaRepository.findById(transaccionDatos.getIdePer());

            titulo.ifPresent(t -> {

                EstadoTitulo estadoTituloAnulado = new EstadoTitulo();
                estadoTituloAnulado.setIdeEstadoTitulo(3); // Anula el titulo
                titulo.get().setEstadoTitulo(estadoTituloAnulado);

                Titulo tituloNuevo = new Titulo();
                tituloNuevo.setCanAccTit(t.getCanAccTit());
                tituloNuevo.setValAccTit(t.getValAccTit());
                tituloNuevo.setClaAccTit(t.getClaAccTit());
                tituloNuevo.setTipAccTit(t.getTipAccTit());
                tituloNuevo.setFecCreTit(LocalDate.from(fechaHoraActual));
                tituloNuevo.setFecFinTit(t.getFecFinTit());
                tituloNuevo.setObsAccTit("NINGUNA");

                EstadoTitulo estadoTitulo = new EstadoTitulo();
                estadoTitulo.setIdeEstadoTitulo(1); //titulo con estado activo
                tituloNuevo.setEstadoTitulo(estadoTitulo);

                personaOptional.ifPresent(persona -> {
                    persona.getTitulos().add(tituloNuevo);
                });

                tituloRepository.save(tituloNuevo);
            });
        }

    }

    public void embargarTitulo(List<Titulo> titulos) {

        for (Titulo titulo: titulos) {

            Optional<Titulo> tituloEncontrado = tituloRepository.findById(titulo.getConseTitulo());

            tituloEncontrado.ifPresent(t -> {
                EstadoTitulo estadoTitulo = new EstadoTitulo();
                estadoTitulo.setIdeEstadoTitulo(4);
                t.setEstadoTitulo(estadoTitulo);

                tituloRepository.save(t);

            });
        }
    }

}
