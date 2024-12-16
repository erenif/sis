package View;

import Model.Admin;
import Model.Student;
import Model.Professor;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminPanel extends JFrame {

    // UI components for Students tab
    private JTable studentsTable;
    private DefaultTableModel studentsTableModel;
    private JButton createStudentButton;
    private JButton editStudentButton;
    private JButton deleteStudentButton;

    // UI components for Professors tab
    private JTable professorsTable;
    private DefaultTableModel professorsTableModel;
    private JButton createProfessorButton;
    private JButton editProfessorButton;
    private JButton deleteProfessorButton;

    public AdminPanel() {

        setTitle("Admin Panel");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10,10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBorder(new EmptyBorder(10,10,10,10));
        JLabel welcomeLabel = new JLabel("Welcome, Admin !");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(welcomeLabel);
        add(topPanel, BorderLayout.NORTH);

        // Main content: JTabbedPane with "Students" and "Professors" tabs
        JTabbedPane tabbedPane = new JTabbedPane();

        // Students tab
        JPanel studentsPanel = new JPanel(new BorderLayout(10,10));
        studentsPanel.setBorder(new EmptyBorder(10,10,10,10));
        tabbedPane.addTab("Students", studentsPanel);

        // Professors tab
        JPanel professorsPanel = new JPanel(new BorderLayout(10,10));
        professorsPanel.setBorder(new EmptyBorder(10,10,10,10));
        tabbedPane.addTab("Professors", professorsPanel);

        add(tabbedPane, BorderLayout.CENTER);

        // Setup Students Panel
        JLabel studentsLabel = new JLabel("Manage Students:");
        studentsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        studentsPanel.add(studentsLabel, BorderLayout.NORTH);

        studentsTableModel = new DefaultTableModel(new Object[]{"Student ID", "Student Name", "GPA", "Available Credits"}, 0);
        studentsTable = new JTable(studentsTableModel);
        studentsPanel.add(new JScrollPane(studentsTable), BorderLayout.CENTER);

        // Student buttons
        JPanel studentButtonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,10,10));
        createStudentButton = new JButton("Create Student");
        editStudentButton = new JButton("Edit Student");
        deleteStudentButton = new JButton("Delete Student");
        studentButtonsPanel.add(createStudentButton);
        studentButtonsPanel.add(editStudentButton);
        studentButtonsPanel.add(deleteStudentButton);
        studentsPanel.add(studentButtonsPanel, BorderLayout.SOUTH);

        // Setup Professors Panel
        JLabel professorsLabel = new JLabel("Manage Professors:");
        professorsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        professorsPanel.add(professorsLabel, BorderLayout.NORTH);

        professorsTableModel = new DefaultTableModel(new Object[]{"Professor ID", "Professor Name"}, 0);
        professorsTable = new JTable(professorsTableModel);
        professorsPanel.add(new JScrollPane(professorsTable), BorderLayout.CENTER);

        // Professor buttons
        JPanel professorButtonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,10,10));
        createProfessorButton = new JButton("Create Professor");
        editProfessorButton = new JButton("Edit Professor");
        deleteProfessorButton = new JButton("Delete Professor");
        professorButtonsPanel.add(createProfessorButton);
        professorButtonsPanel.add(editProfessorButton);
        professorButtonsPanel.add(deleteProfessorButton);
        professorsPanel.add(professorButtonsPanel, BorderLayout.SOUTH);

        // Load mock data
        loadMockData();

        // Action Listeners (No DAO logic, just comments)
        createStudentButton.addActionListener(e -> {
            // Show dialog to create new student
            JPanel inputPanel = new JPanel(new GridLayout(5,2,5,5));
            JTextField studentIdField = new JTextField();
            JTextField studentNameField = new JTextField();
            JTextField gpaField = new JTextField();
            JTextField creditsField = new JTextField();
            JPasswordField passwordField = new JPasswordField();

            inputPanel.add(new JLabel("Student ID:"));
            inputPanel.add(studentIdField);
            inputPanel.add(new JLabel("Student Name:"));
            inputPanel.add(studentNameField);
            inputPanel.add(new JLabel("GPA:"));
            inputPanel.add(gpaField);
            inputPanel.add(new JLabel("Available Credits:"));
            inputPanel.add(creditsField);
            inputPanel.add(new JLabel("Password:"));
            inputPanel.add(passwordField);

            int result = JOptionPane.showConfirmDialog(this, inputPanel, "Create New Student", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    int studentId = Integer.parseInt(studentIdField.getText().trim());
                    String studentName = studentNameField.getText().trim();
                    double gpa = Double.parseDouble(gpaField.getText().trim());
                    int credits = Integer.parseInt(creditsField.getText().trim());
                    String password = new String(passwordField.getPassword()).trim();

                    if (!studentName.isEmpty() && !password.isEmpty()) {
                        // TODO: DAO logic to add student to DB with given password
                        studentsTableModel.addRow(new Object[]{studentId, studentName, gpa, credits});
                        JOptionPane.showMessageDialog(this, "Student created successfully!");
                    } else {
                        JOptionPane.showMessageDialog(this, "Please fill all fields.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid input for ID, GPA, or credits.");
                }
            }
        });

        editStudentButton.addActionListener(e -> {
            int selectedRow = studentsTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Select a student from the table first.");
                return;
            }

            int studentId = (int) studentsTableModel.getValueAt(selectedRow, 0);
            String studentName = (String) studentsTableModel.getValueAt(selectedRow, 1);
            double gpa = (double) studentsTableModel.getValueAt(selectedRow, 2);
            int credits = (int) studentsTableModel.getValueAt(selectedRow, 3);

            JPanel inputPanel = new JPanel(new GridLayout(4,2,5,5));
            JTextField studentNameField = new JTextField(studentName);
            JTextField gpaField = new JTextField(String.valueOf(gpa));
            JTextField creditsField = new JTextField(String.valueOf(credits));
            JPasswordField passwordField = new JPasswordField(); // If empty, don't change password

            inputPanel.add(new JLabel("Student Name:"));
            inputPanel.add(studentNameField);
            inputPanel.add(new JLabel("GPA:"));
            inputPanel.add(gpaField);
            inputPanel.add(new JLabel("Available Credits:"));
            inputPanel.add(creditsField);
            inputPanel.add(new JLabel("New Password (leave blank if no change):"));
            inputPanel.add(passwordField);

            int result = JOptionPane.showConfirmDialog(this, inputPanel, "Edit Student (ID: " + studentId + ")", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    String newName = studentNameField.getText().trim();
                    double newGpa = Double.parseDouble(gpaField.getText().trim());
                    int newCredits = Integer.parseInt(creditsField.getText().trim());
                    String newPassword = new String(passwordField.getPassword()).trim();

                    if (!newName.isEmpty()) {
                        // TODO: DAO logic to update student in DB, and password if newPassword not empty
                        studentsTableModel.setValueAt(newName, selectedRow, 1);
                        studentsTableModel.setValueAt(newGpa, selectedRow, 2);
                        studentsTableModel.setValueAt(newCredits, selectedRow, 3);
                        JOptionPane.showMessageDialog(this, "Student updated successfully!");
                    } else {
                        JOptionPane.showMessageDialog(this, "Please fill all fields (except password if no change).");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid input for GPA or credits.");
                }
            }
        });

        deleteStudentButton.addActionListener(e -> {
            int selectedRow = studentsTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Select a student first.");
                return;
            }

            int studentId = (int) studentsTableModel.getValueAt(selectedRow, 0);
            int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete Student ID: " + studentId + "?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                // TODO: DAO logic to delete student from DB
                studentsTableModel.removeRow(selectedRow);
                JOptionPane.showMessageDialog(this, "Student deleted successfully.");
            }
        });

        createProfessorButton.addActionListener(e -> {
            // Show dialog to create new professor
            JPanel inputPanel = new JPanel(new GridLayout(3,2,5,5));
            JTextField professorIdField = new JTextField();
            JTextField professorNameField = new JTextField();
            JPasswordField passwordField = new JPasswordField();

            inputPanel.add(new JLabel("Professor ID:"));
            inputPanel.add(professorIdField);
            inputPanel.add(new JLabel("Professor Name:"));
            inputPanel.add(professorNameField);
            inputPanel.add(new JLabel("Password:"));
            inputPanel.add(passwordField);

            int result = JOptionPane.showConfirmDialog(this, inputPanel, "Create New Professor", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    int professorId = Integer.parseInt(professorIdField.getText().trim());
                    String professorName = professorNameField.getText().trim();
                    String password = new String(passwordField.getPassword()).trim();

                    if (!professorName.isEmpty() && !password.isEmpty()) {
                        // TODO: DAO logic to add professor to DB
                        professorsTableModel.addRow(new Object[]{professorId, professorName});
                        JOptionPane.showMessageDialog(this, "Professor created successfully!");
                    } else {
                        JOptionPane.showMessageDialog(this, "Please fill all fields.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid input for ID.");
                }
            }
        });

        editProfessorButton.addActionListener(e -> {
            int selectedRow = professorsTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Select a professor first.");
                return;
            }

            int professorId = (int) professorsTableModel.getValueAt(selectedRow, 0);
            String professorName = (String) professorsTableModel.getValueAt(selectedRow, 1);

            JPanel inputPanel = new JPanel(new GridLayout(2,2,5,5));
            JTextField professorNameField = new JTextField(professorName);
            JPasswordField passwordField = new JPasswordField(); // If empty, don't change password

            inputPanel.add(new JLabel("Professor Name:"));
            inputPanel.add(professorNameField);
            inputPanel.add(new JLabel("New Password (leave blank if no change):"));
            inputPanel.add(passwordField);

            int result = JOptionPane.showConfirmDialog(this, inputPanel, "Edit Professor (ID: " + professorId + ")", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String newName = professorNameField.getText().trim();
                String newPassword = new String(passwordField.getPassword()).trim();
                if (!newName.isEmpty()) {
                    // TODO: DAO logic to update professor in DB, and password if newPassword not empty
                    professorsTableModel.setValueAt(newName, selectedRow, 1);
                    JOptionPane.showMessageDialog(this, "Professor updated successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Please fill all required fields.");
                }
            }
        });

        deleteProfessorButton.addActionListener(e -> {
            int selectedRow = professorsTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Select a professor first.");
                return;
            }

            int professorId = (int) professorsTableModel.getValueAt(selectedRow, 0);
            int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete Professor ID: " + professorId + "?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                // TODO: DAO logic to delete professor from DB
                professorsTableModel.removeRow(selectedRow);
                JOptionPane.showMessageDialog(this, "Professor deleted successfully.");
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Mock data loading.
    private void loadMockData() {
        // Mock students
        // TODO: Replace with DAO calls to load students
        studentsTableModel.addRow(new Object[]{1001, "Alice", 3.5, 30});
        studentsTableModel.addRow(new Object[]{1002, "Bob", 2.8, 25});

        // Mock professors
        // TODO: Replace with DAO calls to load professors
        professorsTableModel.addRow(new Object[]{2001, "Prof. Johnson"});
        professorsTableModel.addRow(new Object[]{2002, "Prof. Green"});
    }

    // Test main
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminPanel());
    }
}
