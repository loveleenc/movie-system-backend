package com.bookit.application.moviecatalog.storage.resource;

import org.springframework.core.io.UrlResource;

import java.net.MalformedURLException;

public class PosterUrlResource extends UrlResource implements PosterResource {

    public PosterUrlResource(String path) throws MalformedURLException {
        super(path);
    }

    @Override
    public String getContentOrUrlAsString() {
        return this.getURL().toString();
    }
}
