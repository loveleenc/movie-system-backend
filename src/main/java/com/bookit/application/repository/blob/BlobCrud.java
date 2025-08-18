package com.bookit.application.repository.blob;

import java.io.FileInputStream;
import java.io.InputStream;

public interface BlobCrud {
    String createBlob(String blobName, InputStream fileBytes);
    String getBlobPath(String blobName);
    void deleteBlob(String blobName);
}
