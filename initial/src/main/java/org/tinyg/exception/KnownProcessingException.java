package org.tinyg.exception;

public class KnownProcessingException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Could not recover from this exception.";
    }
}
