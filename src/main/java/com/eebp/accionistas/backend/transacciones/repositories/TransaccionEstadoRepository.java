package com.eebp.accionistas.backend.transacciones.repositories;

import com.eebp.accionistas.backend.transacciones.entities.TransaccionEstado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransaccionEstadoRepository extends JpaRepository<TransaccionEstado, Integer> {

    TransaccionEstado findByDescEstado(String descEstado);

}
