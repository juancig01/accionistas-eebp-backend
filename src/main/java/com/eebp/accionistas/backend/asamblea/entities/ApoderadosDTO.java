package com.eebp.accionistas.backend.asamblea.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApoderadosDTO {


    private List<ApoderadoDTO> apoderado;
    private List<PoderdanteDTO> poderDantes;

    // Getters y setters
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApoderadoDTO {
        private String codUsuario;
        private String nombres;

        // Getters y setters
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PoderdanteDTO {
        private String codUsuario;
        private String nombres;

        // Getters y setters
    }
}
