package com.university.exceptions;

public class CourseNotFoundException extends UniversityException {
    
    public CourseNotFoundException(String courseId) {
        super("Course with ID '" + courseId + "' not found.");
    }
    
    public CourseNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
