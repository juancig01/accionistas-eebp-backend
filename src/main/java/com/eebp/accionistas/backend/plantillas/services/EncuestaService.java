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

}
