package com.bookit.catalog.movie.services.storage.resource;

import org.springframework.core.io.Resource;

public interface PosterResource extends Resource {
    public String getContentOrUrlAsString();
}
