package com.eebp.accionistas.backend.comites.services;

import com.eebp.accionistas.backend.comites.entities.VotacionPlancha;
import com.eebp.accionistas.backend.comites.repositories.VotacionPlanchaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class VotacionPlanchaService {

    @Autowired
    VotacionPlanchaRepository votacionPlanchaRepository;

    public VotacionPlancha addVotacion(VotacionPlancha votacionPlancha) {
        return votacionPlanchaRepository.save(votacionPlancha);
    }

    public Map<String, List<Map<String, Object>>> obtenerVotosPorComiteYPlancha() {
        List<Object[]> resultados = votacionPlanchaRepository.obtenerVotosPorComiteYPlancha();
        Map<String, List<Map<String, Object>>> resultadosFinales = new LinkedHashMap<>();

        for (Object[] resultado : resultados) {
            String descComite = (String) resultado[1];

            Map<String, Object> votoMap = new LinkedHashMap<>();
            votoMap.put("idPlancha", resultado[2]);
            votoMap.put("totalVotos", resultado[3]);
            votoMap.put("idComite", resultado[0]);
            votoMap.put("descComite", descComite);
            votoMap.put("nombrePrincipal", resultado[5]);
            votoMap.put("idPrincipal", resultado[4]);
            votoMap.put("idSuplente", resultado[6]);
            votoMap.put("nombreSuplente", resultado[7]);

            // Si el comité ya tiene una entrada en el mapa, agregar a la lista existente
            if (resultadosFinales.containsKey(descComite)) {
                resultadosFinales.get(descComite).add(votoMap);
            } else {
                // Si no existe, crear una nueva lista y agregar el mapa
                List<Map<String, Object>> listaVotos = new ArrayList<>();
                listaVotos.add(votoMap);
                resultadosFinales.put(descComite, listaVotos);
            }
        }

        return resultadosFinales;
    }

    public Map<String, Boolean> obtenerVotosPorComiteYPersona(Integer idPersona) {
        Map<String, Boolean> resultadosFinales = new LinkedHashMap<>();

        // Ejecutar la consulta SQL y obtener los resultados
        List<Object[]> resultados = votacionPlanchaRepository.obtenerVotosPorComiteYPersona(idPersona);

        // Procesar los resultados y formatearlos según el formato deseado
        for (Object[] resultado : resultados) {
            String descComite = (String) resultado[0];
            Integer votoInteger = (Integer) resultado[1];
            Boolean voto = votoInteger == 1; // Convertir 1 a true, 0 a false
            resultadosFinales.put(descComite, voto);
        }

        // Devolver el resultado formateado
        return resultadosFinales;
    }

}
