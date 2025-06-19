package com.eebp.accionistas.backend.acciones.repositories;

import com.eebp.accionistas.backend.acciones.entities.EstadoTitulo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadoTituloRepository extends JpaRepository<EstadoTitulo, Integer> {
}