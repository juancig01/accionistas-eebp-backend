package com.eebp.accionistas.backend.acciones.controllers;

import com.eebp.accionistas.backend.acciones.entities.Titulo;
import com.eebp.accionistas.backend.acciones.services.TituloService;
import com.eebp.accionistas.backend.seguridad.entities.Asset;
import com.eebp.accionistas.backend.transacciones.entities.Transaccion;
import com.eebp.accionistas.backend.transacciones.entities.TransaccionDatos;
import com.eebp.accionistas.backend.transacciones.entities.TransaccionTitulo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/titulos")
public class TitulosController {

    @Autowired
    TituloService tituloService;

    @GetMapping("/{id}")
    public Optional<Titulo> findTituloById(@PathVariable Integer id) {
        return tituloService.findTituloById(id);
    }

    @GetMapping("/reportes")
    public Map<String, List<Asset>> getFilesTitulos() {
        return tituloService.getFilesTitulos();
    }

    @PostMapping
    public Titulo updateTitulo(@RequestBody Titulo titulo) {
        return tituloService.updateTitulo(titulo);
    }

    @PostMapping("/comprar")
    public Transaccion comprarTitulos(@RequestBody TransaccionDatos transaccionCompra) {
       return tituloService.comprarAcciones(transaccionCompra);
    }

    @PostMapping("/donar")
    public TransaccionDatos donarTitulos(@RequestBody TransaccionDatos transaccionDonacion) {
        return tituloService.donarAcciones(transaccionDonacion);
    }

    @PostMapping("/endosar")
    public TransaccionDatos endosarTitulos(@RequestBody TransaccionDatos transaccionEndoso) {
        return tituloService.endosarTitulos(transaccionEndoso);

    }

    @PostMapping("/sucesion")
    public TransaccionDatos sucesionTitulos(@RequestBody TransaccionDatos transaccionSucesion) {
        return tituloService.sucesionTitulos(transaccionSucesion);

    }

    @PostMapping("/embargar")
    public Transaccion embargarTitulos(@RequestBody Transaccion transaccionEmbargo) {
        return tituloService.embargarTitulo(transaccionEmbargo);

    }

    @GetMapping(value = "/formatoTituloAcciones/{conseTitulo}", produces = MediaType.APPLICATION_PDF_VALUE)
    public @ResponseBody byte[] getFormatoAcciones(@PathVariable Integer conseTitulo) throws IOException {
        return tituloService.getFormatoTitulo(conseTitulo);
    }

}
