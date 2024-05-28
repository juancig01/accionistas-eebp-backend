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

    @Query(value = "SELECT p.COD_USUARIO, e.nombre_encuesta, t.desc_tema, pr.id_pregunta, pr.pregunta, pr.tipo_pregunta, " +
            "opr.id_opc_respuesta, opr.opcion_respuesta, r.id_opc_respuesta AS opcion_seleccionada " +
            "FROM respuestas r " +
            "JOIN preguntas pr ON r.id_pregunta = pr.id_pregunta " +
            "JOIN encuesta_tema et ON pr.id_encuesta = et.id_encuesta AND pr.id_tema = et.id_tema " +
            "JOIN temas t ON et.id_tema = t.id_tema " +
            "JOIN encuesta e ON pr.id_encuesta = e.id_encuesta " +
            "JOIN opciones_respuesta opr ON r.id_opc_respuesta = opr.id_opc_respuesta " +
            "JOIN persona p ON r.id_persona = p.COD_USUARIO " +
            "WHERE p.COD_USUARIO = :codUsuario", nativeQuery = true)
    List<Object[]> obtenerRespuestasPorUsuario(@Param("codUsuario") Integer codUsuario);

    @Query(value = "SELECT e.id_encuesta, " +
            "e.nombre_encuesta, " +
            "t.id_tema, " +
            "t.desc_tema, " +
            "p.id_pregunta, " +
            "p.pregunta, " +
            "p.tipo_pregunta, " +
            "COALESCE(r.id_opc_respuesta, NULL) AS respuestaAccionista " +
            "FROM encuesta e " +
            "JOIN asamblea a ON e.id_asamblea = a.consecutivo " +
            "JOIN encuesta_tema et ON e.id_encuesta = et.id_encuesta " +
            "JOIN temas t ON et.id_tema = t.id_tema " +
            "JOIN preguntas p ON e.id_encuesta = p.id_encuesta AND t.id_tema = p.id_tema " +
            "LEFT JOIN respuestas r ON p.id_pregunta = r.id_pregunta AND r.id_persona = :idPersona " +
            "WHERE a.consecutivo = (SELECT MAX(a2.consecutivo) FROM asamblea a2) " +
            "ORDER BY t.desc_tema, p.id_pregunta",
            nativeQuery = true)
    List<Object[]> obtenerPreguntasEncuestas(@Param("idPersona") Integer idPersona);


    @Query(value = "SELECT p.id_pregunta, " +
            "p.tipo_pregunta AS tipoRespuesta, " +
            "o.id_opc_respuesta, " +
            "o.opcion_respuesta AS opcRespuesta, " +
            "r.id_opc_respuesta AS respuestaAccionista " +
            "FROM preguntas p " +
            "JOIN opciones_respuesta o ON p.id_pregunta = o.id_pregunta " +
            "LEFT JOIN respuestas r ON p.id_pregunta = r.id_pregunta AND r.id_persona = :idPersona " +
            "ORDER BY p.id_pregunta, o.id_opc_respuesta", nativeQuery = true)
    List<Object[]> obtenerOpcionesRespuestas(@Param("idPersona") Integer idPersona);

    @Query(value = "SELECT a.consecutivo AS idAsamblea, e.id_encuesta, e.nombre_encuesta, t.id_tema, t.desc_tema, p.id_pregunta, p.pregunta, p.tipo_pregunta " +
            "FROM asamblea a " +
            "JOIN encuesta e ON a.consecutivo = e.id_asamblea " +
            "JOIN encuesta_tema et ON e.id_encuesta = et.id_encuesta " +
            "JOIN temas t ON et.id_tema = t.id_tema " +
            "JOIN preguntas p ON e.id_encuesta = p.id_encuesta AND p.id_tema = t.id_tema " +
            "LEFT JOIN respuestas r ON p.id_pregunta = r.id_pregunta " +
            "WHERE a.consecutivo = :consecutivoAsamblea " +
            "ORDER BY t.desc_tema, p.id_pregunta", nativeQuery = true)
    List<Object[]> obtenerPreguntasEncuestasAsamblea(@Param("consecutivoAsamblea") Integer consecutivoAsamblea);

    @Query(value = "SELECT o.id_opc_respuesta, o.opcion_respuesta AS opcRespuesta, p.id_pregunta, p.tipo_pregunta AS tipoRespuesta " +
            "FROM opciones_respuesta o " +
            "JOIN preguntas p ON o.id_pregunta = p.id_pregunta " +
            "JOIN encuesta e ON p.id_encuesta = e.id_encuesta " +
            "JOIN asamblea a ON e.id_asamblea = a.consecutivo " +
            "WHERE a.consecutivo = :consecutivoAsamblea " +
            "ORDER BY p.id_pregunta, o.id_opc_respuesta", nativeQuery = true)
    List<Object[]> obtenerOpcionesRespuestasAsamblea(@Param("consecutivoAsamblea") Integer consecutivoAsamblea);


}

