package com.eebp.accionistas.backend.accionistas.entities.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccionistaRepresentanteResponse {

    private String codAccionista;
    private String nomAccionista;
    private Integer tipoAccionista;
    private String codRepresentante;
    private String nomRepresentante;
    private String esAccionista;
    private String tipoDocAccionista;
    private String tipoDocRepresentante;
    private String descripcionRechazo;

}
