package com.eebp.accionistas.backend.asamblea.controllers;

import com.eebp.accionistas.backend.asamblea.entities.*;
import com.eebp.accionistas.backend.asamblea.services.PoderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<?> obtenerPoderdantesPorApoderado(@PathVariable Integer idApoderado) {
        ApoderadosDTO resultado = poderService.obtenerPoderdantesPorApoderado(idApoderado);

        // Verificar si tanto el apoderado como los poderdantes están vacíos
        if (resultado.getApoderado().isEmpty() || resultado.getPoderDantes().isEmpty()) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("Error", "No se encontraron apoderados o poderdantes para el ID de apoderado proporcionado.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(errorResponse);
        }

        return ResponseEntity.ok(resultado);
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
