package View.UserPanel;

import Model.Student;
import javax.swing.*;
import java.awt.*;

public class StudentPanel extends JFrame {
    private Student student;

    public StudentPanel(Student student) {
        this.student = student;
        setTitle("Student Panel - " + student.getUserName());
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Welcome, " + student.getUserName() + "!", SwingConstants.CENTER);
        add(welcomeLabel, BorderLayout.CENTER);

        // Add student-specific UI

        setLocationRelativeTo(null);
        setVisible(true);
    }
}

