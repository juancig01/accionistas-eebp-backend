package com.eebp.accionistas.backend.financiero.repositories;

import com.eebp.accionistas.backend.financiero.entities.AccionistasUtilidadDTO;
import com.eebp.accionistas.backend.financiero.entities.Utilidad;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface UtilidadRepository extends JpaRepository<Utilidad, Serializable> {

    @Query("SELECT u FROM Utilidad u WHERE YEAR(u.fecUtilidad) = :anio")
    List<Utilidad> findByAnio(@Param("anio") int anio);

    Utilidad findFirstByOrderByIdeUtilidadDesc();


    @Query("SELECT MIN(c.fechaCorte) FROM Cortes c GROUP BY YEAR(c.fechaCorte)")
    List<Date> findDistinctCorteDates();

    @Query(value = "SELECT " +
            "p.cod_usuario AS codAccionista, " +
            "CONCAT(p.nomPri, ' ', p.nomSeg, ' ', p.apePri, ' ', p.apeSeg) AS nomAccionista, " +
            "'S' AS esAccionista, " +
            "t.folio AS folioTitulo, " +
            "SUM(t.can_acc_tit) AS totalCantidadAcciones " +
            "FROM " +
            "persona p " +
            "JOIN accionista a ON p.cod_usuario = a.cod_usuario " +
            "JOIN titulos_persona tp ON p.cod_usuario = tp.ide_per " +
            "JOIN titulos t ON tp.conse_titulo = t.conse_titulo " +
            "WHERE " +
            "a.aprobado = 'S' " +
            "GROUP BY " +
            "p.cod_usuario, " +
            "CONCAT(p.nomPri, ' ', p.nomSeg, ' ', p.apePri, ' ', p.apeSeg), " +
            "t.folio", nativeQuery = true)
    List<AccionistasUtilidadDTO> accionistasUtilidad();

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO cortes (cod_accionista, nom_accionista, es_accionista, folio_titulo, total_acciones, fecha_corte) " +
            "SELECT " +
            "p.COD_USUARIO AS cod_accionista, " +
            "CONCAT(p.nomPri, ' ', p.nomSeg, ' ', p.apePri, ' ', p.apeSeg) AS nom_accionista, " +
            "'S' AS es_accionista, " +
            "t.folio AS folio_titulo, " +
            "SUM(t.can_acc_tit) AS total_acciones, " +
            ":fechaCorte AS fecha_corte " +
            "FROM " +
            "persona p " +
            "JOIN accionista a ON p.COD_USUARIO = a.COD_USUARIO " +
            "JOIN titulos_persona tp ON p.COD_USUARIO = tp.ide_per " +
            "JOIN titulos t ON tp.conse_titulo = t.conse_titulo " +
            "WHERE " +
            "a.aprobado = 'S' " +
            "GROUP BY " +
            "p.COD_USUARIO, " +
            "CONCAT(p.nomPri, ' ', p.nomSeg, ' ', p.apePri, ' ', p.apeSeg), " +
            "t.folio", nativeQuery = true)
    void insertarEnCortes(@Param("fechaCorte") LocalDate fechaCorte);
}
