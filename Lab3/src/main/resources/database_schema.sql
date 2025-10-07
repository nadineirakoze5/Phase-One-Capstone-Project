-- University Management System Database Schema
-- PostgreSQL Database Schema for Lab 2

-- Create database (run this separately if needed)
-- CREATE DATABASE university_management;

-- Connect to the database
-- \c university_management;

-- Create tables
CREATE TABLE IF NOT EXISTS students (
    id VARCHAR(50) PRIMARY KEY,
    student_id VARCHAR(20) UNIQUE NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone_number VARCHAR(20),
    major VARCHAR(100),
    year_level INTEGER,
    gpa DECIMAL(3,2) DEFAULT 0.0,
    student_type VARCHAR(20) DEFAULT 'UNDERGRADUATE',
    advisor VARCHAR(100),
    is_honors_student BOOLEAN DEFAULT FALSE,
    thesis_title TEXT,
    supervisor VARCHAR(100),
    degree_program VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS instructors (
    id VARCHAR(50) PRIMARY KEY,
    employee_id VARCHAR(20) UNIQUE NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone_number VARCHAR(20),
    department VARCHAR(100),
    title VARCHAR(100),
    salary DECIMAL(10,2),
    years_of_experience INTEGER,
    specializations TEXT[], -- Array of specializations
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS courses (
    course_id VARCHAR(20) PRIMARY KEY,
    course_name VARCHAR(100) NOT NULL,
    description TEXT,
    credits INTEGER NOT NULL,
    department VARCHAR(100),
    instructor_id VARCHAR(50),
    schedule VARCHAR(100),
    location VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (instructor_id) REFERENCES instructors(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS enrollments (
    id SERIAL PRIMARY KEY,
    student_id VARCHAR(50) NOT NULL,
    course_id VARCHAR(20) NOT NULL,
    grade DECIMAL(5,2),
    enrollment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    UNIQUE(student_id, course_id),
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE
);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_students_student_id ON students(student_id);
CREATE INDEX IF NOT EXISTS idx_students_email ON students(email);
CREATE INDEX IF NOT EXISTS idx_students_major ON students(major);
CREATE INDEX IF NOT EXISTS idx_instructors_employee_id ON instructors(employee_id);
CREATE INDEX IF NOT EXISTS idx_instructors_department ON instructors(department);
CREATE INDEX IF NOT EXISTS idx_courses_department ON courses(department);
CREATE INDEX IF NOT EXISTS idx_courses_instructor ON courses(instructor_id);
CREATE INDEX IF NOT EXISTS idx_enrollments_student ON enrollments(student_id);
CREATE INDEX IF NOT EXISTS idx_enrollments_course ON enrollments(course_id);

-- Insert sample data
INSERT INTO instructors (id, employee_id, first_name, last_name, email, phone_number, department, title, salary, years_of_experience, specializations) VALUES
('INST001', 'EMP001', 'Dr. Sarah', 'Johnson', 'sarah.johnson@university.edu', '555-0101', 'Computer Science', 'Professor', 95000.00, 15, ARRAY['Java Programming', 'Database Systems', 'Software Engineering']),
('INST002', 'EMP002', 'Prof. Michael', 'Chen', 'michael.chen@university.edu', '555-0102', 'Mathematics', 'Associate Professor', 85000.00, 12, ARRAY['Calculus', 'Linear Algebra', 'Statistics']),
('INST003', 'EMP003', 'Dr. Emily', 'Rodriguez', 'emily.rodriguez@university.edu', '555-0103', 'Computer Science', 'Assistant Professor', 75000.00, 8, ARRAY['Data Structures', 'Algorithms', 'Machine Learning']);

INSERT INTO courses (course_id, course_name, description, credits, department, instructor_id, schedule, location) VALUES
('CS101', 'Introduction to Programming', 'Basic programming concepts using Java', 3, 'Computer Science', 'INST001', 'MWF 10:00-10:50', 'Room 101'),
('CS201', 'Data Structures', 'Fundamental data structures and algorithms', 4, 'Computer Science', 'INST003', 'TTH 11:00-12:30', 'Room 201'),
('MATH101', 'Calculus I', 'Differential and integral calculus', 4, 'Mathematics', 'INST002', 'MWF 9:00-9:50', 'Room 301'),
('CS301', 'Database Systems', 'Database design and implementation', 3, 'Computer Science', 'INST001', 'TTH 2:00-3:30', 'Room 202'),
('MATH201', 'Linear Algebra', 'Vector spaces and linear transformations', 3, 'Mathematics', 'INST002', 'MWF 1:00-1:50', 'Room 302');

-- Create a function to update the updated_at timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Create triggers to automatically update the updated_at column
CREATE TRIGGER update_students_updated_at BEFORE UPDATE ON students
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_instructors_updated_at BEFORE UPDATE ON instructors
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_courses_updated_at BEFORE UPDATE ON courses
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
