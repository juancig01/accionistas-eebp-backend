package com.eebp.accionistas.backend.plantillas.repositories;

import com.eebp.accionistas.backend.plantillas.entities.Encuesta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EncuestaRepository extends JpaRepository<Encuesta, Integer> {

    @Query("SELECT e.idEncuesta, e.nombreEncuesta, e.fechaCreacion, e.estadoEncuesta, e.tipoEncuesta, " +
            "t.idTema, t.descTema " +
            "FROM Encuesta e " +
            "JOIN e.temas t " +
            "WHERE e.idAsamblea = ?1")
    List<Object[]> findEncuestasConTemasPorAsamblea(Integer idAsamblea);
}
