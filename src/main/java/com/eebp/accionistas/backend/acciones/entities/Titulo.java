package com.eebp.accionistas.backend.acciones.entities;

import com.eebp.accionistas.backend.accionistas.entities.Persona;
import com.eebp.accionistas.backend.transacciones.entities.TransaccionTitulo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
