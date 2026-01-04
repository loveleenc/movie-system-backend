package com.bookit.application.moviecatalog.storage;

import com.azure.core.http.rest.Response;
import com.azure.core.util.Context;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobRequestConditions;
import com.azure.storage.blob.models.BlobStorageException;
import com.azure.storage.blob.models.BlockBlobItem;
import com.azure.storage.blob.options.BlobParallelUploadOptions;
import com.azure.storage.blob.sas.BlobSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;
import com.bookit.application.moviecatalog.storage.resource.PosterResource;
import com.bookit.application.moviecatalog.storage.resource.PosterUrlResource;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.time.OffsetDateTime;

@Service
@Profile("production")
public class PosterStorageService implements StorageService {

    private final BlobContainerClient blobContainerClient;

    public PosterStorageService(StorageProperties properties){
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .endpoint(properties.getUrl())
                .connectionString(properties.getConnectionString())
                .buildClient();
        this.blobContainerClient = blobServiceClient.getBlobContainerClient(properties.getContainerName());
    }

    @Override
    public void init() {

    }

    @Override
    public void updateResource(String filename, MultipartFile file) {
        //TODO: Later
    }

    @Override
    public void store(MultipartFile file, boolean overwrite) throws StorageException{
        try{
            if(file.isEmpty()){
                throw new StorageException("File is empty");
            }
            BlobClient blobClient = this.blobContainerClient.getBlobClient(file.getOriginalFilename());
            try(InputStream inputStream = file.getInputStream()){
                BlobRequestConditions blobRequestConditions = new BlobRequestConditions();
                if (!overwrite) {
                    blobRequestConditions.setIfNoneMatch("*");
                }
                Response<BlockBlobItem> uploadResponse = blobClient.uploadWithResponse((new BlobParallelUploadOptions(inputStream)).setRequestConditions(blobRequestConditions), null, Context.NONE);
                int code = uploadResponse.getStatusCode();
                if(code != 201){
                    throw new StorageException("Failed to store file.");
                }
            }
        }
        catch(IOException | BlobStorageException e){
            throw new StorageException("Failed to store file.", e);
        }
    }

    @Override
    public void delete(String filename) {
        BlobClient blobClient = this.blobContainerClient.getBlobClient(filename);
        blobClient.delete();
    }

    @Override
    public PosterResource getResource(String filename) throws StorageException{
        BlobClient blobClient = this.blobContainerClient.getBlobClient(filename);
        BlobSasPermission blobSasPermission = new BlobSasPermission().setReadPermission(true);
        OffsetDateTime expiryTime = OffsetDateTime.now().plusDays(2);
        BlobServiceSasSignatureValues values = new BlobServiceSasSignatureValues(expiryTime,
                blobSasPermission).setStartTime(OffsetDateTime.now());
        try{
            String blobSasToken = blobClient.generateSas(values);
            return new PosterUrlResource(UriComponentsBuilder.fromUriString(blobClient.getBlobUrl())
                                                            .query(blobSasToken).build().toUriString());
        }
        catch(MalformedURLException e){
            throw new StorageException("Unable to fetch resource", e);
        }
    }
}
