package com.eebp.accionistas.backend.votaciones.services;

import com.eebp.accionistas.backend.accionistas.entities.Accionista;
import com.eebp.accionistas.backend.accionistas.services.AccionistaService;
import com.eebp.accionistas.backend.plantillas.entities.Preguntas;
import com.eebp.accionistas.backend.plantillas.services.PreguntasService;
import com.eebp.accionistas.backend.votaciones.entities.AccionistaNoPermitidoException;
import com.eebp.accionistas.backend.votaciones.entities.Respuestas;
import com.eebp.accionistas.backend.votaciones.repositories.RespuestasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RespuestasService {

    @Autowired
    RespuestasRepository respuestasRepository;

    @Autowired
    AccionistaService accionistaService;

    @Autowired
    PreguntasService preguntasService;

    public void guardarRespuestas(Map<String, List<Map<String, Object>>> datosRespuestas, List<String> votantes) {
        for (Map.Entry<String, List<Map<String, Object>>> entry : datosRespuestas.entrySet()) {
            for (Map<String, Object> pregunta : entry.getValue()) {
                Integer idPregunta = (Integer) pregunta.get("id");
                Integer idOpcRespuesta = (Integer) pregunta.get("respuestaAccionista");

                Optional<Preguntas> preguntaEntity = preguntasService.getPregunta(idPregunta);

                // Verificar si la respuesta ya fue registrada para esta pregunta
                boolean respuestaRegistrada = respuestaRegistrada(idPregunta, votantes);

                for (String votante : votantes) {
                    // Obtener el accionista correspondiente al votante
                    Optional<Accionista> accionistaOptional = accionistaService.getAccionista(votante);

                    if (accionistaOptional.isPresent()) {
                        Accionista accionista = accionistaOptional.get();
                        int tipoAccionista = accionista.getTipoAccionista();

                        if (preguntaEntity.get().getIdTema() == 4) {

                            // Verificar si el tipoAccionista es 1 o 2
                            if (tipoAccionista == 1 || tipoAccionista == 2) {
                                // Generar un error o registrar un mensaje de advertenci
                                System.out.println("El accionista de tipo " + tipoAccionista + " no puede registrar su voto.");
                                continue; // Saltar al siguiente votante
                            }
                        }

                        if (!respuestaRegistrada) {
                            // Si la respuesta no fue registrada, se crea un nuevo registro
                            Respuestas respuesta = new Respuestas();
                            respuesta.setIdPregunta(idPregunta);
                            respuesta.setIdPersona(votante);
                            respuesta.setIdOpcRespuesta(idOpcRespuesta);
                            respuestasRepository.save(respuesta);
                        } else {
                            // Si la respuesta ya fue registrada, se actualiza el registro existente
                            Respuestas respuestaExistente = respuestasRepository.findByIdPreguntaAndIdPersona(idPregunta, votante);
                            if (respuestaExistente != null) {
                                respuestaExistente.setIdOpcRespuesta(idOpcRespuesta);
                                respuestasRepository.save(respuestaExistente);
                            }
                        }
                    } else {
                        // Manejar el caso en el que el accionista no exista
                        System.out.println("No se encontró el accionista con el código de usuario: " + votante);
                    }
                }
            }
        }
    }

    private boolean respuestaRegistrada(Integer idPregunta, List<String> votantes) {
        // Verificar si hay una respuesta registrada para esta pregunta y votante
        return votantes.stream().anyMatch(votante -> respuestasRepository.existsByIdPreguntaAndIdPersona(idPregunta, votante));
    }

    public List<Map<String, Object>> obtenerPreguntasOpcionesYVotosPorEncuesta(Integer idEncuesta) {
        List<Object[]> resultados = respuestasRepository.obtenerPreguntasOpcionesYVotosPorEncuesta(idEncuesta);
        Map<Integer, Map<String, Object>> preguntasMap = new HashMap<>();

        for (Object[] fila : resultados) {
            Integer idPregunta = (Integer) fila[0];
            String pregunta = (String) fila[1];
            Integer idOpcion = (Integer) fila[2];
            String opcion = (String) fila[3];
            Number numVotos = (Number) fila[4];

            Map<String, Object> preguntaMap = preguntasMap.computeIfAbsent(idPregunta, k -> new LinkedHashMap<>());
            preguntaMap.put("idPregunta", idPregunta);
            preguntaMap.put("pregunta", pregunta);

            List<Map<String, Object>> opciones = (List<Map<String, Object>>) preguntaMap.computeIfAbsent("opciones", k -> new ArrayList<>());
            Map<String, Object> opcionMap = new LinkedHashMap<>();
            opcionMap.put("idOpcion", idOpcion);
            opcionMap.put("opcion", opcion);
            opcionMap.put("numVotos", numVotos.longValue());
            opciones.add(opcionMap);
        }

        List<Map<String, Object>> respuesta = new ArrayList<>(preguntasMap.values());

        respuesta.sort((m1, m2) -> {
            int idPregunta1 = (int) m1.get("idPregunta");
            int idPregunta2 = (int) m2.get("idPregunta");

            if (idPregunta1 == idPregunta2) {
                return 0;
            } else if (idPregunta1 < idPregunta2) {
                return -1;
            } else {
                return 1;
            }
        });

        for (Map<String, Object> pregunta : respuesta) {
            List<Map<String, Object>> opciones = (List<Map<String, Object>>) pregunta.get("opciones");
            opciones.sort((m1, m2) -> {
                int idOpcion1 = (int) m1.get("idOpcion");
                int idOpcion2 = (int) m2.get("idOpcion");

                if (idOpcion1 == idOpcion2) {
                    return 0;
                } else if (idOpcion1 < idOpcion2) {
                    return -1;
                } else {
                    return 1;
                }
            });
        }

        return respuesta;
    }
}
