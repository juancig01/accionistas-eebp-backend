package com.eebp.accionistas.backend.acciones.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "titulos")
public class Titulo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer conseTitulo;
    private Integer canAccTit;
    private Integer valAccTit;
    private String claAccTit;
    private String tipAccTit;
    private LocalDate fecCreTit;
    private LocalDateTime fecFinTit;
    private String obsAccTit;

    @OneToOne
    @JoinColumn(name = "ideEstadoTitulo")
    private EstadoTitulo estadoTitulo;

}
