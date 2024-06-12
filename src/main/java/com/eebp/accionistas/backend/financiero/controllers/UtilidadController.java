package com.eebp.accionistas.backend.financiero.controllers;

import com.eebp.accionistas.backend.financiero.entities.Utilidad;
import com.eebp.accionistas.backend.financiero.services.UtilidadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/utilidades")
public class UtilidadController {

    @Autowired
    UtilidadService utilidadService;

    @PostMapping("/agregar")
    public Utilidad addUtilidad(@RequestBody Map<String, Object> utilidadData) {
        return utilidadService.addUtilidad(utilidadData);
    }

    @GetMapping("/ultimaUtilidad")
    public Utilidad getUltimaUtilidad() {
        return utilidadService.getUltimaUtilidad();
    }

    @GetMapping("/obtener-utilidades")
    public List<Utilidad> getAsambleas() {
        return utilidadService.getUtilidades();
    }

    @GetMapping("/{id}")
    public Optional<Utilidad> getUtilidad(@PathVariable Integer id) {
        return utilidadService.findUtildadById(id);
    }

    @GetMapping("/pagoUtilidad/{anio}")
    public ResponseEntity<InputStreamResource> excelPagoUtilidad(@PathVariable int anio) throws Exception {
        ByteArrayInputStream stream = utilidadService.excelPagoUtilidad(anio);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=Pago_Utilidad_" + anio + ".xls");

        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(stream));
    }

    @GetMapping("/formato1010/{anio}")
    public ResponseEntity<InputStreamResource> downloadFormato1010(@PathVariable int anio) throws Exception {
        ByteArrayInputStream stream = utilidadService.generarFormato1010(anio);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=Formato_1010_" + anio + ".xls");

        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(stream));
    }

    @GetMapping("/formato1001/{anio}")
    public ResponseEntity<InputStreamResource> downloadFormato1001(@PathVariable int anio) throws Exception {
        ByteArrayInputStream stream = utilidadService.generarFormato1010(anio);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=Formato_1001_" + anio + ".xls");

        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(stream));
    }

    @GetMapping("/financiero/{anio}")
    public ResponseEntity<InputStreamResource> downloadMultipleFiles(@PathVariable int anio) throws Exception {
        ByteArrayInputStream zipStream = utilidadService.generateMultipleFilesInZip(anio);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=Archivos_Utilidad_" + anio + ".zip");

        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(zipStream));
    }
}
