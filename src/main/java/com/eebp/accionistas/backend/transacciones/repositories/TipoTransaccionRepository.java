package com.eebp.accionistas.backend.transacciones.repositories;

import com.eebp.accionistas.backend.transacciones.entities.TipoTransaccion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoTransaccionRepository extends JpaRepository<TipoTransaccion, Integer> {
}
