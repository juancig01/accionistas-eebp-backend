package com.eebp.accionistas.backend.seguridad.dao;

import lombok.Data;

@Data
public class FileUploadResponse {
    private String fileName;
    private String downloadUri;
    private long size;
}
