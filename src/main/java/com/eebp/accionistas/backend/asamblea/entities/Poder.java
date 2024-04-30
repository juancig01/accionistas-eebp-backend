package com.eebp.accionistas.backend.asamblea.entities;

import com.eebp.accionistas.backend.seguridad.entities.Asset;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "poder")
public class Poder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer consecutivoPoder;
    private Integer consecutivo;
    private String fechaAsamblea;
    private String idApoderado;
    private String idPoderdante;
    private String estado;
}
