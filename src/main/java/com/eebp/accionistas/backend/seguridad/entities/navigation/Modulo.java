package com.eebp.accionistas.backend.seguridad.entities.navigation;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "modulos")
public class Modulo {
    @Id
    @Column(name = "COD_MODULO")
    private Integer id;

    @Column(name = "NOM_MODULO")
    private String title;
    @Column(name = "DESCRIPCION")
    private String subtitle;

    @Column(name = "TYPE")
    private String type;

    @Transient
    private List<Opcion> children;
}
