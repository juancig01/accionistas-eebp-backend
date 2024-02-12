package com.eebp.accionistas.backend.accionistas.services;

import com.eebp.accionistas.backend.accionistas.entities.ActividadEconomica;
import com.eebp.accionistas.backend.accionistas.repositories.ActividadEconomicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActividadEconomicaService {

    @Autowired
    ActividadEconomicaRepository actividadEconomicaRepository;

    public List<ActividadEconomica> getActividadesEconomicas() {
        return actividadEconomicaRepository.findAll();
    }
}
