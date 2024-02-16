package com.eebp.accionistas.backend.transacciones.controllers;

import com.eebp.accionistas.backend.transacciones.entities.Transaccion;
import com.eebp.accionistas.backend.transacciones.services.TransaccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/transaccion")
public class TransaccionController {

    @Autowired
    TransaccionService transaccionService;

    @GetMapping
    public List<Transaccion> getTransaccion() {
        return transaccionService.getTransaccion();
    }

    @GetMapping("/{id}")
    public Optional<Transaccion> findTransaccionById(@PathVariable Integer id) {
        return transaccionService.findTransaccionById(id);
    }

    @PutMapping
    public Transaccion addTransaccion(@RequestBody Transaccion transaccion) {
        return transaccionService.addTransaccion(transaccion);
    }
}
