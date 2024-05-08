package com.eebp.accionistas.backend.plantillas.entities;

import java.util.List;

public class EncuestaDTO {

    private String nombreEncuesta;
    private String fechaCreacion;
    private String estadoEncuesta;
    private Integer idAsamblea;
    private String tipoEncuesta;
    private List<Integer> idsTemas;

    // Constructores, getters y setters

    public EncuestaDTO() {
    }

    public EncuestaDTO(String nombreEncuesta, String fechaCreacion, String estadoEncuesta, Integer idAsamblea, String tipoEncuesta, List<Integer> idsTemas) {
        this.nombreEncuesta = nombreEncuesta;
        this.fechaCreacion = fechaCreacion;
        this.estadoEncuesta = estadoEncuesta;
        this.idAsamblea = idAsamblea;
        this.tipoEncuesta = tipoEncuesta;
        this.idsTemas = idsTemas;
    }

    public String getNombreEncuesta() {
        return nombreEncuesta;
    }

    public void setNombreEncuesta(String nombreEncuesta) {
        this.nombreEncuesta = nombreEncuesta;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getEstadoEncuesta() {
        return estadoEncuesta;
    }

    public void setEstadoEncuesta(String estadoEncuesta) {
        this.estadoEncuesta = estadoEncuesta;
    }

    public Integer getIdAsamblea() {
        return idAsamblea;
    }

    public void setIdAsamblea(Integer idAsamblea) {
        this.idAsamblea = idAsamblea;
    }

    public String getTipoEncuesta() {
        return tipoEncuesta;
    }

    public void setTipoEncuesta(String tipoEncuesta) {
        this.tipoEncuesta = tipoEncuesta;
    }

    public List<Integer> getIdsTemas() {
        return idsTemas;
    }

    public void setIdsTemas(List<Integer> idsTemas) {
        this.idsTemas = idsTemas;
    }

}
