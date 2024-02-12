package com.eebp.accionistas.backend.geo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MunicipioRepository extends JpaRepository<Municipio, Integer> {
    List<Municipio> findByDepartamentoCodigo(Integer codigoDepartamento);
}
