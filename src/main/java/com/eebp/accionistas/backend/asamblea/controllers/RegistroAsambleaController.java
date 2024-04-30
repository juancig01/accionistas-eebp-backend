package com.eebp.accionistas.backend.asamblea.controllers;

import com.eebp.accionistas.backend.asamblea.entities.Asamblea;
import com.eebp.accionistas.backend.asamblea.entities.AsistentesAsambleaDTO;
import com.eebp.accionistas.backend.asamblea.entities.RegistroAsamblea;
import com.eebp.accionistas.backend.asamblea.services.RegistroAsambleaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/obtener-asitentes-asamblea")
    public ResponseEntity<List<AsistentesAsambleaDTO>> obtenerAsistentesAsamblea() {
        List<AsistentesAsambleaDTO> asistentes = registroAsambleaService.obtenerRegistroAsamblea();
        return ResponseEntity.ok(asistentes);
    }
}
