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
    private Float numAccMercado;
    private Float numAccUtilidades;
    private Float participacionAccion;
    private Float valNomAccion;
    private Float valIntrinseco;
    private Float divParticipacion;
    private Date fecUtilidad;

}
