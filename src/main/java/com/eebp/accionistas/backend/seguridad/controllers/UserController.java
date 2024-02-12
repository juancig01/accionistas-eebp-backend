package com.eebp.accionistas.backend.seguridad.controllers;

import com.eebp.accionistas.backend.accionistas.entities.Persona;
import com.eebp.accionistas.backend.accionistas.services.PersonaService;
import com.eebp.accionistas.backend.seguridad.entities.User;
import com.eebp.accionistas.backend.seguridad.exceptions.NewUserException;
import com.eebp.accionistas.backend.seguridad.exceptions.UserNotFoundException;
import com.eebp.accionistas.backend.seguridad.services.UserServiceImpl;
import com.eebp.accionistas.backend.seguridad.utils.PasswordGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    UserServiceImpl userService;

    @Autowired
    PersonaService personaService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    PasswordGenerator passwordGenerator;

    @PostMapping
    public ResponseEntity<User> crearUsuario(@RequestBody User usuario) throws NewUserException {
        String tempPassword = passwordGenerator.generatePassword();
        usuario.setPassword(passwordEncoder.encode(tempPassword));
        return ResponseEntity.ok(userService.crearUsuario(usuario, tempPassword));
    }

    @PostMapping("/administrativo")
    public ResponseEntity<User> crearUsuarioDesdePersona(@RequestBody User usuario) throws NewUserException, UserNotFoundException {
        String tempPassword = passwordGenerator.generatePassword();
        usuario.setPassword(passwordEncoder.encode(tempPassword));
        Persona persona = personaService.getPersona(usuario.getCodUsuario()).get();
        usuario.setNombreUsuario(persona.getNomPri() + " " + persona.getNomSeg());
        usuario.setApellidoUsuario(persona.getApePri() + " " + persona.getApeSeg());
        usuario.setEmail(persona.getCorreoPersona());
        return ResponseEntity.ok(userService.crearUsuario(usuario, tempPassword));
    }

    @GetMapping("/{codUsuario}")
    public ResponseEntity<Optional<User>> obtenerUsuario(@PathVariable String codUsuario) throws UserNotFoundException {
        return ResponseEntity.ok(userService.obtenerUsuario(codUsuario));
    }

    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(userService.obtenerUsuarios());
    }

}
