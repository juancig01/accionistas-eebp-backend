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
@Table(name = "titulos_persona")
public class TitulosPersona {
    @Id
    private Integer conseTitulo;
    private Integer idePer;

}
