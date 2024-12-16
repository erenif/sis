package DAOs;

import Entities.Student;
import Entities.Course;
import Utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO extends DAO.AbstractDB {
    public StudentDAO(Connection connection) {
        super(connection);
    }

    public Student getStudentById(int studentId) throws SQLException {
        String query = "SELECT * FROM Student_Table WHERE student_id = ?";
        ResultSet resultSet = executeQuery(query, studentId);

        if (resultSet.next()) {
            return new Student(
                    resultSet.getInt("student_id"),
                    resultSet.getString("student_name"),
                    getCoursesForStudent(resultSet.getInt("student_id")),
                    resultSet.getDouble("gpa"),
                    resultSet.getInt("available_credits")
            );
        }
        return null;
    }

    public void addStudent(Student student) throws SQLException {
        String query = "INSERT INTO Student_Table (student_id, student_name, password, gpa, available_credits) VALUES (?, ?, ?, ?, ?)";
        executeUpdate(query, student.getUserID(), student.getUserName(), "default_password", student.getGpa(), student.getAvailableCredits());
    }

    public void updateStudent(Student student) throws SQLException {
        String query = "UPDATE Student_Table SET student_name = ?, gpa = ?, available_credits = ? WHERE student_id = ?";
        executeUpdate(query, student.getUserName(), student.getGpa(), student.getAvailableCredits(), student.getUserID());
    }

    public void deleteStudent(int studentId) throws SQLException {
        String query = "DELETE FROM Student_Table WHERE student_id = ?";
        executeUpdate(query, studentId);
    }

    public List<Student> getAllStudents() throws SQLException {
        String query = "SELECT * FROM Student_Table";
        ResultSet resultSet = executeQuery(query);

        List<Student> students = new ArrayList<>();
        while (resultSet.next()) {
            students.add(new Student(
                    resultSet.getInt("student_id"),
                    resultSet.getString("student_name"),
                    getCoursesForStudent(resultSet.getInt("student_id")),
                    resultSet.getDouble("gpa"),
                    resultSet.getInt("available_credits")
            ));
        }
        return students;
    }

    public void enrollInCourse(int studentId, int courseId) throws SQLException {
        String checkCreditsQuery = "SELECT available_credits FROM Student_Table WHERE student_id = ?";
        ResultSet resultSet = executeQuery(checkCreditsQuery, studentId);

        if (resultSet.next()) {
            int availableCredits = resultSet.getInt("available_credits");
            String courseCreditsQuery = "SELECT credits FROM Course_Table WHERE course_id = ?";
            ResultSet courseResultSet = executeQuery(courseCreditsQuery, courseId);

            if (courseResultSet.next()) {
                int courseCredits = courseResultSet.getInt("credits");
                if (availableCredits >= courseCredits) {
                    String enrollQuery = "INSERT INTO Enrollment_Table (student_id, course_id) VALUES (?, ?)";
                    executeUpdate(enrollQuery, studentId, courseId);

                    String updateCreditsQuery = "UPDATE Student_Table SET available_credits = available_credits - ? WHERE student_id = ?";
                    executeUpdate(updateCreditsQuery, courseCredits, studentId);
                } else {
                    throw new SQLException("Not enough available credits to enroll in the course.");
                }
            }
        }
    }

    public void dropCourse(int studentId, int courseId) throws SQLException {
        String courseCreditsQuery = "SELECT credits FROM Course_Table WHERE course_id = ?";
        ResultSet courseResultSet = executeQuery(courseCreditsQuery, courseId);
        if (courseResultSet.next()) {
            int courseCredits = courseResultSet.getInt("credits");

            String dropQuery = "DELETE FROM Enrollment_Table WHERE student_id = ? AND course_id = ?";
            executeUpdate(dropQuery, studentId, courseId);

            String updateCreditsQuery = "UPDATE Student_Table SET available_credits = available_credits + ? WHERE student_id = ?";
            executeUpdate(updateCreditsQuery, courseCredits, studentId);
        }
    }

    private ArrayList<Course> getCoursesForStudent(int studentId) throws SQLException {
        String query = """
            SELECT c.course_id, c.course_name, c.quota, c.start_time, c.end_time, c.syllabus, c.day_of_week
            FROM Course_Table c
            INNER JOIN Enrollment_Table e ON c.course_id = e.course_id
            WHERE e.student_id = ?
        """;
        ResultSet resultSet = executeQuery(query, studentId);
        ArrayList<Course> courses = new ArrayList<>();
        while (resultSet.next()) {
            courses.add(new Course(
                    resultSet.getInt("course_id"),
                    resultSet.getString("course_name"),
                    resultSet.getInt("quota"),
                    resultSet.getString("day_of_week"),
                    resultSet.getTime("start_time").toLocalTime(),
                    resultSet.getTime("end_time").toLocalTime(),
                    resultSet.getString("syllabus")
            ));
        }
        return courses;
    }
}
