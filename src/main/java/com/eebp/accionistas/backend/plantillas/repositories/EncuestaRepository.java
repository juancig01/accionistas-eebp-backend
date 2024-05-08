package com.eebp.accionistas.backend.plantillas.repositories;

import com.eebp.accionistas.backend.plantillas.entities.Encuesta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EncuestaRepository extends JpaRepository<Encuesta, Integer> {
}
