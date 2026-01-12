package com.bookit.catalog.movie.storage.resource;

import org.springframework.core.io.Resource;

public interface PosterResource extends Resource {
    public String getContentOrUrlAsString();
}
