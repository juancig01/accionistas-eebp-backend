package com.eebp.accionistas.backend.acciones.controllers;

import com.eebp.accionistas.backend.acciones.entities.Titulo;
import com.eebp.accionistas.backend.acciones.services.TituloService;
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
    public ResponseEntity<String> comprarTitulos(@RequestBody List<TransaccionDatos> transaccionesCompra) {
        tituloService.comprarAcciones(transaccionesCompra);
        return ResponseEntity.status(HttpStatus.OK).body("Títulos comprados exitosamente.");
    }


    @PostMapping("/donar")
    public ResponseEntity<String> donarTitulos(@RequestBody List<TransaccionDatos> transaccionDatos) {
        tituloService.donarAcciones(transaccionDatos);
        return ResponseEntity.ok("Titulos donados exitosamente.");
    }

    @PostMapping("/endosar")
    public ResponseEntity<String> endosarTitulos(@RequestBody List<TransaccionDatos> transaccionDatos) {
        tituloService.endosarTitulos(transaccionDatos);
        return ResponseEntity.ok("Titulos endosados exitosamente.");
    }

    @PostMapping("/sucesion")
    public ResponseEntity<String> sucesionTitulos(@RequestBody List<TransaccionDatos> transaccionDatos) {
        tituloService.sucesionTitulos(transaccionDatos);
        return ResponseEntity.ok("Titulos endosados exitosamente.");
    }

    @PostMapping("/embargar")
    public ResponseEntity<String> embargarTitulos(@RequestBody List<Titulo> titulos) {
        tituloService.embargarTitulo(titulos);
        return ResponseEntity.ok("Titulos embargados exitosamente.");
    }

}
