package com.bookit.catalog.movie.services.storage.cloud;

import com.bookit.catalog.movie.services.storage.PosterResource;
import org.springframework.core.io.UrlResource;

import java.net.MalformedURLException;

class PosterUrlResource extends UrlResource implements PosterResource {

    public PosterUrlResource(String path) throws MalformedURLException {
        super(path);
    }

    @Override
    public String getContentOrUrlAsString() {
        return this.getURL().toString();
    }
}
