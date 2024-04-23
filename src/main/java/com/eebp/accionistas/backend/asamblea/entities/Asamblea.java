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
@Table(name = "asamblea")
public class Asamblea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAsamblea;
    private String vigencia_asamblea;
    private String obs_asamblea;
    private Date fecha_asamblea;
    private Integer porc_acciones;
    private Integer porc_valor;
    private Integer dps;
    private Integer tot_acciones;
    private Integer tot_acciones_reg;
    private String est_asamblea;
    private Integer indice_digitalizacion;
    private String tipo_asamblea;
    private String logo;
    private Integer cod_usu;
}
