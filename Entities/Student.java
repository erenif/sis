package Entities;

import Entities.Enum.LetterGrades;

import java.util.ArrayList;
import java.util.HashMap;

public class Student extends User{
    private ArrayList<Course> coursesEnrolled;
    private double gpa;
    private int availableCredits;
    private HashMap<Course, LetterGrades> courseLetterGradesHashMap;

    public Student(int userID, String UserName) {
        super(userID, UserName);
        this.coursesEnrolled = new ArrayList<>();
        this.courseLetterGradesHashMap = new HashMap<>();
        this.gpa = 0.0;
        this.availableCredits = 30;
    }

    public Student(int userID, String userName, ArrayList<Course> coursesEnrolled, double gpa,
                   int availableCredits, HashMap<Course, LetterGrades> courseLetterGradesHashMap) {
        super(userID, userName);
        this.coursesEnrolled = coursesEnrolled;
        this.gpa = gpa;
        this.availableCredits = availableCredits;
        this.courseLetterGradesHashMap = courseLetterGradesHashMap;
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
        if (course == null) {
            System.out.println("Course is null!");
            return false;
        } else if (coursesEnrolled.contains(course)) {
            System.out.println("Already enrolled!");
            return false;
        } else if (course.getQuota()<= 0) {
            System.out.println("Quota exceeded!");
            return false;
        } else {
            coursesEnrolled.add(course);
            course.enrollStudent(getUserID());
            courseLetterGradesHashMap.put(course, null);
            setAvailableCredits(getAvailableCredits() - course.getCredits());
            course.setQuota(course.getQuota() - 1);
            System.out.println("Course successfully added!");
            return true;
        }
    }

    public void dropCourse(Course course) {
        if (course != null && coursesEnrolled.contains(course)) {
            coursesEnrolled.remove(course);
            setAvailableCredits(getAvailableCredits() + course.getCredits());
            courseLetterGradesHashMap.remove(course);
            System.out.println("Course successfully dropped!");
        } else {
            System.out.println("Student has not enrolled into the specified course");
        }
    }

    public LetterGrades viewLetterGrade(Course course) {
        return courseLetterGradesHashMap.get(course);
    }

    public double viewGPA(){
        double totalGpa = 0.0;
        int totalCreditsTaken = 0;
        for(Course course: coursesEnrolled){
            LetterGrades letterGrade = courseLetterGradesHashMap.get(course);
            totalCreditsTaken += course.getCredits();
            if (letterGrade == null) {
                totalGpa += 0;
            } else {
                switch (letterGrade){
                    case A -> totalGpa += 4.0;
                    case A_MINUS -> totalGpa += 3.6;
                    case B_PLUS -> totalGpa += 3.3;
                    case B -> totalGpa += 3.0;
                    case B_MINUS -> totalGpa += 2.7;
                    case C_PLUS -> totalGpa += 2.3;
                    case C -> totalGpa += 2.0;
                    case C_MINUS -> totalGpa += 1.7;
                    case D_PLUS -> totalGpa += 1.3;
                    case D -> totalGpa += 1.0;
                    case F -> totalGpa += 0;
                }
            }
        }
        if (totalCreditsTaken == 0) return 0.0;
        return totalGpa / totalCreditsTaken;
    }
}
