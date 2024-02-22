package com.eebp.accionistas.backend.transacciones.entities;

import com.eebp.accionistas.backend.acciones.entities.Titulo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Set;

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
    private String idePer;
    private Integer valTran;

    @OneToOne
    @JoinColumn(name = "codTipTran")
    private TipoTransaccion tipoTransaccion;

    @OneToOne
    @JoinColumn(name = "ideEstado")
    private TransaccionEstado estadoTransaccion;

    @Transient
    private List<TransaccionTitulo> transaccionTitulo;

}
