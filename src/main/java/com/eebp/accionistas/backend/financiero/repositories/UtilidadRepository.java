package com.eebp.accionistas.backend.financiero.repositories;

import com.eebp.accionistas.backend.financiero.entities.AccionistasUtilidadDTO;
import com.eebp.accionistas.backend.financiero.entities.Utilidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.io.Serializable;
import java.util.List;

public interface UtilidadRepository extends JpaRepository<Utilidad, Serializable> {

    @Query("SELECT u FROM Utilidad u WHERE YEAR(u.fecUtilidad) = :anio")
    List<Utilidad> findByAnio(@Param("anio") int anio);

    Utilidad findFirstByOrderByIdeUtilidadDesc();

    @Query(value = "SELECT " +
            "p.codUsuario AS codAccionista, " +
            "CONCAT(p.nomPri, ' ', p.nomSeg, ' ', p.apePri, ' ', p.apeSeg) AS nomAccionista, " +
            "'S' AS esAccionista, " +
            "t.folio AS folioTitulo, " +
            "SUM(t.canAccTit) AS totalCantidadAcciones " +
            "FROM " +
            "Persona p " +
            "JOIN Accionista a ON p.codUsuario = a.codUsuario " +
            "JOIN TitulosPersona tp ON CAST(p.codUsuario AS Integer) = tp.idePer " +
            "JOIN Titulo t ON tp.conseTitulo = t.conseTitulo " +
            "WHERE " +
            "a.aprobado = 'S' " +
            "GROUP BY " +
            "p.codUsuario, " +
            "CONCAT(p.nomPri, ' ', p.nomSeg, ' ', p.apePri, ' ', p.apeSeg), " +
            "t.folio")
    List<AccionistasUtilidadDTO> accionistasUtilidad();
}
