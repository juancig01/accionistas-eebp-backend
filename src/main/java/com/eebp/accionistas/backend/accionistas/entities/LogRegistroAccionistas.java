package com.eebp.accionistas.backend.accionistas.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "log_registro_accionistas")
public class LogRegistroAccionistas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "COD_USUARIO")
    private String codUsuario;

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "accion")
    private String accion;

    @Column(name = "razon")
    private String razon;

    @Column(name = "fecha")
    private LocalDateTime fecha;
}
