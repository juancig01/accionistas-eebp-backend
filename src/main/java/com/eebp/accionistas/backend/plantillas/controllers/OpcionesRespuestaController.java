package com.eebp.accionistas.backend.plantillas.controllers;

import com.eebp.accionistas.backend.plantillas.entities.OpcionesRespuesta;
import com.eebp.accionistas.backend.plantillas.entities.Preguntas;
import com.eebp.accionistas.backend.plantillas.services.OpcionesRespuestaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/encuesta")
public class OpcionesRespuestaController {

    @Autowired
    OpcionesRespuestaService opcionesRespuestaService;

    @PostMapping("/agregar-opciones")
    public ResponseEntity<OpcionesRespuesta> agregarRespuestas(@RequestBody OpcionesRespuesta opcionesRespuesta) {
        OpcionesRespuesta nuevaRespuesta = opcionesRespuestaService.addOpciones(opcionesRespuesta);
        return ResponseEntity.ok().body(nuevaRespuesta);
    }
}
