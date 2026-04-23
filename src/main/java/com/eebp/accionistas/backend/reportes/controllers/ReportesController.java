package com.eebp.accionistas.backend.reportes.controllers;

import com.eebp.accionistas.backend.reportes.entities.ReporteTitulosDTO;
import com.eebp.accionistas.backend.reportes.entities.ReportePersonasDTO;
import com.eebp.accionistas.backend.reportes.services.ReportesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/reportes")
public class ReportesController {

    @Autowired
    ReportesService reportesService;

    @GetMapping("/titulos")
    public List<ReporteTitulosDTO> getReporteTitulos() {
        return reportesService.getReporteTitulos();
    }

    @GetMapping("/personas")
    public List<ReportePersonasDTO> getReportePersonas() {
        return reportesService.getReportePersonas();
    }
}