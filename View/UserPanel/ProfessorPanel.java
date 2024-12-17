package View.UserPanel;

import Entities.Course;
import Entities.Professor;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ProfessorPanel extends JFrame {
    private Professor professor;

    // UI components for the course list
    private JComboBox<Course> courseComboBox;
    private JButton openCourseButton;
    private JButton editCourseButton;
    private JButton deleteCourseButton;
    private JButton createCourseButton; // New button to add courses

    // UI components for displaying students in a selected course
    private JTable studentsTable;
    private JButton assignGradeButton;
    private JTextField gradeField;
    private DefaultTableModel studentsTableModel;

    public ProfessorPanel(Professor professor) {
        this.professor = professor;
        setTitle("Professor Panel - " + professor.getUserName());
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10,10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBorder(new EmptyBorder(10,10,10,10));

        JLabel welcomeLabel = new JLabel("Welcome, Professor " + professor.getUserName() + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(welcomeLabel);

        add(topPanel, BorderLayout.NORTH);

        JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        mainSplit.setDividerLocation(250);
        add(mainSplit, BorderLayout.CENTER);

        // Left panel: Course selection and actions
        JPanel coursePanel = new JPanel(new BorderLayout(10,10));
        coursePanel.setBorder(new EmptyBorder(10,10,10,10));

        JLabel coursesLabel = new JLabel("Your Courses:");
        coursesLabel.setFont(new Font("Arial", Font.BOLD, 14));
        coursePanel.add(coursesLabel, BorderLayout.NORTH);

        // Load courses from professor's list
        List<Course> courses = professor.getCoursesTaught();

        courseComboBox = new JComboBox<>(courses.toArray(new Course[0]));
        coursePanel.add(courseComboBox, BorderLayout.CENTER);

        JPanel courseActionsPanel = new JPanel(new GridLayout(4,1,5,5));
        openCourseButton = new JButton("Open Course");
        editCourseButton = new JButton("Edit Course");
        deleteCourseButton = new JButton("Delete Course");
        createCourseButton = new JButton("Create Course"); // New button

        courseActionsPanel.add(openCourseButton);
        courseActionsPanel.add(editCourseButton);
        courseActionsPanel.add(deleteCourseButton);
        courseActionsPanel.add(createCourseButton);

        coursePanel.add(courseActionsPanel, BorderLayout.SOUTH);

        mainSplit.setLeftComponent(coursePanel);

        // Right panel: Students in selected course and assigning notes
        JPanel studentsPanel = new JPanel(new BorderLayout(10,10));
        studentsPanel.setBorder(new EmptyBorder(10,10,10,10));

        JLabel enrolledLabel = new JLabel("Enrolled Students:");
        enrolledLabel.setFont(new Font("Arial", Font.BOLD, 14));
        studentsPanel.add(enrolledLabel, BorderLayout.NORTH);

        // Table for students
        studentsTableModel = new DefaultTableModel(new Object[]{"Student ID", "Student Name", "Current Grade"}, 0);
        studentsTable = new JTable(studentsTableModel);
        studentsPanel.add(new JScrollPane(studentsTable), BorderLayout.CENTER);

        // Panel for assigning grade
        JPanel assignGradePanel = new JPanel(new FlowLayout(FlowLayout.LEFT,10,10));
        assignGradePanel.add(new JLabel("Assign Grade:"));
        gradeField = new JTextField(5);
        assignGradePanel.add(gradeField);
        assignGradeButton = new JButton("Assign");
        assignGradePanel.add(assignGradeButton);

        studentsPanel.add(assignGradePanel, BorderLayout.SOUTH);

        mainSplit.setRightComponent(studentsPanel);

        // Action Listeners (No DAO logic, just comments)
        openCourseButton.addActionListener(e -> {
            Course selectedCourse = (Course) courseComboBox.getSelectedItem();
            if (selectedCourse == null) {
                JOptionPane.showMessageDialog(this, "No course selected.");
                return;
            }

            // Clear table before loading
            studentsTableModel.setRowCount(0);

            // TODO: DAO logic to fetch students enrolled in selectedCourse
            // Mock data:
            Object[][] mockStudents = {
                    {101, "Student A", "B"},
                    {102, "Student B", "A"},
                    {103, "Student C", "C"},
            };

            for (Object[] studentData : mockStudents) {
                studentsTableModel.addRow(studentData);
            }
        });

        editCourseButton.addActionListener(e -> {
            Course selectedCourse = (Course) courseComboBox.getSelectedItem();
            if (selectedCourse == null) {
                JOptionPane.showMessageDialog(this, "No course selected.");
                return;
            }

            String newName = JOptionPane.showInputDialog(this, "Enter new course name:", selectedCourse.getCourseName());
            if (newName != null && !newName.trim().isEmpty()) {
                selectedCourse.setCourseName(newName);
                // TODO: DAO logic to update the course in the DB
                courseComboBox.repaint();
            }
        });

        deleteCourseButton.addActionListener(e -> {
            Course selectedCourse = (Course) courseComboBox.getSelectedItem();
            if (selectedCourse == null) {
                JOptionPane.showMessageDialog(this, "No course selected.");
                return;
            }

            int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete " + selectedCourse.getCourseName() + "?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                // TODO: DAO logic to delete course from DB
                professor.getCoursesTaught().remove(selectedCourse);
                courseComboBox.removeItem(selectedCourse);
                JOptionPane.showMessageDialog(this, "Course deleted successfully.");
            }
        });

        assignGradeButton.addActionListener(e -> {
            int selectedRow = studentsTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Select a student from the table first.");
                return;
            }

            String grade = gradeField.getText().trim();
            if (grade.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a grade.");
                return;
            }

            // TODO: DAO logic to assign grade to the student in the DB
            studentsTableModel.setValueAt(grade.toUpperCase(), selectedRow, 2);
            JOptionPane.showMessageDialog(this, "Grade assigned successfully.");
        });

        createCourseButton.addActionListener(e -> {
            // Show a dialog to input new course details
            JPanel inputPanel = new JPanel(new GridLayout(5,2,5,5));

            JTextField courseIdField = new JTextField();
            JTextField courseNameField = new JTextField();
            JTextField creditsField = new JTextField();
            JTextField quotaField = new JTextField();
            JTextField scheduleField = new JTextField(); // format: "Wednesday, 11.00-12.00"
            JTextField syllabusField = new JTextField();

            inputPanel.add(new JLabel("Course ID:"));
            inputPanel.add(courseIdField);
            inputPanel.add(new JLabel("Course Name:"));
            inputPanel.add(courseNameField);
            inputPanel.add(new JLabel("Credits:"));
            inputPanel.add(creditsField);
            inputPanel.add(new JLabel("Quota:"));
            inputPanel.add(quotaField);
            inputPanel.add(new JLabel("Schedule:"));
            inputPanel.add(scheduleField);
            inputPanel.add(new JLabel("Syllabus:"));
            inputPanel.add(syllabusField);

            int result = JOptionPane.showConfirmDialog(this, inputPanel, "Create New Course", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    int courseId = Integer.parseInt(courseIdField.getText().trim());
                    String courseName = courseNameField.getText().trim();
                    int credits = Integer.parseInt(creditsField.getText().trim());
                    int quota = Integer.parseInt(quotaField.getText().trim());
                    String schedule = scheduleField.getText().trim();
                    String syllabus = syllabusField.getText().trim();

                    if (!courseName.isEmpty() && !schedule.isEmpty() && !syllabus.isEmpty()) {
                        // Create new course in the model
                        Course newCourse = new Course(courseId, courseName, quota, credits, schedule, syllabus);
                        professor.getCoursesTaught().add(newCourse);
                        courseComboBox.addItem(newCourse);

                        // TODO: DAO logic to add course to DB and link to professor
                        JOptionPane.showMessageDialog(this, "Course created successfully!");
                    } else {
                        JOptionPane.showMessageDialog(this, "Please fill all fields properly.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid numeric input for ID, credits, or quota.");
                }
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    // For testing purposes
    public static void main(String[] args) {
        // Mock a professor with some courses
        Professor mockProfessor = new Professor(1, "Dr. Smith");
        mockProfessor.createCourse(201, "Data Structures", 3, 30, "Syllabus DS", "Wednesday, 11.00-12.00");
        mockProfessor.createCourse(202, "Algorithms", 3, 25, "Syllabus Algo", "Friday, 10.00-11.00");

        SwingUtilities.invokeLater(() -> new ProfessorPanel(mockProfessor));
    }
}
