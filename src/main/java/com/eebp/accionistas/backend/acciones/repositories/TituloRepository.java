package com.eebp.accionistas.backend.acciones.repositories;

import com.eebp.accionistas.backend.acciones.entities.Titulo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TituloRepository extends JpaRepository<Titulo, Integer> {
    @Query(value = "SELECT " +
            "SUM(t.can_acc_tit) AS numeroDeAccionesEnElMercado, " +
            "SUM(CASE " +
            "    WHEN (SELECT COUNT(*) FROM titulos_persona tp WHERE tp.conse_titulo = t.conse_titulo AND tp.ide_per = '846000553') = 0 " +
            "    THEN t.can_acc_tit " +
            "    ELSE 0 " +
            "END) AS numeroDeAccionesExcluyendoEmpresa " +
            "FROM titulos t " +
            "WHERE t.ide_estado_titulo = 1", nativeQuery = true)
    List<Object[]> obtenerAccionesInfo();


    @Query(value = "UPDATE titulos SET ide_estado_titulo = :estadoId WHERE conse_titulo = :tituloId", nativeQuery = true)
    void updateEstadoTitulo(@Param("tituloId") Integer tituloId, @Param("estadoId") Integer estadoId);
}
