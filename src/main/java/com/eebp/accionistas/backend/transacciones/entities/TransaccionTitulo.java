package com.eebp.accionistas.backend.transacciones.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
