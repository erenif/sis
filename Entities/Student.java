package Entities;

import java.util.ArrayList;

public class Student {
    private int studentId;
    private String studentName;
    private ArrayList<Course> courseList;
    private double gpa;
    private int availableCredits;

    public Student() {
        this.courseList = new ArrayList<>();
    }

    public Student(int studentId, String studentName, ArrayList<Course> courseList, double gpa, int availableCredits) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.courseList = courseList != null ? courseList : new ArrayList<>();
        this.gpa = gpa;
        this.availableCredits = availableCredits;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public ArrayList<Course> getCourseList() {
        return courseList;
    }

    public void setCourseList(ArrayList<Course> courseList) {
        this.courseList = courseList;
    }

    public double getGpa() {
        return gpa;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

    public int getAvailableCredits() {
        return availableCredits;
    }

    public void setAvailableCredits(int availableCredits) {
        this.availableCredits = availableCredits;
    }

    public void addCourseLocally(Course course) {
        if (course != null) {
            courseList.add(course);
        }
    }

    public void dropCourseLocally(Course course) {
        if (course != null) {
            courseList.remove(course);
        }
    }
}



