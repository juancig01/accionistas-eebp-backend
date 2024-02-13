package com.eebp.accionistas.backend.acciones.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
public class Titulo {

    @Id
    private Integer conseTitulo;
    private String[] accionistas;
    private Integer canAccTit;
    private Integer valAccTit;
    private String claAccTit;
    private String tipAccTit;
    private String estTit;
    private LocalDate fecCreTit;
    private String codUsuReg;
    private LocalDateTime fecFinTit;
    private String obsAccTit;
}
