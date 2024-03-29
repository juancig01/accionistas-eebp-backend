package com.eebp.accionistas.backend.transacciones.entities;

import com.eebp.accionistas.backend.acciones.entities.EstadoTitulo;
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
@Table(name = "transaccion_titulo")
@IdClass(TransaccionTituloKey.class)
public class TransaccionTitulo {

    @Id
    private Integer conseTrans;

    @Id
    private Integer conseTitulo;

    private Integer numAcciones;

    @Transient
    private String descEstado;
}
