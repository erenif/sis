package View;

import DAOs.CourseDAO;
import DAOs.ProfessorDAO;
import DAOs.StudentDAO;
import Entities.Professor;
import Entities.Student;
import View.UserPanel.ProfessorPanel;
import View.UserPanel.StudentPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;

public class LoginPanel extends JPanel {
    private JFrame parentFrame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleSelector;

    // DAO OBJECTS
    private StudentDAO studentDAO;
    private ProfessorDAO professorDAO;
    private CourseDAO courseDAO;

    public LoginPanel(JFrame parentFrame, Connection connection) {
        this.parentFrame = parentFrame;

        // DAO OBJECTS-CONNECTION
        this.studentDAO = new StudentDAO(connection);
        this.professorDAO = new ProfessorDAO(connection);
        this.courseDAO = new CourseDAO(connection);

        setLayout(new GridBagLayout());

        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
        userPanel.setBackground(new Color(255, 255, 255, 150));
        userPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // LOGO PANEL
        JLabel logoLabel = new JLabel("UNIVERSITY COURSE SELECTION SYSTEM", SwingConstants.CENTER);
        logoLabel.setFont(new Font("Arial", Font.BOLD, 20));
        logoLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        userPanel.add(logoLabel);

        // ROLE SELECTOR
        JPanel rolePanel = new JPanel(new FlowLayout());
        JLabel roleLabel = new JLabel("Select Role:");
        roleSelector = new JComboBox<>(new String[]{"Admin", "Professor", "Student"});
        rolePanel.add(roleLabel);
        rolePanel.add(roleSelector);
        rolePanel.setOpaque(false);
        userPanel.add(rolePanel);

        // USER DATA
        JPanel userDataPanel = new JPanel();
        userDataPanel.setLayout(new GridLayout(2, 2, 5, 5));
        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        userDataPanel.add(usernameLabel);
        userDataPanel.add(usernameField);
        userDataPanel.add(passwordLabel);
        userDataPanel.add(passwordField);
        userDataPanel.setOpaque(false);
        userDataPanel.setBorder(new EmptyBorder(10, 10, 20, 10));
        userPanel.add(userDataPanel);

        // ENTER BUTTON
        JButton enterButton = new JButton("Login");
        enterButton.setPreferredSize(new Dimension(180, 40));
        enterButton.setBackground(new Color(255, 255, 255));
        enterButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        userPanel.add(enterButton);

        // ADD USER PANEL TO FRAME USING GRIDBAG LAYOUT
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        add(userPanel, gbc);

        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final String username = usernameField.getText().trim();
                final String password = new String(passwordField.getPassword());
                final String role = (String) roleSelector.getSelectedItem();

                try {
                    if (role.equals("Admin")) {
                        // Varsayılan admin girişi
                        if ("admin".equals(username) && "admin123".equals(password)) {
                            openAdminPanel();
                        } else {
                            JOptionPane.showMessageDialog(null, "Login failed. Invalid admin credentials.");
                        }
                    } else if (role.equals("Professor")) {
                        // PROFESSOR USERNAME PASSWORD CONTROL
                        Professor professor = verifyProfessorCredentials(username, password);
                        if (professor != null) {
                            openProfessorPanel(professor);
                        } else {
                            JOptionPane.showMessageDialog(null, "Login failed. Invalid professor credentials.");
                        }
                    } else if (role.equals("Student")) {
                        // STUDENT USERNAME PASSWORD CONTROL
                        Student student = verifyStudentCredentials(username, password);
                        if (student != null) {
                            openStudentPanel(student);
                        } else {
                            JOptionPane.showMessageDialog(null, "Login failed. Invalid student credentials.");
                        }
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
                }
            }
        });
    }

    private Professor verifyProfessorCredentials(String username, String password) throws SQLException {
        String query = "SELECT professor_id, professor_name FROM Professor_Table WHERE professor_name = ? AND password = ?";
        var rs = professorDAO.executeQuery(query, username, password);
        if (rs.next()) {
            int id = rs.getInt("professor_id");
            String name = rs.getString("professor_name");
            return new Professor(id, name, professorDAO.getCoursesByProfessor(id));
        }
        return null;
    }

    private Student verifyStudentCredentials(String username, String password) throws SQLException {
        String query = "SELECT student_id FROM Student_Table WHERE student_name = ? AND password = ?";
        var rs = studentDAO.executeQuery(query, username, password);
        if (rs.next()) {
            int id = rs.getInt("student_id");
            return studentDAO.getStudentById(id);
        }
        return null;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(240, 240, 240));
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    private void openAdminPanel() {
        parentFrame.dispose();
        new AdminPanel();
    }

    private void openProfessorPanel(Professor professor) {
        parentFrame.dispose();
        new ProfessorPanel(professor);
    }

    private void openStudentPanel(Student student) {
        parentFrame.dispose();
        new StudentPanel(student, studentDAO, courseDAO);
    }
}
