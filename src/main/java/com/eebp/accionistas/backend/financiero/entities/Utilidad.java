package com.eebp.accionistas.backend.financiero.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

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
    private String numAccMercado;
    private String numAccUtilidades;
    private String participacionAccion;
    private String valNomAccion;
    private String valIntrinseco;
    private String divParticipacion;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fecUtilidad;
    private Integer pagoUtilidad;
    private Integer pago1;
    private Integer pago2;
    private Integer pago3;
    private Integer consecutivoAsamblea;

}
