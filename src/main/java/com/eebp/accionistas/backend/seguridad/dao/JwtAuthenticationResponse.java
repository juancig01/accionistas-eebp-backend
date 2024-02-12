package com.eebp.accionistas.backend.seguridad.dao;

import com.eebp.accionistas.backend.seguridad.entities.FuseUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtAuthenticationResponse {
    private String accessToken;
    private FuseUser user;
    private final String tokenType = "bearer";
}
