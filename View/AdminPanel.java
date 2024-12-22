package View;

import DAOs.CourseDAO;
import DAOs.ProfessorDAO;
import DAOs.StudentDAO;
import Entities.Course;
import Entities.Professor;
import Entities.Student;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.List;

public class AdminPanel extends JFrame {

    private JTable studentsTable;
    private DefaultTableModel studentsTableModel;

    private JTable professorsTable;
    private DefaultTableModel professorsTableModel;

    private JTable coursesTable;
    private DefaultTableModel coursesTableModel;

    private StudentDAO studentDAO;
    private ProfessorDAO professorDAO;
    private CourseDAO courseDAO;
    private JButton logoutButton;

    public AdminPanel(StudentDAO studentDAO, ProfessorDAO professorDAO, CourseDAO courseDAO, Connection connection) {
        this.studentDAO = studentDAO;
        this.professorDAO = professorDAO;
        this.courseDAO = courseDAO;

        setTitle("Admin Panel");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Top panel with welcome label
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JLabel welcomeLabel = new JLabel("Welcome, Admin!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(welcomeLabel);

        logoutButton = new JButton("Logout");
        topPanel.add(logoutButton, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();

        // === Students Tab ===
        JPanel studentsPanel = new JPanel(new BorderLayout(10, 10));
        studentsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        tabbedPane.addTab("Students", studentsPanel);

        studentsTableModel = new DefaultTableModel(new Object[]{"Student ID", "Student Name", "GPA", "Available Credits"}, 0);
        studentsTable = new JTable(studentsTableModel);
        studentsPanel.add(new JScrollPane(studentsTable), BorderLayout.CENTER);

        JPanel studentButtonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JButton createStudentButton = new JButton("Create Student");
        JButton editStudentButton = new JButton("Edit Student");
        JButton deleteStudentButton = new JButton("Delete Student");
        studentButtonsPanel.add(createStudentButton);
        studentButtonsPanel.add(editStudentButton);
        studentButtonsPanel.add(deleteStudentButton);
        studentsPanel.add(studentButtonsPanel, BorderLayout.SOUTH);

        // === Professors Tab ===
        JPanel professorsPanel = new JPanel(new BorderLayout(10, 10));
        professorsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        tabbedPane.addTab("Professors", professorsPanel);

        professorsTableModel = new DefaultTableModel(new Object[]{"Professor ID", "Professor Name"}, 0);
        professorsTable = new JTable(professorsTableModel);
        professorsPanel.add(new JScrollPane(professorsTable), BorderLayout.CENTER);

        JPanel professorButtonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JButton createProfessorButton = new JButton("Create Professor");
        JButton editProfessorButton = new JButton("Edit Professor");
        JButton deleteProfessorButton = new JButton("Delete Professor");
        professorButtonsPanel.add(createProfessorButton);
        professorButtonsPanel.add(editProfessorButton);
        professorButtonsPanel.add(deleteProfessorButton);
        professorsPanel.add(professorButtonsPanel, BorderLayout.SOUTH);

        // === Courses Tab ===
        JPanel coursesPanel = new JPanel(new BorderLayout(10, 10));
        coursesPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        tabbedPane.addTab("Courses", coursesPanel);

        coursesTableModel = new DefaultTableModel(
                new Object[]{"Course ID", "Name", "Quota", "Credits", "Start", "End", "Day", "Syllabus"}, 0
        );
        coursesTable = new JTable(coursesTableModel);
        coursesPanel.add(new JScrollPane(coursesTable), BorderLayout.CENTER);

        JPanel courseButtonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JButton createCourseButton = new JButton("Create Course");
        JButton deleteCourseButton = new JButton("Delete Course");
        courseButtonsPanel.add(createCourseButton);
        courseButtonsPanel.add(deleteCourseButton);
        coursesPanel.add(courseButtonsPanel, BorderLayout.SOUTH);

        add(tabbedPane, BorderLayout.CENTER);

        // Load initial data
        loadDataFromDatabase();

        // ========== Action Listeners ==========

        logoutButton.addActionListener(e -> {
            // Close this AdminPanel
            dispose();
            JFrame frame = new JFrame("Login");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 300);
            frame.setLocationRelativeTo(null);
            frame.add(new LoginPanel(frame, connection));
            frame.setVisible(true);

            // Show the LoginPanel again
            JFrame loginFrame = new JFrame("Login");
            loginFrame.setSize(600, 400);
            loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Create a new LoginPanel, passing the same connection, if needed
            LoginPanel loginPanel = new LoginPanel(loginFrame, connection);
            loginFrame.add(loginPanel);

            loginFrame.setLocationRelativeTo(null);
            loginFrame.setVisible(true);
        });

        // Create Student
        createStudentButton.addActionListener(e -> {
            JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5));
            JTextField idField = new JTextField();
            JTextField nameField = new JTextField();
            JTextField gpaField = new JTextField("0.0");
            JTextField creditsField = new JTextField("30");
            JPasswordField passwordField = new JPasswordField();  // password

            inputPanel.add(new JLabel("Student ID:"));
            inputPanel.add(idField);
            inputPanel.add(new JLabel("Student Name:"));
            inputPanel.add(nameField);
            inputPanel.add(new JLabel("GPA:"));
            inputPanel.add(gpaField);
            inputPanel.add(new JLabel("Available Credits:"));
            inputPanel.add(creditsField);
            inputPanel.add(new JLabel("Password:"));
            inputPanel.add(passwordField);

            int result = JOptionPane.showConfirmDialog(this, inputPanel,
                    "Create New Student", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    int studentId = Integer.parseInt(idField.getText().trim());
                    String studentName = nameField.getText().trim();
                    double gpa = Double.parseDouble(gpaField.getText().trim());
                    int availableCredits = Integer.parseInt(creditsField.getText().trim());
                    String password = new String(passwordField.getPassword()).trim();

                    if (!studentName.isEmpty() && !password.isEmpty()) {
                        // Add student to DB
                        Student s = new Student(studentId, studentName, password); // updated constructor
                        s.setGpa(gpa);
                        s.setAvailableCredits(availableCredits);
                        studentDAO.addStudent(s);

                        loadDataFromDatabase();
                        JOptionPane.showMessageDialog(this, "Student created successfully!");
                    } else {
                        JOptionPane.showMessageDialog(this, "Name and password are required.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid input.");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error creating student: " + ex.getMessage());
                }
            }
        });

