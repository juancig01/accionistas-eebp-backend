package com.eebp.accionistas.backend.comites.repositories;

import com.eebp.accionistas.backend.comites.entities.Plancha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlanchaRepository extends JpaRepository<Plancha, Integer> {

    @Query(value = "SELECT pl.id_principal AS codUsuario, " +
            "CONCAT(pe.nomPri, ' ', pe.nomSeg, ' ', pe.apePri, ' ', pe.apeSeg) AS nombresPrincipal, " +
            "pl.id_suplente AS codUsuarioSuplente, " +
            "CONCAT(s.nomPri, ' ', s.nomSeg, ' ', s.apePri, ' ', s.apeSeg) AS nombresSuplente, " +
            "pl.id_plancha AS idPlancha, " +
            "c.id_comite AS idComite " +
            "FROM plancha pl " +
            "JOIN comites c ON pl.id_comite = c.id_comite " +
            "JOIN persona pe ON pl.id_principal = pe.COD_USUARIO " +
            "LEFT JOIN persona s ON pl.id_suplente = s.COD_USUARIO " +
            "WHERE pl.id_asamblea = ( " +
            "    SELECT consecutivo " +
            "    FROM asamblea " +
            "    WHERE consecutivo = (SELECT MAX(consecutivo) FROM asamblea) " +
            ")", nativeQuery = true)
    List<Object[]> obtenerPlanchasUltimaAsamblea();
}
