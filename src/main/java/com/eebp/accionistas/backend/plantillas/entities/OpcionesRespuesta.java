package com.eebp.accionistas.backend.plantillas.entities;

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
@Table(name = "opciones_respuesta")
public class OpcionesRespuesta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idOpcRespuesta;
    private Integer idPregunta;
    private String opcionRespuesta;
}
