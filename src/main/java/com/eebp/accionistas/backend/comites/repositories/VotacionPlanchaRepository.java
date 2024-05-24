package com.eebp.accionistas.backend.comites.repositories;

import com.eebp.accionistas.backend.comites.entities.VotacionPlancha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VotacionPlanchaRepository extends JpaRepository<VotacionPlancha, Integer> {

    @Query(value = "SELECT vp.id_comite, c.desc_comite, vp.id_plancha, COUNT(*) AS total_votos, " +
            "p_principal.COD_USUARIO AS id_principal, " +
            "CONCAT(p_principal.nomPri, ' ', p_principal.nomSeg, ' ', p_principal.apePri, ' ', p_principal.apeSeg) AS nombre_principal, " +
            "p_suplente.COD_USUARIO AS id_suplente, " +
            "CONCAT(IFNULL(p_suplente.nomPri, ''), ' ', IFNULL(p_suplente.nomSeg, ''), ' ', IFNULL(p_suplente.apePri, ''), ' ', IFNULL(p_suplente.apeSeg, '')) AS nombre_suplente " +
            "FROM votacion_planchas vp " +
            "JOIN comites c ON vp.id_comite = c.id_comite " +
            "JOIN plancha pl ON vp.id_plancha = pl.id_plancha " +
            "JOIN persona p_principal ON pl.id_principal = p_principal.COD_USUARIO " +
            "LEFT JOIN persona p_suplente ON pl.id_suplente = p_suplente.COD_USUARIO " +
            "WHERE vp.id_plancha IN (SELECT id_plancha FROM plancha WHERE id_asamblea = (SELECT MAX(consecutivo) FROM asamblea)) " +
            "GROUP BY c.desc_comite, vp.id_comite, vp.id_plancha, p_principal.COD_USUARIO, p_principal.nomPri, p_principal.nomSeg, p_principal.apePri, p_principal.apeSeg, " +
            "p_suplente.COD_USUARIO, p_suplente.nomPri, p_suplente.nomSeg, p_suplente.apePri, p_suplente.apeSeg", nativeQuery = true)
    List<Object[]> obtenerVotosPorComiteYPlancha();
}
