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
@Table(name = "votacion_planchas")
public class VotacionPlancha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idVoto;

    private Integer idComite;
    private Integer idPlancha;
    private Integer idPersona;
}
