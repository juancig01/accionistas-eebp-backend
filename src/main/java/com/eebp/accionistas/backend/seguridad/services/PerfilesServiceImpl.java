package com.eebp.accionistas.backend.seguridad.services;

import com.eebp.accionistas.backend.seguridad.entities.Perfil;
import com.eebp.accionistas.backend.seguridad.entities.navigation.Modulo;
import com.eebp.accionistas.backend.seguridad.entities.navigation.Opcion;
import com.eebp.accionistas.backend.seguridad.repositories.ModulosRepository;
import com.eebp.accionistas.backend.seguridad.repositories.OpcionesRepository;
import com.eebp.accionistas.backend.seguridad.repositories.PerfilesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PerfilesServiceImpl implements PerfilesService {

    @Autowired
    PerfilesRepository perfilesRepository;

    @Autowired
    ModulosRepository modulosRepository;

    @Autowired
    OpcionesRepository opcionesRepository;

    @Override
    public List<Perfil> obtenerPerfiles() {
        return perfilesRepository.findAll();
    }

    @Override
    public List<Modulo> getModulosByUsuario(String codUsuario) {
        return modulosRepository.getModulosByUsuario(codUsuario).stream().map(modulo -> {
            modulo.setChildren(opcionesRepository.getOpcionesByUsuarioAndModulo(codUsuario, modulo.getId()));
            return modulo;
        }).collect(Collectors.toList());
    }

    @Override
    public List<Modulo> getModulos() {
        return modulosRepository.findAll().stream().map(modulo -> {
            modulo.setChildren(opcionesRepository.getOpcionesByModulo(modulo.getId()));
            return modulo;
        }).collect(Collectors.toList());
    }
}
