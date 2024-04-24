package com.eebp.accionistas.backend.asamblea.controllers;

import com.eebp.accionistas.backend.asamblea.entities.Asamblea;
import com.eebp.accionistas.backend.asamblea.services.AsambleaService;
import com.eebp.accionistas.backend.transacciones.entities.Transaccion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/asamblea")

public class AsambleaController {

    @Autowired
    AsambleaService asambleaService;

    @PostMapping("/crear-asamblea")
    public Asamblea addAsamblea(@RequestBody Asamblea asamblea) {
        return asambleaService.addAsamblea(asamblea);
    }
    @GetMapping("/obtener-asambleas")
    public List<Asamblea> getAsambleas() {
        return asambleaService.getAsambleas();
    }

}
