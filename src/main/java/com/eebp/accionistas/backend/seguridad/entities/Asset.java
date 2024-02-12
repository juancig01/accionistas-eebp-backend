package com.eebp.accionistas.backend.seguridad.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Asset {
    private String fileName;
    private String url;
}
