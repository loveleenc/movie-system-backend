package com.bookit.application.storage;

import java.io.InputStream;

public interface BlobCrud {
    void createBlob(String blobName, InputStream fileBytes);
    String getBlobPath(String blobName);
    void deleteBlob(String blobName);
}
