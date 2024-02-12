package com.eebp.accionistas.backend.seguridad.services;

import com.eebp.accionistas.backend.seguridad.dao.*;
import com.eebp.accionistas.backend.seguridad.entities.EmailDetails;
import com.eebp.accionistas.backend.seguridad.entities.FuseUser;
import com.eebp.accionistas.backend.seguridad.entities.User;
import com.eebp.accionistas.backend.seguridad.exceptions.AuthException;
import com.eebp.accionistas.backend.seguridad.repositories.PerfilesRepository;
import com.eebp.accionistas.backend.seguridad.repositories.UserRepository;
import com.eebp.accionistas.backend.seguridad.repositories.UsuarioPerfilRepository;
import com.eebp.accionistas.backend.seguridad.utils.PasswordGenerator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioPerfilRepository usuarioPerfilRepository;

    @Autowired
    private PerfilesRepository perfilesRepository;

    @Autowired
    private EmailServiceImpl emailService;

    @Override
    public JwtAuthenticationResponse signIn(SigninRequest request) throws AuthException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getCodUsuario(), request.getPassword()));
            log.info("Usuario " + request.getCodUsuario() + " inicio sesion correctamente.");
        } catch (Exception e) {
            log.info("Credenciales no validas para iniciar sesion. Usuario: " + request.getCodUsuario());
            throw new AuthException();
        }



        var user = userRepository.findByCodUsuario(request.getCodUsuario())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
        var jwt = jwtService.generateToken(user);
        FuseUser fuseUser = FuseUser.builder()
                .id(user.getCodUsuario())
                .profile(perfilesRepository.findById(usuarioPerfilRepository.getUsuarioPerfilByCodUsuario(request.getCodUsuario()).getCodPerfil()).get().getNomPerfil())
                .name(user.getNombreUsuario() + " " + user.getApellidoUsuario())
                .avatar("assets/images/avatars/" + user.getCodUsuario() + ".jpg")
                .email(user.getEmail())
                .status("Online").build();
        return JwtAuthenticationResponse.builder().accessToken(jwt).user(fuseUser).build();
    }

    @Override
    public JwtAuthenticationResponse signInWithToken(SigninWithTokenRequest request) {
        String codigoUsuario = jwtService.extractUserName(request.getAccessToken());
        var user = userRepository.findByCodUsuario(codigoUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Invalid operation"));
        var jwt = jwtService.generateToken(user);
        FuseUser fuseUser = FuseUser.builder()
                .id(user.getCodUsuario())
                .profile(perfilesRepository.findById(usuarioPerfilRepository.getUsuarioPerfilByCodUsuario(codigoUsuario).getCodPerfil()).get().getNomPerfil())
                .name(user.getNombreUsuario() + " " + user.getApellidoUsuario())
                .avatar("assets/images/avatars/" + user.getCodUsuario() + ".jpg")
                .email(user.getEmail())
                .status("Online").build();
        return JwtAuthenticationResponse.builder().accessToken(jwt).user(fuseUser).build();
    }

    @Override
    public void forgotPassword(ForgotPasswordRequest forgotPasswordRequest) {
        User user = userRepository.findByCodUsuario(forgotPasswordRequest.getCodUsuario())
                .orElseThrow(() -> new IllegalArgumentException("Invalid operation"));
        PasswordGenerator passwordGenerator = new PasswordGenerator();
        String newPassword = passwordGenerator.generatePassword();
        user.setPassword(passwordEncoder.encode(newPassword));
        emailService.sendSimpleMail(
                EmailDetails.builder()
                        .recipient(user.getEmail())
                        .subject("Recuperación de contraseña en el sistema de accionistas EEBP")
                        .msgBody("<table style='width: 600px; border-collapse: collapse; height: 147px;' border='0'>\n" +
                                "<tbody>\n" +
                                "<tr style='height: 91px;'>\n" +
                                "<td style='width: 23.5796%; text-align: center; height: 91px;'><img src='https://eebpsa.com.co/wp-content/uploads/2020/08/lOGO-2.1.png' /></td>\n" +
                                "<td style='width: 67.4766%; height: 91px;'>\n" +
                                "<h3 style='text-align: center;'><strong>RECUPERAR CONTRASEÑA - SISTEMA DE ACCIONISTAS </strong></h3>\n" +
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
                                "<p style='text-align: left;'><strong>NUEVA CONTRASE&Ntilde;A</strong></p>\n" +
                                "</td>\n" +
                                "<td style='width: 67.4766%; text-align: center;'>\n" +
                                "<p style='text-align: left;'>" + newPassword + "</p>\n" +
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
                        .build());
        userRepository.save(user);
    }

    @Override
    public void resetPassword(ResetPasswordRequest resetPasswordRequest) {
        User user = userRepository.findByCodUsuario(resetPasswordRequest.getCodUsuario())
                .orElseThrow(() -> new IllegalArgumentException("Invalid operation"));
        user.setPassword(passwordEncoder.encode(resetPasswordRequest.getPassword()));
        userRepository.save(user);
    }
}
