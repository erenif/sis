package DAOs;

import Entities.Course;
import Entities.Enum.LetterGrades;
import Entities.Enum.WeekDays;
import Entities.Student;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StudentDAO extends DAOs.AbstractDB {
    public StudentDAO(Connection connection) {
        super(connection);
    }

    public Student getStudentById(int studentId) throws SQLException {
        String query = "SELECT * FROM Student_Table WHERE student_id = ?";
        ResultSet resultSet = executeQuery(query, studentId);

        if (resultSet.next()) {
            ArrayList<Course> courses = getCoursesForStudent(studentId);
            HashMap<Course, LetterGrades> courseGrades = getCourseLetterGradesForStudent(studentId, courses);

            return new Student(
                    resultSet.getInt("student_id"),
                    resultSet.getString("student_name"),
                    courses,
                    resultSet.getDouble("gpa"),
                    resultSet.getInt("available_credits"),
                    courseGrades
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
            ArrayList<Course> courses = getCoursesForStudent(resultSet.getInt("student_id"));
            HashMap<Course, LetterGrades> courseGrades = getCourseLetterGradesForStudent(resultSet.getInt("student_id"), courses);

            students.add(new Student(
                    resultSet.getInt("student_id"),
                    resultSet.getString("student_name"),
                    courses,
                    resultSet.getDouble("gpa"),
                    resultSet.getInt("available_credits"),
                    courseGrades
            ));
        }
        return students;
    }

    public void enrollInCourse(int studentId, int courseId) throws SQLException {
        String checkCreditsQuery = "SELECT available_credits FROM Student_Table WHERE student_id = ?";
        ResultSet resultSet = executeQuery(checkCreditsQuery, studentId);

        if (resultSet.next()) {
            int availableCredits = resultSet.getInt("available_credits");

            String courseQuery = "SELECT credits, quota FROM Course_Table WHERE course_id = ?";
            ResultSet courseResultSet = executeQuery(courseQuery, courseId);

            if (courseResultSet.next()) {
                int courseCredits = courseResultSet.getInt("credits");
                int courseQuota = courseResultSet.getInt("quota");

                if (courseQuota <= 0) {
                    throw new SQLException("Course quota is full.");
                }

                if (availableCredits >= courseCredits) {
                    String enrollQuery = "INSERT INTO Enrollment_Table (student_id, course_id) VALUES (?, ?)";
                    executeUpdate(enrollQuery, studentId, courseId);

                    String updateCreditsQuery = "UPDATE Student_Table SET available_credits = available_credits - ? WHERE student_id = ?";
                    executeUpdate(updateCreditsQuery, courseCredits, studentId);

                    String updateQuotaQuery = "UPDATE Course_Table SET quota = quota - 1 WHERE course_id = ?";
                    executeUpdate(updateQuotaQuery, courseId);
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

            String updateQuotaQuery = "UPDATE Course_Table SET quota = quota + 1 WHERE course_id = ?";
            executeUpdate(updateQuotaQuery, courseId);
        }
    }

    public ArrayList<Course> getPrerequisites(int courseId) throws SQLException {
        String query = """
        SELECT c.course_id, c.course_name, c.quota, c.credits, c.start_time, c.end_time, c.course_day, c.syllabus
        FROM Prerequisite_Table p
        INNER JOIN Course_Table c ON p.prerequisite_course_id = c.course_id
        WHERE p.course_id = ?
    """;
        ResultSet resultSet = executeQuery(query, courseId);
        ArrayList<Course> prerequisites = new ArrayList<>();

        while (resultSet.next()) {
            prerequisites.add(new Course(
                    resultSet.getInt("course_id"),
                    resultSet.getString("course_name"),
                    resultSet.getInt("quota"),
                    resultSet.getInt("credits"),
                    resultSet.getTime("start_time").toLocalTime(),
                    resultSet.getTime("end_time").toLocalTime(),
                    WeekDays.valueOf(resultSet.getString("course_day").toUpperCase()),
                    resultSet.getString("syllabus"),
                    new ArrayList<>()
            ));
        }
        return prerequisites;
    }

    public ArrayList<Course> getCoursesForStudent(int studentId) throws SQLException {
        String query = """
        SELECT c.course_id, c.course_name, c.quota, c.credits, c.start_time, c.end_time, s.day_of_week, c.syllabus
        FROM Course_Table c
        INNER JOIN Enrollment_Table e ON c.course_id = e.course_id
        INNER JOIN Schedule_Table s ON c.course_id = s.course_id
        WHERE e.student_id = ?
    """;
        ResultSet resultSet = executeQuery(query, studentId);
        ArrayList<Course> courses = new ArrayList<>();

        while (resultSet.next()) {
            // Her ders için prerequisite'leri doldur
            ArrayList<Course> prerequisites = getPrerequisites(resultSet.getInt("course_id"));

            courses.add(new Course(
                    resultSet.getInt("course_id"),
                    resultSet.getString("course_name"),
                    resultSet.getInt("quota"),
                    resultSet.getInt("credits"),
                    resultSet.getTime("start_time").toLocalTime(),
                    resultSet.getTime("end_time").toLocalTime(),
                    WeekDays.valueOf(resultSet.getString("day_of_week").toUpperCase()),
                    resultSet.getString("syllabus"),
                    prerequisites
            ));
        }
        return courses;
    }

    public HashMap<Course, LetterGrades> getCourseLetterGradesForStudent(int studentId, ArrayList<Course> courses) throws SQLException {
        String query = "SELECT course_id, grade FROM Enrollment_Table WHERE student_id = ?";
        ResultSet resultSet = executeQuery(query, studentId);
        HashMap<Course, LetterGrades> courseGrades = new HashMap<>();

        while (resultSet.next()) {
            int courseId = resultSet.getInt("course_id");
            String grade = resultSet.getString("grade");

            for (Course course : courses) {
                if (course.getCourseId() == courseId) {
                    courseGrades.put(course, grade != null ? LetterGrades.valueOf(grade) : null);
                    break;
                }
            }
        }
        return courseGrades;
    }

    public Student verifyStudentCredentials(String username, String password) throws SQLException {
        String query = "SELECT student_id FROM Student_Table WHERE student_name = ? AND password = ?";
        ResultSet resultSet = executeQuery(query, username, password);
        if (resultSet.next()) {
            int id = resultSet.getInt("student_id");
            return getStudentById(id);
        }
        return null;
    }

}
