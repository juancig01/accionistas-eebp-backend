package com.eebp.accionistas.backend.votaciones.controllers;

import com.eebp.accionistas.backend.votaciones.services.RespuestasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/respuestas")
public class RespuestasController {

    @Autowired
    RespuestasService respuestasService;

    @PostMapping("/guardarRespuestas")
    public ResponseEntity<Map<String, String>> guardarRespuestas(@RequestBody Map<String, Object> datos) {
        try {
            @SuppressWarnings("unchecked")
            List<Map<String, String>> datosPoderdantes = (List<Map<String, String>>) datos.get("datosPoderdantes");
            @SuppressWarnings("unchecked")
            List<Map<String, String>> datosVotante = (List<Map<String, String>>) datos.get("datosVotante");

            List<String> votantes = new ArrayList<>();
            votantes.addAll(datosPoderdantes.stream().map(m -> m.get("codUsuario")).collect(Collectors.toList()));
            votantes.addAll(datosVotante.stream().map(m -> m.get("codUsuario")).collect(Collectors.toList()));

            Map<String, List<Map<String, Object>>> respuestas = new HashMap<>();

            for (Map.Entry<String, Object> entry : datos.entrySet()) {
                String categoria = entry.getKey();
                if (!categoria.equals("datosPoderdantes") && !categoria.equals("datosVotante")) {
                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> preguntas = (List<Map<String, Object>>) entry.getValue();
                    respuestas.put(categoria, preguntas);
                }
            }

            respuestasService.guardarRespuestas(respuestas, votantes);
            Map<String, String> respuestaJson = new HashMap<>();
            respuestaJson.put("message", "Respuestas guardadas exitosamente");
            return ResponseEntity.ok(respuestaJson);
        } catch (Exception e) {
            Map<String, String> respuestaJson = new HashMap<>();
            respuestaJson.put("message", "Error al guardar las respuestas: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuestaJson);
        }
    }

    @GetMapping("/encuesta/{idEncuesta}")
    public List<Map<String, Object>> obtenerPreguntasOpcionesYVotosPorEncuesta(@PathVariable Integer idEncuesta) {
        return respuestasService.obtenerPreguntasOpcionesYVotosPorEncuesta(idEncuesta);
    }
}
