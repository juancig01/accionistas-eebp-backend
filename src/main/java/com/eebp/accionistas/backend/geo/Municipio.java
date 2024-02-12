package com.eebp.accionistas.backend.geo;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "municipios")
public class Municipio {
    @Id
    @Column(name = "codigo")
    @JsonProperty("codigoMunicipio")
    private Integer codigo;

    @Column(name = "nombre_municipio")
    private String nombreMunicipio;

    @JoinColumn(name = "codigo_departamento", referencedColumnName = "codigo")
    @ManyToOne(cascade = CascadeType.ALL)
    private Departamento departamento;
}
