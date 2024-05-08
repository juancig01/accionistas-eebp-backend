package com.eebp.accionistas.backend.plantillas.controllers;

import com.eebp.accionistas.backend.plantillas.entities.Preguntas;
import com.eebp.accionistas.backend.plantillas.services.PreguntasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/encuesta")
public class PreguntasController {

    @Autowired
    PreguntasService preguntasService;

    @PostMapping("/agregar-preguntas")
    public ResponseEntity<Preguntas> agregarPregunta(@RequestBody Preguntas pregunta) {
        Preguntas nuevaPregunta = preguntasService.addPreguntas(pregunta);
        return ResponseEntity.ok().body(nuevaPregunta);
    }
}
