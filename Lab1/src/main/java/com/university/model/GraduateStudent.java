package com.university.model;

import java.util.Map;

public class GraduateStudent extends Student {
    private String thesisTitle;
    private String supervisor;
    private String degreeProgram;
    
    public GraduateStudent(String id, String firstName, String lastName, String email, 
                          String phoneNumber, String studentId, String major, int yearLevel) {
        super(id, firstName, lastName, email, phoneNumber, studentId, major, yearLevel);
    }
    
    public String getThesisTitle() {
        return thesisTitle;
    }
    
    public void setThesisTitle(String thesisTitle) {
        this.thesisTitle = thesisTitle;
    }
    
    public String getSupervisor() {
        return supervisor;
    }
    
    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }
    
    public String getDegreeProgram() {
        return degreeProgram;
    }
    
    public void setDegreeProgram(String degreeProgram) {
        this.degreeProgram = degreeProgram;
    }
    
    @Override
    public void calculateGPA() {
        if (getCourseGrades().isEmpty()) {
            setGpa(0.0);
            return;
        }
        
        double totalPoints = 0.0;
        int totalCredits = 0;
        
        for (Map.Entry<Course, Double> entry : getCourseGrades().entrySet()) {
            Course course = entry.getKey();
            Double grade = entry.getValue();
            
            double gradePoints = convertToGradePoints(grade);
            totalPoints += gradePoints * course.getCredits();
            totalCredits += course.getCredits();
        }
        
        setGpa(totalCredits > 0 ? totalPoints / totalCredits : 0.0);
    }
    
    private double convertToGradePoints(double grade) {
        if (grade >= 90) return 4.0;
        if (grade >= 80) return 3.0;
        if (grade >= 70) return 2.0;
        return 0.0;
    }
    
    @Override
    public String getRole() {
        return "Graduate Student";
    }
    
    @Override
    public String toString() {
        return String.format("GraduateStudent{id='%s', studentId='%s', name='%s %s', major='%s', year=%d, gpa=%.2f, program='%s'}", 
                getId(), getStudentId(), getFullName(), getMajor(), getYearLevel(), getGpa(), degreeProgram);
    }
}
