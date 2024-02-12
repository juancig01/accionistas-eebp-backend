package com.eebp.accionistas.backend.seguridad.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "Credenciales no validas para iniciar sesion.")
public class AuthException extends Exception{
}
