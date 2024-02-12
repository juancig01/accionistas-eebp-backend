package com.eebp.accionistas.backend.seguridad.repositories;

import com.eebp.accionistas.backend.seguridad.entities.navigation.Opcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OpcionesRepository extends JpaRepository<Opcion, Integer> {
    @Query(value = "select distinct orden, opciones.COD_OPCION, opciones.NOM_OPCION, opciones.TYPE, opciones.ICON, opciones.LINK, opciones.ACTIVE from usuario_sistema \n" +
            "join usuario_opciones on usuario_sistema.COD_USUARIO = usuario_opciones.COD_USUARIO \n" +
            "join opciones on opciones.COD_OPCION  = usuario_opciones.COD_OPCION \n" +
            "join modulos on modulos.COD_MODULO  = opciones.COD_MODULO where usuario_sistema.COD_USUARIO = ?1 and modulos.COD_MODULO = ?2 order by orden", nativeQuery = true)
    List<Opcion> getOpcionesByUsuarioAndModulo(String codUsuario, Integer codModulo);

    @Query(value = "select distinct orden, opciones.COD_OPCION, opciones.NOM_OPCION, opciones.TYPE, opciones.ICON, opciones.LINK, opciones.ACTIVE from " +
            "opciones join modulos on modulos.COD_MODULO  = opciones.COD_MODULO where modulos.COD_MODULO = ?1 order by orden", nativeQuery = true)
    List<Opcion> getOpcionesByModulo(Integer codModulo);
}
