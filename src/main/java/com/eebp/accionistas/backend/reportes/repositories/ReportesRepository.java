package com.eebp.accionistas.backend.reportes.repositories;

import com.eebp.accionistas.backend.reportes.entities.ReporteTitulosDTO;
import com.eebp.accionistas.backend.reportes.entities.ReportePersonasDTO;
import com.eebp.accionistas.backend.accionistas.entities.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportesRepository extends JpaRepository<Persona, String> {

    @Query(value =
            "SELECT " +
                    "  p.tipDocumento        AS tipDocumento, " +
                    "  p.COD_USUARIO         AS codUsuario, " +
                    "  CONCAT(COALESCE(p.nomPri,''),' ',COALESCE(p.nomSeg,''),' ',COALESCE(p.apePri,''),' ',COALESCE(p.apeSeg,'')) AS nombreCompleto, " +
                    "  m.nombre_municipio    AS municipioExp, " +
                    "  a.aprobado            AS aprobado, " +
                    "  SUM(t.can_acc_tit)    AS canAccTit, " +
                    "  COUNT(t.conse_titulo) AS numTitulos, " +
                    "  ROUND(SUM(t.can_acc_tit) * 100.0 / (SELECT SUM(can_acc_tit) FROM titulos WHERE ide_estado_titulo = 1), 4) AS porcentajeParticipacion, " +
                    "  t.val_acc_tit         AS valAccTit, " +
                    "  b.nomBanco            AS entidadBancaria, " +
                    "  p.tipoCuentaBancaria  AS tipoCuentaBancaria, " +
                    "  p.numCuentaBancaria   AS numCuentaBancaria " +
                    "FROM persona p " +
                    "JOIN accionista a ON p.COD_USUARIO = a.COD_USUARIO " +
                    "JOIN titulos_persona tp ON p.COD_USUARIO = tp.ide_per " +
                    "JOIN titulos t ON tp.conse_titulo = t.conse_titulo " +
                    "LEFT JOIN municipios m ON p.municipioExp = m.codigo " +
                    "LEFT JOIN bancos b ON p.entidadBancaria = b.codBanco " +
                    "WHERE t.ide_estado_titulo = 1 " +
                    "GROUP BY p.COD_USUARIO, p.tipDocumento, p.nomPri, p.nomSeg, p.apePri, p.apeSeg, " +
                    "         m.nombre_municipio, a.aprobado, t.val_acc_tit, b.nomBanco, p.tipoCuentaBancaria, p.numCuentaBancaria",
            nativeQuery = true)
    List<ReporteTitulosDTO> getReporteTitulos();

    @Query(value =
            "SELECT " +
                    "  p.tipDocumento           AS tipDocumento, " +
                    "  p.COD_USUARIO            AS codUsuario, " +
                    "  CONCAT(COALESCE(p.nomPri,''),' ',COALESCE(p.nomSeg,''),' ',COALESCE(p.apePri,''),' ',COALESCE(p.apeSeg,'')) AS nombreCompleto, " +
                    "  p.correoPersona          AS correoPersona, " +
                    "  p.celPersona             AS celPersona, " +
                    "  p.dirDomicilio           AS dirDomicilio, " +
                    "  m.nombre_municipio       AS municipioDomicilio, " +
                    "  d.nombre_departamento    AS departamentoDomicilio, " +
                    "  p.fecNacimiento          AS fecNacimiento, " +
                    "  p.estCivPersona          AS estCivPersona, " +
                    "  p.profPersona            AS profPersona " +
                    "FROM persona p " +
                    "LEFT JOIN municipios m ON p.municipioDomicilio = m.codigo " +
                    "LEFT JOIN departamentos d ON p.departamentoDomicilio = d.codigo",
            nativeQuery = true)
    List<ReportePersonasDTO> getReportePersonas();
}