package org.tinyg.exception;

public class UnexpectedProcessingException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Could not recover from this exception.";
    }
}
