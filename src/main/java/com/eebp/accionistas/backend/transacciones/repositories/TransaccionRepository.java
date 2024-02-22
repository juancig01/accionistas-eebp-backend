package com.eebp.accionistas.backend.transacciones.repositories;

import com.eebp.accionistas.backend.transacciones.entities.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransaccionRepository extends JpaRepository<Transaccion, Integer> { }
