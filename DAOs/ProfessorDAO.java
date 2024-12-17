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

    public ArrayList<Course> getCoursesByProfessor(int professorId) throws SQLException {
        String query = """
            SELECT c.course_id, c.course_name, c.quota, c.start_time, c.end_time, c.syllabus, c.day_of_week
            FROM Course_Table c
            INNER JOIN Teaching_Table t ON c.course_id = t.course_id
            WHERE t.professor_id = ?
        """;
        ResultSet resultSet = executeQuery(query, professorId);

        ArrayList<Course> courses = new ArrayList<>();
        while (resultSet.next()) {
            courses.add(new Course(
                    resultSet.getInt("course_id"),
                    resultSet.getString("course_name"),
                    resultSet.getInt("quota"),
                    resultSet.getInt("credits"),
                    resultSet.getTime("start_time").toLocalTime(),
                    resultSet.getTime("end_time").toLocalTime(),
                    WeekDays.valueOf(resultSet.getString("course_day")),
                    resultSet.getString("syllabus")
            ));
        }
        return courses;
    }
}