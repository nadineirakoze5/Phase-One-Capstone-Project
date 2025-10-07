package com.university.exceptions;

public class DatabaseException extends UniversityException {
    
    public DatabaseException(String message) {
        super("Database error: " + message);
    }
    
    public DatabaseException(String message, Throwable cause) {
        super("Database error: " + message, cause);
    }
}
