package com.eebp.accionistas.backend.seguridad.entities;

import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuario_sistema")
public class User implements UserDetails {
    @Id
    @Column(name = "COD_USUARIO")
    private String codUsuario;

    @Column(name = "PASSWORD")
    @JsonIgnore
    private String password;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "NOM_USUARIO")
    private String nombreUsuario;

    @Column(name = "APE_USUARIO")
    private String apellidoUsuario;

    @Transient
    private Integer perfil;

    @Transient String nomPerfil;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("USER"));
    }

    @Override
    public String getUsername() {
        return codUsuario;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

