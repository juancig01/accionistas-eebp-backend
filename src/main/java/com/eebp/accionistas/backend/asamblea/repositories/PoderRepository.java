package com.eebp.accionistas.backend.asamblea.repositories;

import com.eebp.accionistas.backend.asamblea.entities.Poder;
import com.eebp.accionistas.backend.asamblea.entities.PoderesDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PoderRepository extends JpaRepository<Poder, Integer> {

    Optional<Poder> findById(Integer id);

    @Query("SELECT " +
            "pow.consecutivoPoder AS consecutivo, " +
            "pd.codUsuario AS idPoderdante, " +
            "CONCAT(pd.nomPri, ' ', pd.nomSeg, ' ', pd.apePri, ' ', pd.apeSeg) AS nombrePoderdante, " +
            "SUM(t.canAccTit) AS accionesPoderdante, " +
            "CONCAT(p.nomPri, ' ', p.nomSeg, ' ', p.apePri, ' ', p.apeSeg) AS nombreApoderado, " +
            "p.codUsuario AS idApoderado, " +
            "pow.estado AS estado " +
            "FROM " +
            "Persona p " +
            "JOIN " +
            "Accionista a ON p.codUsuario = a.codUsuario " +
            "JOIN " +
            "TitulosPersona tp ON CAST(p.codUsuario AS Integer) = tp.idePer " +
            "JOIN " +
            "Titulo t ON tp.conseTitulo = t.conseTitulo " +
            "JOIN " +
            "Poder pow ON p.codUsuario = pow.idApoderado " +
            "JOIN " +
            "Persona pd ON pow.idPoderdante = pd.codUsuario " +
            "GROUP BY " +
            "pow.consecutivoPoder, " +
            "pd.codUsuario, " +
            "CONCAT(pd.nomPri, ' ', pd.nomSeg, ' ', pd.apePri, ' ', pd.apeSeg), " +
            "p.codUsuario, " +
            "CONCAT(p.nomPri, ' ', p.nomSeg, ' ', p.apePri, ' ', p.apeSeg), " +
            "pow.estado")
    List<PoderesDTO> obtenerDatosPoderes();

    @Query("SELECT apoderado.codUsuario AS idApoderado, " +
            "poderdante.codUsuario AS idPoderdante, " +
            "CONCAT(apoderado.nomPri, ' ', apoderado.nomSeg, ' ', apoderado.apePri, ' ', apoderado.apeSeg) AS nombreApoderado, " +
            "CONCAT(poderdante.nomPri, ' ', poderdante.nomSeg, ' ', poderdante.apePri, ' ', poderdante.apeSeg) AS nombrePoderdante " +
            "FROM Poder poder " +
            "JOIN Persona apoderado ON poder.idApoderado = apoderado.codUsuario " +
            "JOIN Persona poderdante ON poder.idPoderdante = poderdante.codUsuario " +
            "WHERE poder.idApoderado = :idApoderado")
    List<Object[]> obtenerPoderdantesPorApoderado(@Param("idApoderado") String idApoderado);
}
