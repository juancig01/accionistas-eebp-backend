package com.eebp.accionistas.backend.transacciones.entities;


import com.eebp.accionistas.backend.acciones.entities.TomadorTitulo;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class TransaccionDatos {

    private Integer cantAcciones;
    private List<TransaccionTitulo> titulos;
    private List<TomadorTitulo> tomadores;

}
