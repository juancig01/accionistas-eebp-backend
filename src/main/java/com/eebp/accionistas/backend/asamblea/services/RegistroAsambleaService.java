package com.eebp.accionistas.backend.asamblea.services;

import com.eebp.accionistas.backend.accionistas.entities.response.AccionistaRepresentanteResponse;
import com.eebp.accionistas.backend.accionistas.services.AccionistaService;
import com.eebp.accionistas.backend.asamblea.entities.AsistentesAsambleaDTO;
import com.eebp.accionistas.backend.asamblea.entities.Poder;
import com.eebp.accionistas.backend.asamblea.entities.RegistroAsamblea;
import com.eebp.accionistas.backend.asamblea.repositories.PoderRepository;
import com.eebp.accionistas.backend.asamblea.repositories.RegistroAsambleaRepository;
import com.eebp.accionistas.backend.financiero.repositories.UtilidadRepository;
import com.eebp.accionistas.backend.seguridad.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class RegistroAsambleaService {

    @Autowired
    RegistroAsambleaRepository registroAsambleaRepository;

    @Autowired
    UtilidadRepository utilidadRepository;

    @Autowired
    PoderRepository poderRepository;

    @Autowired
    AsambleaService asambleaService;

    @Autowired
    AccionistaService accionistaService;

    public RegistroAsamblea addegistroAsamblea(RegistroAsamblea registroAsamblea) {

        Integer consecutivoAsamblea = asambleaService.getConsecutivoAsamblea();
        String idePer = registroAsamblea.getIdePer();

        // 1. Validar si ya existe registrado en la asamblea
        Optional<RegistroAsamblea> existingRegistro =
                registroAsambleaRepository.findByConsecutivoAndIdePer(consecutivoAsamblea, idePer);

        if (existingRegistro.isPresent()) {
            throw new IllegalArgumentException("El registro de asamblea ya existe para este miembro con el consecutivo dado.");
        }

        // 2. Validar si la persona es poderdante
        Optional<Poder> poderComoPoderdante =
                poderRepository.findByConsecutivoAndIdPoderdanteAndEstado(consecutivoAsamblea, idePer);

        if (poderComoPoderdante.isPresent()) {
            throw new IllegalArgumentException("El miembro no puede asistir porque otorgó poder para esta asamblea.");
        }

        // 3. Registrar asistencia
        registroAsamblea.setAsistencia(true);
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
        List<AsistentesAsambleaDTO> registrosConAsistenciaTrue = registros.stream()
                // Se asume que se refiere a 'true' por el uso posterior de totalAcciones
                .filter(registro -> registro.getAsistencia())
                .collect(Collectors.toList());

        int totalRegistros = registrosConAsistenciaTrue.size();
        long totalAcciones = 0; // Usar long si las acciones pueden superar Integer.MAX_VALUE
        for (AsistentesAsambleaDTO registro : registrosConAsistenciaTrue) {
            totalAcciones += registro.getAcciones();
        }

        Integer consecutivoAsamblea = asambleaService.getConsecutivoAsamblea();
        // Se asume que getTotalAcciones devuelve Integer o Long
        Integer totalAccionesGeneral = registroAsambleaRepository.getTotalAcciones();

        // El cálculo del total de accionistas es irrelevante para el Quórum, se mantiene para contexto.
        List<AccionistaRepresentanteResponse> listaAccionistas = accionistaService.getAccionistas();
        int totalAccionistas = listaAccionistas.size();

        // A. CALCULAR EL QUORUM CON 2 DECIMALES
        double quorumCalculado = totalAccionesGeneral > 0 ?
                (double) totalAcciones / totalAccionesGeneral * 100 : 0;

        // Para cumplir con Map<String, Integer>, NO PODEMOS DEVOLVER DECIMALES.
        // Opción 1 (Compromiso): Devolver el valor redondeado a entero como estaba para la lógica.
        int quorumEntero = (int) Math.round(quorumCalculado);

        // Opción 2 (Precisión forzada): Devolver el valor multiplicado por 10000 (para representar 2 decimales * 100)
        // No se recomienda, causa confusión.

        // B. COMPROBAR QUORUM
        boolean quorumSuperado = quorumEntero >= 50 + 1; // Se sigue usando el entero para la lógica

        Map<String, Integer> totales = new HashMap<>();
        totales.put("totalRegistros", totalRegistros);
        totales.put("totalAccionesAsamblea", (int) totalAcciones); // Cast a int si es necesario
        totales.put("quorum", quorumEntero); // Valor Entero
        totales.put("consecutivoAsamblea", consecutivoAsamblea);
        totales.put("totalAcciones", totalAccionesGeneral);
        totales.put("quorumSuperado", quorumSuperado ? 1 : 0);

        return totales;
    }
}
