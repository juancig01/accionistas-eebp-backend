package com.eebp.accionistas.backend.seguridad.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Usuario no existe en la base de datos.")
public class UserNotFoundException extends Exception{
}
