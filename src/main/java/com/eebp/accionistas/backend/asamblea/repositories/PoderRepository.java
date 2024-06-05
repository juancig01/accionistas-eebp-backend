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
            "JOIN " +
            "Asamblea asmb ON pow.consecutivo = asmb.consecutivo " +  // Cambiado para unirse por consecutivo
            "WHERE " +
            "asmb.consecutivo = (SELECT MAX(a.consecutivo) FROM Asamblea a) " +  // Subconsulta para obtener la última asamblea
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


    @Query(value = "SELECT ra.id_asistente, ra.huella, ra.asistencia, ra.ide_Per, " +
            "CONCAT(apoderado.nomPri, ' ', apoderado.nomSeg, ' ', apoderado.apePri, ' ', apoderado.apeSeg) AS nombreApoderado, " +
            "p.id_poderdante, " +
            "CONCAT(poderdante.nomPri, ' ', poderdante.nomSeg, ' ', poderdante.apePri, ' ', poderdante.apeSeg) AS nombrePoderdante, " +
            "CASE WHEN a1.tipoAccionista IN (1, 2) OR a2.tipoAccionista IN (1, 2) THEN 'SI' ELSE 'NO' END AS validacion " +
            "FROM registro_asamblea ra " +
            "LEFT JOIN (SELECT p1.* FROM poder p1 " +
            "INNER JOIN (SELECT id_apoderado, MAX(consecutivo) AS max_consecutivo FROM poder GROUP BY id_apoderado) p2 " +
            "ON p1.id_apoderado = p2.id_apoderado AND p1.consecutivo = p2.max_consecutivo) p ON ra.ide_per = p.id_apoderado " +
            "LEFT JOIN persona apoderado ON ra.ide_per = apoderado.COD_USUARIO " +
            "LEFT JOIN persona poderdante ON p.id_poderdante = poderdante.COD_USUARIO " +
            "LEFT JOIN accionista a1 ON apoderado.COD_USUARIO = a1.COD_USUARIO " +
            "LEFT JOIN accionista a2 ON poderdante.COD_USUARIO = a2.COD_USUARIO " +
            "WHERE ra.consecutivo = (SELECT MAX(r.consecutivo) FROM registro_asamblea r) " +
            "AND ra.ide_per = :idePer", nativeQuery = true)
    List<Object[]> obtenerDetallesPorIdePer(@Param("idePer") String idePer);
}
