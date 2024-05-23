package com.eebp.accionistas.backend.comites.controllers;

import com.eebp.accionistas.backend.comites.entities.VotacionPlancha;
import com.eebp.accionistas.backend.comites.services.VotacionPlanchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/votacion-plancha")
public class VotacionPlanchaController {

    @Autowired
    VotacionPlanchaService votacionPlanchaService;

    @PostMapping
    public VotacionPlancha addVoto(@RequestBody VotacionPlancha votacionPlancha) {
        return votacionPlanchaService.addVotacion(votacionPlancha);
    }

    @GetMapping("/votos")
    public ResponseEntity<List<Map<String, Object>>> obtenerVotosPorComiteYPlancha() {
        List<Map<String, Object>> resultados = votacionPlanchaService.obtenerVotosPorComiteYPlancha();
        return new ResponseEntity<>(resultados, HttpStatus.OK);
    }
}
