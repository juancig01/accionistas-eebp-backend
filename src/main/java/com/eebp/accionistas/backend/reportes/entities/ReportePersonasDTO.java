package com.eebp.accionistas.backend.reportes.entities;

public interface ReportePersonasDTO {
    String getTipDocumento();
    String getCodUsuario();
    String getNombreCompleto();
    String getCorreoPersona();
    String getCelPersona();
    String getDirDomicilio();
    String getMunicipioDomicilio();
    String getDepartamentoDomicilio();
    String getFecNacimiento();
    String getEstCivPersona();
    String getProfPersona();
}