package Entities;

import Entities.Enum.WeekDays;
import java.time.LocalTime;
import java.util.ArrayList;

public class Professor extends User {
    private ArrayList<Course> coursesTaught;
    private String password;

    // Constructor - Ders Listesi Olmadan
    public Professor(int userID, String userName, String password) {
        super(userID, userName);
        this.password = password;
        this.coursesTaught = new ArrayList<>();
    }

    // Constructor - Ders Listesi Dahil
    public Professor(int userId, String username, String password, ArrayList<Course> courseList) {
        super(userId, username);
        this.password = password;
        this.coursesTaught = courseList;
    }

    // Getter ve Setter'lar
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<Course> getCoursesTaught() {
        return coursesTaught;
    }

    public void setCoursesTaught(ArrayList<Course> coursesTaught) {
        this.coursesTaught = coursesTaught;
    }

    // Yeni Ders Oluşturma
    public void createCourse(int courseId, String courseName, int credits, int quota,
                             LocalTime startTime, LocalTime endTime, WeekDays day, String syllabus) {
        Course newCourse = new Course(courseId, courseName, quota, credits, startTime, endTime, day, syllabus, new ArrayList<>());
        coursesTaught.add(newCourse);
        System.out.println("Course " + courseName + " created successfully by Professor " + getUserName());
    }

    // Ders Silme
    public boolean deleteCourse(int courseId) {
        for (Course course : coursesTaught) {
            if (course.getCourseId() == courseId) {
                coursesTaught.remove(course);
                System.out.println("Course " + course.getCourseName() + " deleted successfully");
                return true;
            }
        }
        System.out.println("Course with ID " + courseId + " not found.");
        return false;
    }

    // Ders Güncelleme
    public boolean updateCourse(int courseId, String courseName, int quota, int credits,
                                LocalTime startTime, LocalTime endTime, WeekDays day, String syllabus) {
        for (Course course : coursesTaught) {
            if (course.getCourseId() == courseId) {
                course.setCourseId(courseId);
                course.setCourseName(courseName);
                course.setCredits(credits);
                course.setQuota(quota);
                course.setSyllabus(syllabus);
                course.setCourse_day(day);
                course.setStartTime(startTime);
                course.setEndTime(endTime);
                System.out.println("Course " + courseName + " updated successfully");
                return true;
            }
        }
        System.out.println("Course with ID " + courseId + " not found.");
        return false;
    }

    // Not Girişi
    public boolean enterGrade(int courseId, int studentId, String grade) {
        // Not girişi DAO üzerinden yapılmalı.
        // Örnek: studentDAO.updateGrade(courseId, studentId, grade);
        System.out.println("Grade entered for student " + studentId + " in course " + courseId);
        return true;
    }

    // Yeni Ekleme: Şifre Doğrulama
    public boolean verifyPassword(String inputPassword) {
        return this.password.equals(inputPassword);
    }

    // Ders Listesini Görüntüleme
    public void viewCoursesTaught() {
        System.out.println("Courses taught by Professor " + getUserName() + ":");
        for (Course course : coursesTaught) {
            System.out.println(course.toString());
        }
    }

    @Override
    public String toString() {
        return "Professor{" +
                "userID=" + getUserID() +
                ", userName='" + getUserName() + '\'' +
                ", coursesTaught=" + coursesTaught.size() +
                '}';
    }
}

