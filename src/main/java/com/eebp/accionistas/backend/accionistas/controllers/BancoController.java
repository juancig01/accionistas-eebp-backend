package com.eebp.accionistas.backend.accionistas.controllers;


import com.eebp.accionistas.backend.accionistas.entities.Banco;
import com.eebp.accionistas.backend.accionistas.services.BancoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/bancos")
public class BancoController {

    @Autowired
    private BancoService bancoService;

    @GetMapping
    public List<Banco> getBancos() {
        return bancoService.getbancos();
    }

}
