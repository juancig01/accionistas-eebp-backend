package com.eebp.accionistas.backend.asamblea.controllers;

import com.eebp.accionistas.backend.asamblea.entities.*;
import com.eebp.accionistas.backend.asamblea.services.PoderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/asamblea")
public class PoderController {

    @Autowired
    PoderService poderService;

    @PostMapping("/registro-poderes")
    public Poder addPoder(@RequestBody Poder poder) {
        return poderService.addPoder(poder);
    }

    @GetMapping("/obtener-registro-poderes")
    public List<PoderesDTO> obtenerPoderesConArchivos() {
        return poderService.obtenerPoderesConArchivos();
    }

    @GetMapping("/poder/{idApoderado}")
    public ApoderadosDTO obtenerPoderdantesPorApoderado(@PathVariable Integer idApoderado) {
        return poderService.obtenerPoderdantesPorApoderado(idApoderado);
    }

    @PutMapping("/actualizar-estado/{consecutivoPoder}")
    public ResponseEntity<Poder> actualizarEstadoPoder(@PathVariable Integer consecutivoPoder, @RequestBody EstadoPoderDTO estadoPoderDTO) {
        String nuevoEstado = estadoPoderDTO.getEstado();
        Poder poderActualizado = poderService.actualizarEstadoPoder(consecutivoPoder, nuevoEstado);
        if (poderActualizado != null) {
            return ResponseEntity.ok(poderActualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
