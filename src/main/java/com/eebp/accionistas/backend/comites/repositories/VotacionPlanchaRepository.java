package com.eebp.accionistas.backend.comites.repositories;

import com.eebp.accionistas.backend.comites.entities.VotacionPlancha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VotacionPlanchaRepository extends JpaRepository<VotacionPlancha, Integer> {

    @Query(value = "SELECT vp.id_comite, c.desc_comite, vp.id_plancha, COUNT(*) AS total_votos " +
            "FROM votacion_planchas vp " +
            "JOIN comites c ON vp.id_comite = c.id_comite " +
            "WHERE vp.id_plancha IN (SELECT id_plancha FROM plancha WHERE id_asamblea = (SELECT MAX(consecutivo) FROM asamblea)) " +
            "GROUP BY c.desc_comite, vp.id_comite, vp.id_plancha", nativeQuery = true)
    List<Object[]> obtenerVotosPorComiteYPlancha();
}
