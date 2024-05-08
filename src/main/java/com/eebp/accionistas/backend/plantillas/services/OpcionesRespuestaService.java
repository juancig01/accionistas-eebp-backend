package com.eebp.accionistas.backend.plantillas.services;

import com.eebp.accionistas.backend.plantillas.entities.OpcionesRespuesta;
import com.eebp.accionistas.backend.plantillas.repositories.OpcionesRespuestasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OpcionesRespuestaService {

    @Autowired
    OpcionesRespuestasRepository opcionesRespuestasRepository;


    public OpcionesRespuesta addOpciones(OpcionesRespuesta opcionesRespuesta) {
        return opcionesRespuestasRepository.save(opcionesRespuesta);
    }
}
