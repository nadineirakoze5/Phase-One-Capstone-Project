package com.university.exceptions;

public class UniversityException extends Exception {
    
    public UniversityException(String message) {
        super(message);
    }
    
    public UniversityException(String message, Throwable cause) {
        super(message, cause);
    }
}
