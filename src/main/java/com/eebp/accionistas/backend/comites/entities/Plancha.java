package com.eebp.accionistas.backend.comites.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "plancha")
public class Plancha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPlancha;

    private Integer idAsamblea;
    private Integer idComite;
    private String idPrincipal;
    private String idSuplente;
}
