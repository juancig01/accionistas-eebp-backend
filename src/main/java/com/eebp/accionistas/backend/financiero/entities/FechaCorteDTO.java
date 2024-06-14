package com.eebp.accionistas.backend.financiero.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor

public class FechaCorteDTO {

    private String fechaCorte;

    public FechaCorteDTO(String fechaCorte) {
        this.fechaCorte = fechaCorte;
    }
}
