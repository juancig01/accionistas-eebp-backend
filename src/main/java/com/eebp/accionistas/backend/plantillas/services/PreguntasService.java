package com.eebp.accionistas.backend.plantillas.services;

import com.eebp.accionistas.backend.accionistas.entities.Accionista;
import com.eebp.accionistas.backend.plantillas.entities.OpcionesRespuesta;
import com.eebp.accionistas.backend.plantillas.entities.PreguntaDTO;
import com.eebp.accionistas.backend.plantillas.entities.Preguntas;
import com.eebp.accionistas.backend.plantillas.repositories.OpcionesRespuestasRepository;
import com.eebp.accionistas.backend.plantillas.repositories.PreguntasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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

    public Optional<Preguntas> getPregunta(Integer id) {
        return preguntasRepository.findById(id);
    }


    public Map<String, List<Map<String, Object>>> obtenerOpcionesPorIdPregunta(Integer idPregunta) {
        List<Object[]> opciones = preguntasRepository.obtenerOpcionesPorIdPregunta(idPregunta);
        Map<String, List<Map<String, Object>>> respuesta = new HashMap<>();

        List<Map<String, Object>> opcionesList = new ArrayList<>();

        for (Object[] opcion : opciones) {
            Map<String, Object> opcionMap = new HashMap<>();
            opcionMap.put("idOpcRespuesta", opcion[0]);
            opcionMap.put("opcRespuesta", opcion[1]);
            opcionesList.add(opcionMap);
        }

        respuesta.put("opcionRespuesta", opcionesList);

        return respuesta;
    }

    private List<String> transformarAListaStrings(List<Object> opcionesObject) {
        return opcionesObject.stream()
                .map(Object::toString)
                .collect(Collectors.toList());
    }
}
