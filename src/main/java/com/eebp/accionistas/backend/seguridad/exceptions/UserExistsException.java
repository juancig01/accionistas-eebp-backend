package com.eebp.accionistas.backend.seguridad.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "El usuario ya existe en la base de datos.")
public class UserExistsException extends Exception{
}
