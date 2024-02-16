package com.eebp.accionistas.backend.transacciones.services;

import com.eebp.accionistas.backend.transacciones.entities.TipoTransaccion;
import com.eebp.accionistas.backend.transacciones.entities.Transaccion;
import com.eebp.accionistas.backend.transacciones.repositories.TipoTransaccionRepository;
import com.eebp.accionistas.backend.transacciones.repositories.TransaccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransaccionService {

    @Autowired
    TransaccionRepository transaccionRepository;

    public List<Transaccion> getTransaccion() {
        return transaccionRepository.findAll();
    }

    public Transaccion addTransaccion(Transaccion transaccion) {
        return transaccionRepository.save(transaccion);
    }

    public Optional<Transaccion> findTransaccionById(Integer id) {
        return transaccionRepository.findById(id);
    }

}
