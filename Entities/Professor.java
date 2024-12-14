package Entities;

import java.util.List;

public class Professor {
    private int professorId;
    private String professorName;
    private List<Course> courseList;

    public Professor() {
    }

    public Professor(int professorId, String professorName, List<Course> courseList) {
        this.professorId = professorId;
        this.professorName = professorName;
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
}

