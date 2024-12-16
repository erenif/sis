package View.UserPanel;

import Model.Professor;
import javax.swing.*;
import java.awt.*;

public class ProfessorPanel extends JFrame {
    private Professor professor;

    public ProfessorPanel(Professor professor) {
        this.professor = professor;
        setTitle("Professor Panel - " + professor.getUserName());
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Welcome, Professor " + professor.getUserName() + "!", SwingConstants.CENTER);
        add(welcomeLabel, BorderLayout.CENTER);

        // Add professor-specific UI

        setLocationRelativeTo(null);
        setVisible(true);
    }
}

