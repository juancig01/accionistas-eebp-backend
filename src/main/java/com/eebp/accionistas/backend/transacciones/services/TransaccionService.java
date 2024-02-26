package com.eebp.accionistas.backend.transacciones.services;

import com.eebp.accionistas.backend.acciones.entities.EstadoTitulo;
import com.eebp.accionistas.backend.acciones.entities.Titulo;
import com.eebp.accionistas.backend.acciones.repositories.TituloRepository;
import com.eebp.accionistas.backend.transacciones.entities.Transaccion;
import com.eebp.accionistas.backend.transacciones.entities.TransaccionEstado;
import com.eebp.accionistas.backend.transacciones.entities.TransaccionTitulo;
import com.eebp.accionistas.backend.transacciones.repositories.TransaccionEstadoRepository;
import com.eebp.accionistas.backend.transacciones.repositories.TransaccionRepository;
import com.eebp.accionistas.backend.transacciones.repositories.TransaccionTituloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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

    public List<Transaccion> getTransaccionesTramite() {
        List<Transaccion> transacciones = transaccionRepository.findAll();
        List<Transaccion> transaccionesTramite = new ArrayList<>();

        for (Transaccion transaccion : transacciones) {
            TransaccionEstado estadoTransaccion = transaccion.getEstadoTransaccion();
            if (estadoTransaccion != null && estadoTransaccion.getDescEstado().equals("En tramite")) {
                List<TransaccionTitulo> transaccionTitulos = transaccionTituloRepository.findByConseTrans(transaccion.getConseTrans());
                transaccion.setTransaccionTitulo(transaccionTitulos);
                transaccionesTramite.add(transaccion);
            }
        }
        return transaccionesTramite;
    }

    public List<Transaccion> getTransaccionesAprobado() {
        List<Transaccion> transacciones = transaccionRepository.findAll();
        List<Transaccion> transaccionesAprobado = new ArrayList<>();

        for (Transaccion transaccion : transacciones) {
            TransaccionEstado estadoTransaccion = transaccion.getEstadoTransaccion();
            if (estadoTransaccion != null && estadoTransaccion.getDescEstado().equals("Aprobado")) {
                List<TransaccionTitulo> transaccionTitulos = transaccionTituloRepository.findByConseTrans(transaccion.getConseTrans());
                transaccion.setTransaccionTitulo(transaccionTitulos);
                transaccionesAprobado.add(transaccion);
            }
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
        t.setTransaccionTitulo(transaccionTituloRepository.findByConseTrans(t.getConseTrans()));
        return transaccionRepository.findById(id);
    }

    public Transaccion updateTransaccion(Transaccion transaccion) {
        Optional<TransaccionEstado> estadoTransaccionOptional = transaccionEstadoRepository
                .findById(transaccion.getEstadoTransaccion().getIdeEstado());
        TransaccionEstado estadoTransaccion = estadoTransaccionOptional.get();
        transaccion.setEstadoTransaccion(estadoTransaccion);
        return transaccionRepository.save(transaccion);
    }

}
