package Entities;
import Entities.Enums.WeekDays;

import java.time.LocalTime;

public class Course {
    private int courseId;
    private String courseName;
    private int quota;
    private int credits;
    private LocalTime startTime;
    private LocalTime endTime;
    private WeekDays course_day;
    private String syllabus;


    public Course(int courseId, String courseName, int quota, int credits,
                  LocalTime startTime, LocalTime endTime, WeekDays course_day,
                  String syllabus) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.quota = quota;
        this.credits = credits;
        this.startTime = startTime;
        this.endTime = endTime;
        this.course_day = course_day;
        this.syllabus = syllabus;
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

    public WeekDays getCourse_day() {
        return course_day;
    }

    public void setCourse_day(WeekDays course_day) {
        this.course_day = course_day;
    }

    public String getSyllabus() {
        return syllabus;
    }

    public void setSyllabus(String syllabus) {
        this.syllabus = syllabus;
    }

    public boolean enrollStudent(int studentId) {
        if (quota > 0) {
            quota--;
            System.out.println("Student with ID " + studentId + " successfully enrolled.");
            return true;
        } else {
            System.out.println("Enrollment failed. Quota is full.");
            return false;
        }
    }
}
