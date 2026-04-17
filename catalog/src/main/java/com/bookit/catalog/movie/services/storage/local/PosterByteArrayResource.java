package com.bookit.catalog.movie.services.storage.local;

import com.bookit.catalog.movie.services.storage.PosterResource;
import org.springframework.core.io.ByteArrayResource;

import java.util.Base64;

class PosterByteArrayResource extends ByteArrayResource implements PosterResource {
    private static final String DATA_URI_SCHEME = "data:image/png;base64,";
    public PosterByteArrayResource(byte[] byteArray) {
        super(byteArray);
    }

    @Override
    public String getContentOrUrlAsString() {
        return DATA_URI_SCHEME + Base64.getEncoder().encodeToString(this.getByteArray());
    }
}
