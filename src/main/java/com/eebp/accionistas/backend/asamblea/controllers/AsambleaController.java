package com.eebp.accionistas.backend.asamblea.controllers;

import com.eebp.accionistas.backend.asamblea.entities.Asamblea;
import com.eebp.accionistas.backend.asamblea.services.AsambleaService;
import com.eebp.accionistas.backend.transacciones.entities.Transaccion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @GetMapping("/obtener-consecutivo-asamblea")
    public Map<String, Integer> obtenerUltimoConsecutivoAsamblea() {
        Integer ultimoConsecutivo = asambleaService.getUltimoConsecutivoAsamblea();
        Map<String, Integer> response = new HashMap<>();
        response.put("consecutivo", ultimoConsecutivo);
        return response;
    }

    @PostMapping("/enviar-invitacion/{id}")
    public ResponseEntity<String> enviarEmailAccionistas(@PathVariable Integer id) {
        try {
            Asamblea asamblea = asambleaService.sendEmailAccionistas(id);
            if (asamblea != null) {
                return ResponseEntity.ok("Correos electrónicos enviados correctamente a los accionistas.");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al enviar los correos electrónicos.");
        }
    }

}
