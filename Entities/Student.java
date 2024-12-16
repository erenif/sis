package Entities;

import java.util.ArrayList;
import java.util.HashMap;

public class Student extends User{
    private ArrayList<Course> coursesEnrolled;
    private ArrayList<Course> completedCourses;
    private double gpa;
    private HashMap<String, ArrayList<Course>> gpaBySemester;
    private int availableCredits;
    private HashMap<String, HashMap<Integer, Course>> schedule;

    public Student(int userID, String UserName) {
        super(userID, UserName);
        this.coursesEnrolled = new ArrayList<>();
        this.completedCourses = new ArrayList<>();
        this.gpaBySemester = new HashMap<>();
        this.gpa = 0.0;
        this.availableCredits = 30;
        this.schedule = new HashMap<>();

        String[] weekdays = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        for (String weekday : weekdays) {
            schedule.put(weekday, new HashMap<>());
        }
    }

    public Student(int userID, String userName, ArrayList<Course> coursesEnrolled, double gpa, int availableCredits) {
        super(userID, userName);
        this.coursesEnrolled = coursesEnrolled;
        this.completedCourses = new ArrayList<>();
        this.gpaBySemester = new HashMap<>();
        this.gpa = gpa;
        this.availableCredits = availableCredits;
        this.schedule = new HashMap<>();

        String[] weekdays = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        for (String weekday : weekdays) {
            schedule.put(weekday, new HashMap<>());
        }

        for (Course c : coursesEnrolled) {
            updateSchedule(c);
        }
    }

    public ArrayList<Course> getCourseList() {
        return coursesEnrolled;
    }

    public void setCourseList(ArrayList<Course> courseList) {
        this.coursesEnrolled = courseList;
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

    public boolean addCourse(Course course) {
        if (coursesEnrolled.contains(course)) {
            System.out.println("Already enrolled!");
            return false;
        } else if (course.getQuota()<= 0) {
            System.out.println("Quota exceeded!");
            return false;
        } else if (course == null) {
            System.out.println("Course is null!");
            return false;
        } else if (updateSchedule(course) == false) {
            return false;
        } else {
            coursesEnrolled.add(course);
            updateSchedule(course);
            course.enrollStudent(getUserID());
            System.out.println("Course successfully added!");
            return true;
        }
    }

    public void dropCourse(Course course) {
        if (course != null) {
            coursesEnrolled.remove(course);
            course.dropStudent(getUserID());
            System.out.println("Course successfully dropped!");
        }
    }

    public boolean updateSchedule(Course course) {
        String schedule = course.getSchedule(); // Schedule format: Wednesday, 11.00-12.00
        String[] parts = schedule.split(", ");
        String day = parts[0];
        String[] time = parts[1].split("-");
        int startHour = Integer.parseInt(time[0].split("\\.")[0]);
        int endHour = Integer.parseInt(time[1].split("\\.")[0]);

        HashMap<Integer, Course> daySchedule = this.schedule.get(day);
        for (int hour = startHour; hour < endHour; hour++) {
            if (daySchedule.containsKey(hour)) {
                System.out.println(course.getCourseName() + " overlaps with " + daySchedule.get(hour).getCourseName());
                return false;
            }
        }
        for (int hour = startHour; hour < endHour; hour++) {
            daySchedule.put(hour, course);
        }
        System.out.println(course.getCourseName() + " successfully added into schedule.");
        return true;
    }

    public boolean completeCourse(Course course, String grade) {
        if (!grade.equalsIgnoreCase("F")) {
            completedCourses.add(course);
            return true;
        } else {
            return false;
        }
    }

    public String viewLetterGrade(Course course) {

        return null;
    }

    public double viewGPA(){

        return 0.0;
    }



}



