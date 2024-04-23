package com.eebp.accionistas.backend.asamblea.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "poder")
public class Poder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPoder;
    private Integer idAsamblea;
    private Integer vigenciaAsamblea;
    private String idApoderado;
    private String idPoderdante;
    private String estadoPoder;
    private Date fecha;
    private String indiceDigitalizacion;
}
