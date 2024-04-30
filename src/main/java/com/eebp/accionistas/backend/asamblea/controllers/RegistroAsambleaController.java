package com.eebp.accionistas.backend.asamblea.controllers;

import com.eebp.accionistas.backend.asamblea.entities.Asamblea;
import com.eebp.accionistas.backend.asamblea.entities.RegistroAsamblea;
import com.eebp.accionistas.backend.asamblea.services.RegistroAsambleaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
}
