package com.eebp.accionistas.backend.seguridad.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "El usuario no se pudo crear en el sistema.")
public class NewUserException extends Exception {
}
