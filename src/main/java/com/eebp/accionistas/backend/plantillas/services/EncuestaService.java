package com.eebp.accionistas.backend.plantillas.services;

import com.eebp.accionistas.backend.asamblea.services.AsambleaService;
import com.eebp.accionistas.backend.plantillas.entities.EncuestTemasDTO;
import com.eebp.accionistas.backend.plantillas.entities.Encuesta;
import com.eebp.accionistas.backend.plantillas.entities.EncuestaDTO;
import com.eebp.accionistas.backend.plantillas.entities.Temas;
import com.eebp.accionistas.backend.plantillas.repositories.EncuestaRepository;
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


}
