package DAOs;

import Entities.Course;
import Entities.Enum.LetterGrades;
import Entities.Enum.WeekDays;
import Entities.Student;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO extends AbstractDB {

    public CourseDAO(Connection connection) {
        super(connection);
    }

    // 1. Belirli ID'ye sahip dersi getirir
    public Course getCourseById(int courseId) throws SQLException {
        String query = "SELECT * FROM Course_Table WHERE course_id = ?";
        ResultSet resultSet = executeQuery(query, courseId);

        if (resultSet.next()) {
            return mapCourse(resultSet, getPrerequisites(courseId));
        }
        return null;
    }

    // 2. Yeni bir dersi ekler
    public void addCourse(Course course) {
        String query = "INSERT INTO Course_Table (course_id, course_name, quota, credits, start_time, end_time, course_day, syllabus) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            executeUpdate(query, course.getCourseId(), course.getCourseName(), course.getQuota(), course.getCredits(),
                    course.getStartTime(), course.getEndTime(), course.getCourse_day().toString(), course.getSyllabus());

            addPrerequisites(course.getCourseId(), course.getPrerequisiteCourse());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 3. Bir dersi günceller
    public void updateCourse(Course course) throws SQLException {
        String query = "UPDATE Course_Table SET course_name = ?, quota = ?, credits = ?, start_time = ?, end_time = ?, " +
                "course_day = ?, syllabus = ? WHERE course_id = ?";
        executeUpdate(query, course.getCourseName(), course.getQuota(), course.getCredits(),
                course.getStartTime(), course.getEndTime(), course.getCourse_day().toString(),
                course.getSyllabus(), course.getCourseId());

        // Ön koşul derslerini güncelle
        deletePrerequisites(course.getCourseId());
        addPrerequisites(course.getCourseId(), course.getPrerequisiteCourse());
    }

    // 4. Bir dersi siler
    public void deleteCourse(int courseId) throws SQLException {
        deletePrerequisites(courseId); // Ön koşulları sil
        String query = "DELETE FROM Course_Table WHERE course_id = ?";
        executeUpdate(query, courseId);
    }

    // 5. Tüm dersleri getirir
    public List<Course> getAllCourses() throws SQLException {
        String query = "SELECT * FROM Course_Table";
        ResultSet resultSet = executeQuery(query);

        List<Course> courses = new ArrayList<>();
        while (resultSet.next()) {
            try {
                courses.add(mapCourse(resultSet, getPrerequisites(resultSet.getInt("course_id"))));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return courses;
    }


    // 6. Belirli bir gün verilen dersleri getirir
    public List<Course> getCoursesByDay(WeekDays day) throws SQLException {
        String query = "SELECT * FROM Course_Table WHERE course_day = ?";
        ResultSet resultSet = executeQuery(query, day.toString());

        List<Course> courses = new ArrayList<>();
        while (resultSet.next()) {
            courses.add(mapCourse(resultSet, getPrerequisites(resultSet.getInt("course_id"))));
        }
        return courses;
    }

    // 7. Bir dersin ön koşul derslerini getirir
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
            prerequisites.add(mapCourse(resultSet, new ArrayList<>()));
        }
        return prerequisites;
    }

    // 8. Ön koşul derslerini ekler
    private void addPrerequisites(int courseId, ArrayList<Course> prerequisites) throws SQLException {
        if (prerequisites == null) return;

        String query = "INSERT INTO Prerequisite_Table (course_id, prerequisite_course_id) VALUES (?, ?)";
        for (Course prerequisite : prerequisites) {
            executeUpdate(query, courseId, prerequisite.getCourseId());
        }
    }

    // 9. Ön koşul derslerini siler
    private void deletePrerequisites(int courseId) throws SQLException {
        String query = "DELETE FROM Prerequisite_Table WHERE course_id = ?";
        executeUpdate(query, courseId);
    }

    // 10. ResultSet'ten Course nesnesi oluşturur
    private Course mapCourse(ResultSet resultSet, ArrayList<Course> prerequisites) throws SQLException {
        return new Course(
                resultSet.getInt("course_id"),
                resultSet.getString("course_name"),
                resultSet.getInt("quota"),
                resultSet.getInt("credits"),
                resultSet.getTime("start_time").toLocalTime(),
                resultSet.getTime("end_time").toLocalTime(),
                WeekDays.valueOf(resultSet.getString("course_day")),
                resultSet.getString("syllabus"),
                prerequisites
        );
    }
}