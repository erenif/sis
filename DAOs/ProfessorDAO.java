package DAOs;

import Entities.Enum.WeekDays;
import Entities.Professor;
import Entities.Course;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProfessorDAO extends DAOs.AbstractDB {

    public ProfessorDAO(Connection connection) {
        super(connection);
    }

    public Professor getProfessorById(int professorId) throws SQLException {
        String query = "SELECT * FROM Professor_Table WHERE professor_id = ?";
        ResultSet resultSet = executeQuery(query, professorId);

        if (resultSet.next()) {
            return new Professor(
                    resultSet.getInt("professor_id"),
                    resultSet.getString("professor_name"),
                    getCoursesByProfessor(professorId)
            );
        }
        return null;
    }

    public void addProfessor(Professor professor) throws SQLException {
        String query = "INSERT INTO Professor_Table (professor_id, professor_name) VALUES (?, ?)";
        executeUpdate(query, professor.getUserID(), professor.getUserName());
    }

    public void updateProfessor(Professor professor) throws SQLException {
        String query = "UPDATE Professor_Table SET professor_name = ? WHERE professor_id = ?";
        executeUpdate(query, professor.getUserName(), professor.getUserID());
    }

    public void deleteProfessor(int professorId) throws SQLException {
        String query = "DELETE FROM Professor_Table WHERE professor_id = ?";
        executeUpdate(query, professorId);
    }

    public List<Professor> getAllProfessors() throws SQLException {
        String query = "SELECT * FROM Professor_Table";
        ResultSet resultSet = executeQuery(query);

        List<Professor> professors = new ArrayList<>();
        while (resultSet.next()) {
            professors.add(new Professor(
                    resultSet.getInt("professor_id"),
                    resultSet.getString("professor_name"),
                    getCoursesByProfessor(resultSet.getInt("professor_id"))
            ));
        }
        return professors;
    }

    public void assignCourseToProfessor(int professorId, int courseId) throws SQLException {
        String query = "INSERT INTO Teaching_Table (professor_id, course_id) VALUES (?, ?)";
        executeUpdate(query, professorId, courseId);
    }

    public void unassignCourseFromProfessor(int courseId) throws SQLException {
        String query = "DELETE FROM Teaching_Table WHERE course_id = ?";
        executeUpdate(query, courseId);
    }

    private ArrayList<Course> getPrerequisites(int courseId) throws SQLException {
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

    public ArrayList<Course> getCoursesByProfessor(int professorId) throws SQLException {
        String query = """
        SELECT c.course_id, c.course_name, c.quota, c.credits, c.start_time, c.end_time, c.course_day, c.syllabus
        FROM Course_Table c
        INNER JOIN Teaching_Table t ON c.course_id = t.course_id
        WHERE t.professor_id = ?
    """;
        ResultSet resultSet = executeQuery(query, professorId);

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
                    WeekDays.valueOf(resultSet.getString("course_day").toUpperCase()),
                    resultSet.getString("syllabus"),
                    prerequisites
            ));
        }
        return courses;
    }

    public Professor verifyProfessorCredentials(String username, String password) throws SQLException {
        String query = "SELECT professor_id, professor_name FROM Professor_Table WHERE professor_name = ? AND password = ?";
        ResultSet resultSet = executeQuery(query, username, password);
        if (resultSet.next()) {
            int id = resultSet.getInt("professor_id");
            String name = resultSet.getString("professor_name");
            return new Professor(id, name, getCoursesByProfessor(id));
        }
        return null;
    }
}