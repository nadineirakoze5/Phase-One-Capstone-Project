package com.university.dao;

import com.university.database.DatabaseConnection;
import com.university.model.Course;
import com.university.model.Instructor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {
    private DatabaseConnection dbConnection;
    
    public CourseDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }
    
    public boolean createCourse(Course course) {
        String sql = "INSERT INTO courses (course_id, course_name, description, credits, " +
                    "department, instructor_id, schedule, location) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, course.getCourseId());
            stmt.setString(2, course.getCourseName());
            stmt.setString(3, course.getDescription());
            stmt.setInt(4, course.getCredits());
            stmt.setString(5, course.getDepartment());
            stmt.setString(6, course.getInstructor() != null ? course.getInstructor().getId() : null);
            stmt.setString(7, course.getSchedule());
            stmt.setString(8, course.getLocation());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error creating course: " + e.getMessage());
            return false;
        }
    }
    
    public Course getCourseById(String courseId) {
        String sql = "SELECT c.*, i.first_name, i.last_name, i.email, i.phone_number, " +
                    "i.employee_id, i.department as instructor_dept, i.title, i.salary, " +
                    "i.years_of_experience FROM courses c " +
                    "LEFT JOIN instructors i ON c.instructor_id = i.id WHERE c.course_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, courseId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToCourse(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving course: " + e.getMessage());
        }
        
        return null;
    }
    
    public List<Course> getAllCourses() {
        String sql = "SELECT c.*, i.first_name, i.last_name, i.email, i.phone_number, " +
                    "i.employee_id, i.department as instructor_dept, i.title, i.salary, " +
                    "i.years_of_experience FROM courses c " +
                    "LEFT JOIN instructors i ON c.instructor_id = i.id " +
                    "ORDER BY c.course_name";
        List<Course> courses = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                courses.add(mapResultSetToCourse(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving all courses: " + e.getMessage());
        }
        
        return courses;
    }
    
    public boolean updateCourse(Course course) {
        String sql = "UPDATE courses SET course_name = ?, description = ?, credits = ?, " +
                    "department = ?, instructor_id = ?, schedule = ?, location = ? WHERE course_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, course.getCourseName());
            stmt.setString(2, course.getDescription());
            stmt.setInt(3, course.getCredits());
            stmt.setString(4, course.getDepartment());
            stmt.setString(5, course.getInstructor() != null ? course.getInstructor().getId() : null);
            stmt.setString(6, course.getSchedule());
            stmt.setString(7, course.getLocation());
            stmt.setString(8, course.getCourseId());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating course: " + e.getMessage());
            return false;
        }
    }
    
    public boolean deleteCourse(String courseId) {
        String sql = "DELETE FROM courses WHERE course_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, courseId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting course: " + e.getMessage());
            return false;
        }
    }
    
    public List<Course> getCoursesByDepartment(String department) {
        String sql = "SELECT c.*, i.first_name, i.last_name, i.email, i.phone_number, " +
                    "i.employee_id, i.department as instructor_dept, i.title, i.salary, " +
                    "i.years_of_experience FROM courses c " +
                    "LEFT JOIN instructors i ON c.instructor_id = i.id " +
                    "WHERE c.department ILIKE ? ORDER BY c.course_name";
        List<Course> courses = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + department + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                courses.add(mapResultSetToCourse(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error searching courses by department: " + e.getMessage());
        }
        
        return courses;
    }
    
    public List<Course> getCoursesByInstructor(String instructorId) {
        String sql = "SELECT c.*, i.first_name, i.last_name, i.email, i.phone_number, " +
                    "i.employee_id, i.department as instructor_dept, i.title, i.salary, " +
                    "i.years_of_experience FROM courses c " +
                    "LEFT JOIN instructors i ON c.instructor_id = i.id " +
                    "WHERE c.instructor_id = ? ORDER BY c.course_name";
        List<Course> courses = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, instructorId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                courses.add(mapResultSetToCourse(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving courses by instructor: " + e.getMessage());
        }
        
        return courses;
    }
    
    private Course mapResultSetToCourse(ResultSet rs) throws SQLException {
        String courseId = rs.getString("course_id");
        String courseName = rs.getString("course_name");
        String description = rs.getString("description");
        int credits = rs.getInt("credits");
        String department = rs.getString("department");
        String schedule = rs.getString("schedule");
        String location = rs.getString("location");
        
        Course course = new Course(courseId, courseName, description, credits, department, schedule, location);

        String instructorId = rs.getString("instructor_id");
        if (instructorId != null) {
            String firstName = rs.getString("first_name");
            String lastName = rs.getString("last_name");
            String email = rs.getString("email");
            String phoneNumber = rs.getString("phone_number");
            String employeeId = rs.getString("employee_id");
            String instructorDept = rs.getString("instructor_dept");
            String title = rs.getString("title");
            double salary = rs.getDouble("salary");
            int yearsOfExperience = rs.getInt("years_of_experience");
            
            Instructor instructor = new Instructor(instructorId, firstName, lastName, email, 
                                                 phoneNumber, employeeId, instructorDept, 
                                                 title, salary, yearsOfExperience);
            course.setInstructor(instructor);
        }
        
        return course;
    }
}
