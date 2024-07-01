package com.eebp.accionistas.backend.transacciones.repositories;

import com.eebp.accionistas.backend.transacciones.entities.TransaccionTitulo;
import com.eebp.accionistas.backend.transacciones.entities.TransaccionTituloKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransaccionTituloRepository extends JpaRepository<TransaccionTitulo, TransaccionTituloKey> {
    /*List<TransaccionTitulo> findByConseTrans(Integer conseTrans);*/

    @Query("SELECT new com.eebp.accionistas.backend.transacciones.entities.TransaccionTitulo(tt.conseTrans, tt.conseTitulo, tt.numAcciones, et.descEstado) FROM TransaccionTitulo tt JOIN Titulo t ON tt.conseTitulo = t.conseTitulo JOIN EstadoTitulo et ON t.estadoTitulo.ideEstadoTitulo = et.ideEstadoTitulo WHERE tt.conseTrans = :conseTrans")
    List<TransaccionTitulo> findTransaccionesPorConseTrans(@Param("conseTrans") Integer conseTrans);

    @Query("SELECT new com.eebp.accionistas.backend.transacciones.entities.TransaccionTitulo(tt.conseTrans, tt.conseTitulo, tt.numAcciones, et.descEstado) " +
            "FROM TransaccionTitulo tt " +
            "JOIN Titulo t ON tt.conseTitulo = t.conseTitulo " +
            "JOIN EstadoTitulo et ON t.estadoTitulo.ideEstadoTitulo = et.ideEstadoTitulo " +
            "WHERE tt.conseTrans = :conseTrans " +
            "AND et.ideEstadoTitulo != 3")
    List<TransaccionTitulo> findTransaccionesPorConseTransExcluyendoEstado3(@Param("conseTrans") Integer conseTrans);
}
