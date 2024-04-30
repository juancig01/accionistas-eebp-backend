package com.eebp.accionistas.backend.asamblea.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.hpsf.Decimal;


import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "registro_asamblea")
public class RegistroAsamblea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAsistente;
    private Integer consecutivo;
    private Boolean asistencia;
    private String idePer;
    private String huella;

}
