package com.eebp.accionistas.backend.plantillas.services;

import com.eebp.accionistas.backend.asamblea.services.AsambleaService;
import com.eebp.accionistas.backend.plantillas.entities.EncuestTemasDTO;
import com.eebp.accionistas.backend.plantillas.entities.Encuesta;
import com.eebp.accionistas.backend.plantillas.entities.EncuestaDTO;
import com.eebp.accionistas.backend.plantillas.entities.Temas;
import com.eebp.accionistas.backend.plantillas.repositories.EncuestaRepository;
import com.eebp.accionistas.backend.plantillas.repositories.PreguntasRepository;
import com.eebp.accionistas.backend.plantillas.repositories.TemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EncuestaService {

    @Autowired
    EncuestaRepository encuestaRepository;

    @Autowired
    TemaRepository temaRepository;

    @Autowired
    PreguntasRepository preguntasRepository;

    @Autowired
    PreguntasService preguntasService;

    @Autowired
    AsambleaService asambleaService;

    public Encuesta crearEncuesta(EncuestaDTO encuestaDto) {
        Integer consecutivoAsamblea = asambleaService.getConsecutivoAsamblea();

        Encuesta encuesta = new Encuesta();
        encuesta.setNombreEncuesta(encuestaDto.getNombreEncuesta());
        encuesta.setFechaCreacion(encuestaDto.getFechaCreacion());
        encuesta.setEstadoEncuesta(encuestaDto.getEstadoEncuesta());
        encuesta.setIdAsamblea(consecutivoAsamblea);
        encuesta.setTipoEncuesta(encuestaDto.getTipoEncuesta());

        Set<Temas> temas = new HashSet<>();
        for (Integer idTema : encuestaDto.getIdsTemas()) {
            Optional<Temas> temaOptional = temaRepository.findById(idTema);
            temaOptional.ifPresent(temas::add);
        }
        encuesta.setTemas(temas);

        return encuestaRepository.save(encuesta);
    }

    public List<EncuestTemasDTO> getEncuestasDTOByAsambleaId(Integer asambleaId) {
        List<Object[]> encuestasAndTemas = encuestaRepository.findEncuestasAndTemasByAsambleaId(asambleaId);

        return encuestasAndTemas.stream().map(obj -> {
            EncuestTemasDTO encuestaDTO = new EncuestTemasDTO();
            encuestaDTO.setIdEncuesta((Integer) obj[0]);
            encuestaDTO.setNombreEncuesta((String) obj[1]);
            encuestaDTO.setFechaCreacion((String) obj[2]);
            encuestaDTO.setEstadoEncuesta((String) obj[3]);
            encuestaDTO.setIdAsamblea(null);
            encuestaDTO.setTipoEncuesta((String) obj[4]);
            String temasConcatenados = (String) obj[5];
            List<Integer> temas = Arrays.stream(temasConcatenados.split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            encuestaDTO.setTemas(temas);
            return encuestaDTO;
        }).collect(Collectors.toList());
    }

    public Map<String, List<Map<String, Object>>> obtenerPreguntasPorAsambleaFormateadas(Integer consecutivoAsamblea) {
        List<Object[]> resultados = encuestaRepository.obtenerPreguntasPorAsamblea(consecutivoAsamblea);
        return procesarPreguntasPorAsamblea(resultados);
    }

    public Map<String, List<Map<String, Object>>> procesarPreguntasPorAsamblea(List<Object[]> resultados) {
        Map<String, List<Map<String, Object>>> preguntasPorTema = new LinkedHashMap<>();

        for (Object[] fila : resultados) {
            Integer idTema = (Integer) fila[3];
            //String descTema = (String) fila[4];
            String descTema = convertirCamelCase((String) fila[4]);
            Integer idPregunta = (Integer) fila[5];
            String tipoPregunta = (String) fila[6];
            String pregunta = (String) fila[7];

            // Obtener las opciones de respuesta para esta pregunta
            Map<String, List<Map<String, Object>>> opcionesRespuesta = obtenerOpcionesRespuestaPorIdPregunta(idPregunta);

            // Crear mapa para la pregunta con el orden deseado
            Map<String, Object> preguntaMap = new LinkedHashMap<>();
            preguntaMap.put("id", idPregunta);
            preguntaMap.put("tipoRespuesta", tipoPregunta);
            preguntaMap.put("pregunta", pregunta);
            preguntaMap.put("opcionesRespuesta", opcionesRespuesta.get("opcionRespuesta"));
            preguntaMap.put("respuestaAccionista", null); // Agregar el valor nulo para respuestaAccionista

            // Obtener la lista de preguntas para el tema actual
            List<Map<String, Object>> preguntas = preguntasPorTema.computeIfAbsent(descTema, k -> new ArrayList<>());

            // Agregar la pregunta al mapa de preguntas por tema
            preguntas.add(preguntaMap);
        }

        return preguntasPorTema;
    }

    // Método para obtener las opciones de respuesta por ID de pregunta
    private Map<String, List<Map<String, Object>>> obtenerOpcionesRespuestaPorIdPregunta(Integer idPregunta) {
        return preguntasService.obtenerOpcionesPorIdPregunta(idPregunta);
    }


    public Map<String, List<Map<String, Object>>> obtenerEncuestasYRespuestas(Integer idPersona) {
        Map<String, List<Map<String, Object>>> resultado = new HashMap<>();

        List<Object[]> preguntasEncuestas = encuestaRepository.obtenerPreguntasEncuestas(idPersona);
        List<Object[]> opcionesRespuestas = encuestaRepository.obtenerOpcionesRespuestas(idPersona);

        for (Object[] pregunta : preguntasEncuestas) {
            // Obtener el tema y convertir a formato camelCase
            String tema = convertirCamelCase((String) pregunta[3]);

            // Obtener o inicializar la lista de preguntas para el tema actual
            List<Map<String, Object>> preguntasTema = resultado.computeIfAbsent(tema, k -> new ArrayList<>());

            // Construir el mapa de la pregunta actual
            Map<String, Object> preguntaMap = new HashMap<>();
            preguntaMap.put("id", pregunta[4]);
            preguntaMap.put("tipoRespuesta", pregunta[6]);
            preguntaMap.put("pregunta", pregunta[5]);
            preguntaMap.put("opcionesRespuesta", new ArrayList<>());

            // Buscar respuestas correspondientes a la pregunta actual
            List<Map<String, Object>> opcionesPregunta = new ArrayList<>();
            for (Object[] opcion : opcionesRespuestas) {
                if (opcion[0].equals(pregunta[4])) {
                    Map<String, Object> opcionMap = new HashMap<>();
                    opcionMap.put("idOpcRespuesta", opcion[2]);
                    opcionMap.put("opcRespuesta", opcion[3]);
                    opcionesPregunta.add(opcionMap);
                }
            }
            preguntaMap.put("opcionesRespuesta", opcionesPregunta);

            // Verificar si hay respuesta del accionista y agregarla
            if (pregunta[7] != null) {
                preguntaMap.put("respuestaAccionista", pregunta[7]);
            } else {
                preguntaMap.put("respuestaAccionista", null);
            }

            preguntasTema.add(preguntaMap);
        }

        return resultado;
    }

    // Función para convertir a formato camelCase
    private String convertirCamelCase(String input) {
        StringBuilder camelCase = new StringBuilder();
        boolean capitalizeNext = false;
        for (char c : input.toCharArray()) {
            if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            } else if (capitalizeNext) {
                camelCase.append(Character.toUpperCase(c));
                capitalizeNext = false;
            } else {
                camelCase.append(Character.toLowerCase(c));
            }
        }
        return camelCase.toString();
    }

    public Map<String, List<Map<String, Object>>> obtenerEncuestasYRespuestasAsamblea(Integer consecutivoAsamblea) {
        Map<String, List<Map<String, Object>>> resultado = new LinkedHashMap<>();
        Set<Integer> preguntasProcesadas = new HashSet<>();

        // Obtener preguntas y encuestas por asamblea
        List<Object[]> preguntasEncuestas = encuestaRepository.obtenerPreguntasEncuestasAsamblea(consecutivoAsamblea);

        // Obtener opciones de respuesta por asamblea
        List<Object[]> opcionesRespuestas = encuestaRepository.obtenerOpcionesRespuestasAsamblea(consecutivoAsamblea);

        // Procesar preguntas y encuestas
        for (Object[] pregunta : preguntasEncuestas) {
            String tema = convertirCamelCase((String) pregunta[4]); // Nombre del tema
            Integer idPregunta = (Integer) pregunta[5];

            // Verificar si ya hemos procesado esta pregunta
            if (preguntasProcesadas.contains(idPregunta)) {
                continue; // Omitir preguntas duplicadas
            }

            List<Map<String, Object>> preguntasTema = resultado.computeIfAbsent(tema, k -> new ArrayList<>());

            Map<String, Object> preguntaMap = new LinkedHashMap<>(); // LinkedHashMap conserva el orden de inserción
            preguntaMap.put("id", idPregunta); // Id de la pregunta
            preguntaMap.put("tipoRespuesta", pregunta[7]); // Tipo de respuesta
            preguntaMap.put("pregunta", pregunta[6]); // Texto de la pregunta
            preguntaMap.put("opcionesRespuesta", new ArrayList<>());

            // Agregar opciones de respuesta para esta pregunta
            for (Object[] opcion : opcionesRespuestas) {
                if (opcion[2].equals(idPregunta)) {
                    Map<String, Object> opcionMap = new LinkedHashMap<>();
                    opcionMap.put("idOpcRespuesta", opcion[0]); // Id de la opción de respuesta
                    opcionMap.put("opcRespuesta", opcion[1]); // Texto de la opción de respuesta
                    ((List<Map<String, Object>>) preguntaMap.get("opcionesRespuesta")).add(opcionMap);
                }
            }

            // Agregar la respuesta del accionista si existe
            preguntaMap.put("respuestaAccionista", pregunta.length > 8 ? pregunta[8] : null);

            preguntasTema.add(preguntaMap);
            preguntasProcesadas.add(idPregunta); // Marcar la pregunta como procesada
        }

        return resultado;
    }



}
