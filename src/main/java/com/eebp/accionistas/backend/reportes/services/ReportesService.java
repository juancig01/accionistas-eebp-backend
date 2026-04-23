package com.eebp.accionistas.backend.reportes.services;

import com.eebp.accionistas.backend.reportes.entities.ReporteTitulosDTO;
import com.eebp.accionistas.backend.reportes.entities.ReportePersonasDTO;
import com.eebp.accionistas.backend.reportes.repositories.ReportesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportesService {

    @Autowired
    ReportesRepository reportesRepository;

    public List<ReporteTitulosDTO> getReporteTitulos() {
        return reportesRepository.getReporteTitulos();
    }

    public List<ReportePersonasDTO> getReportePersonas() {
        return reportesRepository.getReportePersonas();
    }
}