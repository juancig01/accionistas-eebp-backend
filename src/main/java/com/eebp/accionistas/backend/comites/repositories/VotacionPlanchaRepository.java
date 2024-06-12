package com.eebp.accionistas.backend.comites.repositories;

import com.eebp.accionistas.backend.comites.entities.VotacionPlancha;
import com.eebp.accionistas.backend.comites.entities.VotoDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VotacionPlanchaRepository extends JpaRepository<VotacionPlancha, Integer> {

    @Query(value = "SELECT vp.id_comite, c.desc_comite, vp.id_plancha, SUM(t.can_acc_tit) AS total_votos, " +
            "p_principal.COD_USUARIO AS id_principal, " +
            "COALESCE(CONCAT(COALESCE(p_principal.nomPri, ''), ' ', COALESCE(p_principal.nomSeg, ''), ' ', COALESCE(p_principal.apePri, ''), ' ', COALESCE(p_principal.apeSeg, '')), COALESCE(p_principal.razonSocial, 'Sin Razón Social')) AS nombre_principal, " +
            "p_suplente.COD_USUARIO AS id_suplente, " +
            "COALESCE(CONCAT(COALESCE(p_suplente.nomPri, ''), ' ', COALESCE(p_suplente.nomSeg, ''), ' ', COALESCE(p_suplente.apePri, ''), ' ', COALESCE(p_suplente.apeSeg, '')), COALESCE(p_suplente.razonSocial, 'Sin Razón Social')) AS nombre_suplente " +
            "FROM votacion_planchas vp " +
            "JOIN comites c ON vp.id_comite = c.id_comite " +
            "JOIN plancha pl ON vp.id_plancha = pl.id_plancha " +
            "JOIN persona p_principal ON pl.id_principal = p_principal.COD_USUARIO " +
            "LEFT JOIN persona p_suplente ON pl.id_suplente = p_suplente.COD_USUARIO " +
            "LEFT JOIN titulos_persona tp ON vp.id_persona = tp.ide_per " +
            "LEFT JOIN titulos t ON tp.conse_titulo = t.conse_titulo " +
            "WHERE vp.id_plancha IN (SELECT id_plancha FROM plancha WHERE id_asamblea = (SELECT MAX(consecutivo) FROM asamblea)) " +
            "GROUP BY c.desc_comite, vp.id_comite, vp.id_plancha, p_principal.COD_USUARIO, p_principal.nomPri, p_principal.nomSeg, p_principal.apePri, p_principal.apeSeg, p_suplente.COD_USUARIO, p_suplente.nomPri, p_suplente.nomSeg, p_suplente.apePri, p_suplente.apeSeg", nativeQuery = true)
    List<Object[]> obtenerVotosPorComiteYPlancha();

    @Query(value =
            "SELECT " +
                    "    c.desc_comite, " +
                    "    CASE WHEN vpvotos.votos IS NOT NULL THEN 'SI' ELSE 'NO' END AS voto  " +
                    "FROM " +
                    "    comites c " +
                    "LEFT JOIN ( " +
                    "    SELECT " +
                    "        id_comite, " +
                    "        COUNT(*) AS votos " +
                    "    FROM " +
                    "        votacion_planchas " +
                    "    WHERE " +
                    "        id_persona = :idPersona " +
                    "    GROUP BY " +
                    "        id_comite " +
                    ") AS vpvotos ON c.id_comite = vpvotos.id_comite " +
                    "WHERE " +
                    "    c.id_comite IN (1, 5) " +
                    "ORDER BY " +
                    "    c.id_comite", nativeQuery = true)
    List<Object[]> obtenerVotosPorComiteYPersona(Integer idPersona);

}
