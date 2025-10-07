package com.university.model;

import java.util.*;

public class Instructor extends Person {
    private String employeeId;
    private String department;
    private String title;
    private double salary;
    private Set<Course> assignedCourses;
    private List<String> specializations;
    private int yearsOfExperience;
    
    public Instructor(String id, String firstName, String lastName, String email, 
                     String phoneNumber, String employeeId, String department, 
                     String title, double salary, int yearsOfExperience) {
        super(id, firstName, lastName, email, phoneNumber);
        this.employeeId = employeeId;
        this.department = department;
        this.title = title;
        this.salary = salary;
        this.yearsOfExperience = yearsOfExperience;
        this.assignedCourses = new HashSet<>();
        this.specializations = new ArrayList<>();
    }
    
    public String getEmployeeId() {
        return employeeId;
    }
    
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public double getSalary() {
        return salary;
    }
    
    public void setSalary(double salary) {
        this.salary = salary;
    }
    
    public Set<Course> getAssignedCourses() {
        return new HashSet<>(assignedCourses);
    }
    
    public List<String> getSpecializations() {
        return new ArrayList<>(specializations);
    }
    
    public int getYearsOfExperience() {
        return yearsOfExperience;
    }
    
    public void setYearsOfExperience(int yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }
    
    public void assignToCourse(Course course) {
        assignedCourses.add(course);
        course.setInstructor(this);
    }
    
    public void removeFromCourse(Course course) {
        assignedCourses.remove(course);
        if (course.getInstructor() == this) {
            course.setInstructor(null);
        }
    }
    
    public void addSpecialization(String specialization) {
        if (!specializations.contains(specialization)) {
            specializations.add(specialization);
        }
    }
    
    public void removeSpecialization(String specialization) {
        specializations.remove(specialization);
    }
    
    public int getTotalStudents() {
        return assignedCourses.stream()
                .mapToInt(Course::getEnrollmentCount)
                .sum();
    }
    
    public double getAverageCourseGrade(Course course) {
        if (assignedCourses.contains(course)) {
            return course.getAverageGrade();
        }
        return 0.0;
    }
    
    public boolean canTeachCourse(String subject) {
        return specializations.contains(subject) || 
               specializations.stream().anyMatch(spec -> spec.toLowerCase().contains(subject.toLowerCase()));
    }
    
    @Override
    public String getRole() {
        return "Instructor";
    }
    
    @Override
    public String toString() {
        return String.format("Instructor{id='%s', employeeId='%s', name='%s %s', title='%s', department='%s', courses=%d}", 
                id, employeeId, getFullName(), title, department, assignedCourses.size());
    }
}
