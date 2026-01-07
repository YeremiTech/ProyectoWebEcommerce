package com.ecommerce.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.*;
import java.time.Instant;
import java.util.Set;

@Component
public class FileStorage {

    private static final Set<String> ALLOWED = Set.of("image/png", "image/jpeg", "image/jpg", "image/webp");

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    public String saveUserPhoto(MultipartFile file) throws IOException {
        return save(file, "Foto");
    }

    public String saveProductoPhoto(MultipartFile file) throws IOException {
        return save(file, "Producto");
    }

    private String save(MultipartFile file, String subfolder) throws IOException {
        if (file == null || file.isEmpty()) return null;
        if (!ALLOWED.contains(file.getContentType()))
            throw new IOException("Tipo no permitido: " + file.getContentType());

        Path base = Paths.get(uploadDir, subfolder);
        Files.createDirectories(base);

        String orig = file.getOriginalFilename() == null ? "img" : file.getOriginalFilename();
        String clean = orig.replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
        String filename = Instant.now().toEpochMilli() + "_" + clean;

        Path target = base.resolve(filename);
        try (InputStream in = file.getInputStream()) {
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        }

        return "uploads/" + subfolder + "/" + filename;
    }
}
