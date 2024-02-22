package com.eebp.accionistas.backend.transacciones.controllers;

import com.eebp.accionistas.backend.seguridad.dao.FileUploadResponse;
import com.eebp.accionistas.backend.seguridad.entities.Asset;
import com.eebp.accionistas.backend.seguridad.utils.FileUploadUtil;
import com.eebp.accionistas.backend.transacciones.entities.Transaccion;
import com.eebp.accionistas.backend.transacciones.services.TransaccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/transaccion")
public class TransaccionController {

    @Autowired
    TransaccionService transaccionService;

    @GetMapping
    public List<Transaccion> getTransaccion() {
        return transaccionService.getTransaccion();
    }

    @GetMapping("/{id}")
    public Optional<Transaccion> findTransaccionById(@PathVariable Integer id) {
        return transaccionService.findTransaccionById(id);
    }

    @PutMapping
    public Transaccion addTransaccion(@RequestBody Transaccion transaccion) {
        return transaccionService.addTransaccion(transaccion);
    }

    @PostMapping("/uploadFile")
    public ResponseEntity<FileUploadResponse> uploadFile(@RequestParam("file") MultipartFile multipartFile)
            throws IOException {
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        long size = multipartFile.getSize();

        String fileCode = FileUploadUtil.saveFile(fileName, multipartFile);

        FileUploadResponse response = new FileUploadResponse();
        response.setFileName(fileName);
        response.setSize(size);
        response.setDownloadUri("/downloadFile/" + fileCode);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/aprobar/archivos/{conseTrans}")
    public List<Asset> getAprobacionTransaccionFiles(@PathVariable String conseTrans) {
        return FileUploadUtil.files(conseTrans, "transaccion").stream().map(file -> {
            file.setUrl("/assets/images/avatars/" + file.getFileName());
            return file;
        }).collect(Collectors.toList());
    }
}
