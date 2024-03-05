package com.eebp.accionistas.backend.transacciones.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TransaccionDatos {

    @Id
    private Integer conseTrans;
    private Integer conseTitulo;
    private Integer numAcciones;
    private String idePer;

}
