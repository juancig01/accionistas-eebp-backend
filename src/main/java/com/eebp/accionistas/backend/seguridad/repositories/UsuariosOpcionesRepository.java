package com.eebp.accionistas.backend.seguridad.repositories;

import com.eebp.accionistas.backend.seguridad.entities.navigation.UsuariosOpciones;
import com.eebp.accionistas.backend.seguridad.entities.navigation.UsuariosOpcionesId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsuariosOpcionesRepository extends JpaRepository<UsuariosOpciones, UsuariosOpcionesId> {
    List<UsuariosOpciones> findByCodUsuario(String codUsuario);
}
