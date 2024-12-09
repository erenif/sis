package Entities;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class Professor {
    private int professorId;
    private String professorName;
    private List<Course> courseList;

    public Professor() {
    }

    public Professor(String professorName, int professorId, List<Course> courseList) {
        this.professorName = professorName;
        this.professorId = professorId;
        this.courseList = courseList;
    }

    public int getProfessorId() {
        return professorId;
    }

    public void setProfessorId(int professorId) {
        this.professorId = professorId;
    }

    public String getProfessorName() {
        return professorName;
    }

    public void setProfessorName(String professorName) {
        this.professorName = professorName;
    }

    public List<Course> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<Course> courseList) {
        this.courseList = courseList;
    }

    public void createCourse(int courseId, String courseName, int quota, int credit, String syllabus, LocalTime startTime, LocalTime endTime, String day) {
        Course course = new Course(courseId,courseName,quota,day,startTime,endTime,syllabus);
        courseList.add(courseId,course);
    }

    public void deleteCourse(int courseId) {
        courseList.remove(courseId);
    }

    public void updateCourse(int courseId, String courseName, int quota, int credit, String syllabus, LocalTime startTime, LocalTime endTime, String day) {
        Course course = courseList.get(courseId);
        course = new Course(courseId,courseName,quota,day,startTime,endTime,syllabus);
    }


}
