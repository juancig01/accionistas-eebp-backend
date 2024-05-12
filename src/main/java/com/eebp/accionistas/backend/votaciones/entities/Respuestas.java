package com.eebp.accionistas.backend.votaciones.entities;

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
@Table(name = "respuestas")
public class Respuestas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idRespuesta;

    private Integer idPregunta;
    private String idPersona;
    private Integer idOpcRespuesta;
}
