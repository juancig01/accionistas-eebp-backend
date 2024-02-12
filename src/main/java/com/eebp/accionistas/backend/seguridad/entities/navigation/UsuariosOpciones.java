package com.eebp.accionistas.backend.seguridad.entities.navigation;

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
@Table(name = "usuario_opciones")
@IdClass(UsuariosOpcionesId.class)
public class UsuariosOpciones {
    @Id
    @Column(name = "COD_USUARIO")
    String codUsuario;

    @Id
    @Column(name = "COD_OPCION")
    Integer codOpcion;
}
