package com.eebp.accionistas.backend.acciones.controllers;

import com.eebp.accionistas.backend.acciones.entities.Titulo;
import com.eebp.accionistas.backend.acciones.services.TituloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/titulos")
public class TitulosController {

    @Autowired
    TituloService tituloService;

    @GetMapping("/{id}")
    public Optional<Titulo> findTituloById(@PathVariable Integer id) {
        return tituloService.findTituloById(id);
    }

}
