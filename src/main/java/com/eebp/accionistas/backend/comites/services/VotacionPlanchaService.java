package com.eebp.accionistas.backend.comites.services;

import com.eebp.accionistas.backend.comites.entities.VotacionPlancha;
import com.eebp.accionistas.backend.comites.repositories.VotacionPlanchaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VotacionPlanchaService {

    @Autowired
    VotacionPlanchaRepository votacionPlanchaRepository;

    public VotacionPlancha addVotacion(VotacionPlancha votacionPlancha) {
        return votacionPlanchaRepository.save(votacionPlancha);
    }

    public List<Map<String, Object>> obtenerVotosPorComiteYPlancha() {
        List<Object[]> resultados = votacionPlanchaRepository.obtenerVotosPorComiteYPlancha();
        List<Map<String, Object>> resultadosFinales = new ArrayList<>();

        for (Object[] resultado : resultados) {
            Map<String, Object> votoMap = new HashMap<>();
            votoMap.put("idComite", resultado[0]);
            votoMap.put("descComite", resultado[1]);
            votoMap.put("idPlancha", resultado[2]);
            votoMap.put("totalVotos", resultado[3]);
            resultadosFinales.add(votoMap);
        }

        return resultadosFinales;
    }
}
