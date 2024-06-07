package com.eebp.accionistas.backend.asamblea.repositories;

import com.eebp.accionistas.backend.asamblea.entities.Asamblea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AsambleaRepository extends JpaRepository<Asamblea, Integer> {
    Optional<Asamblea> findByTipoAsamblea(String tipoAsamblea);

    @Query("SELECT a FROM Asamblea a WHERE a.tipoAsamblea = :tipoAsamblea AND SUBSTRING(a.fechaAsamblea, 1, 4) = :year")
    Optional<Asamblea> findByTipoAsambleaAndYear(@Param("tipoAsamblea") String tipoAsamblea, @Param("year") String year);


    @Query(value = "SELECT SUM(t.can_acc_tit) AS total_acciones "
            + "FROM titulos t "
            + "INNER JOIN titulos_persona tp ON t.conse_titulo = tp.conse_titulo "
            + "WHERE tp.ide_per = :idePer", nativeQuery = true)
    Integer totalAccionesPorPersona(@Param("idePer") Integer idePer);
}
