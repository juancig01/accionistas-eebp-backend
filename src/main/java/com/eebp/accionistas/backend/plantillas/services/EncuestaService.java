package com.eebp.accionistas.backend.plantillas.services;

import com.eebp.accionistas.backend.asamblea.services.AsambleaService;
import com.eebp.accionistas.backend.plantillas.entities.Encuesta;
import com.eebp.accionistas.backend.plantillas.entities.EncuestaDTO;
import com.eebp.accionistas.backend.plantillas.entities.Temas;
import com.eebp.accionistas.backend.plantillas.repositories.EncuestaRepository;
import com.eebp.accionistas.backend.plantillas.repositories.TemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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


}
