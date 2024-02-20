package com.eebp.accionistas.backend.transacciones.repositories;

import com.eebp.accionistas.backend.transacciones.entities.TipoTransaccion;
import com.eebp.accionistas.backend.transacciones.entities.TransaccionTitulo;
import com.eebp.accionistas.backend.transacciones.entities.TransaccionTituloKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransaccionTituloRepository extends JpaRepository<TransaccionTitulo, TransaccionTituloKey> {
    List<TransaccionTitulo> findByConseTrans(Integer conseTrans);
}
