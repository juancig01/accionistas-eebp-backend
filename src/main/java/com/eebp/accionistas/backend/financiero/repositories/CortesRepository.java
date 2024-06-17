package com.eebp.accionistas.backend.financiero.repositories;

import com.eebp.accionistas.backend.financiero.entities.Cortes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface CortesRepository extends JpaRepository<Cortes, Integer> {

    List<Cortes> findByFechaCorteBetweenAndEsAccionista(Date startDate, Date endDate, String esAccionista);
}
