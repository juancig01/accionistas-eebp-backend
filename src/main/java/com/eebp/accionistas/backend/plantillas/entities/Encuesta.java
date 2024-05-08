package com.eebp.accionistas.backend.plantillas.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "encuesta")
public class Encuesta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEncuesta;
    private String nombreEncuesta;
    private String fechaCreacion;
    private String estadoEncuesta;
    private Integer idAsamblea;
    private String tipoEncuesta;

    @ManyToMany
    @JoinTable(
            name = "encuesta_tema",
            joinColumns = @JoinColumn(name = "id_encuesta"),
            inverseJoinColumns = @JoinColumn(name = "id_tema")
    )
    private Set<Temas> temas = new HashSet<>();

}
