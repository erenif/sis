package Utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    public static void initializeDatabase(Connection connection) {
        try (Statement statement = connection.createStatement()) {
            // Course_Table
            statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS Course_Table (
                    course_id INT PRIMARY KEY,
                    course_name VARCHAR(255) NOT NULL,
                    quota INT NOT NULL,
                    credits INT NOT NULL,
                    start_time TIME NOT NULL,
                    end_time TIME NOT NULL,
                    course_day VARCHAR(50) NOT NULL,
                    syllabus VARCHAR(255) NOT NULL
                );
            """);

            // Prerequisite_Table
            statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS Prerequisite_Table (
                    course_id INT NOT NULL,
                    prerequisite_course_id INT NOT NULL,
                    PRIMARY KEY (course_id, prerequisite_course_id),
                    FOREIGN KEY (course_id) REFERENCES Course_Table(course_id),
                    FOREIGN KEY (prerequisite_course_id) REFERENCES Course_Table(course_id)
                );
            """);

            // Student_Table
            statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS Student_Table (
                    student_id INT PRIMARY KEY,
                    student_name VARCHAR(255) NOT NULL,
                    password VARCHAR(255) NOT NULL,
                    gpa FLOAT DEFAULT 0.0,
                    available_credits INT NOT NULL
                );
            """);

            // Diğer tabloları ekleyin
            // ...

            // Veri ekleme (örnek veriler)
            statement.executeUpdate("""
                INSERT INTO Course_Table (course_id, course_name, quota, credits, start_time, end_time, course_day, syllabus)
                VALUES
                    (1, 'Introduction to Programming', 30, 4, '09:00:00', '11:00:00', 'MONDAY', 'Basic programming concepts'),
                    (2, 'Data Structures', 25, 3, '11:30:00', '13:00:00', 'TUESDAY', 'Learn about data organization')
                ON DUPLICATE KEY UPDATE course_name=VALUES(course_name);
            """);

            statement.executeUpdate("""
                INSERT INTO Student_Table (student_id, student_name, password, gpa, available_credits)
                VALUES
                    (101, 'Alice Smith', 'password123', 3.8, 15),
                    (102, 'Bob Johnson', 'password456', 3.5, 20)
                ON DUPLICATE KEY UPDATE student_name=VALUES(student_name);
            """);

            System.out.println("Database initialized successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error initializing database: " + e.getMessage());
        }
    }
}