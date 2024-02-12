package com.eebp.accionistas.backend.seguridad.repositories;

import com.eebp.accionistas.backend.seguridad.entities.navigation.Modulo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ModulosRepository extends JpaRepository<Modulo, Integer> {
    @Query(value = "select distinct modulos.COD_MODULO, modulos.NOM_MODULO, modulos.DESCRIPCION, modulos.type from usuario_sistema \n" +
            "join usuario_opciones on usuario_sistema.COD_USUARIO = usuario_opciones.COD_USUARIO \n" +
            "join opciones on opciones.COD_OPCION  = usuario_opciones.COD_OPCION \n" +
            "join modulos on modulos.COD_MODULO  = opciones.COD_MODULO where usuario_sistema.COD_USUARIO = ?1", nativeQuery = true)
    List<Modulo> getModulosByUsuario(String codUsuario);
}
