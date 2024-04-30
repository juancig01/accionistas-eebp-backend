package com.eebp.accionistas.backend.asamblea.services;

import com.eebp.accionistas.backend.asamblea.entities.RegistroAsamblea;
import com.eebp.accionistas.backend.asamblea.repositories.RegistroAsambleaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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

}
