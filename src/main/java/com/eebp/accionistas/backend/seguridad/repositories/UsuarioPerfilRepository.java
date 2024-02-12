package com.eebp.accionistas.backend.seguridad.repositories;

import com.eebp.accionistas.backend.seguridad.entities.UsuarioPerfil;
import com.eebp.accionistas.backend.seguridad.entities.UsuarioPerfilId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioPerfilRepository extends JpaRepository<UsuarioPerfil, UsuarioPerfilId> {
    UsuarioPerfil getUsuarioPerfilByCodUsuario(String codUsuario);
}
