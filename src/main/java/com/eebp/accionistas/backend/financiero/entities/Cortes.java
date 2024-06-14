package com.eebp.accionistas.backend.financiero.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Cortes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCorte;

    private String codAccionista;
    private String nomAccionista;
    private String esAccionista;
    private Integer folioTitulo;
    private Integer totalAcciones;
    private Date fechaCorte;
}
