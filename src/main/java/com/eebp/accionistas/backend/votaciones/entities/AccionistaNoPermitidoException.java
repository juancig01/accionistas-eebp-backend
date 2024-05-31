package com.eebp.accionistas.backend.votaciones.entities;

public class AccionistaNoPermitidoException extends RuntimeException {
    public AccionistaNoPermitidoException(String message) {
        super(message);
    }
}
