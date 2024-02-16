package com.eebp.accionistas.backend.parametrizacion.services;

import com.eebp.accionistas.backend.parametrizacion.entities.Parametro;
import com.eebp.accionistas.backend.parametrizacion.repositories.ParametroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ParametroService {

    @Autowired
    ParametroRepository parametroRepository;

    public List<Parametro> getParametros() {
        return parametroRepository.findAll();
    }

    public Optional<Parametro> findParametroById(Integer id) {
        return parametroRepository.findById(id);
    }

    public Parametro addParametro(Parametro parametro) {
        return parametroRepository.save(parametro);
    }

    public Parametro updateParametro(Parametro parametro) {
        return parametroRepository.save(parametro);
    }

    public void deleteParametroById(Integer id) {
        parametroRepository.deleteById(id);
    }

}
