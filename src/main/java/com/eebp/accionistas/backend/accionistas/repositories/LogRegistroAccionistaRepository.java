package com.eebp.accionistas.backend.accionistas.repositories;

import com.eebp.accionistas.backend.accionistas.entities.LogRegistroAccionistas;
import jakarta.persistence.OrderBy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogRegistroAccionistaRepository extends JpaRepository<LogRegistroAccionistas, String> {

    @OrderBy("id DESC")
    List<LogRegistroAccionistas> getLogRegistroAccionistasByCodUsuario(String codUsuario);
}
