package com.eebp.accionistas.backend.reportes.entities;

public interface ReporteTitulosDTO {
    String getTipDocumento();
    String getCodUsuario();
    String getNombreCompleto();
    String getMunicipioExp();
    String getAprobado();
    Integer getCanAccTit();
    Integer getNumTitulos();
    Double getPorcentajeParticipacion();
    Integer getValAccTit();
    String getEntidadBancaria();
    String getTipoCuentaBancaria();
    Long getNumCuentaBancaria();
}