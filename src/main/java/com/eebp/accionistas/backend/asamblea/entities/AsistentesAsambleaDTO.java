package com.eebp.accionistas.backend.asamblea.entities;

public interface AsistentesAsambleaDTO {

    Integer getIdAsistente();
    Boolean getAsistencia();
    String getCodUsuario();
    String getNombres();
    String getApellidos();
    Integer getAcciones();

    String getCorreoPersona();

    String getCelPersona();
}
