package com.eebp.accionistas.backend.plantillas.services;

import com.eebp.accionistas.backend.plantillas.entities.OpcionesRespuesta;
import com.eebp.accionistas.backend.plantillas.entities.PreguntaDTO;
import com.eebp.accionistas.backend.plantillas.entities.Preguntas;
import com.eebp.accionistas.backend.plantillas.repositories.OpcionesRespuestasRepository;
import com.eebp.accionistas.backend.plantillas.repositories.PreguntasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PreguntasService {

    @Autowired
    PreguntasRepository preguntasRepository;

    @Autowired
    OpcionesRespuestasRepository opcionesRespuestasRepository;

    public void guardarPreguntaConOpciones(PreguntaDTO request) {

        Preguntas pregunta = new Preguntas();
        pregunta.setIdEncuesta(request.getIdEncuesta());
        pregunta.setIdTema(request.getIdTema());
        pregunta.setPregunta(request.getPregunta());
        pregunta.setTipoPregunta(request.getTipoPregunta());

        preguntasRepository.save(pregunta);

        for (String opcion : request.getOpcionesRespuesta()) {
            OpcionesRespuesta opcionRespuesta = new OpcionesRespuesta();
            opcionRespuesta.setOpcionRespuesta(opcion);
            opcionRespuesta.setPreguntas(pregunta);
            opcionesRespuestasRepository.save(opcionRespuesta);
        }
    }
}
