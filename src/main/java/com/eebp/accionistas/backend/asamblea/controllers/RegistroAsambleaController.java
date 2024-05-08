package com.eebp.accionistas.backend.asamblea.controllers;

import com.eebp.accionistas.backend.asamblea.entities.AsistentesAsambleaDTO;
import com.eebp.accionistas.backend.asamblea.entities.RegistroAsamblea;
import com.eebp.accionistas.backend.asamblea.services.RegistroAsambleaService;
import com.eebp.accionistas.backend.seguridad.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/asamblea")
public class RegistroAsambleaController {

    @Autowired
    RegistroAsambleaService registroAsambleaService;

    @PostMapping("/registrar-asistente-asamblea")
    public RegistroAsamblea addRegistroAsamblea(@RequestBody RegistroAsamblea registroAsamblea) {
        return registroAsambleaService.addRegistroAsamblea(registroAsamblea);
    }

    @PutMapping("/actualizar-asistente-asamblea")
    public RegistroAsamblea updateRegistroAsamblea(@RequestBody RegistroAsamblea registroAsamblea) {
        return registroAsambleaService.updateRegistroAsamblea(registroAsamblea);
    }

    @GetMapping("/obtener-asitentes-asamblea")
    public ResponseEntity<List<AsistentesAsambleaDTO>> obtenerAsistentesAsamblea() {
        List<AsistentesAsambleaDTO> asistentes = registroAsambleaService.obtenerRegistroAsamblea();
        return ResponseEntity.ok(asistentes);
    }

    @GetMapping("/obtener-datos-asamblea")
    public ResponseEntity<Map<String, Integer>> obtenerTotales() throws UserNotFoundException {
        List<AsistentesAsambleaDTO> registros = registroAsambleaService.obtenerRegistroAsamblea();
        Map<String, Integer> totales = registroAsambleaService.obtenerTotales(registros);
        return ResponseEntity.ok(totales);
    }
}
