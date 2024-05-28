package com.eebp.accionistas.backend.comites.services;

import com.eebp.accionistas.backend.comites.entities.VotacionPlancha;
import com.eebp.accionistas.backend.comites.entities.VotoDTO;
import com.eebp.accionistas.backend.comites.repositories.VotacionPlanchaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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
            String descComiteCamelCase = toCamelCase(descComite);

            Map<String, Object> votoMap = new LinkedHashMap<>();
            votoMap.put("idPlancha", resultado[2]);
            votoMap.put("totalVotos", resultado[3]);
            votoMap.put("idComite", resultado[0]);
            votoMap.put("descComite", descComite);
            votoMap.put("nombrePrincipal", resultado[5]);
            votoMap.put("idPrincipal", resultado[4]);
            votoMap.put("idSuplente", resultado[6]);
            votoMap.put("nombreSuplente", resultado[7]);

            if (resultadosFinales.containsKey(descComiteCamelCase)) {
                resultadosFinales.get(descComiteCamelCase).add(votoMap);
            } else {

                List<Map<String, Object>> listaVotos = new ArrayList<>();
                listaVotos.add(votoMap);
                resultadosFinales.put(descComiteCamelCase, listaVotos);
            }
        }

        return resultadosFinales;
    }

    private String toCamelCase(String input) {
        String[] parts = input.toLowerCase().split(" ");
        String camelCaseString = parts[0];
        for (int i = 1; i < parts.length; i++) {
            camelCaseString += capitalize(parts[i]);
        }
        return camelCaseString;
    }

    private String capitalize(String input) {
        if (input == null || input.length() == 0) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    public List<VotoDTO> obtenerVotosPorComiteYPersona(Integer idPersona) {
        List<Object[]> resultados = votacionPlanchaRepository.obtenerVotosPorComiteYPersona(idPersona);

        // Mapeo de resultados a instancias de VotoDTO
        List<VotoDTO> votosDTOList = resultados.stream()
                .map(resultado -> new VotoDTO(
                        toCamelCase((String) resultado[0]),
                        (String) resultado[1]
                ))
                .collect(Collectors.toList());

        return votosDTOList;
    }
}
