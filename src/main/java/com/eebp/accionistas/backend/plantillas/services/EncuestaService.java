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
            String descTema = (String) fila[4];
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

        List<Object[]> preguntasEncuestas = encuestaRepository.obtenerPreguntasEncuestas(Integer.valueOf(String.valueOf(idPersona)));
        List<Object[]> opcionesRespuestas = encuestaRepository.obtenerOpcionesRespuestas(Integer.valueOf(String.valueOf(idPersona)));

        for (Object[] pregunta : preguntasEncuestas) {
            String tema = (String) pregunta[3];
            List<Map<String, Object>> preguntasTema = resultado.computeIfAbsent(tema, k -> new ArrayList<>());

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
}
