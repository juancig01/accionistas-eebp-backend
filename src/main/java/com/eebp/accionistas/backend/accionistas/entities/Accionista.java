package com.eebp.accionistas.backend.accionistas.entities;

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
@Table(name = "accionista")
public class Accionista {

    @Id
    @Column(name = "COD_USUARIO")
    private String codUsuario;

    @Column(name = "codrepresentante")
    private String codRepresentante;

    @Column(name = "tipoaccionista")
    private Integer tipoAccionista;

    @Column(name = "aprobado")
    private String aprobado;

    @Column(name = "descripcionrechazo")
    private String descripcionRechazo;

    @Column(name = "numcarnet")
    private String numCarnet;

    @Column(name = "tiporepresentante")
    private Integer tipoRepresentante;

}
