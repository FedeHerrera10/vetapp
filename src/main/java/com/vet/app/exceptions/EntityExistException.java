package com.vet.app.exceptions;

public class EntityExistException extends RuntimeException {

    public EntityExistException(String message) {
        super(message);
    }

}
