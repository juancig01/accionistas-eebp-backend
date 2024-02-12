package com.eebp.accionistas.backend.accionistas.repositories;

import com.eebp.accionistas.backend.accionistas.entities.Accionista;
import com.eebp.accionistas.backend.accionistas.entities.response.AccionistaRepresentanteResponse;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccionistaRepository extends JpaRepository<Accionista, String> {
    @Modifying
    @Transactional
    @Query(value = "UPDATE Accionista SET aprobado='S' WHERE codUsuario =:codigo")
    void aprobarAccionista(@Param("codigo") String codUsuario);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Accionista SET aprobado='N', descripcionRechazo=:descripcion WHERE codUsuario =:codigo")
    void rechazarAccionista(@Param("codigo") String codUsuario, @Param("descripcion") String descripcionRechazo);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Accionista SET tipoAccionista=:tipoAccionista WHERE codUsuario =:codUsuario")
    void actualizarTipoAccionista(@Param("codUsuario") String codUsuario, @Param("tipoAccionista") Integer tipoAccionista);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Accionista SET codRepresentante=:codRepresentante WHERE codUsuario =:codUsuario")
    void actualizarRepresentante(@Param("codUsuario") String codUsuario, @Param("codRepresentante") String codRepresentante);

    @Query(value = "select \n" +
            "\tp1.COD_USUARIO codAccionista, concat(p1.nomPri, \" \", p1.nomSeg, \" \", p1.apePri, \" \", p1.apeSeg) nomAccionista, p2.COD_USUARIO codRepresentante, concat(p2.nomPri, \" \", p2.nomSeg, \" \", p2.apePri, \" \", p2.apeSeg) nomRepresentante, 'S' esAccionista\n" +
            "from persona p1 join accionista on p1.COD_USUARIO = accionista.COD_USUARIO join persona p2 on p2.COD_USUARIO = accionista.codRepresentante where p1.COD_USUARIO =:codUsuario", nativeQuery = true)
    public Object getAccionistaRepresentante(@Param("codUsuario") String codUsuario);

}
