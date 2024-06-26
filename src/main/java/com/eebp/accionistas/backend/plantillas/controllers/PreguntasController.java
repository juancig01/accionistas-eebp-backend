package com.eebp.accionistas.backend.plantillas.controllers;

import com.eebp.accionistas.backend.plantillas.entities.PreguntaDTO;
import com.eebp.accionistas.backend.plantillas.entities.Preguntas;
import com.eebp.accionistas.backend.plantillas.services.PreguntasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/encuesta")
public class PreguntasController {

    @Autowired
    PreguntasService preguntasService;

    @GetMapping("/pregunta/{id}")
    public Optional<Preguntas> getPregunta(@PathVariable Integer id) {
        return preguntasService.getPregunta(id);
    }

    @PostMapping("/guardarPregunta")
    public ResponseEntity<Map<String, Object>> guardarPregunta(@RequestBody PreguntaDTO request) {
        preguntasService.guardarPreguntaConOpciones(request);
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Pregunta y opciones de respuesta guardadas correctamente");
        respuesta.put("codigo", HttpStatus.OK.value());
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }

    @GetMapping("/preguntas/{idPregunta}")
    public ResponseEntity<Map<String, List<Map<String, Object>>>> obtenerOpcionesPorIdPregunta(@PathVariable Integer idPregunta) {
        Map<String, List<Map<String, Object>>> opcionesRespuesta = preguntasService.obtenerOpcionesPorIdPregunta(idPregunta);
        return new ResponseEntity<>(opcionesRespuesta, HttpStatus.OK);
    }
}
