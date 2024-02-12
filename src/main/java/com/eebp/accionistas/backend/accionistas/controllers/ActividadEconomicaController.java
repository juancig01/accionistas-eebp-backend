package com.eebp.accionistas.backend.accionistas.controllers;

import com.eebp.accionistas.backend.accionistas.entities.ActividadEconomica;
import com.eebp.accionistas.backend.accionistas.services.ActividadEconomicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/actividadEconomica")
public class ActividadEconomicaController {

    @Autowired
    ActividadEconomicaService actividadEconomicaService;

    @GetMapping
    public List<ActividadEconomica> obtenerActividadesEconomicas() {
        return actividadEconomicaService.getActividadesEconomicas();
    }
}
