package com.eebp.accionistas.backend.transacciones.entities;

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
@Table(name = "transacciones")
public class Transaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer conseTrans;
    private Date fecTrans;
    private Integer conseTitulo;
    private String idePer;
    private Integer canAccTran;
    private String tipMovTran;
    private Integer valTran;
    private Integer saldoAcc;
    private String ideAccRecibe;
    private String conseTitNuevo;
    private Integer conseTitAnt;
    private Integer conseTitTraslado;

    @ManyToOne
    @JoinColumn(name = "codTipTran")
    private TipoTransaccion tipoTransaccion;

}
