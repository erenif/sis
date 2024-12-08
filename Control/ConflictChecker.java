package Control;

import Entities.Course;
import Entities.Student;

import java.time.LocalTime;
import java.util.ArrayList;

//This class will act for potential schedule conflicts for
public class ConflictChecker {
    public boolean isTimeConflictStudent(Course newCourse, Student student){
        LocalTime course_start = newCourse.getStart_time();
        LocalTime end_time = newCourse.getEnd_time();
        ArrayList<Course> enrolled_courses = student.getCourseList();
        for(Course course : enrolled_courses){
            // 1: Kayıt olmak istenen kursun başlangıç saati, varolan kursun başlangıcından sonra, bitişinden önce olması durumu
            if(newCourse.getStart_time().isBefore(course.getEnd_time()) && newCourse.getStart_time().isAfter(course.getStart_time())){
                return false;
            } //2. Varolan kursun başlangıç saati, yeni kursun başlangıcından sonra, bitişinden önce olması durumu
            else if (course.getStart_time().isBefore(newCourse.getEnd_time()) && course.getStart_time().isAfter(newCourse.getStart_time())) {
                return false;
            } //3. Herhangi iki kursun başlangıç veya bitiş saatlerinin çakışması durumu
            else if (course.getStart_time() == newCourse.getStart_time() || course.getEnd_time() == course.getEnd_time()) {
                return false;
            }
        }
        return true;
    }
}
