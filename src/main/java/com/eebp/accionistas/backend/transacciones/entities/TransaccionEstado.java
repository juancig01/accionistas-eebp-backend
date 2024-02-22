package com.eebp.accionistas.backend.transacciones.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "estado_transaccion")
public class TransaccionEstado {

    @Id
    private Integer ideEstado;
    private String descEstado;
}
