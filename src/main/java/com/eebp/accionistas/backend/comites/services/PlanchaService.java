package com.eebp.accionistas.backend.comites.services;

import com.eebp.accionistas.backend.asamblea.services.AsambleaService;
import com.eebp.accionistas.backend.comites.entities.Plancha;
import com.eebp.accionistas.backend.comites.repositories.PlanchaRepository;
import com.eebp.accionistas.backend.seguridad.entities.Asset;
import com.eebp.accionistas.backend.seguridad.utils.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PlanchaService {

    @Autowired
    PlanchaRepository planchaRepository;

    @Autowired
    AsambleaService asambleaService;

    public Plancha addPlancha(Plancha plancha) {

        Integer consecutivoAsamblea = asambleaService.getConsecutivoAsamblea();
        plancha.setIdAsamblea(consecutivoAsamblea);

        String idPrincipal = plancha.getIdPrincipal();
        String idSuplente = plancha.getIdSuplente();

        System.out.println("Consecutivo Asamblea: " + consecutivoAsamblea);
        System.out.println("Id Principal: " + idPrincipal);
        System.out.println("Id Suplente: " + idSuplente);

        Optional<Plancha> existingPlanchaPrincipal = planchaRepository.findByIdAsambleaAndIdPrincipal(consecutivoAsamblea, idPrincipal);
        if (existingPlanchaPrincipal.isPresent()) {
            throw new IllegalArgumentException("La persona principal ya está registrada en una plancha para esta asamblea.");
        }

        if (idSuplente != null) {
            Optional<Plancha> existingPlanchaSuplente = planchaRepository.findByIdAsambleaAndIdSuplente(consecutivoAsamblea, idSuplente);
            if (existingPlanchaSuplente.isPresent()) {
                throw new IllegalArgumentException("La persona suplente ya está registrada en una plancha para esta asamblea.");
            }
        }

        return planchaRepository.save(plancha);
    }


    public List<Asset> getFilesUser(@PathVariable Integer codUsuario) {
        return FileUploadUtil.files(String.valueOf(codUsuario)).stream().map(file -> {
            file.setUrl("/assets/images/avatars/" + file.getFileName());
            return file;
        }).collect(Collectors.toList());
    }

    public Map<String, Object> obtenerDatos() {
        List<Object[]> resultados = planchaRepository.obtenerPlanchasUltimaAsamblea();

        Map<String, Object> respuesta = new HashMap<>();
        Map<Integer, List<Map<String, Object>>> juntaDirectiva = new HashMap<>();
        Map<Integer, List<Map<String, Object>>> presidente = new HashMap<>();
        Map<Integer, List<Map<String, Object>>> comiteEscrutador = new HashMap<>();
        Map<Integer, List<Map<String, Object>>> aprobadorActa = new HashMap<>();
        Map<Integer, List<Map<String, Object>>> revisorFiscal = new HashMap<>();

        // Obtener archivos para todos los códigos de usuario únicos
        Map<String, List<Asset>> archivosPorUsuario = new HashMap<>();
        for (Object[] fila : resultados) {
            String codUsuario = (String) fila[0];
            String codUsuarioSuplente = (String) fila[2];

            // Añadir el código de usuario principal
            archivosPorUsuario.put(codUsuario, getFilesUser(Integer.valueOf(codUsuario)));

            // Añadir el código de usuario suplente si existe
            if (codUsuarioSuplente != null) {
                archivosPorUsuario.put(codUsuarioSuplente, getFilesUser(Integer.valueOf(codUsuarioSuplente)));
            }
        }

        for (Object[] fila : resultados) {
            String codUsuario = (String) fila[0];
            String nombres = (String) fila[1];
            String codUsuarioSuplente = (String) fila[2];
            String nombresSuplente = (String) fila[3];
            int idPlancha = (int) fila[4];
            int idComite = (int) fila[5];

            Map<String, Object> postulante = new HashMap<>();
            postulante.put("codUsuario", codUsuario);
            postulante.put("nombres", nombres);
            postulante.put("files", archivosPorUsuario.get(codUsuario));
            postulante.put("idPlancha", idPlancha);
            postulante.put("idComite", idComite);

            Map<String, Object> suplente = new HashMap<>();
            suplente.put("codUsuario", codUsuarioSuplente);
            suplente.put("nombres", nombresSuplente);
            suplente.put("files", archivosPorUsuario.get(codUsuarioSuplente));
            suplente.put("idPlancha", idPlancha);
            suplente.put("idComite", idComite);

            List<Map<String, Object>> plancha = new ArrayList<>();
            plancha.add(postulante);
            if (nombresSuplente != null) {
                plancha.add(suplente);
            }

            switch (idComite) {
                case 1:
                    juntaDirectiva.put(idPlancha, plancha);
                    break;
                case 2:
                    presidente.put(idPlancha, plancha);
                    break;
                case 3:
                    comiteEscrutador.put(idPlancha, plancha);
                    break;
                case 4:
                    aprobadorActa.put(idPlancha, plancha);
                    break;
                case 5:
                    revisorFiscal.put(idPlancha, plancha);
                    break;
            }
        }

        respuesta.put("juntaDirectiva", new HashMap<String, Object>() {{
            put("postulantes", new ArrayList<>(juntaDirectiva.values()));
        }});

        respuesta.put("presidente", new HashMap<String, Object>() {{
            put("postulantes", new ArrayList<>(presidente.values()));
        }});

        respuesta.put("comiteEscrutador", new HashMap<String, Object>() {{
            put("postulantes", new ArrayList<>(comiteEscrutador.values()));
        }});

        respuesta.put("aprobadorActa", new HashMap<String, Object>() {{
            put("postulantes", new ArrayList<>(aprobadorActa.values()));
        }});

        respuesta.put("revisorFiscal", new HashMap<String, Object>() {{
            put("postulantes", new ArrayList<>(revisorFiscal.values()));
        }});

        return respuesta;
    }

}
