package com.eebp.accionistas.backend.plantillas.repositories;

import com.eebp.accionistas.backend.plantillas.entities.Preguntas;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreguntasRepository extends JpaRepository<Preguntas, Integer> {
}
