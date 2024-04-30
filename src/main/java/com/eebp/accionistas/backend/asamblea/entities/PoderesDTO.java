package com.eebp.accionistas.backend.asamblea.entities;

import com.eebp.accionistas.backend.seguridad.entities.Asset;
import jakarta.persistence.Transient;

import java.util.List;

public interface PoderesDTO {
    Integer getConsecutivo();
    String getIdPoderdante();
    String getNombrePoderdante();
    Integer getAccionesPoderdante();
    String getNombreApoderado();
    String getIdApoderado();
    String getEstado();
    List<Asset> getFiles();

}
