package com.bookit.application.repository.blob;


import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.sas.BlobSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.InputStream;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Objects;

@Component
public class MoviePosterBlob implements BlobCrud {
    private final BlobContainerClient blobContainerClient;
    private final String url;
    private final String containerName;

    public MoviePosterBlob(Environment env) {
        this.url = Objects.requireNonNull(env.getProperty("spring.azureblobsource.url"));
        this.containerName = Objects.requireNonNull(env.getProperty("spring.azureblobsource.containerName"));
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .endpoint(this.url)
                .connectionString(Objects.requireNonNull(env.getProperty("spring.azureblobsource.connectionString")))
                .buildClient();
        this.blobContainerClient = blobServiceClient.getBlobContainerClient(this.containerName);

    }

    public String createBlob(String fileName, InputStream fileBytes) {
        BlobClient blobClient = this.blobContainerClient.getBlobClient(fileName);
        blobClient.upload(fileBytes);
        return fileName;
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
        return UriComponentsBuilder.fromUriString(this.url)
                .pathSegment(this.containerName, blobName)
                .query(blobSasToken)
                .build().toUriString();
    }
}
