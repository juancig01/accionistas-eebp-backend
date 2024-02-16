package com.eebp.accionistas.backend.acciones.repositories;

import com.eebp.accionistas.backend.acciones.entities.Titulo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TituloRepository extends JpaRepository<Titulo, Integer> {
}
