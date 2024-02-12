package com.eebp.accionistas.backend.seguridad.controllers;

import com.eebp.accionistas.backend.seguridad.entities.Perfil;
import com.eebp.accionistas.backend.seguridad.entities.navigation.DefaultNavigation;
import com.eebp.accionistas.backend.seguridad.entities.navigation.UsuariosOpciones;
import com.eebp.accionistas.backend.seguridad.repositories.UsuariosOpcionesRepository;
import com.eebp.accionistas.backend.seguridad.services.PerfilesServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seguridad/perfiles")
@CrossOrigin(origins = "*")
public class PerfilesController {

    @Autowired
    PerfilesServiceImpl perfilesService;

    @Autowired
    UsuariosOpcionesRepository usuariosOpcionesRepository;

    @GetMapping
    public List<Perfil> obtenerPerfiles() {
        return perfilesService.obtenerPerfiles();
    }

    @GetMapping("/navigation/{codUsuario}")
    public DefaultNavigation getModulosByUsuario(@PathVariable String codUsuario) {
        return DefaultNavigation.builder().modulos(perfilesService.getModulosByUsuario(codUsuario)).build();
    }

    @GetMapping("/navigation")
    public DefaultNavigation getModulos() {
        return DefaultNavigation.builder().modulos(perfilesService.getModulos()).build();
    }

    @PostMapping("/usuariosopciones")
    public UsuariosOpciones addUsuariosOpciones(@RequestBody UsuariosOpciones usuariosOpciones) {
        return usuariosOpcionesRepository.save(usuariosOpciones);
    }

    @DeleteMapping("/usuariosopciones")
    public void deleteUsuariosOpciones(@RequestBody UsuariosOpciones usuariosOpciones) {
        usuariosOpcionesRepository.delete(usuariosOpciones);
    }
}
