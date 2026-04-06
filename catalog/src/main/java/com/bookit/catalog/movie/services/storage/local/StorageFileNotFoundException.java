package com.bookit.catalog.movie.services.storage.local;

import com.bookit.catalog.movie.services.storage.StorageException;

class StorageFileNotFoundException extends StorageException {
    public StorageFileNotFoundException(String message){
        super(message);
    }

    public StorageFileNotFoundException(String message, Throwable cause){
        super(message, cause);
    }

}
