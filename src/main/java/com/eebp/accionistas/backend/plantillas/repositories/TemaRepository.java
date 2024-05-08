package com.eebp.accionistas.backend.plantillas.repositories;

import com.eebp.accionistas.backend.plantillas.entities.Temas;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemaRepository extends JpaRepository<Temas, Integer> {
}
