package com.eebp.accionistas.backend.accionistas.repositories;

import com.eebp.accionistas.backend.accionistas.entities.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, String> {
}