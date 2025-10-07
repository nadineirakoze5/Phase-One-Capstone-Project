package com.university.exceptions;

public class StudentNotFoundException extends UniversityException {
    
    public StudentNotFoundException(String studentId) {
        super("Student with ID '" + studentId + "' not found.");
    }
    
    public StudentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