        // Edit Student
        editStudentButton.addActionListener(e -> {
            int selectedRow = studentsTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Select a student first.");
                return;
            }

            int studentId = (int) studentsTableModel.getValueAt(selectedRow, 0);
            String studentName = (String) studentsTableModel.getValueAt(selectedRow, 1);
            double studentGpa = (double) studentsTableModel.getValueAt(selectedRow, 2);
            int studentCredits = (int) studentsTableModel.getValueAt(selectedRow, 3);

            JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5));
            JTextField nameField = new JTextField(studentName);
            JTextField gpaField = new JTextField(String.valueOf(studentGpa));
            JTextField creditsField = new JTextField(String.valueOf(studentCredits));
            JPasswordField passwordField = new JPasswordField(); // if blank, keep old password

            inputPanel.add(new JLabel("Student Name:"));
            inputPanel.add(nameField);
            inputPanel.add(new JLabel("GPA:"));
            inputPanel.add(gpaField);
            inputPanel.add(new JLabel("Available Credits:"));
            inputPanel.add(creditsField);
            inputPanel.add(new JLabel("New Password (blank = no change):"));
            inputPanel.add(passwordField);

            int result = JOptionPane.showConfirmDialog(this, inputPanel,
                    "Edit Student (ID: " + studentId + ")", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    String newName = nameField.getText().trim();
                    double newGpa = Double.parseDouble(gpaField.getText().trim());
                    int newCredits = Integer.parseInt(creditsField.getText().trim());
                    String newPassword = new String(passwordField.getPassword()).trim();

                    // Fetch existing student from DB
                    Student existing = studentDAO.getStudentById(studentId);
                    if (existing == null) {
                        JOptionPane.showMessageDialog(this, "Student not found in DB.");
                        return;
                    }

                    existing.setUserName(newName);
                    existing.setGpa(newGpa);
                    existing.setAvailableCredits(newCredits);
                    if (!newPassword.isEmpty()) {
                        existing.setPassword(newPassword);  // only update if not blank
                    }
                    studentDAO.updateStudent(existing);

                    loadDataFromDatabase();
                    JOptionPane.showMessageDialog(this, "Student updated successfully!");
                } catch (NumberFormatException ex2) {
                    JOptionPane.showMessageDialog(this, "Invalid numeric value.");
                } catch (SQLException ex2) {
                    JOptionPane.showMessageDialog(this, "Error updating student: " + ex2.getMessage());
                }
            }
        });

        // Delete Student
        deleteStudentButton.addActionListener(e -> {
            int selectedRow = studentsTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Select a student first.");
                return;
            }
            int studentId = (int) studentsTableModel.getValueAt(selectedRow, 0);

            int choice = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete Student ID: " + studentId + "?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                try {
                    studentDAO.deleteStudent(studentId);
                    loadDataFromDatabase();
                    JOptionPane.showMessageDialog(this, "Student deleted successfully.");
                } catch (SQLException ex3) {
                    JOptionPane.showMessageDialog(this, "Error deleting student: " + ex3.getMessage());
                }
            }
        });

        // Create Professor
        createProfessorButton.addActionListener(e -> {
            JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));
            JTextField idField = new JTextField();
            JTextField nameField = new JTextField();
            JPasswordField passwordField = new JPasswordField();

            inputPanel.add(new JLabel("Professor ID:"));
            inputPanel.add(idField);
            inputPanel.add(new JLabel("Professor Name:"));
            inputPanel.add(nameField);
            inputPanel.add(new JLabel("Password:"));
            inputPanel.add(passwordField);

            int result = JOptionPane.showConfirmDialog(this, inputPanel,
                    "Create New Professor", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    int professorId = Integer.parseInt(idField.getText().trim());
                    String professorName = nameField.getText().trim();
                    String password = new String(passwordField.getPassword()).trim();

                    if (!professorName.isEmpty() && !password.isEmpty()) {
                        Professor p = new Professor(professorId, professorName, password);
                        professorDAO.addProfessor(p);
                        loadDataFromDatabase();
                        JOptionPane.showMessageDialog(this, "Professor created successfully!");
                    } else {
                        JOptionPane.showMessageDialog(this, "Name and password are required.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid ID.");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error creating professor: " + ex.getMessage());
                }
            }
        });

        // Edit Professor
        editProfessorButton.addActionListener(e -> {
            int selectedRow = professorsTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Select a professor first.");
                return;
            }

            int professorId = (int) professorsTableModel.getValueAt(selectedRow, 0);
            String profName = (String) professorsTableModel.getValueAt(selectedRow, 1);

            JPanel inputPanel = new JPanel(new GridLayout(2, 2, 5, 5));
            JTextField nameField = new JTextField(profName);
            JPasswordField passwordField = new JPasswordField(); // blank = no change

            inputPanel.add(new JLabel("Professor Name:"));
            inputPanel.add(nameField);
            inputPanel.add(new JLabel("New Password (blank = no change):"));
            inputPanel.add(passwordField);

            int result = JOptionPane.showConfirmDialog(this, inputPanel,
                    "Edit Professor (ID: " + professorId + ")", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    String newName = nameField.getText().trim();
                    String newPassword = new String(passwordField.getPassword()).trim();

                    // Fetch existing professor
                    Professor existing = professorDAO.getProfessorById(professorId);
                    if (existing == null) {
                        JOptionPane.showMessageDialog(this, "Professor not found in DB.");
                        return;
                    }

                    existing.setUserName(newName);
                    if (!newPassword.isEmpty()) {
                        existing.setPassword(newPassword);
                    }

                    professorDAO.updateProfessor(existing);
                    loadDataFromDatabase();
                    JOptionPane.showMessageDialog(this, "Professor updated successfully!");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error updating professor: " + ex.getMessage());
                }
            }
        });

        // Delete Professor
        deleteProfessorButton.addActionListener(e -> {
            int selectedRow = professorsTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Select a professor first.");
                return;
            }
            int professorId = (int) professorsTableModel.getValueAt(selectedRow, 0);

            int choice = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete Professor ID: " + professorId + "?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                try {
                    professorDAO.deleteProfessor(professorId);
                    loadDataFromDatabase();
                    JOptionPane.showMessageDialog(this, "Professor deleted successfully.");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error deleting professor: " + ex.getMessage());
                }
            }
        });

        // Create Course
        createCourseButton.addActionListener(e -> {
            JPanel inputPanel = new JPanel(new GridLayout(8, 2, 5, 5));
            JTextField courseIdField = new JTextField();
            JTextField courseNameField = new JTextField();
            JTextField quotaField = new JTextField();
            JTextField creditsField = new JTextField();
            JTextField startTimeField = new JTextField("09:00");
            JTextField endTimeField = new JTextField("10:00");
            JTextField dayField = new JTextField("MONDAY");
            JTextField syllabusField = new JTextField();

            inputPanel.add(new JLabel("Course ID:"));
            inputPanel.add(courseIdField);
            inputPanel.add(new JLabel("Course Name:"));
            inputPanel.add(courseNameField);
            inputPanel.add(new JLabel("Quota:"));
            inputPanel.add(quotaField);
            inputPanel.add(new JLabel("Credits:"));
            inputPanel.add(creditsField);
            inputPanel.add(new JLabel("Start Time (HH:MM):"));
            inputPanel.add(startTimeField);
            inputPanel.add(new JLabel("End Time (HH:MM):"));
            inputPanel.add(endTimeField);
            inputPanel.add(new JLabel("Day (e.g. MONDAY):"));
            inputPanel.add(dayField);
            inputPanel.add(new JLabel("Syllabus:"));
            inputPanel.add(syllabusField);

            int result = JOptionPane.showConfirmDialog(this, inputPanel,
                    "Create New Course", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    int courseId = Integer.parseInt(courseIdField.getText().trim());
                    String courseName = courseNameField.getText().trim();
                    int quota = Integer.parseInt(quotaField.getText().trim());
                    int credits = Integer.parseInt(creditsField.getText().trim());
                    String startTime = startTimeField.getText().trim();
                    String endTime = endTimeField.getText().trim();
                    String day = dayField.getText().trim().toUpperCase();
                    String syllabus = syllabusField.getText().trim();

                    if (!courseName.isEmpty() && !day.isEmpty()) {
                        Course c = new Course(
                                courseId,
                                courseName,
                                quota,
                                credits,
                                java.time.LocalTime.parse(startTime),
                                java.time.LocalTime.parse(endTime),
                                Entities.Enum.WeekDays.valueOf(day),
                                syllabus,
                                new java.util.ArrayList<>()
                        );
                        // Add course to DB
                        courseDAO.addCourse(c);
                        loadDataFromDatabase();
                        JOptionPane.showMessageDialog(this, "Course created successfully!");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid numeric input.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Invalid Day or Time format: " + ex.getMessage());
                }
            }
        });

        // Delete Course
        deleteCourseButton.addActionListener(e -> {
            int selectedRow = coursesTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Select a course first.");
                return;
            }
            int courseId = (int) coursesTableModel.getValueAt(selectedRow, 0);

            int choice = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete Course ID: " + courseId + "?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                try {
                    courseDAO.deleteCourse(courseId);
                    loadDataFromDatabase();
                    JOptionPane.showMessageDialog(this, "Course deleted successfully.");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error deleting course: " + ex.getMessage());
                }
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Loads or reloads the data from the database into each table.
     */
    private void loadDataFromDatabase() {
        try {
            // ----- Load Students -----
            studentsTableModel.setRowCount(0);
            List<Student> students = studentDAO.getAllStudents();
            for (Student s : students) {
                studentsTableModel.addRow(new Object[]{
                        s.getUserID(), s.getUserName(), s.getGpa(), s.getAvailableCredits()
                });
            }

            // ----- Load Professors -----
            professorsTableModel.setRowCount(0);
            List<Professor> professors = professorDAO.getAllProfessors();
            for (Professor p : professors) {
                professorsTableModel.addRow(new Object[]{
                        p.getUserID(), p.getUserName()
                });
            }

            // ----- Load Courses -----
            coursesTableModel.setRowCount(0);
            List<Course> courses = courseDAO.getAllCourses();
            for (Course c : courses) {
                coursesTableModel.addRow(new Object[]{
                        c.getCourseId(),
                        c.getCourseName(),
                        c.getQuota(),
                        c.getCredits(),
                        c.getStartTime(),
                        c.getEndTime(),
                        c.getCourse_day(),
                        c.getSyllabus()
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage());
        }
    }
}
