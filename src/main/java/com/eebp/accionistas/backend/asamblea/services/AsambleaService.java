package com.eebp.accionistas.backend.asamblea.services;

import com.eebp.accionistas.backend.asamblea.entities.Asamblea;
import com.eebp.accionistas.backend.asamblea.repositories.AsambleaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AsambleaService {

    @Autowired
    AsambleaRepository asambleaRepository;

    public Asamblea addAsamblea(Asamblea asamblea) {
        asamblea.setEstado("ACTIVA");
        return asambleaRepository.save(asamblea);
    }

    public List<Asamblea> getAsambleas() {
        List<Asamblea> asambleas = asambleaRepository.findAll();
        return asambleas;
    }
}
