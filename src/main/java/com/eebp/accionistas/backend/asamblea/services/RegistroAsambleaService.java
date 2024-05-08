package com.eebp.accionistas.backend.asamblea.services;

import com.eebp.accionistas.backend.accionistas.entities.response.AccionistaRepresentanteResponse;
import com.eebp.accionistas.backend.accionistas.services.AccionistaService;
import com.eebp.accionistas.backend.asamblea.entities.AsistentesAsambleaDTO;
import com.eebp.accionistas.backend.asamblea.entities.RegistroAsamblea;
import com.eebp.accionistas.backend.asamblea.repositories.RegistroAsambleaRepository;
import com.eebp.accionistas.backend.financiero.repositories.UtilidadRepository;
import com.eebp.accionistas.backend.seguridad.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class RegistroAsambleaService {

    @Autowired
    RegistroAsambleaRepository registroAsambleaRepository;

    @Autowired
    UtilidadRepository utilidadRepository;

    @Autowired
    AsambleaService asambleaService;

    @Autowired
    AccionistaService accionistaService;

    public RegistroAsamblea addRegistroAsamblea(RegistroAsamblea registroAsamblea) {

        registroAsamblea.setAsistencia(false);
        Integer consecutivoAsamblea = asambleaService.getConsecutivoAsamblea();
        registroAsamblea.setConsecutivo(consecutivoAsamblea);

        return registroAsambleaRepository.save(registroAsamblea);
    }

    public RegistroAsamblea updateRegistroAsamblea(RegistroAsamblea registroAsamblea) {
        return registroAsambleaRepository.save(registroAsamblea);
    }

    public List<AsistentesAsambleaDTO> obtenerRegistroAsamblea() {
        return registroAsambleaRepository.obtenerRegistroAsamblea();
    }

    public Map<String, Integer> obtenerTotales(List<AsistentesAsambleaDTO> registros) throws UserNotFoundException {
        List<AsistentesAsambleaDTO> registrosConAsistenciaFalse = registros.stream()
                .filter(registro -> !registro.getAsistencia())
                .collect(Collectors.toList());

        int totalRegistros = registrosConAsistenciaFalse.size();
        int totalAcciones = 0;
        for (AsistentesAsambleaDTO registro : registrosConAsistenciaFalse) {
            totalAcciones += registro.getAcciones();
        }

        List<AccionistaRepresentanteResponse> listaAccionistas = accionistaService.getAccionistas();
        int totalAccionistas = listaAccionistas.size();
        double quorumDouble = totalAccionistas > 0 ? (double) totalRegistros / totalAccionistas * 100 : 0;
        int quorum = (int) Math.round(quorumDouble);

        Map<String, Integer> totales = new HashMap<>();
        totales.put("totalRegistros", totalRegistros);
        totales.put("totalAcciones", totalAcciones);
        totales.put("quorum", quorum);

        return totales;
    }
}
