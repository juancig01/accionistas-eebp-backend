package com.eebp.accionistas.backend.seguridad.repositories;

import com.eebp.accionistas.backend.seguridad.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByCodUsuario(String codUsuario);
}
