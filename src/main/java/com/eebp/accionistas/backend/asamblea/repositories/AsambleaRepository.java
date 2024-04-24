package com.eebp.accionistas.backend.asamblea.repositories;

import com.eebp.accionistas.backend.asamblea.entities.Asamblea;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AsambleaRepository extends JpaRepository<Asamblea, Integer> {
}
