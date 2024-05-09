package com.eebp.accionistas.backend.asamblea.services;

import com.eebp.accionistas.backend.asamblea.entities.Asamblea;
import com.eebp.accionistas.backend.asamblea.entities.Poder;
import com.eebp.accionistas.backend.asamblea.entities.PoderesDTO;
import com.eebp.accionistas.backend.asamblea.repositories.PoderRepository;
import com.eebp.accionistas.backend.seguridad.entities.Asset;
import com.eebp.accionistas.backend.seguridad.utils.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PoderService {

    @Autowired
    PoderRepository poderRepository;

    @Autowired
    AsambleaService asambleaService;

    public Poder addPoder(Poder poder) {
        poder.setEstado("TRAMITE");
        Integer consecutivoAsamblea = asambleaService.getConsecutivoAsamblea();
        poder.setConsecutivo(consecutivoAsamblea);
        if (consecutivoAsamblea != null) {
            Optional<Asamblea> asambleaOptional = asambleaService.findAsambleaById(consecutivoAsamblea);
            if (asambleaOptional.isPresent()) {
                Asamblea asamblea = asambleaOptional.get();
                poder.setFechaAsamblea(asamblea.getFechaAsamblea().toString());
            }
        }
        return poderRepository.save(poder);
    }

    public List<Asset> getFilesPoder(@PathVariable Integer consecutivoPoder) {
        return FileUploadUtil.files(String.valueOf(consecutivoPoder), "formatoRegistroPoder").stream().map(file -> {
            file.setUrl("/assets/images/avatars/" + file.getFileName());
            return file;
        }).collect(Collectors.toList());
    }

    public List<PoderesDTO> obtenerPoderesConArchivos() {
        List<PoderesDTO> poderes = poderRepository.obtenerDatosPoderes();
        return poderes.stream()
                .map(poder -> {
                    List<Asset> files = getFilesPoder(poder.getConsecutivo());
                    return new PoderDTOImpl(poder, files);
                })
                .collect(Collectors.toList());
    }

    private static class PoderDTOImpl implements PoderesDTO {
        private final Integer consecutivo;
        private final String idPoderdante;
        private final String nombrePoderdante;
        private final Integer accionesPoderdante;
        private final String nombreApoderado;
        private final String idApoderado;
        private final String estado;
        private final List<Asset> files;

        public PoderDTOImpl(PoderesDTO poder, List<Asset> files) {
            this.consecutivo = poder.getConsecutivo();
            this.idPoderdante = poder.getIdPoderdante();
            this.nombrePoderdante = poder.getNombrePoderdante();
            this.accionesPoderdante = poder.getAccionesPoderdante();
            this.nombreApoderado = poder.getNombreApoderado();
            this.idApoderado = poder.getIdApoderado();
            this.estado = poder.getEstado();
            this.files = new ArrayList<>(files);
        }

        @Override
        public Integer getConsecutivo() {
            return consecutivo;
        }

        @Override
        public String getIdPoderdante() {
            return idPoderdante;
        }

        @Override
        public String getNombrePoderdante() {
            return nombrePoderdante;
        }

        @Override
        public Integer getAccionesPoderdante() {
            return accionesPoderdante;
        }

        @Override
        public String getNombreApoderado() {
            return nombreApoderado;
        }

        @Override
        public String getIdApoderado() {
            return idApoderado;
        }

        @Override
        public String getEstado() {
            return estado;
        }

        @Override
        public List<Asset> getFiles() {
            return files;
        }
    }

    public Poder actualizarEstadoPoder(Integer id, String nuevoEstado) {
        Optional<Poder> poderOptional = poderRepository.findById(id);
        if (poderOptional.isPresent()) {
            Poder poder = poderOptional.get();
            poder.setEstado(nuevoEstado);
            return poderRepository.save(poder);
        }
        return null;
    }

}
