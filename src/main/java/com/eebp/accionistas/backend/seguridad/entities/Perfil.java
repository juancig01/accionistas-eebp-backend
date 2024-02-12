package com.eebp.accionistas.backend.seguridad.entities;

import jakarta.persistence.Column;
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
@Table(name = "perfiles")
public class Perfil {
    @Id
    @Column(name = "COD_PERFIL")
    private Integer codPerfil;

    @Column(name = "NOM_PERFIL")
    private String nomPerfil;
}
