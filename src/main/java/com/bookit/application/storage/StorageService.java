package com.bookit.application.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    void init();
    void store(MultipartFile file, boolean overwrite) throws UploadException;
    void delete(String filename);
    Resource getResource(String filename);
    void updateResource(String filename, MultipartFile file);
}
