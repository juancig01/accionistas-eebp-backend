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
@Table(name = "temas")
public class Temas {

    @Id
    private Integer idTema;
    private String descTema;

    @ManyToMany(mappedBy = "temas")
    private Set<Encuesta> encuestas = new HashSet<>();

}
