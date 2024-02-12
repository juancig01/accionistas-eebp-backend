package com.eebp.accionistas.backend.seguridad.services;

import com.eebp.accionistas.backend.seguridad.entities.Perfil;
import com.eebp.accionistas.backend.seguridad.entities.navigation.Modulo;
import com.eebp.accionistas.backend.seguridad.entities.navigation.Opcion;

import java.util.List;

public interface PerfilesService {
    List<Perfil> obtenerPerfiles();

    List<Modulo> getModulosByUsuario(String codUsuario);

    List<Modulo> getModulos();
}
