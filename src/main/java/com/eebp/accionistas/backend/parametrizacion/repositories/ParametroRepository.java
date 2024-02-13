package com.eebp.accionistas.backend.parametrizacion.repositories;

import com.eebp.accionistas.backend.parametrizacion.entities.Parametro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParametroRepository extends JpaRepository<Parametro, Integer> {
}
