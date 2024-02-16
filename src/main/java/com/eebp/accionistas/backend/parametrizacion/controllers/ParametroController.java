package com.eebp.accionistas.backend.parametrizacion.controllers;

import com.eebp.accionistas.backend.parametrizacion.entities.Parametro;
import com.eebp.accionistas.backend.parametrizacion.services.ParametroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/parametros")
public class ParametroController {

    @Autowired
    ParametroService parametroService;

    @GetMapping
    public List<Parametro> getParametros() {
        return parametroService.getParametros();
    }

    @GetMapping("/{id}")
    public Optional<Parametro> findParametroById(@PathVariable Integer id) {
        return parametroService.findParametroById(id);
    }

    @PutMapping
    public Parametro addParametro(@RequestBody Parametro parametro) {
        return parametroService.addParametro(parametro);
    }

    @PostMapping
    public Parametro updateParametro(@RequestBody Parametro parametro) {
        return parametroService.updateParametro(parametro);
    }

    @DeleteMapping("{id}")
    public void deleteParametroById(@PathVariable Integer id) {
        parametroService.deleteParametroById(id);
    }

}
