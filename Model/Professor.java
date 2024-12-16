package Model;

import java.util.ArrayList;
import java.util.List;

public class Professor extends User {
    private ArrayList<Course> coursesTaught;

    public Professor(int userID, String userName) {
        super(userID,userName);
        this.coursesTaught = new ArrayList<>();
    }

    public Professor(int userID, String userName, List<Course> coursesTaught) {
        super(userID, userName);
        this.coursesTaught = new ArrayList<>(coursesTaught);
    }

    public void setCourseList(List<Course> coursesTaught) {
        this.coursesTaught = new ArrayList<>(coursesTaught);
    }

    public ArrayList<Course> getCoursesTaught() {
        return coursesTaught;
    }

    public void setCourseList(ArrayList<Course> coursesTaught) {
        this.coursesTaught = coursesTaught;
    }

    public void createCourse(int courseId, String courseName, int credits, int quota, String syllabus, String schedule) {
        Course newCourse = new Course(courseId, courseName, quota, credits, schedule, syllabus);
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
    public boolean updateCourse(int courseId, String courseName, int credits, int quota, String syllabus, String schedule) {
        for (Course course : coursesTaught) {
            if (course.getCourseId() == courseId) {
                course.setCourseName(courseName);
                course.setCredits(credits);
                course.setQuota(quota);
                course.setSyllabus(syllabus);
                course.setSchedule(schedule);
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

