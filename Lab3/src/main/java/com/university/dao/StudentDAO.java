package com.university.dao;

import com.university.database.DatabaseConnection;
import com.university.model.Student;
import com.university.model.UndergraduateStudent;
import com.university.model.GraduateStudent;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {
    private DatabaseConnection dbConnection;
    
    public StudentDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }
    
    public boolean createStudent(Student student) {
        String sql = "INSERT INTO students (id, student_id, first_name, last_name, email, phone_number, " +
                    "major, year_level, gpa, student_type, advisor, is_honors_student, thesis_title, " +
                    "supervisor, degree_program) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, student.getId());
            stmt.setString(2, student.getStudentId());
            stmt.setString(3, student.getFirstName());
            stmt.setString(4, student.getLastName());
            stmt.setString(5, student.getEmail());
            stmt.setString(6, student.getPhoneNumber());
            stmt.setString(7, student.getMajor());
            stmt.setInt(8, student.getYearLevel());
            stmt.setDouble(9, student.getGpa());
            

            if (student instanceof UndergraduateStudent) {
                UndergraduateStudent undergrad = (UndergraduateStudent) student;
                stmt.setString(10, "UNDERGRADUATE");
                stmt.setString(11, undergrad.getAdvisor());
                stmt.setBoolean(12, undergrad.isHonorsStudent());
                stmt.setString(13, null);
                stmt.setString(14, null);
                stmt.setString(15, null);
            } else if (student instanceof GraduateStudent) {
                GraduateStudent grad = (GraduateStudent) student;
                stmt.setString(10, "GRADUATE");
                stmt.setString(11, null);
                stmt.setBoolean(12, false);
                stmt.setString(13, grad.getThesisTitle());
                stmt.setString(14, grad.getSupervisor());
                stmt.setString(15, grad.getDegreeProgram());
            } else {
                stmt.setString(10, "UNDERGRADUATE");
                stmt.setString(11, null);
                stmt.setBoolean(12, false);
                stmt.setString(13, null);
                stmt.setString(14, null);
                stmt.setString(15, null);
            }
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error creating student: " + e.getMessage());
            return false;
        }
    }
    
    public Student getStudentById(String studentId) {
        String sql = "SELECT * FROM students WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, studentId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToStudent(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving student: " + e.getMessage());
        }
        
        return null;
    }
    
    public List<Student> getAllStudents() {
        String sql = "SELECT * FROM students ORDER BY first_name";
        List<Student> students = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                students.add(mapResultSetToStudent(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving all students: " + e.getMessage());
        }
        
        return students;
    }
    
    public boolean updateStudent(Student student) {
        String sql = "UPDATE students SET student_id = ?, first_name = ?, last_name = ?, " +
                    "email = ?, phone_number = ?, major = ?, year_level = ?, gpa = ?, " +
                    "student_type = ?, advisor = ?, is_honors_student = ?, thesis_title = ?, " +
                    "supervisor = ?, degree_program = ? WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, student.getStudentId());
            stmt.setString(2, student.getFirstName());
            stmt.setString(3, student.getLastName());
            stmt.setString(4, student.getEmail());
            stmt.setString(5, student.getPhoneNumber());
            stmt.setString(6, student.getMajor());
            stmt.setInt(7, student.getYearLevel());
            stmt.setDouble(8, student.getGpa());

            if (student instanceof UndergraduateStudent) {
                UndergraduateStudent undergrad = (UndergraduateStudent) student;
                stmt.setString(9, "UNDERGRADUATE");
                stmt.setString(10, undergrad.getAdvisor());
                stmt.setBoolean(11, undergrad.isHonorsStudent());
                stmt.setString(12, null);
                stmt.setString(13, null);
                stmt.setString(14, null);
            } else if (student instanceof GraduateStudent) {
                GraduateStudent grad = (GraduateStudent) student;
                stmt.setString(9, "GRADUATE");
                stmt.setString(10, null);
                stmt.setBoolean(11, false);
                stmt.setString(12, grad.getThesisTitle());
                stmt.setString(13, grad.getSupervisor());
                stmt.setString(14, grad.getDegreeProgram());
            } else {
                stmt.setString(9, "UNDERGRADUATE");
                stmt.setString(10, null);
                stmt.setBoolean(11, false);
                stmt.setString(12, null);
                stmt.setString(13, null);
                stmt.setString(14, null);
            }
            
            stmt.setString(15, student.getId());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating student: " + e.getMessage());
            return false;
        }
    }
    
    public boolean deleteStudent(String studentId) {
        String sql = "DELETE FROM students WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, studentId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting student: " + e.getMessage());
            return false;
        }
    }
    
    public List<Student> getStudentsByMajor(String major) {
        String sql = "SELECT * FROM students WHERE major ILIKE ? ORDER BY last_name, first_name";
        List<Student> students = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + major + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                students.add(mapResultSetToStudent(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error searching students by major: " + e.getMessage());
        }
        
        return students;
    }
    
    private Student mapResultSetToStudent(ResultSet rs) throws SQLException {
        String id = rs.getString("id");
        String studentId = rs.getString("student_id");
        String firstName = rs.getString("first_name");
        String lastName = rs.getString("last_name");
        String email = rs.getString("email");
        String phoneNumber = rs.getString("phone_number");
        String major = rs.getString("major");
        int yearLevel = rs.getInt("year_level");
        double gpa = rs.getDouble("gpa");
        String studentType = rs.getString("student_type");
        
        Student student;
        
        if ("GRADUATE".equals(studentType)) {
            GraduateStudent gradStudent = new GraduateStudent(id, firstName, lastName, email, 
                                                             phoneNumber, studentId, major, yearLevel);
            gradStudent.setThesisTitle(rs.getString("thesis_title"));
            gradStudent.setSupervisor(rs.getString("supervisor"));
            gradStudent.setDegreeProgram(rs.getString("degree_program"));
            gradStudent.setGpa(gpa);
            student = gradStudent;
        } else {
            UndergraduateStudent undergradStudent = new UndergraduateStudent(id, firstName, lastName, email, 
                                                                           phoneNumber, studentId, major, yearLevel);
            undergradStudent.setAdvisor(rs.getString("advisor"));
            undergradStudent.setHonorsStudent(rs.getBoolean("is_honors_student"));
            undergradStudent.setGpa(gpa);
            student = undergradStudent;
        }
        
        return student;
    }
}
