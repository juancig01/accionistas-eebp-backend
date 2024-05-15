package com.eebp.accionistas.backend.plantillas.controllers;

import com.eebp.accionistas.backend.plantillas.entities.EncuestTemasDTO;
import com.eebp.accionistas.backend.plantillas.entities.Encuesta;
import com.eebp.accionistas.backend.plantillas.entities.EncuestaDTO;
import com.eebp.accionistas.backend.plantillas.services.EncuestaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/encuesta")
public class EncuestaController {

    @Autowired
    EncuestaService encuestaService;
    @PostMapping("/agregar")
    public ResponseEntity<Encuesta> crearEncuesta(@RequestBody EncuestaDTO encuestaDto) {
        Encuesta encuesta = encuestaService.crearEncuesta(encuestaDto);
        return ResponseEntity.ok(encuesta);
    }

    @GetMapping("/{idAsamblea}")
    public ResponseEntity<?> getEncuestasByAsamblea(@PathVariable Integer idAsamblea) {
        List<EncuestTemasDTO> encuestas = encuestaService.getEncuestasDTOByAsambleaId(idAsamblea);
        if (encuestas == null || encuestas.isEmpty()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("mensaje", "No se encontraron encuestas para la asamblea especificada");
            errorResponse.put("codigo", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        } else {
            return ResponseEntity.ok(encuestas);
        }
    }

    @GetMapping("/resumen/{consecutivoAsamblea}")
    public Map<String, List<Map<String, Object>>> obtenerPreguntasPorAsamblea(@PathVariable Integer consecutivoAsamblea) {
        return encuestaService.obtenerPreguntasPorAsambleaFormateadas(consecutivoAsamblea);
    }

    @GetMapping("/asignada/{idPersona}")
    public ResponseEntity<Map<String, List<Map<String, Object>>>> obtenerEncuestasYRespuestas(@PathVariable Integer idPersona) {
        Map<String, List<Map<String, Object>>> resultado = encuestaService.obtenerEncuestasYRespuestas(idPersona);
        return ResponseEntity.ok(resultado);
    }
}
