package com.eebp.accionistas.backend.asamblea.repositories;

import com.eebp.accionistas.backend.asamblea.entities.AsistentesAsambleaDTO;
import com.eebp.accionistas.backend.asamblea.entities.RegistroAsamblea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RegistroAsambleaRepository extends JpaRepository<RegistroAsamblea, Integer> {

    @Query("SELECT " +
            "ra.idAsistente AS idAsistente, " +
            "ra.asistencia AS asistencia, " +
            "ra.idePer AS codUsuario, " +
            "CONCAT(p.nomPri, ' ', p.nomSeg) AS nombres, " +
            "CONCAT(p.apePri, ' ', p.apeSeg) AS apellidos, " +
            "SUM(t.canAccTit) AS acciones, " +
            "p.celPersona AS celPersona, " +
            "p.correoPersona AS correoPersona " +
            "FROM " +
            "Persona p " +
            "JOIN " +
            "RegistroAsamblea ra ON ra.idePer = p.codUsuario " +
            "JOIN " +
            "TitulosPersona tp ON CAST(p.codUsuario AS Integer) = tp.idePer " +
            "JOIN " +
            "Titulo t ON tp.conseTitulo = t.conseTitulo " +
            "GROUP BY " +
            "ra.idAsistente, " +
            "ra.asistencia, " +
            "ra.idePer, " +
            "nombres, " +
            "apellidos, " +
            "p.celPersona, " +
            "p.correoPersona " +
            "ORDER BY ra.idAsistente")
    List<AsistentesAsambleaDTO> obtenerRegistroAsamblea();
}
