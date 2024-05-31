package com.eebp.accionistas.backend.votaciones.repositories;

import com.eebp.accionistas.backend.votaciones.entities.Respuestas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RespuestasRepository extends JpaRepository<Respuestas, Integer> {
    boolean existsByIdPreguntaAndIdPersonaIn(Integer idPregunta, List<String> idPersonas);

    boolean existsByIdPreguntaAndIdPersona(Integer idPregunta, String idPersona);

    Respuestas findByIdPreguntaAndIdPersona(Integer idPregunta, String idPersona);

    @Query(value = "SELECT " +
            "p.id_pregunta, " +
            "p.pregunta, " +
            "op.id_opc_respuesta, " +
            "op.opcion_respuesta, " +
            "COALESCE(SUM(t.can_acc_tit), 0) AS num_votos " +
            "FROM preguntas p " +
            "JOIN encuesta e ON p.id_encuesta = e.id_encuesta " +
            "LEFT JOIN opciones_respuesta op ON p.id_pregunta = op.id_pregunta " +
            "LEFT JOIN respuestas r ON op.id_opc_respuesta = r.id_opc_respuesta " +
            "LEFT JOIN titulos_persona tp ON r.id_persona = tp.ide_per " +
            "LEFT JOIN titulos t ON tp.conse_titulo = t.conse_titulo " +
            "WHERE e.id_encuesta = :idEncuesta " +
            "GROUP BY p.id_pregunta, p.pregunta, op.id_opc_respuesta, op.opcion_respuesta " +
            "ORDER BY p.id_pregunta, op.id_opc_respuesta", nativeQuery = true)
    List<Object[]> obtenerPreguntasOpcionesYVotosPorEncuesta(@Param("idEncuesta") Integer idEncuesta);

}
