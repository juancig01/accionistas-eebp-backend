package com.eebp.accionistas.backend.asamblea.controllers;

import com.eebp.accionistas.backend.acciones.entities.Titulo;
import com.eebp.accionistas.backend.asamblea.entities.Asamblea;
import com.eebp.accionistas.backend.asamblea.services.AsambleaService;
import com.eebp.accionistas.backend.seguridad.entities.Asset;
import com.eebp.accionistas.backend.transacciones.entities.Transaccion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/asamblea")

public class AsambleaController {

    @Autowired
    AsambleaService asambleaService;

    @PostMapping("/crear-asamblea")
    public Asamblea addAsamblea(@RequestBody Asamblea asamblea) {
        return asambleaService.addAsamblea(asamblea);
    }

    @GetMapping("/obtener-asambleas")
    public List<Asamblea> getAsambleas() {
        return asambleaService.getAsambleas();
    }

    @GetMapping("/reportes/{id}")
    public ResponseEntity<Map<String, List<Asset>>> getReportesAsamblea(@PathVariable Integer id) {
        Map<String, List<Asset>> reportes = asambleaService.getReportesAsamblea(id);
        return ResponseEntity.ok(reportes);
    }

    @GetMapping("/formatos")
    public Map<String, List<Asset>> getFormatosActas() {
        return asambleaService.getFormatosActas();
    }

    @PutMapping("/actualizar")
    public Asamblea updateAsamblea(@RequestBody Asamblea asamblea) {
        return asambleaService.updateAsamblea(asamblea);
    }

    @GetMapping("/{id}")
    public Optional<Asamblea> findAsambleaById(@PathVariable Integer id) {
        return asambleaService.findAsambleaById(id);
    }

    @GetMapping("/consecutivo")
    public Map<String, Integer> getConsecutivoAsamblea() {
        Integer ultimoConsecutivo = asambleaService.getConsecutivoAsamblea();
        Map<String, Integer> response = new HashMap<>();
        response.put("ultimoConsecutivo", ultimoConsecutivo);
        return response;
    }

    @GetMapping("/obtener-consecutivo-asamblea")
    public Map<String, Integer> obtenerUltimoConsecutivoAsamblea() {
        Integer ultimoConsecutivo = asambleaService.getUltimoConsecutivoAsamblea();
        Map<String, Integer> response = new HashMap<>();
        response.put("consecutivo", ultimoConsecutivo);
        return response;
    }

    @PostMapping("/enviar-invitacion/{id}")
    public ResponseEntity<Map<String, String>> enviarEmailAccionistas(@PathVariable Integer id) {
        Map<String, String> response = new HashMap<>();
        try {
            Asamblea asamblea = asambleaService.sendEmailAccionistas(id);
            if (asamblea != null) {
                response.put("message", "Correos electrónicos enviados correctamente a los accionistas.");
                return ResponseEntity.ok(response);
            } else {
                response.put("error", "No se encontró la asamblea con el ID proporcionado.");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            response.put("error", "Error al enviar los correos electrónicos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping(value = "/certificado/{codUsuario}", produces = MediaType.APPLICATION_PDF_VALUE)
    public @ResponseBody byte[] getFormatoAcciones(@PathVariable Integer codUsuario) throws IOException {
        return asambleaService.getCertificado(codUsuario);
    }

}
