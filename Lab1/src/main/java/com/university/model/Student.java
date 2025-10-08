package com.university.model;

import java.util.*;

public class Student extends Person {
    private String studentId;
    private String major;
    private int yearLevel;
    private Double gpa;
    private List<Course> enrolledCourses;
    private Map<Course, Double> courseGrades;
    
    public Student(String id, String firstName, String lastName, String email, 
                   String phoneNumber, String studentId, String major, int yearLevel) {
        super(id, firstName, lastName, email, phoneNumber);
        this.studentId = studentId;
        this.major = major;
        this.yearLevel = yearLevel;
        this.gpa = 0.0;
        this.enrolledCourses = new ArrayList<>();
        this.courseGrades = new HashMap<>();
    }
    
    public String getStudentId() {
        return studentId;
    }
    
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
    
    public String getMajor() {
        return major;
    }
    
    public void setMajor(String major) {
        this.major = major;
    }
    
    public int getYearLevel() {
        return yearLevel;
    }
    
    public void setYearLevel(int yearLevel) {
        this.yearLevel = yearLevel;
    }
    
    public Double getGpa() {
        return gpa;
    }
    
    public void setGpa(Double gpa) {
        this.gpa = gpa;
    }
    
    public List<Course> getEnrolledCourses() {
        return new ArrayList<>(enrolledCourses);
    }
    
    public Map<Course, Double> getCourseGrades() {
        return new HashMap<>(courseGrades);
    }
    
    public void enrollInCourse(Course course) {
        if (!enrolledCourses.contains(course)) {
            enrolledCourses.add(course);
            course.addStudent(this);
        }
    }
    
    public void dropCourse(Course course) {
        if (enrolledCourses.contains(course)) {
            enrolledCourses.remove(course);
            course.removeStudent(this);
            courseGrades.remove(course);
        }
    }
    
    public void addGrade(Course course, double grade) {
        if (enrolledCourses.contains(course)) {
            courseGrades.put(course, grade);
            calculateGPA();
        }
    }
    
    public void calculateGPA() {
        if (courseGrades.isEmpty()) {
            this.gpa = 0.0;
            return;
        }
        
        double totalPoints = 0.0;
        int totalCredits = 0;
        
        for (Map.Entry<Course, Double> entry : courseGrades.entrySet()) {
            Course course = entry.getKey();
            Double grade = entry.getValue();
            totalPoints += grade * course.getCredits();
            totalCredits += course.getCredits();
        }
        
        this.gpa = totalCredits > 0 ? totalPoints / totalCredits : 0.0;
    }
    
    public boolean isEnrolledInCourse(Course course) {
        return enrolledCourses.contains(course);
    }
    
    public int getTotalEnrolledCredits() {
        return enrolledCourses.stream()
                .mapToInt(Course::getCredits)
                .sum();
    }
    
    @Override
    public String getRole() {
        return "Student";
    }
    
    @Override
    public String toString() {
        return String.format("Student{id='%s', studentId='%s', name='%s %s', major='%s', year=%d, gpa=%.2f}", 
                id, studentId, getFullName(), major, yearLevel, gpa);
    }
}
