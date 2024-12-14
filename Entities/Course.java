package Entities;

import java.time.LocalTime;

public class Course {
    private int courseId;
    private String courseName;
    private int quota;
    private int credits;
    private LocalTime startTime;
    private LocalTime endTime;
    private String syllabus;
    private String day;

    public Course() {
    }

    public Course(int courseId, String courseName, int quota, String day,
                  LocalTime startTime, LocalTime endTime, String syllabus) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.quota = quota;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.syllabus = syllabus;
    }

    public Course(int courseId, String courseName, int quota, String day,
                  LocalTime startTime, LocalTime endTime, String syllabus, int credits) {
        this(courseId, courseName, quota, day, startTime, endTime, syllabus);
        this.credits = credits;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getQuota() {
        return quota;
    }

    public void setQuota(int quota) {
        this.quota = quota;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getSyllabus() {
        return syllabus;
    }

    public void setSyllabus(String syllabus) {
        this.syllabus = syllabus;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
