package com.eebp.accionistas.backend.financiero.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class Utilidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ideUtilidad;
    private Integer numAccMercado;
    private Integer numAccUtilidades;
    private Integer participacionAccion;
    private Integer valNomAccion;
    private Integer valIntrinseco;
    private Integer divParticipacion;
    private Date fecUtilidad;
    private Integer pagoUtilidad;

}
