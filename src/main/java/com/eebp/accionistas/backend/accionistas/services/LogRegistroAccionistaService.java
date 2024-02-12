package com.eebp.accionistas.backend.accionistas.services;

import com.eebp.accionistas.backend.accionistas.entities.LogRegistroAccionistas;
import com.eebp.accionistas.backend.accionistas.repositories.LogRegistroAccionistaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogRegistroAccionistaService {

    @Autowired
    LogRegistroAccionistaRepository logRegistroAccionistaRepository;

    public void add(LogRegistroAccionistas log) {
        logRegistroAccionistaRepository.save(log);
    }

    public List<LogRegistroAccionistas> getLogByCodUsuario(String codUsuario) {
        return logRegistroAccionistaRepository.getLogRegistroAccionistasByCodUsuario(codUsuario);
    }
}
