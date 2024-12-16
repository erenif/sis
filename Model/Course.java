package Model;
import java.util.HashMap;
import java.util.List;

public class Course {
    private int courseId;
    private String courseName;
    private int quota;
    private int credits;
    private String schedule;
    private String syllabus;
    private HashMap<Integer, String> studentGrades;
    private List<Course> prerequisites;
    ////private


    public Course() {
    }

    public Course(int courseId, String courseName, int quota, int credits,  String schedule, String syllabus) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.quota = quota;
        this.credits = credits;
        this.schedule = schedule;
        this.syllabus = syllabus;
        this.studentGrades = new HashMap<>();
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

    public String getSchedule() {return schedule;}

    public void setSchedule(String schedule) {this.schedule = schedule;}

    public String getSyllabus() {
        return syllabus;
    }

    public void setSyllabus(String syllabus) {
        this.syllabus = syllabus;
    }

    public List<Course> getPrerequisites() {
        return prerequisites;
    }

    public void addPrerequisite(Course course) {
        prerequisites.add(course);
    }

    public boolean hasPrerequisite(Course course) {
        return prerequisites.contains(course);
    }

    public void removePrerequisite(Course course) {
        prerequisites.remove(course);
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

    public boolean dropStudent(int studentId) {
        if (studentGrades.containsKey(studentId)) {
            quota++;
            studentGrades.remove(studentId); // Remove any grade entry for the student
            System.out.println("Student with ID " + studentId + " successfully dropped.");
            return true;
        } else {
            System.out.println("Student with ID " + studentId + " is not enrolled in " + courseName);
            return false;
        }
    }

    public void assignGrade(int studentId, String letterGrade) {
        if (studentGrades.containsKey(studentId)) {
            studentGrades.put(studentId, letterGrade);
            System.out.println("Grade " + letterGrade + " assigned to student with ID " + studentId + " for " + courseName);
        } else {
            System.out.println("Student with ID " + studentId + " is not enrolled in " + courseName);
        }
    }

}
