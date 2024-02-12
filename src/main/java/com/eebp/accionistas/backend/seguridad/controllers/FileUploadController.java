package com.eebp.accionistas.backend.seguridad.controllers;

import java.io.IOException;

import com.eebp.accionistas.backend.seguridad.dao.FileUploadResponse;
import com.eebp.accionistas.backend.seguridad.utils.FileUploadUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = "*")
public class FileUploadController {

    @PostMapping("/api/uploadFile")
    public ResponseEntity<FileUploadResponse> uploadFile(
            @RequestParam("file") MultipartFile multipartFile)
            throws IOException {

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        long size = multipartFile.getSize();

        String filecode = FileUploadUtil.saveFile(fileName, multipartFile);

        FileUploadResponse response = new FileUploadResponse();
        response.setFileName(fileName);
        response.setSize(size);
        response.setDownloadUri("/downloadFile/" + filecode);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
