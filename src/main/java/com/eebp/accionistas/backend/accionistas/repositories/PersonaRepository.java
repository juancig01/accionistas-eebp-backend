package com.eebp.accionistas.backend.accionistas.repositories;

import com.eebp.accionistas.backend.acciones.entities.Titulo;
import com.eebp.accionistas.backend.acciones.entities.TitulosPersona;
import com.eebp.accionistas.backend.accionistas.entities.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, String> {
    List<Persona> findByTitulos(Titulo titulo);
}