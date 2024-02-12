package com.eebp.accionistas.backend.geo;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "departamentos")
public class Departamento {

    @Id
    @Column(name = "codigo")
    @JsonProperty("codigoDepartamento")
    private Integer codigo;

    @Column(name = "nombre_departamento")
    private String nombreDepartamento;
}
