package com.bookit.application.services.storage.resource;

import com.bookit.application.services.storage.StorageException;
import org.springframework.core.io.ByteArrayResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class PosterByteArrayResource extends ByteArrayResource implements PosterResource {
    private static final String DATA_URI_SCHEME = "data:image/png;base64,";
    public PosterByteArrayResource(byte[] byteArray) {
        super(byteArray);
    }

    @Override
    public String getContentOrUrlAsString() {
        return DATA_URI_SCHEME + Base64.getEncoder().encodeToString(this.getByteArray());
    }
}
