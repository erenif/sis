package DAOs;

import Model.Course;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO extends DAO.AbstractDB {

    public CourseDAO(Connection connection) {
        super(connection);
    }

    public Course getCourseById(int courseId) throws SQLException {
        String query = "SELECT * FROM Course_Table WHERE course_id = ?";
        ResultSet resultSet = executeQuery(query, courseId);

        if (resultSet.next()) {
            return new Course(
                    resultSet.getInt("course_id"),
                    resultSet.getString("course_name"),
                    resultSet.getInt("quota"),
                    resultSet.getString("day_of_week"),
                    resultSet.getTime("start_time").toLocalTime(),
                    resultSet.getTime("end_time").toLocalTime(),
                    resultSet.getString("syllabus")
            );
        }
        return null;
    }

    public void addCourse(Course course) throws SQLException {
        String query = "INSERT INTO Course_Table (course_id, course_name, quota, start_time, end_time, syllabus, credits) VALUES (?, ?, ?, ?, ?, ?, ?)";
        executeUpdate(query, course.getCourseId(), course.getCourseName(), course.getQuota(),
                course.getStartTime(), course.getEndTime(), course.getSyllabus(), course.getCredits());
    }

    public void updateCourse(Course course) throws SQLException {
        String query = "UPDATE Course_Table SET course_name = ?, quota = ?, start_time = ?, end_time = ?, syllabus = ?, credits = ? WHERE course_id = ?";
        executeUpdate(query, course.getCourseName(), course.getQuota(), course.getStartTime(),
                course.getEndTime(), course.getSyllabus(), course.getCredits(), course.getCourseId());
    }

    public void deleteCourse(int courseId) throws SQLException {
        String query = "DELETE FROM Course_Table WHERE course_id = ?";
        executeUpdate(query, courseId);
    }

    public List<Course> getAllCourses() throws SQLException {
        String query = "SELECT * FROM Course_Table";
        ResultSet resultSet = executeQuery(query);

        List<Course> courses = new ArrayList<>();
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

    public List<Course> getCoursesByDay(String dayOfWeek) throws SQLException {
        String query = "SELECT * FROM Course_Table WHERE day_of_week = ?";
        ResultSet resultSet = executeQuery(query, dayOfWeek);

        List<Course> courses = new ArrayList<>();
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