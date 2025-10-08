package com.university;

import com.university.dao.StudentDAO;
import com.university.dao.CourseDAO;
import com.university.dao.EnrollmentDAO;
import com.university.model.*;
import com.university.database.DatabaseConnection;

import java.util.*;
import java.util.Scanner;

public class UniversityManagementSystem {
    private Scanner scanner;
    private StudentDAO studentDAO;
    private CourseDAO courseDAO;
    private EnrollmentDAO enrollmentDAO;
    private boolean running;
    
    public UniversityManagementSystem() {
        this.scanner = new Scanner(System.in);
        this.studentDAO = new StudentDAO();
        this.courseDAO = new CourseDAO();
        this.enrollmentDAO = new EnrollmentDAO();
        this.running = true;
    }
    
    public static void main(String[] args) {
        UniversityManagementSystem system = new UniversityManagementSystem();
        system.run();
    }
    
    public void run() {
        System.out.println("=== University Management System ===");
        System.out.println("Welcome to the University Management System!");
        
        // Test database connection
        if (!testDatabaseConnection()) {
            System.out.println("Failed to connect to database. Please check your database configuration.");
            return;
        }
        
        while (running) {
            displayMainMenu();
            try {
                int choice = getIntInput("Enter your choice: ");
                handleMainMenuChoice(choice);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                System.out.println("Please try again.");
            }
        }
        
        scanner.close();
        System.out.println("Thank you for using the University Management System!");
    }
    
    private boolean testDatabaseConnection() {
        System.out.println("Testing database connection...");
        DatabaseConnection dbConnection = DatabaseConnection.getInstance();
        boolean connected = dbConnection.testConnection();
        
        if (connected) {
            System.out.println(" Database connection successful!");
        } else {
            System.out.println(" Database connection failed!");
        }
        
        return connected;
    }
    
    private void displayMainMenu() {
        System.out.println("\n=== Main Menu ===");
        System.out.println("1. Student Management");
        System.out.println("2. Course Management");
        System.out.println("3. Enrollment Management");
        System.out.println("4. View Reports");
        System.out.println("5. Exit");
        System.out.println("==================");
    }
    
