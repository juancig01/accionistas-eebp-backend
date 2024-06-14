package com.eebp.accionistas.backend.financiero.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FechaCorteRequest {

    private LocalDate fechaCorte;
}
