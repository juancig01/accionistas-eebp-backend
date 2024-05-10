package com.eebp.accionistas.backend.plantillas.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EncuestTemasDTO {

    private Integer idEncuesta;
    private String nombreEncuesta;
    private String fechaCreacion;
    private String estadoEncuesta;
    private Integer idAsamblea;
    private String tipoEncuesta;
    private List<Integer> temas;
}
