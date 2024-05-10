package com.eebp.accionistas.backend.plantillas.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PreguntaDTO {
    private Integer idEncuesta;
    private Integer idTema;
    private String pregunta;
    private String tipoPregunta;
    private List<String> opcionesRespuesta;

}
