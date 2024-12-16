package Entities;

import Entities.Enums.WeekDays;

import java.time.LocalTime;
import java.util.ArrayList;

public class Professor extends User {
    private ArrayList<Course> coursesTaught;

    public Professor(int userID, String userName) {
        super(userID,userName);
        this.coursesTaught = new ArrayList<>();
    }

    public Professor(int userId, String username, ArrayList<Course> courseList){
        super(userId, username);
        this.coursesTaught = courseList;
    }

    public ArrayList<Course> getCoursesTaught() {
        return coursesTaught;
    }

    public void setCourseList(ArrayList<Course> coursesTaught) {
        this.coursesTaught = coursesTaught;
    }

    public void createCourse(int courseId, String courseName, int quota, int credits, LocalTime startTime, LocalTime endTime, WeekDays day, String syllabus) {
        Course newCourse = new Course(courseId, courseName, quota, credits, startTime, endTime, day, syllabus);
        coursesTaught.add(newCourse);
        System.out.println("Course " + courseName + " created successfully by Professor " + getUserName());
    }

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
    public boolean updateCourse(int courseId, String courseName, int quota, int credits, LocalTime starTime, LocalTime endTime, WeekDays day, String syllabus) {
        for (Course course : coursesTaught) {
            if (course.getCourseId() == courseId) {
                course.setCourseId(courseId);
                course.setCourseName(courseName);
                course.setCredits(credits);
                course.setQuota(quota);
                course.setSyllabus(syllabus);
                course.setCourse_day(day);
                course.setStartTime(starTime);
                course.setEndTime(endTime);
                System.out.println("Course " + courseName + " updated successfully");
                return true;
            }
        }
        System.out.println("Course with ID " + courseId + " not found.");
        return false;
    }
    public boolean enterGrade(int courseId, int studentId, String grade) {
        for (Course course : coursesTaught) {
            if (course.getCourseId() == courseId) {
                course.assignGrade(studentId, grade);
                //Implement here the Student class's completeCourse method.
                return true;
            }
        }
        return false;
    }
}

