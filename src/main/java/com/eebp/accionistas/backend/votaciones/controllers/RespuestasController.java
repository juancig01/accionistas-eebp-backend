package com.eebp.accionistas.backend.votaciones.controllers;

import com.eebp.accionistas.backend.votaciones.services.RespuestasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/respuestas")
public class RespuestasController {

    @Autowired
    RespuestasService respuestasService;

    @PostMapping("/guardarRespuestas")
    public ResponseEntity<String> guardarRespuestas(@RequestBody Map<String, Object> datos) {
        try {
            @SuppressWarnings("unchecked")
            List<String> votantes = (List<String>) datos.get("votantes");
            @SuppressWarnings("unchecked")
            Map<String, List<Map<String, Object>>> respuestas = (Map<String, List<Map<String, Object>>>) datos.get("respuestas");

            respuestasService.guardarRespuestas(respuestas, votantes);
            return new ResponseEntity<>("Respuestas guardadas exitosamente", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al guardar las respuestas: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/encuesta/{idEncuesta}")
    public List<Map<String, Object>> obtenerPreguntasOpcionesYVotosPorEncuesta(@PathVariable Integer idEncuesta) {
        return respuestasService.obtenerPreguntasOpcionesYVotosPorEncuesta(idEncuesta);
    }
}
