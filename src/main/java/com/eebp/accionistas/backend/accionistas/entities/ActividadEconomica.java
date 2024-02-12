package com.eebp.accionistas.backend.accionistas.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "actividades_economicas")
public class ActividadEconomica {
    @Column(name = "codigo")
    @Id
    private Integer codActEco;

    @Column(name = "descripcion")
    private String nomActEco;
}
