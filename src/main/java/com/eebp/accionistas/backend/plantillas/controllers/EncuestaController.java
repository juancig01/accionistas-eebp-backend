package com.eebp.accionistas.backend.plantillas.controllers;

import com.eebp.accionistas.backend.plantillas.entities.EncuestTemasDTO;
import com.eebp.accionistas.backend.plantillas.entities.Encuesta;
import com.eebp.accionistas.backend.plantillas.entities.EncuestaDTO;
import com.eebp.accionistas.backend.plantillas.services.EncuestaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public List<EncuestTemasDTO> getEncuestasByAsamblea(@PathVariable Integer idAsamblea) {
        return encuestaService.getEncuestasDTOByAsambleaId(idAsamblea);
    }
}
