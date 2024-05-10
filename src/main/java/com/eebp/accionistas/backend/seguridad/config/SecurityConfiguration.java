package com.eebp.accionistas.backend.seguridad.config;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import com.eebp.accionistas.backend.seguridad.services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserService userService;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request.requestMatchers(
                        "/api/auth/**",
                                "/error",
                                "/api/*",
                                "/api/usuarios/*",
                                "/api/seguridad/perfiles/*",
                                "/api/seguridad/perfiles/navigation/*",
                                "/api/seguridad/perfiles/*",
                                "/api/accionistas/*",
                                "/api/accionistas/*/*",
                                "/api/geo/*",
                                "/api/geo/municipios/*",
                                "/api/geo/departamentos/*/municipios",
                                "/api/accionistas/*",
                                "/api/accionista",
                                "/api/accionista/*",
                                "/api/accionista/pdfPendientesAprobar",
                                "/api/accionista/aprobar",
                                "/api/accionista/aprobar/archivos/*",
                                "/api/accionista/rechazar",
                                "/api/accionista/actualizarRepresentante",
                                "/api/accionista/actualizarTipoAccionista",
                                "/api/accionista/accionistaRepresentante/*",
                                "/api/accionista/ruta/*",
                                "/api/bancos",
                                "/api/actividadEconomica",
                                "/api/accionista/aprobar/archivos/eliminar/*",
                                "/api/titulos/*",
                                "/api/parametros",
                                "/api/parametros/*",
                                "/api/transaccion",
                                "/api/transaccion/*",
                                "/api/transaccion/aprobar/archivos/*",
                                "/api/titulos/formatoTituloAcciones/*",
                                "/api/utilidades/*",
                                "/api/accionistas/borrar/*",
                                "/api/asamblea/*",
                                "/api/asamblea/enviar-invitacion/*",
                                "/api/asamblea/registrar-asistente-asamblea",
                                "/api/asamblea/actualizar-estado/*",
                                "/api/encuesta/*")
                        .permitAll().anyRequest().authenticated())
                .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider()).addFilterBefore(
                        jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService.userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

}
