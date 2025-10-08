package com.university.model;
import java.util.Map;

public class UndergraduateStudent extends Student {
    private String advisor;
    private boolean isHonorsStudent;
    
    public UndergraduateStudent(String id, String firstName, String lastName, String email, 
                               String phoneNumber, String studentId, String major, int yearLevel) {
        super(id, firstName, lastName, email, phoneNumber, studentId, major, yearLevel);
        this.isHonorsStudent = false;
    }
    
    public String getAdvisor() {
        return advisor;
    }
    
    public void setAdvisor(String advisor) {
        this.advisor = advisor;
    }
    
    public boolean isHonorsStudent() {
        return isHonorsStudent;
    }
    
    public void setHonorsStudent(boolean honorsStudent) {
        isHonorsStudent = honorsStudent;
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
        if (grade >= 60) return 1.0;
        return 0.0;
    }
    
    @Override
    public String getRole() {
        return "Undergraduate Student";
    }

    @Override
    public String toString() {
        double displayGpa = (getGpa() == null) ? 0.0 : getGpa();
        return String.format(
                "GraduateStudent{id='%s', studentId='%s', name='%s', major='%s', year=%d, gpa=%.2f, program='%s'}",
                getId(), getStudentId(), getFullName(), getMajor(), getYearLevel(), displayGpa, degreeProgram
        );
    }
}
