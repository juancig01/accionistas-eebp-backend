package com.eebp.accionistas.backend.accionistas.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "El acionista ya se encuentra registrado en la base de datos.")
public class AccionistaExistsException extends Exception{
}