    private void handleMainMenuChoice(int choice) {
        switch (choice) {
            case 1:
                studentManagementMenu();
                break;
            case 2:
                courseManagementMenu();
                break;
            case 3:
                enrollmentManagementMenu();
                break;
            case 4:
                viewReportsMenu();
                break;
            case 5:
                running = false;
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }
    
    private void studentManagementMenu() {
        while (true) {
            System.out.println("\n=== Student Management ===");
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Search Student by ID");
            System.out.println("4. Search Students by Major");
            System.out.println("5. Update Student");
            System.out.println("6. Delete Student");
            System.out.println("7. Back to Main Menu");
            System.out.println("=========================");
            
            try {
                int choice = getIntInput("Enter your choice: ");
                
                switch (choice) {
                    case 1:
                        addStudent();
                        break;
                    case 2:
                        viewAllStudents();
                        break;
                    case 3:
                        searchStudentById();
                        break;
                    case 4:
                        searchStudentsByMajor();
                        break;
                    case 5:
                        updateStudent();
                        break;
                    case 6:
                        deleteStudent();
                        break;
                    case 7:
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    
    private void addStudent() {
        try {
            System.out.println("\n=== Add New Student ===");
            
            String id = getStringInput("Enter student ID: ");
            String studentId = getStringInput("Enter student number: ");
            String firstName = getStringInput("Enter first name: ");
            String lastName = getStringInput("Enter last name: ");
            String email = getStringInput("Enter email: ");
            String phoneNumber = getStringInput("Enter phone number: ");
            String major = getStringInput("Enter major: ");
            int yearLevel = getIntInput("Enter year level: ");
            
            System.out.println("Student type:");
            System.out.println("1. Undergraduate");
            System.out.println("2. Graduate");
            int studentType = getIntInput("Choose student type: ");
            
            Student student;
            
            if (studentType == 1) {
                student = new UndergraduateStudent(id, firstName, lastName, email, phoneNumber, studentId, major, yearLevel);
                String advisor = getStringInput("Enter advisor name (optional): ");
                if (!advisor.isEmpty()) {
                    ((UndergraduateStudent) student).setAdvisor(advisor);
                }
                boolean isHonors = getBooleanInput("Is honors student? (y/n): ");
                ((UndergraduateStudent) student).setHonorsStudent(isHonors);
            } else if (studentType == 2) {
                student = new GraduateStudent(id, firstName, lastName, email, phoneNumber, studentId, major, yearLevel);
                String thesisTitle = getStringInput("Enter thesis title (optional): ");
                if (!thesisTitle.isEmpty()) {
                    ((GraduateStudent) student).setThesisTitle(thesisTitle);
                }
                String supervisor = getStringInput("Enter supervisor name (optional): ");
                if (!supervisor.isEmpty()) {
                    ((GraduateStudent) student).setSupervisor(supervisor);
                }
                String degreeProgram = getStringInput("Enter degree program (optional): ");
                if (!degreeProgram.isEmpty()) {
                    ((GraduateStudent) student).setDegreeProgram(degreeProgram);
                }
            } else {
                System.out.println("Invalid student type. Creating undergraduate student.");
                student = new UndergraduateStudent(id, firstName, lastName, email, phoneNumber, studentId, major, yearLevel);
            }
            
            if (studentDAO.createStudent(student)) {
                System.out.println(" Student added successfully!");
            } else {
                System.out.println(" Failed to add student. Please check your input.");
            }
            
        } catch (Exception e) {
            System.out.println("Error adding student: " + e.getMessage());
        }
    }
    
    private void viewAllStudents() {
        System.out.println("\n=== All Students ===");
        List<Student> students = studentDAO.getAllStudents();
        
        if (students.isEmpty()) {
            System.out.println("No students found.");
        } else {
            for (Student student : students) {
                System.out.println(student);
            }
        }
    }
    
    private void searchStudentById() {
        String studentId = getStringInput("Enter student ID to search: ");
        Student student = studentDAO.getStudentById(studentId);
        
        if (student != null) {
            System.out.println("\n=== Student Found ===");
            System.out.println(student);
        } else {
            System.out.println("Student not found.");
        }
    }
    
    private void searchStudentsByMajor() {
        String major = getStringInput("Enter major to search: ");
        List<Student> students = studentDAO.getStudentsByMajor(major);
        
        if (students.isEmpty()) {
            System.out.println("No students found with major: " + major);
        } else {
            System.out.println("\n=== Students with Major: " + major + " ===");
            for (Student student : students) {
                System.out.println(student);
            }
        }
    }
    
    private void updateStudent() {
        String studentId = getStringInput("Enter student ID to update: ");
        Student student = studentDAO.getStudentById(studentId);
        
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }
        
        System.out.println("Current student information:");
        System.out.println(student);
        
        System.out.println("\nEnter new information (press Enter to keep current value):");
        
        String firstName = getStringInput("First name [" + student.getFirstName() + "]: ");
        if (!firstName.isEmpty()) student.setFirstName(firstName);
        
        String lastName = getStringInput("Last name [" + student.getLastName() + "]: ");
        if (!lastName.isEmpty()) student.setLastName(lastName);
        
        String email = getStringInput("Email [" + student.getEmail() + "]: ");
        if (!email.isEmpty()) student.setEmail(email);
        
        String phoneNumber = getStringInput("Phone number [" + student.getPhoneNumber() + "]: ");
        if (!phoneNumber.isEmpty()) student.setPhoneNumber(phoneNumber);
        
        String major = getStringInput("Major [" + student.getMajor() + "]: ");
        if (!major.isEmpty()) student.setMajor(major);
        
        int yearLevel = getIntInput("Year level [" + student.getYearLevel() + "]: ");
        if (yearLevel > 0) student.setYearLevel(yearLevel);
        
        if (studentDAO.updateStudent(student)) {
            System.out.println("✓ Student updated successfully!");
        } else {
            System.out.println("✗ Failed to update student.");
        }
    }
    
    private void deleteStudent() {
        String studentId = getStringInput("Enter student ID to delete: ");
        
        System.out.println("Are you sure you want to delete this student? (y/n): ");
        String confirmation = scanner.nextLine().toLowerCase();
        
        if (confirmation.equals("y") || confirmation.equals("yes")) {
            if (studentDAO.deleteStudent(studentId)) {
                System.out.println("✓ Student deleted successfully!");
            } else {
                System.out.println("✗ Failed to delete student.");
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }
    
    private void courseManagementMenu() {
        while (true) {
            System.out.println("\n=== Course Management ===");
            System.out.println("1. Add Course");
            System.out.println("2. View All Courses");
            System.out.println("3. Search Course by ID");
            System.out.println("4. Search Courses by Department");
            System.out.println("5. Update Course");
            System.out.println("6. Delete Course");
            System.out.println("7. Back to Main Menu");
            System.out.println("=========================");
            
            try {
                int choice = getIntInput("Enter your choice: ");
                
                switch (choice) {
                    case 1:
                        addCourse();
                        break;
                    case 2:
                        viewAllCourses();
                        break;
                    case 3:
                        searchCourseById();
                        break;
                    case 4:
                        searchCoursesByDepartment();
                        break;
                    case 5:
                        updateCourse();
                        break;
                    case 6:
                        deleteCourse();
                        break;
                    case 7:
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    
    private void addCourse() {
        try {
            System.out.println("\n=== Add New Course ===");
            
            String courseId = getStringInput("Enter course ID: ");
            String courseName = getStringInput("Enter course name: ");
            String description = getStringInput("Enter course description: ");
            int credits = getIntInput("Enter number of credits: ");
            String department = getStringInput("Enter department: ");
            String schedule = getStringInput("Enter schedule: ");
            String location = getStringInput("Enter location: ");
            
            Course course = new Course(courseId, courseName, description, credits, department, schedule, location);
            
            if (courseDAO.createCourse(course)) {
                System.out.println("✓ Course added successfully!");
            } else {
                System.out.println("✗ Failed to add course. Please check your input.");
            }
            
        } catch (Exception e) {
            System.out.println("Error adding course: " + e.getMessage());
        }
    }
    
    private void viewAllCourses() {
        System.out.println("\n=== All Courses ===");
        List<Course> courses = courseDAO.getAllCourses();
        
        if (courses.isEmpty()) {
            System.out.println("No courses found.");
        } else {
            for (Course course : courses) {
                System.out.println(course);
            }
        }
    }
    
    private void searchCourseById() {
        String courseId = getStringInput("Enter course ID to search: ");
        Course course = courseDAO.getCourseById(courseId);
        
        if (course != null) {
            System.out.println("\n=== Course Found ===");
            System.out.println(course);
        } else {
            System.out.println("Course not found.");
        }
    }
    
    private void searchCoursesByDepartment() {
        String department = getStringInput("Enter department to search: ");
        List<Course> courses = courseDAO.getCoursesByDepartment(department);
        
        if (courses.isEmpty()) {
            System.out.println("No courses found in department: " + department);
        } else {
            System.out.println("\n=== Courses in Department: " + department + " ===");
            for (Course course : courses) {
                System.out.println(course);
            }
        }
    }
    
    private void updateCourse() {
        String courseId = getStringInput("Enter course ID to update: ");
        Course course = courseDAO.getCourseById(courseId);
        
        if (course == null) {
            System.out.println("Course not found.");
            return;
        }
        
        System.out.println("Current course information:");
        System.out.println(course);
        
        System.out.println("\nEnter new information (press Enter to keep current value):");
        
        String courseName = getStringInput("Course name [" + course.getCourseName() + "]: ");
        if (!courseName.isEmpty()) course.setCourseName(courseName);
        
        String description = getStringInput("Description [" + course.getDescription() + "]: ");
        if (!description.isEmpty()) course.setDescription(description);
        
        int credits = getIntInput("Credits [" + course.getCredits() + "]: ");
        if (credits > 0) course.setCredits(credits);
        
        String department = getStringInput("Department [" + course.getDepartment() + "]: ");
        if (!department.isEmpty()) course.setDepartment(department);
        
        String schedule = getStringInput("Schedule [" + course.getSchedule() + "]: ");
        if (!schedule.isEmpty()) course.setSchedule(schedule);
        
        String location = getStringInput("Location [" + course.getLocation() + "]: ");
        if (!location.isEmpty()) course.setLocation(location);
        
        if (courseDAO.updateCourse(course)) {
            System.out.println("✓ Course updated successfully!");
        } else {
            System.out.println("✗ Failed to update course.");
        }
    }
    
    private void deleteCourse() {
        String courseId = getStringInput("Enter course ID to delete: ");
        
        System.out.println("Are you sure you want to delete this course? (y/n): ");
        String confirmation = scanner.nextLine().toLowerCase();
        
        if (confirmation.equals("y") || confirmation.equals("yes")) {
            if (courseDAO.deleteCourse(courseId)) {
                System.out.println("✓ Course deleted successfully!");
            } else {
                System.out.println("✗ Failed to delete course.");
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }
    
    private void enrollmentManagementMenu() {
        while (true) {
            System.out.println("\n=== Enrollment Management ===");
            System.out.println("1. Enroll Student in Course");
            System.out.println("2. Drop Student from Course");
            System.out.println("3. Add Grade");
            System.out.println("4. View Student's Courses");
            System.out.println("5. View Course Roster");
            System.out.println("6. View All Enrollments");
            System.out.println("7. Back to Main Menu");
            System.out.println("=============================");
            
            try {
                int choice = getIntInput("Enter your choice: ");
                
                switch (choice) {
                    case 1:
                        enrollStudent();
                        break;
                    case 2:
                        dropStudent();
                        break;
                    case 3:
                        addGrade();
                        break;
                    case 4:
                        viewStudentCourses();
                        break;
                    case 5:
                        viewCourseRoster();
                        break;
                    case 6:
                        viewAllEnrollments();
                        break;
                    case 7:
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    
    private void enrollStudent() {
        String studentId = getStringInput("Enter student ID: ");
        String courseId = getStringInput("Enter course ID: ");
        
        if (enrollmentDAO.enrollStudent(studentId, courseId)) {
            System.out.println("✓ Student enrolled successfully!");
        } else {
            System.out.println("✗ Failed to enroll student. Please check student and course IDs.");
        }
    }
    
    private void dropStudent() {
        String studentId = getStringInput("Enter student ID: ");
        String courseId = getStringInput("Enter course ID: ");
        
        if (enrollmentDAO.dropStudent(studentId, courseId)) {
            System.out.println("✓ Student dropped successfully!");
        } else {
            System.out.println("✗ Failed to drop student. Please check student and course IDs.");
        }
    }
    
    private void addGrade() {
        String studentId = getStringInput("Enter student ID: ");
        String courseId = getStringInput("Enter course ID: ");
        double grade = getDoubleInput("Enter grade: ");
        
        if (enrollmentDAO.addGrade(studentId, courseId, grade)) {
            System.out.println("✓ Grade added successfully!");
        } else {
            System.out.println("✗ Failed to add grade. Please check student and course IDs.");
        }
    }
    
    private void viewStudentCourses() {
        String studentId = getStringInput("Enter student ID: ");
        List<String> courseIds = enrollmentDAO.getStudentCourses(studentId);
        
        if (courseIds.isEmpty()) {
            System.out.println("Student is not enrolled in any courses.");
        } else {
            System.out.println("\n=== Student's Courses ===");
            for (String courseId : courseIds) {
                Course course = courseDAO.getCourseById(courseId);
                if (course != null) {
                    System.out.println(course);
                }
            }
        }
    }
    
    private void viewCourseRoster() {
        String courseId = getStringInput("Enter course ID: ");
        List<String> studentIds = enrollmentDAO.getCourseStudents(courseId);
        
        if (studentIds.isEmpty()) {
            System.out.println("No students enrolled in this course.");
        } else {
            System.out.println("\n=== Course Roster ===");
            for (String studentId : studentIds) {
                Student student = studentDAO.getStudentById(studentId);
                if (student != null) {
                    System.out.println(student);
                }
            }
        }
    }
    
    private void viewAllEnrollments() {
        System.out.println("\n=== All Enrollments ===");
        List<EnrollmentDAO.EnrollmentRecord> enrollments = enrollmentDAO.getAllEnrollments();
        
        if (enrollments.isEmpty()) {
            System.out.println("No enrollments found.");
        } else {
            for (EnrollmentDAO.EnrollmentRecord enrollment : enrollments) {
                System.out.println(enrollment);
            }
        }
    }
    
    private void viewReportsMenu() {
        while (true) {
            System.out.println("\n=== Reports ===");
            System.out.println("1. Student Statistics");
            System.out.println("2. Course Statistics");
            System.out.println("3. Enrollment Statistics");
            System.out.println("4. Back to Main Menu");
            System.out.println("================");
            
            try {
                int choice = getIntInput("Enter your choice: ");
                
                switch (choice) {
                    case 1:
                        viewStudentStatistics();
                        break;
                    case 2:
                        viewCourseStatistics();
                        break;
                    case 3:
                        viewEnrollmentStatistics();
                        break;
                    case 4:
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    
    private void viewStudentStatistics() {
        System.out.println("\n=== Student Statistics ===");
        List<Student> students = studentDAO.getAllStudents();
        
        System.out.println("Total Students: " + students.size());
        
        long undergradCount = students.stream()
                .filter(s -> s instanceof UndergraduateStudent)
                .count();
        long gradCount = students.stream()
                .filter(s -> s instanceof GraduateStudent)
                .count();
        
        System.out.println("Undergraduate Students: " + undergradCount);
        System.out.println("Graduate Students: " + gradCount);
        
        if (!students.isEmpty()) {
            double avgGPA = students.stream()
                    .mapToDouble(s -> s.getGpa() != null ? s.getGpa() : 0.0)
                    .average()
                    .orElse(0.0);
            System.out.println("Average GPA: " + String.format("%.2f", avgGPA));
        }
    }
    
    private void viewCourseStatistics() {
        System.out.println("\n=== Course Statistics ===");
        List<Course> courses = courseDAO.getAllCourses();
        
        System.out.println("Total Courses: " + courses.size());
        
        Map<String, Long> departmentCount = new HashMap<>();
        for (Course course : courses) {
            departmentCount.merge(course.getDepartment(), 1L, Long::sum);
        }
        
        System.out.println("\nCourses by Department:");
        departmentCount.forEach((dept, count) -> 
            System.out.println("  " + dept + ": " + count));
    }
    
    private void viewEnrollmentStatistics() {
        System.out.println("\n=== Enrollment Statistics ===");
        List<EnrollmentDAO.EnrollmentRecord> enrollments = enrollmentDAO.getAllEnrollments();
        
        System.out.println("Total Active Enrollments: " + enrollments.size());
        
        if (!enrollments.isEmpty()) {
            double avgGrade = enrollments.stream()
                    .filter(e -> e.getGrade() > 0)
                    .mapToDouble(EnrollmentDAO.EnrollmentRecord::getGrade)
                    .average()
                    .orElse(0.0);
            System.out.println("Average Grade: " + String.format("%.2f", avgGrade));
        }
    }
    
    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
    
    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }
    
    private double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }
    
    private boolean getBooleanInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("y") || input.equals("yes") || input.equals("true")) {
                return true;
            } else if (input.equals("n") || input.equals("no") || input.equals("false")) {
                return false;
            } else {
                System.out.println("Please enter 'y' or 'n'.");
            }
        }
    }
}
