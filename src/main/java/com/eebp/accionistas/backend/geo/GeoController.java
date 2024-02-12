package com.eebp.accionistas.backend.geo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/geo")
public class GeoController {

    @Autowired
    GeoService geoService;

    @GetMapping("/departamentos")
    public List<Departamento> getDepartamentos() {
        return geoService.getDepartamentos();
    }

    @GetMapping("/municipios")
    public List<Municipio> getMunicipios() {
        return geoService.getMunicipios();
    }

    @GetMapping("/departamentos/{codigoDepartamento}/municipios")
    public List<Municipio> getMunicipiosByDepartamento(@PathVariable Integer codigoDepartamento) {
        return geoService.getMunicipiosByDepartamento(codigoDepartamento);
    }

    @GetMapping("municipios/{codigoMunicipio}")
    public Optional<Municipio> getMunicipioById(@PathVariable Integer codigoMunicipio) {
        return geoService.getMunicipioById(codigoMunicipio);
    }
}
