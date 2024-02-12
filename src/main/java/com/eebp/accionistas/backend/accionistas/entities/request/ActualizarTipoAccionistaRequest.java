package com.eebp.accionistas.backend.accionistas.entities.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActualizarTipoAccionistaRequest {
    private String codUsuario;
    private Integer tipoAccionista;
}
