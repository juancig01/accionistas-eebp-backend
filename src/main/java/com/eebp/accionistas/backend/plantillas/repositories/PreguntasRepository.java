package com.eebp.accionistas.backend.plantillas.repositories;

import com.eebp.accionistas.backend.plantillas.entities.OpcionesRespuesta;
import com.eebp.accionistas.backend.plantillas.entities.Preguntas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PreguntasRepository extends JpaRepository<Preguntas, Integer> {


    @Query("SELECT o.idOpcRespuesta, o.opcionRespuesta FROM OpcionesRespuesta o WHERE o.preguntas.idPregunta = :idPregunta")
    List<Object[]> obtenerOpcionesPorIdPregunta(Integer idPregunta);
}
