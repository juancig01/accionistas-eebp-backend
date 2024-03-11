package com.eebp.accionistas.backend.acciones.repositories;

import com.eebp.accionistas.backend.acciones.entities.TitulosPersona;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TituloPersonaRepository extends JpaRepository<TitulosPersona, Integer> {
    TitulosPersona findByConseTitulo(Integer conseTitulo);
}
