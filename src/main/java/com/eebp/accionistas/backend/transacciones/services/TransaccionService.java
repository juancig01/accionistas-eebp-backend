package com.eebp.accionistas.backend.transacciones.services;

import com.eebp.accionistas.backend.transacciones.entities.Transaccion;
import com.eebp.accionistas.backend.transacciones.entities.TransaccionTitulo;
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

    public List<Transaccion> getTransaccion() {
        return transaccionRepository.findAll();
    }

    public Transaccion addTransaccion(Transaccion transaccion) {
        Transaccion t = transaccionRepository.save(transaccion);
        t.getTransaccionTitulo().stream()
                .forEach(transaccionTitulo -> transaccionTitulo.setConseTrans(t.getConseTrans()));
        transaccionTituloRepository.saveAll(t.getTransaccionTitulo());
        return t;
    }

    public Optional<Transaccion> findTransaccionById(Integer id) {
        Transaccion t = transaccionRepository.findById(id).get();
        t.setTransaccionTitulo(transaccionTituloRepository.findByConseTrans(t.getConseTrans()));
        return transaccionRepository.findById(id);
    }

}
