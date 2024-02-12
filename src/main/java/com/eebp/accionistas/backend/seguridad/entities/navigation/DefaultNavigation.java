package com.eebp.accionistas.backend.seguridad.entities.navigation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DefaultNavigation {

    @JsonProperty("default")
    private List<Modulo> modulos;
}
