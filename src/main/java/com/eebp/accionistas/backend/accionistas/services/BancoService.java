package com.eebp.accionistas.backend.accionistas.services;

import com.eebp.accionistas.backend.accionistas.entities.Banco;
import com.eebp.accionistas.backend.accionistas.repositories.BancoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BancoService {

    @Autowired
    private BancoRepository bancoRepository;

    public List<Banco> getbancos() {
        return bancoRepository.findAll();
    }
}
