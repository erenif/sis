package View;

import Model.Admin;
import Model.Professor;
import Model.Student;
import View.UserPanel.ProfessorPanel;
import View.UserPanel.StudentPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPanel extends JPanel {
    private JFrame parentFrame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleSelector;

    public LoginPanel(JFrame parentFrame) {
        this.parentFrame = parentFrame;
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
        // Now we provide 3 roles: Admin, Professor, Student
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
        gbc.insets = new Insets(10, 10, 10, 10); // Margins around the panel
        gbc.anchor = GridBagConstraints.CENTER;
        add(userPanel, gbc);

        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword());
                String role = (String) roleSelector.getSelectedItem();

                // For demonstration, we check hardcoded credentials.
                // Replace this logic with actual DAO calls and object retrieval.
                if (role.equals("Admin")) {
                    // Hardcoded admin credentials: admin / admin123
                    if ("admin".equals(username) && "admin123".equals(password)) {
                        openAdminPanel();
                    } else {
                        JOptionPane.showMessageDialog(null, "Login failed. Invalid admin credentials.");
                    }
                } else if (role.equals("Professor")) {
                    // Hardcoded professor credentials: prof / prof123
                    if ("prof".equals(username) && "prof123".equals(password)) {
                        Professor professor = new Professor(2, "Professor User");
                        openProfessorPanel(professor);
                    } else {
                        JOptionPane.showMessageDialog(null, "Login failed. Invalid professor credentials.");
                    }
                } else if (role.equals("Student")) {
                    // Hardcoded student credentials: stud / stud123
                    if ("stud".equals(username) && "stud123".equals(password)) {
                        Student student = new Student(3, "Student User");
                        openStudentPanel(student);
                    } else {
                        JOptionPane.showMessageDialog(null, "Login failed. Invalid student credentials.");
                    }
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw background color
        g.setColor(new Color(240, 240, 240));
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    // Open admin panel
    private void openAdminPanel() {
        parentFrame.dispose(); // Close the login frame
        new AdminPanel();
    }

    // Open professor panel with a Professor object
    private void openProfessorPanel(Professor professor) {
        parentFrame.dispose();
        new ProfessorPanel(professor);
    }

    // Open student panel with a Student object
    private void openStudentPanel(Student student) {
        parentFrame.dispose();
        new StudentPanel(student);
    }
}



