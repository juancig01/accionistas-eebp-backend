package com.eebp.accionistas.backend.seguridad.entities.navigation;

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
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "opciones")
public class Opcion {
    @Id
    @Column(name = "COD_OPCION")
    private String id;
    @Column(name = "NOM_OPCION")
    private String title;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "ICON")
    private String icon;

    @Column(name = "LINK")
    private String link;

    @Column(name = "ACTIVE")
    private Boolean active;
}
