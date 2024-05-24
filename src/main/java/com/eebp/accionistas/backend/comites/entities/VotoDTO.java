package com.eebp.accionistas.backend.comites.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VotoDTO {

    private String descComite;
    private String voto;
    //String getDescComite();
    //int getVoto();
}
