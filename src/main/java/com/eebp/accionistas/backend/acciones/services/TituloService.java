package com.eebp.accionistas.backend.acciones.services;

import com.eebp.accionistas.backend.acciones.entities.Titulo;

import com.eebp.accionistas.backend.acciones.repositories.TituloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class TituloService {

    @Autowired
    TituloRepository tituloRepository;

    public Optional<Titulo> findTituloById(Integer id) {
        return tituloRepository.findById(id);
    }

}
