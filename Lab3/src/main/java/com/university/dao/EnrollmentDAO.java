package com.university.dao;

import com.university.database.DatabaseConnection;
import com.university.model.Student;
import com.university.model.Course;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentDAO {
    private DatabaseConnection dbConnection;
    
    public EnrollmentDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }
    
    /**
     * Enrolls a student in a course.
     * @param studentId Student ID
     * @param courseId Course ID
     * @return true if successful, false otherwise
     */
    public boolean enrollStudent(String studentId, String courseId) {
        String sql = "INSERT INTO enrollments (student_id, course_id, status) VALUES (?, ?, 'ACTIVE') " +
                    "ON CONFLICT (student_id, course_id) DO UPDATE SET status = 'ACTIVE'";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, studentId);
            stmt.setString(2, courseId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error enrolling student: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Drops a student from a course.
     * @param studentId Student ID
     * @param courseId Course ID
     * @return true if successful, false otherwise
     */
    public boolean dropStudent(String studentId, String courseId) {
        String sql = "UPDATE enrollments SET status = 'DROPPED' WHERE student_id = ? AND course_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, studentId);
            stmt.setString(2, courseId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error dropping student: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Adds a grade for a student in a course.
     * @param studentId Student ID
     * @param courseId Course ID
     * @param grade Grade to assign
     * @return true if successful, false otherwise
     */
    public boolean addGrade(String studentId, String courseId, double grade) {
        String sql = "UPDATE enrollments SET grade = ? WHERE student_id = ? AND course_id = ? AND status = 'ACTIVE'";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDouble(1, grade);
            stmt.setString(2, studentId);
            stmt.setString(3, courseId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error adding grade: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Gets the grade for a student in a specific course.
     * @param studentId Student ID
     * @param courseId Course ID
     * @return Grade or -1 if not found
     */
    public double getGrade(String studentId, String courseId) {
        String sql = "SELECT grade FROM enrollments WHERE student_id = ? AND course_id = ? AND status = 'ACTIVE'";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, studentId);
            stmt.setString(2, courseId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble("grade");
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving grade: " + e.getMessage());
        }
        
        return -1;
    }
    
    /**
     * Gets all courses a student is enrolled in.
     * @param studentId Student ID
     * @return List of course IDs
     */
    public List<String> getStudentCourses(String studentId) {
        String sql = "SELECT course_id FROM enrollments WHERE student_id = ? AND status = 'ACTIVE'";
        List<String> courseIds = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, studentId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                courseIds.add(rs.getString("course_id"));
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving student courses: " + e.getMessage());
        }
        
        return courseIds;
    }
    
    /**
     * Gets all students enrolled in a specific course.
     * @param courseId Course ID
     * @return List of student IDs
     */
    public List<String> getCourseStudents(String courseId) {
        String sql = "SELECT student_id FROM enrollments WHERE course_id = ? AND status = 'ACTIVE'";
        List<String> studentIds = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, courseId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                studentIds.add(rs.getString("student_id"));
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving course students: " + e.getMessage());
        }
        
        return studentIds;
    }
    
    /**
     * Gets detailed enrollment information with student and course details.
     * @return List of enrollment records with JOIN data
     */
    public List<EnrollmentRecord> getAllEnrollments() {
        String sql = "SELECT e.*, s.first_name, s.last_name, s.student_id as student_number, " +
                    "c.course_name, c.credits FROM enrollments e " +
                    "JOIN students s ON e.student_id = s.id " +
                    "JOIN courses c ON e.course_id = c.course_id " +
                    "WHERE e.status = 'ACTIVE' ORDER BY s.last_name, c.course_name";
        List<EnrollmentRecord> enrollments = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                EnrollmentRecord record = new EnrollmentRecord();
                record.setStudentId(rs.getString("student_id"));
                record.setStudentName(rs.getString("first_name") + " " + rs.getString("last_name"));
                record.setStudentNumber(rs.getString("student_number"));
                record.setCourseId(rs.getString("course_id"));
                record.setCourseName(rs.getString("course_name"));
                record.setCredits(rs.getInt("credits"));
                record.setGrade(rs.getDouble("grade"));
                record.setEnrollmentDate(rs.getTimestamp("enrollment_date"));
                record.setStatus(rs.getString("status"));
                
                enrollments.add(record);
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving all enrollments: " + e.getMessage());
        }
        
        return enrollments;
    }
    
    /**
     * Checks if a student is enrolled in a course.
     * @param studentId Student ID
     * @param courseId Course ID
     * @return true if enrolled, false otherwise
     */
    public boolean isEnrolled(String studentId, String courseId) {
        String sql = "SELECT COUNT(*) FROM enrollments WHERE student_id = ? AND course_id = ? AND status = 'ACTIVE'";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, studentId);
            stmt.setString(2, courseId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking enrollment: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Gets enrollment statistics for a course.
     * @param courseId Course ID
     * @return EnrollmentStats object with statistics
     */
    public EnrollmentStats getCourseStats(String courseId) {
        String sql = "SELECT COUNT(*) as total_enrolled, AVG(grade) as average_grade, " +
                    "COUNT(CASE WHEN grade IS NOT NULL THEN 1 END) as graded_count " +
                    "FROM enrollments WHERE course_id = ? AND status = 'ACTIVE'";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, courseId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                EnrollmentStats stats = new EnrollmentStats();
                stats.setTotalEnrolled(rs.getInt("total_enrolled"));
                stats.setAverageGrade(rs.getDouble("average_grade"));
                stats.setGradedCount(rs.getInt("graded_count"));
                return stats;
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving course stats: " + e.getMessage());
        }
        
        return new EnrollmentStats();
    }
    
    /**
     * Inner class to represent enrollment records with JOIN data.
     */
    public static class EnrollmentRecord {
        private String studentId;
        private String studentName;
        private String studentNumber;
        private String courseId;
        private String courseName;
        private int credits;
        private double grade;
        private Timestamp enrollmentDate;
        private String status;
        
        // Getters and setters
        public String getStudentId() { return studentId; }
        public void setStudentId(String studentId) { this.studentId = studentId; }
        
        public String getStudentName() { return studentName; }
        public void setStudentName(String studentName) { this.studentName = studentName; }
        
        public String getStudentNumber() { return studentNumber; }
        public void setStudentNumber(String studentNumber) { this.studentNumber = studentNumber; }
        
        public String getCourseId() { return courseId; }
        public void setCourseId(String courseId) { this.courseId = courseId; }
        
        public String getCourseName() { return courseName; }
        public void setCourseName(String courseName) { this.courseName = courseName; }
        
        public int getCredits() { return credits; }
        public void setCredits(int credits) { this.credits = credits; }
        
        public double getGrade() { return grade; }
        public void setGrade(double grade) { this.grade = grade; }
        
        public Timestamp getEnrollmentDate() { return enrollmentDate; }
        public void setEnrollmentDate(Timestamp enrollmentDate) { this.enrollmentDate = enrollmentDate; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        
        @Override
        public String toString() {
            return String.format("Enrollment{student='%s (%s)', course='%s (%s)', grade=%.2f, status='%s'}", 
                    studentName, studentNumber, courseName, courseId, grade, status);
        }
    }
    
    /**
     * Inner class to represent enrollment statistics.
     */
    public static class EnrollmentStats {
        private int totalEnrolled;
        private double averageGrade;
        private int gradedCount;
        
        // Getters and setters
        public int getTotalEnrolled() { return totalEnrolled; }
        public void setTotalEnrolled(int totalEnrolled) { this.totalEnrolled = totalEnrolled; }
        
        public double getAverageGrade() { return averageGrade; }
        public void setAverageGrade(double averageGrade) { this.averageGrade = averageGrade; }
        
        public int getGradedCount() { return gradedCount; }
        public void setGradedCount(int gradedCount) { this.gradedCount = gradedCount; }
        
        @Override
        public String toString() {
            return String.format("Stats{enrolled=%d, avgGrade=%.2f, graded=%d}", 
                    totalEnrolled, averageGrade, gradedCount);
        }
    }
}
