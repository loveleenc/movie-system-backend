package com.bookit.application.storage;


import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.sas.BlobSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;
import org.springframework.core.env.Environment;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.InputStream;
import java.time.OffsetDateTime;
import java.util.Objects;

public class MoviePosterBlob implements BlobCrud {
    private final BlobContainerClient blobContainerClient;

    public MoviePosterBlob(Environment env) {
        String url = Objects.requireNonNull(env.getProperty("spring.azureblobsource.url"));
        String containerName = Objects.requireNonNull(env.getProperty("spring.azureblobsource.containerName"));
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .endpoint(url)
                .connectionString(Objects.requireNonNull(env.getProperty("spring.azureblobsource.connectionString")))
                .buildClient();
        this.blobContainerClient = blobServiceClient.getBlobContainerClient(containerName);
    }

    public void createBlob(String fileName, InputStream fileBytes) {
        BlobClient blobClient = this.blobContainerClient.getBlobClient(fileName);
        blobClient.upload(fileBytes, false);
    }

    public void deleteBlob(String blobName) {
        BlobClient blobClient = this.blobContainerClient.getBlobClient(blobName);
        blobClient.delete();
    }

    public String getBlobPath(String blobName) {
        BlobClient blobClient = blobContainerClient.getBlobClient(blobName);
        BlobSasPermission blobSasPermission = new BlobSasPermission().setReadPermission(true);
        OffsetDateTime expiryTime = OffsetDateTime.now().plusDays(2);
        BlobServiceSasSignatureValues values = new BlobServiceSasSignatureValues(expiryTime,
                blobSasPermission).setStartTime(OffsetDateTime.now());

        String blobSasToken = blobClient.generateSas(values);
        return UriComponentsBuilder.fromUriString(blobClient.getBlobUrl())
                .query(blobSasToken)
                .build().toUriString();
    }
}
