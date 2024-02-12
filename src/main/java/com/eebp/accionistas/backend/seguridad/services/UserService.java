package com.eebp.accionistas.backend.seguridad.services;

import com.eebp.accionistas.backend.seguridad.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {
    UserDetailsService userDetailsService();
}
