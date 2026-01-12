package com.bookit.catalog.movie.storage;

import com.bookit.catalog.movie.storage.resource.PosterByteArrayResource;
import com.bookit.catalog.movie.storage.resource.PosterResource;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@Profile("development")
public class LocalStorageService implements StorageService {
    private final Path storageDirectory = Paths.get("storage-dir");

    public LocalStorageService(){
        this.init();
    }

    @Override
    public void init() {
        if (!Files.isDirectory(this.storageDirectory)) {
            try {
                Files.createDirectory(this.storageDirectory);
            } catch (IOException e) {
                throw new StorageException("Could not initialize storage", e);
            }
        }
    }

    @Override
    public void store(MultipartFile file, boolean overwrite) throws UploadException {
        if (file.isEmpty()) {
            throw new UploadException("Unable tto save file");
        }

        Path destinationPath = this.storageDirectory.resolve(Paths.get(file.getOriginalFilename())).toAbsolutePath();
        if (!destinationPath.getParent().equals(this.storageDirectory.toAbsolutePath())) {
            throw new StorageException("Cannot store file outside storage directoy");
        }

        try (InputStream inputStream = file.getInputStream()) {
            if (overwrite) {
                Files.copy(inputStream, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            } else {
                Files.copy(inputStream, destinationPath);
            }

        } catch (IOException e) {
            throw new StorageException("Failed to store file", e);
        }
    }

    @Override
    public void delete(String filename) {
        try {
            Files.deleteIfExists(Paths.get(this.storageDirectory.toString(), filename));
        } catch (IOException e) {
            throw new StorageException("Unable to delete file", e);
        }
    }

    @Override
    public PosterResource getResource(String filename) {
        try {
            Path file = this.storageDirectory.resolve(filename);
            byte[] image = Files.readAllBytes(file);
            PosterResource resource = new PosterByteArrayResource(image);
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else{
                throw new StorageFileNotFoundException("Could not read the file");
            }
        } catch (IOException e) {
            throw new StorageFileNotFoundException("Could not read the file", e);
        }
    }

    @Override
    public void updateResource(String filename, MultipartFile file) {
        //TODO: later
    }
}

