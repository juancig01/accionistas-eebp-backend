package com.eebp.accionistas.backend.acciones.controllers;

import com.eebp.accionistas.backend.acciones.entities.Titulo;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/titulos")
public class TitulosController {
    //prueba commit
    @GetMapping("/{codUsuario}")
    public List<Titulo> getTitulosByCodUsuario(@PathVariable String codUsuario) {
        List<Titulo> titulos = new ArrayList<>();
        String[] accionistas = {"0010"};
        Titulo t1 = Titulo.builder()
                .idTitulo(123)
                .accionistas(accionistas)
                .canAccTit(3)
                .valAccTit(15000)
                .estTit("A")
                .claAccTit("A")
                .tipAccTit("O")
                .build();
        Titulo t2 = Titulo.builder()
                .idTitulo(124)
                .accionistas(accionistas)
                .canAccTit(2)
                .valAccTit(10000)
                .estTit("A")
                .claAccTit("A")
                .tipAccTit("O")
                .build();
        titulos.add(t1);
        titulos.add(t2);
        return titulos;
    }
}
