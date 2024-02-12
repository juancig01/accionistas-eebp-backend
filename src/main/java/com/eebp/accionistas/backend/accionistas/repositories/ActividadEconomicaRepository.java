package com.eebp.accionistas.backend.accionistas.repositories;

import com.eebp.accionistas.backend.accionistas.entities.ActividadEconomica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActividadEconomicaRepository extends JpaRepository<ActividadEconomica, Integer> {
}
