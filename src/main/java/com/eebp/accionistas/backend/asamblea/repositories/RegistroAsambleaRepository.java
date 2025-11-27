package com.eebp.accionistas.backend.asamblea.repositories;

import com.eebp.accionistas.backend.asamblea.entities.AsistentesAsambleaDTO;
import com.eebp.accionistas.backend.asamblea.entities.RegistroAsamblea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RegistroAsambleaRepository extends JpaRepository<RegistroAsamblea, Integer> {

    Optional<RegistroAsamblea> findByConsecutivoAndIdePer(Integer consecutivo, String idePer);

    @Query("SELECT " +
            "ra.idAsistente AS idAsistente, " +
            "ra.asistencia AS asistencia, " +
            "ra.idePer AS codUsuario, " +
            "COALESCE(CONCAT(p.nomPri, ' ', p.nomSeg), p.razonSocial) AS nombres, " +
            "COALESCE(CONCAT(p.apePri, ' ', p.apeSeg), p.razonSocial) AS apellidos, " +

            // CÁLCULO MODIFICADO: Suma de acciones propias (ap_t) + acciones representadas (pd_t)
            "COALESCE(SUM(ap_t.canAccTit), 0) + COALESCE(SUM(pd_t.canAccTit), 0) AS acciones, " +

            "p.celPersona AS celPersona, " +
            "p.correoPersona AS correoPersona " +
            "FROM " +
            "Persona p " +
            "JOIN " +
            "RegistroAsamblea ra ON ra.idePer = p.codUsuario " +

            // 1. UNIONES PARA ACCIONES PROPIAS DEL ASISTENTE ('p' / Apoderado)
            // Usamos LEFT JOIN y nuevos alias (ap_tp, ap_t) para manejar a asistentes sin acciones.
            "LEFT JOIN TitulosPersona ap_tp ON CAST(p.codUsuario AS Integer) = ap_tp.idePer " +
            "LEFT JOIN Titulo ap_t ON ap_tp.conseTitulo = ap_t.conseTitulo " +

            // 2. UNIONES PARA ACCIONES REPRESENTADAS (Poderdante)
            // Buscamos los Poderes donde 'p' es el Apoderado
            "LEFT JOIN Poder pow ON p.codUsuario = pow.idApoderado " +
            "LEFT JOIN Persona pd ON pow.idPoderdante = pd.codUsuario " +
            "LEFT JOIN TitulosPersona pd_tp ON CAST(pd.codUsuario AS Integer) = pd_tp.idePer " +
            "LEFT JOIN Titulo pd_t ON pd_tp.conseTitulo = pd_t.conseTitulo " +

            "WHERE " +
            "ra.consecutivo = (SELECT MAX(a1.consecutivo) FROM Asamblea a1) " +
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


    @Query(value = "SELECT SUM(can_acc_tit) AS totalAccionesGeneral " +
            "FROM accionista ac " +
            "LEFT JOIN titulos_persona tp ON ac.COD_USUARIO = tp.ide_per " +
            "LEFT JOIN titulos t ON tp.conse_titulo = t.conse_titulo " +
            "WHERE ac.aprobado = 'S'", nativeQuery = true)
    Integer getTotalAcciones();
}
