package com.eebp.accionistas.backend.acciones.entities;

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
@Table(name = "estado_titulo")
public class EstadoTitulo {

    @Id
    private Integer ideEstadoTitulo;

    private String descEstado;

}
