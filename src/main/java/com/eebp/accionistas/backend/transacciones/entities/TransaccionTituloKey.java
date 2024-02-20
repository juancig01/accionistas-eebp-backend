package com.eebp.accionistas.backend.transacciones.entities;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

public class TransaccionTituloKey implements Serializable {

    private Integer conseTitulo;
    private Integer conseTrans;

}
