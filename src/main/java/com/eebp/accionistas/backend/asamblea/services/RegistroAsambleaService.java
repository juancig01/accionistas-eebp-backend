package com.eebp.accionistas.backend.asamblea.services;

import com.eebp.accionistas.backend.asamblea.entities.AsistentesAsambleaDTO;
import com.eebp.accionistas.backend.asamblea.entities.RegistroAsamblea;
import com.eebp.accionistas.backend.asamblea.repositories.RegistroAsambleaRepository;
import com.eebp.accionistas.backend.seguridad.entities.Asset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class RegistroAsambleaService {

    @Autowired
    RegistroAsambleaRepository registroAsambleaRepository;

    @Autowired
    AsambleaService asambleaService;

    public RegistroAsamblea addRegistroAsamblea(RegistroAsamblea registroAsamblea) {

        registroAsamblea.setAsistencia(false);
        Integer consecutivoAsamblea = asambleaService.getConsecutivoAsamblea();
        registroAsamblea.setConsecutivo(consecutivoAsamblea);

        return registroAsambleaRepository.save(registroAsamblea);
    }

    public List<AsistentesAsambleaDTO> obtenerRegistroAsamblea() {

        return registroAsambleaRepository.obtenerRegistroAsamblea();
    }

}
