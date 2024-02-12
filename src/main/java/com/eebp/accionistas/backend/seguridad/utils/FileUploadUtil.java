package com.eebp.accionistas.backend.seguridad.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.eebp.accionistas.backend.seguridad.entities.Asset;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.multipart.MultipartFile;

public class FileUploadUtil {
    public static String saveFile(String fileName, MultipartFile multipartFile)
            throws IOException {
        Path uploadPath = Paths.get("../frontend/src/assets/images/avatars");

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileCode = RandomStringUtils.randomAlphanumeric(8);

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save file: " + fileName, ioe);
        }

        return fileCode;
    }

    public static List<Asset> files(String codUsuario, String typeFile) {
        String directoryPath = "../frontend/src/assets/images/avatars";
        File directory = new File(directoryPath);
        List<Asset> fileNames = new ArrayList<>();
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        fileNames.add(Asset.builder().fileName(file.getName()).build());
                    }
                }
            }
        } else {
            System.err.println("The specified directory does not exist or is not a directory.");
        }
        return fileNames.stream().filter(file -> file.getFileName().contains(typeFile + "_" + codUsuario)).collect(Collectors.toList());
    }

    public static boolean deleteFile(String fileName) {
        String directoryPath = "../frontend/src/assets/images/avatars";
        File directory = new File(directoryPath);
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && file.getName().equalsIgnoreCase(fileName)) {
                        return file.delete();
                    }
                }
            } else {
                return false;
            }
        } else {
            System.err.println("The specified directory does not exist or is not a directory.");
            return false;
        }
        return false;
    }
}