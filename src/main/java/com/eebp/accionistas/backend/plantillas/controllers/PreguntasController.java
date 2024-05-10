package com.eebp.accionistas.backend.plantillas.controllers;

import com.eebp.accionistas.backend.plantillas.entities.PreguntaDTO;
import com.eebp.accionistas.backend.plantillas.entities.Preguntas;
import com.eebp.accionistas.backend.plantillas.services.PreguntasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/encuesta")
public class PreguntasController {

    @Autowired
    PreguntasService preguntasService;

    @PostMapping("/guardarPregunta")
    public ResponseEntity<Map<String, Object>> guardarPregunta(@RequestBody PreguntaDTO request) {
        preguntasService.guardarPreguntaConOpciones(request);
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Pregunta y opciones de respuesta guardadas correctamente");
        respuesta.put("codigo", HttpStatus.OK.value());
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }
}
