package com.eebp.accionistas.backend.seguridad.services;

import com.eebp.accionistas.backend.accionistas.entities.Persona;
import com.eebp.accionistas.backend.accionistas.repositories.PersonaRepository;
import com.eebp.accionistas.backend.seguridad.entities.EmailDetails;
import com.eebp.accionistas.backend.seguridad.entities.User;
import com.eebp.accionistas.backend.seguridad.entities.UsuarioPerfil;
import com.eebp.accionistas.backend.seguridad.exceptions.NewUserException;
import com.eebp.accionistas.backend.seguridad.exceptions.UserExistsException;
import com.eebp.accionistas.backend.seguridad.exceptions.UserNotFoundException;
import com.eebp.accionistas.backend.seguridad.repositories.PerfilesRepository;
import com.eebp.accionistas.backend.seguridad.repositories.UserRepository;
import com.eebp.accionistas.backend.seguridad.repositories.UsuarioPerfilRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    private UsuarioPerfilRepository usuarioPerfilRepository;

    @Autowired
    private PerfilesRepository perfilesRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private EmailServiceImpl emailService;

    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) {
                return userRepository.findByCodUsuario(username)
                        .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
            }
        };
    }

    public User crearUsuario(User user, String tempPassword) throws NewUserException {
        try {
            if (!userRepository.findByCodUsuario(user.getCodUsuario()).isPresent()) {
                User response = userRepository.save(user);
                usuarioPerfilRepository.save(
                        UsuarioPerfil.builder()
                                .codUsuario(user.getCodUsuario())
                                .codPerfil(user.getPerfil())
                                .build()
                );
                log.info("Usuario " + user.getCodUsuario() + " creado exitosamente");
                emailService.sendSimpleMail(
                        EmailDetails.builder()
                                .recipient(user.getEmail())
                                .subject("Registro exitoso en el sistema de accionistas EEBP")
                                .msgBody("<table style='width: 600px; border-collapse: collapse; height: 147px;' border='0'>\n" +
                                        "<tbody>\n" +
                                        "<tr style='height: 91px;'>\n" +
                                        "<td style='width: 23.5796%; text-align: center; height: 91px;'><img src='https://eebpsa.com.co/wp-content/uploads/2020/08/lOGO-2.1.png' /></td>\n" +
                                        "<td style='width: 67.4766%; height: 91px;'>\n" +
                                        "<h3 style='text-align: center;'><strong>BIENVENIDO AL SISTEMA DE ACCIONISTAS </strong></h3>\n" +
                                        "<h3 style='text-align: center;'><strong>Empresa de Energ&iacute;a del Bajo Putumayo S.A. E.S.P.</strong></h3>\n" +
                                        "</td>\n" +
                                        "</tr>\n" +
                                        "<tr style='height: 10px;'>\n" +
                                        "<td style='text-align: center; height: 10px; width: 91.0562%;' colspan='2'>\n" +
                                        "<p>&nbsp;</p>\n" +
                                        "<p style='text-align: left;'>Sus credenciales para iniciar sesi&oacute;n son las siguientes:</p>\n" +
                                        "</td>\n" +
                                        "</tr>\n" +
                                        "<tr style='height: 46px;'>\n" +
                                        "<td style='width: 23.5796%; text-align: center; height: 46px;'>\n" +
                                        "<p style='text-align: left;'><strong>USUARIO</strong></p>\n" +
                                        "</td>\n" +
                                        "<td style='width: 67.4766%; text-align: center; height: 46px;'>\n" +
                                        "<p style='text-align: left;'>" + user.getCodUsuario() + "</p>\n" +
                                        "</td>\n" +
                                        "</tr>\n" +
                                        "<tr>\n" +
                                        "<td style='width: 23.5796%; text-align: center;'>\n" +
                                        "<p style='text-align: left;'><strong>CONTRASE&Ntilde;A</strong></p>\n" +
                                        "</td>\n" +
                                        "<td style='width: 67.4766%; text-align: center;'>\n" +
                                        "<p style='text-align: left;'>" + tempPassword + "</p>\n" +
                                        "</td>\n" +
                                        "</tr>\n" +
                                        "<tr>\n" +
                                        "<td style='width: 91.0562%; text-align: center;' colspan='2'>\n" +
                                        "<p style='text-align: left;'>&nbsp;</p>\n" +
                                        "<p style='text-align: left;'><span style='text-decoration: underline;'>Se recomienda cambiar su contrase&ntilde;a desde el panel de usuario en Sistema de Accionistas.</span></p>\n" +
                                        "<p style='text-align: left;'>&nbsp;</p>\n" +
                                        "</td>\n" +
                                        "</tr>\n" +
                                        "</tbody>\n" +
                                        "</table>\n" +
                                        "<p><strong>&nbsp;</strong></p>")
                                .build()
                );
                log.info("Email enviado con clave temporal al usuario " + user.getCodUsuario());
                return response;
            } else {
                log.info("El usuario con identificación " + user.getCodUsuario() + " ya existe en la base de datos.");
                throw new UserExistsException();
            }

        } catch (Exception e) {
            log.info("Error al crear el usuario " + user.getCodUsuario() + ". " + e.getMessage());
            throw new NewUserException();
        }

    }

    public Optional<User> obtenerUsuario(String codUsuario) throws UserNotFoundException {
        Optional<User> response = userRepository.findByCodUsuario(codUsuario);
        if (!response.isEmpty()) {
            response.get().setPerfil(usuarioPerfilRepository.getUsuarioPerfilByCodUsuario(response.get().getCodUsuario()).getCodPerfil());
            response.get().setNomPerfil(perfilesRepository.findById(usuarioPerfilRepository.getUsuarioPerfilByCodUsuario(response.get().getCodUsuario()).getCodPerfil()).get().getNomPerfil());
            return response;
        } else {
            log.info("Usuario no encontrado: " + codUsuario);
            throw new UserNotFoundException();
        }
    }

    public List<User> obtenerUsuarios() {
        return userRepository.findAll().stream().map(user -> {
            String codUsuario = user.getCodUsuario();
            Optional<Persona> personaOptional = personaRepository.findById(codUsuario);

            if (personaOptional.isPresent()) {
                Persona persona = personaOptional.get();
                if (persona.getRazonSocial() != null && "NIT".equals(persona.getTipDocumento())) {
                    user.setNombreUsuario(persona.getRazonSocial());
                    user.setApellidoUsuario(persona.getRazonSocial());
                } else {
                    // Si no es un NIT o la razón social es nula, usar otros valores para el nombre
                    user.setNombreUsuario(persona.getNomPri());
                }
            }

            user.setPerfil(usuarioPerfilRepository.getUsuarioPerfilByCodUsuario(codUsuario).getCodPerfil());
            user.setNomPerfil(perfilesRepository.findById(usuarioPerfilRepository.getUsuarioPerfilByCodUsuario(codUsuario).getCodPerfil()).get().getNomPerfil());
            return user;
        }).collect(Collectors.toList());
    }
}
