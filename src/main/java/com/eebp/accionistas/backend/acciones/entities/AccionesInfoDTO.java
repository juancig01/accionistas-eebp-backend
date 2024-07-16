package com.eebp.accionistas.backend.acciones.entities;

public class AccionesInfoDTO {
    private int numeroDeAccionesEnElMercado;
    private int numeroDeAccionesConDerechoAUtilidades;

    public AccionesInfoDTO(int numeroDeAccionesEnElMercado, int numeroDeAccionesConDerechoAUtilidades) {
        this.numeroDeAccionesEnElMercado = numeroDeAccionesEnElMercado;
        this.numeroDeAccionesConDerechoAUtilidades = numeroDeAccionesConDerechoAUtilidades;
    }

    // Getters y setters
    public int getNumeroDeAccionesEnElMercado() {
        return numeroDeAccionesEnElMercado;
    }

    public void setNumeroDeAccionesEnElMercado(int numeroDeAccionesEnElMercado) {
        this.numeroDeAccionesEnElMercado = numeroDeAccionesEnElMercado;
    }

    public int getNumeroDeAccionesConDerechoAUtilidades() {
        return numeroDeAccionesConDerechoAUtilidades;
    }

    public void setNumeroDeAccionesConDerechoAUtilidades(int numeroDeAccionesConDerechoAUtilidades) {
        this.numeroDeAccionesConDerechoAUtilidades = numeroDeAccionesConDerechoAUtilidades;
    }
}
