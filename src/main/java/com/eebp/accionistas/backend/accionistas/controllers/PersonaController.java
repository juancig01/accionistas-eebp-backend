package com.eebp.accionistas.backend.accionistas.controllers;

import com.eebp.accionistas.backend.accionistas.entities.Persona;
import com.eebp.accionistas.backend.accionistas.services.PersonaService;
import com.eebp.accionistas.backend.seguridad.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/accionistas")
public class PersonaController {

    @Autowired
    PersonaService personaService;

    @GetMapping
    public List<Persona> getPersonas() {
        return personaService.getPersonas();
    }

    @PostMapping
    public Persona addPersona(@RequestBody Persona persona) {
        return personaService.addPersona(persona);
    }

    @DeleteMapping("/borrar/{codUsuario}")
    public void deletePersonaById(@PathVariable String codUsuario) {
        personaService.deletePersonaById(codUsuario);
    }

    @GetMapping("/{codUsuario}")
    public Optional<Persona> getPersona(@PathVariable String codUsuario) throws UserNotFoundException {
        return personaService.getPersona(codUsuario);
    }

    @PostMapping("/declaracion")
    public Persona addDeclaracionPersona(@RequestBody Persona persona) {
        return personaService.addDeclaracionPersona(persona);
    }

    @PostMapping("/autorizacion")
    public Persona addAutorizacionPersona(@RequestBody Persona persona) {
        return personaService.addAutorizacionPersona(persona);
    }

    @GetMapping(value = "/pdfAutorizacion/{codUsuario}", produces = MediaType.APPLICATION_PDF_VALUE)
    public @ResponseBody byte[] getPDFAutorizacion(@PathVariable String codUsuario) throws IOException {
        return personaService.getPDFAutorizacion(codUsuario);
    }

    @GetMapping(value = "/pdfDeclaracion/{codUsuario}", produces = MediaType.APPLICATION_PDF_VALUE)
    public @ResponseBody byte[] getPDFDeclaracion(@PathVariable String codUsuario) throws IOException, UserNotFoundException {
        return personaService.getPDFDeclaracion(codUsuario);
    }

    @GetMapping(value = "/pdfDatosPersonales/{codUsuario}", produces = MediaType.APPLICATION_PDF_VALUE)
    public @ResponseBody byte[] getPDFDatosPersonales(@PathVariable String codUsuario) throws IOException {
        return personaService.getPDFDatosPersonales(codUsuario);
    }
}
