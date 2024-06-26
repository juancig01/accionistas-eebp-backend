package com.eebp.accionistas.backend.comites.controllers;

import com.eebp.accionistas.backend.comites.entities.VotacionPlancha;
import com.eebp.accionistas.backend.comites.entities.VotoDTO;
import com.eebp.accionistas.backend.comites.services.VotacionPlanchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public ResponseEntity<Map<String, List<Map<String, Object>>>> obtenerVotosPorComiteYPlancha() {
        Map<String, List<Map<String, Object>>> votos = votacionPlanchaService.obtenerVotosPorComiteYPlancha();
        return ResponseEntity.ok(votos);
    }

    @GetMapping("/{idPersona}")
    public ResponseEntity<Map<String, String>> obtenerVotosPorComiteYPersona(@PathVariable Integer idPersona) {
        List<VotoDTO> votos = votacionPlanchaService.obtenerVotosPorComiteYPersona(idPersona);

        // Convertir la lista de VotoDTO a un Map<String, Integer>
        Map<String, String> votosMap = votos.stream()
                .collect(Collectors.toMap(VotoDTO::getDescComite, VotoDTO::getVoto));

        return new ResponseEntity<>(votosMap, HttpStatus.OK);
    }

}
