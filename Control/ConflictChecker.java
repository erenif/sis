package Control;

import Entities.Course;
import Entities.Student;

import java.time.LocalTime;
import java.util.ArrayList;

//This class will act for potential schedule conflicts for
public class ConflictChecker {
    public boolean isTimeConflictStudent(Course newCourse, Student student){
        LocalTime course_start = newCourse.getStartTime();
        LocalTime end_time = newCourse.getEndTime();
        ArrayList<Course> enrolled_courses = student.getCourseList();
        for(Course course : enrolled_courses){
            // 1: Kayıt olmak istenen kursun başlangıç saati, varolan kursun başlangıcından sonra, bitişinden önce olması durumu
            if(newCourse.getStartTime().isBefore(course.getEndTime()) && newCourse.getStartTime().isAfter(course.getStartTime())){
                return false;
            } //2. Varolan kursun başlangıç saati, yeni kursun başlangıcından sonra, bitişinden önce olması durumu
            else if (course.getStartTime().isBefore(newCourse.getEndTime()) && course.getStartTime().isAfter(newCourse.getStartTime())) {
                return false;
            } //3. Herhangi iki kursun başlangıç veya bitiş saatlerinin çakışması durumu
            else if (course.getStartTime() == newCourse.getStartTime() || course.getEndTime() == course.getEndTime()) {
                return false;
            }
        }
        return true;
    }
}
