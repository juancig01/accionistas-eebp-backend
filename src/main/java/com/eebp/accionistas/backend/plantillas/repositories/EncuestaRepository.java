package com.eebp.accionistas.backend.plantillas.repositories;

import com.eebp.accionistas.backend.plantillas.entities.Encuesta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EncuestaRepository extends JpaRepository<Encuesta, Integer> {

    @Query("SELECT e.idEncuesta, e.nombreEncuesta, e.fechaCreacion, e.estadoEncuesta, e.tipoEncuesta, GROUP_CONCAT(t.idTema) AS idTemas " +
            "FROM Encuesta e " +
            "INNER JOIN e.temas t " +  // Cambio aquí
            "WHERE e.idAsamblea = :asambleaId " +
            "GROUP BY e.idEncuesta, e.nombreEncuesta, e.fechaCreacion, e.estadoEncuesta, e.tipoEncuesta")
    List<Object[]> findEncuestasAndTemasByAsambleaId(@Param("asambleaId") Integer asambleaId);
}

