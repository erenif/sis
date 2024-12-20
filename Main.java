import DAOs.CourseDAO;
import DAOs.StudentDAO;
import Entities.Student;
import Utils.DatabaseConnection;
import View.UserPanel.StudentPanel;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        try (Connection connection = DatabaseConnection.getDataSource().getConnection()) {
            // DAO'ları oluştur
            StudentDAO studentDAO = new StudentDAO(connection);
            CourseDAO courseDAO = new CourseDAO(connection);

            // Test için bir öğrenci oluştur (Normalde LoginPanel'den gelmeli)
            Student student = studentDAO.getStudentById(101); // Mevcut bir öğrenci ID'si
            if (student != null) {
                // StudentPanel'i başlat
                new StudentPanel(student, studentDAO, courseDAO);
            } else {
                System.out.println("Student not found!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}