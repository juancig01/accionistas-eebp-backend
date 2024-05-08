package com.eebp.accionistas.backend.plantillas.services;

import com.eebp.accionistas.backend.plantillas.entities.Preguntas;
import com.eebp.accionistas.backend.plantillas.repositories.PreguntasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PreguntasService {

    @Autowired
    PreguntasRepository preguntasRepository;

    public Preguntas addPreguntas(Preguntas preguntas) {
        return preguntasRepository.save(preguntas);
    }
}
