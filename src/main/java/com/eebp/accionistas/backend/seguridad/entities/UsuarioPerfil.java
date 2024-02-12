package com.eebp.accionistas.backend.seguridad.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Entity
@AllArgsConstructor
@Table(name = "usuario_perfil")
@IdClass(UsuarioPerfilId.class)
public class UsuarioPerfil {

    public UsuarioPerfil() {}

    @Id
    @Column(name = "COD_USUARIO")
    private String codUsuario;

    @Id
    @Column(name = "COD_PERFIL")
    private Integer codPerfil;

}