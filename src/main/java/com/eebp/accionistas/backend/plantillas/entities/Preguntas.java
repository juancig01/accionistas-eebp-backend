package com.eebp.accionistas.backend.plantillas.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "preguntas")
public class Preguntas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPregunta;

    private Integer idEncuesta;
    private Integer idTema;
    private String pregunta;
    private String tipoPregunta;

    @OneToMany(mappedBy = "preguntas", cascade = CascadeType.ALL)
    private List<OpcionesRespuesta> opcionesRespuesta = new ArrayList<>();


}
