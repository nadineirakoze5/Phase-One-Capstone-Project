package com.university.model;

import java.util.*;

public class Course {
    private String courseId;
    private String courseName;
    private String description;
    private int credits;
    private String department;
    private Instructor instructor;
    private Set<Student> enrolledStudents;
    private Map<Student, Double> studentGrades;
    private String schedule;
    private String location;
    
    public Course(String courseId, String courseName, String description, 
                  int credits, String department, String schedule, String location) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.description = description;
        this.credits = credits;
        this.department = department;
        this.schedule = schedule;
        this.location = location;
        this.enrolledStudents = new HashSet<>();
        this.studentGrades = new HashMap<>();
    }
    
    public String getCourseId() {
        return courseId;
    }
    
    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }
    
    public String getCourseName() {
        return courseName;
    }
    
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public int getCredits() {
        return credits;
    }
    
    public void setCredits(int credits) {
        this.credits = credits;
    }
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    public Instructor getInstructor() {
        return instructor;
    }
    
    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }
    
    public Set<Student> getEnrolledStudents() {
        return new HashSet<>(enrolledStudents);
    }
    
    public Map<Student, Double> getStudentGrades() {
        return new HashMap<>(studentGrades);
    }
    
    public String getSchedule() {
        return schedule;
    }
    
    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public void addStudent(Student student) {
        enrolledStudents.add(student);
    }
    
    public void removeStudent(Student student) {
        enrolledStudents.remove(student);
        studentGrades.remove(student);
    }
    
    public void addGrade(Student student, double grade) {
        if (enrolledStudents.contains(student)) {
            studentGrades.put(student, grade);
        }
    }
    
    public double getStudentGrade(Student student) {
        return studentGrades.getOrDefault(student, 0.0);
    }
    
    public int getEnrollmentCount() {
        return enrolledStudents.size();
    }
    
    public boolean isStudentEnrolled(Student student) {
        return enrolledStudents.contains(student);
    }
    
    public double getAverageGrade() {
        if (studentGrades.isEmpty()) {
            return 0.0;
        }
        
        return studentGrades.values().stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
    }
    
    @Override
    public String toString() {
        return String.format("Course{id='%s', name='%s', credits=%d, department='%s', instructor='%s', enrollment=%d}", 
                courseId, courseName, credits, department, 
                instructor != null ? instructor.getFullName() : "TBA", enrolledStudents.size());
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Course course = (Course) obj;
        return courseId != null ? courseId.equals(course.courseId) : course.courseId == null;
    }
    
    @Override
    public int hashCode() {
        return courseId != null ? courseId.hashCode() : 0;
    }
}
