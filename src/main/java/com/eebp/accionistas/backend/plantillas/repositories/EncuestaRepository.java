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

    @Query("SELECT a.consecutivo, e.idEncuesta, e.nombreEncuesta, t.idTema, t.descTema, p.idPregunta, p.tipoPregunta, p.pregunta " +
            "FROM Asamblea a " +
            "INNER JOIN Encuesta e ON a.consecutivo = e.idAsamblea " +
            "INNER JOIN e.temas t " +
            "LEFT JOIN Preguntas p ON p.idTema = t.idTema AND p.idEncuesta = e.idEncuesta " +
            "WHERE a.consecutivo = :consecutivoAsamblea AND p.idPregunta IS NOT NULL")
    List<Object[]> obtenerPreguntasPorAsamblea(Integer consecutivoAsamblea);
}

