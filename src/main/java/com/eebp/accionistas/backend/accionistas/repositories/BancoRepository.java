package com.eebp.accionistas.backend.accionistas.repositories;

import com.eebp.accionistas.backend.accionistas.entities.Banco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BancoRepository extends JpaRepository<Banco, Integer> {
}
