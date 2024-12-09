package Entities;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class Course {
    private int courseId;
    private String courseName;
    private int quota;
    private LocalTime start_time;
    private LocalTime end_time;
    private String syllabus;
    private String day;

    public Course() {
    }

    public Course(int courseId, String courseName, int quota, String day,
                  LocalTime start_time, LocalTime end_time, String syllabus) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.quota = quota;
        this.start_time = start_time;
        this.end_time = end_time;
        this.syllabus = syllabus;
        this.day = day;
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

    public LocalTime getStart_time() {
        return start_time;
    }

    public void setStart_time(LocalTime start_time) {
        this.start_time = start_time;
    }

    public LocalTime getEnd_time() {
        return end_time;
    }

    public void setEnd_time(LocalTime end_time) {
        this.end_time = end_time;
    }

    public String getSyllabus() {
        return syllabus;
    }

    public void setSyllabus(String syllabus) {
        this.syllabus = syllabus;
    }
}
