package com.eebp.accionistas.backend.financiero.controllers;

import com.eebp.accionistas.backend.financiero.entities.Utilidad;
import com.eebp.accionistas.backend.financiero.services.UtilidadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/utilidades")
public class UtilidadController {

    @Autowired
    UtilidadService utilidadService;

    @PostMapping("/agregar")
    public Utilidad addUtilidad(@RequestBody Utilidad utilidad) {
        return utilidadService.addUtilidad(utilidad);
    }

    @GetMapping("/{id}")
    public Optional<Utilidad> getUtilidad(@PathVariable Integer id) {
        return utilidadService.findUtildadById(id);
    }

    @GetMapping("/pagoUtilidad")
    public ResponseEntity<InputStreamResource> excelPagoUtilidad() throws Exception {
        ByteArrayInputStream stream = utilidadService.excelPagoUtilidad();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=Pago Utilidaad.xls");

        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(stream));
    }
}
