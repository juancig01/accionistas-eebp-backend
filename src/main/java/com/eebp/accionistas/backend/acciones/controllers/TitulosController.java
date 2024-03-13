package com.eebp.accionistas.backend.acciones.controllers;

import com.eebp.accionistas.backend.acciones.entities.Titulo;
import com.eebp.accionistas.backend.acciones.services.TituloService;
import com.eebp.accionistas.backend.transacciones.entities.Transaccion;
import com.eebp.accionistas.backend.transacciones.entities.TransaccionDatos;
import com.eebp.accionistas.backend.transacciones.entities.TransaccionTitulo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/titulos")
public class TitulosController {

    @Autowired
    TituloService tituloService;

    @GetMapping("/{id}")
    public Optional<Titulo> findTituloById(@PathVariable Integer id) {
        return tituloService.findTituloById(id);
    }

    @PostMapping("/comprar")
    public ResponseEntity<String> comprarTitulos(@RequestBody TransaccionDatos transaccionCompra) {
        tituloService.comprarAcciones(transaccionCompra);
        return ResponseEntity.status(HttpStatus.OK).body("Títulos comprados exitosamente.");
    }

    @PostMapping("/donar")
    public ResponseEntity<String> donarTitulos(@RequestBody TransaccionDatos transaccionDonacion) {
        tituloService.donarAcciones(transaccionDonacion);
        return ResponseEntity.ok("Titulos donados exitosamente.");
    }

    @PostMapping("/endosar")
    public ResponseEntity<String> endosarTitulos(@RequestBody TransaccionDatos transaccionEndoso) {
        tituloService.endosarTitulos(transaccionEndoso);
        return ResponseEntity.ok("Titulos endosados exitosamente.");
    }

    @PostMapping("/sucesion")
    public ResponseEntity<String> sucesionTitulos(@RequestBody TransaccionDatos transaccionSucesion) {
        tituloService.sucesionTitulos(transaccionSucesion);
        return ResponseEntity.ok("Titulos endosados exitosamente.");
    }

    @PostMapping("/embargar")
    public ResponseEntity<String> embargarTitulos(@RequestBody Transaccion transaccionEmbargo) {
        tituloService.embargarTitulo(transaccionEmbargo);
        return ResponseEntity.ok("Titulos embargados exitosamente.");
    }

}
