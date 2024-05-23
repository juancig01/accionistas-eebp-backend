package com.eebp.accionistas.backend.comites.controllers;

import com.eebp.accionistas.backend.comites.entities.Plancha;
import com.eebp.accionistas.backend.comites.services.PlanchaService;
import com.eebp.accionistas.backend.seguridad.entities.Asset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/plancha")
public class PlanchaController {

    @Autowired
    PlanchaService planchaService;

    @PostMapping
    public Plancha addPlancha(@RequestBody Plancha plancha) {
        return planchaService.addPlancha(plancha);
    }

    @GetMapping("/resumen")
    public Map<String, Object> obtenerDatos() {
        return planchaService.obtenerDatos();
    }

    @GetMapping("/{codUsuario}")
    public List<Asset> testFilesUser(@PathVariable Integer codUsuario) {
        return planchaService.getFilesUser(codUsuario);
    }
}
