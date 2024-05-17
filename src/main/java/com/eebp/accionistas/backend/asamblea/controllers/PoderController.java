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

    @GetMapping("/poder/{idePer}")
    public ResponseEntity<?> obtenerDetallesPorIdePer(@PathVariable Integer idePer) {
        ApoderadosDTO detalles = poderService.obtenerDetallesPorIdePer(idePer);
        if (detalles != null) {
            return ResponseEntity.ok(detalles);
        } else {
            Map<String, String> mensaje = new HashMap<>();
            mensaje.put("message", "No encontrado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
        }
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
