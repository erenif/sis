package View.UserPanel;

import DAOs.CourseDAO;
import DAOs.ProfessorDAO;
import DAOs.StudentDAO;
import Entities.Course;
import Entities.Enum.WeekDays;
import Entities.Professor;
import Entities.Student;
import Utils.DatabaseConnection;
import View.LoginPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
// TODO: Veritabanından students enrolled almak için ek DAO metotları gerekli

public class ProfessorPanel extends JFrame {
    private Professor professor;
    private StudentDAO studentDAO;
    private CourseDAO courseDAO;
    private ProfessorDAO professorDAO;

    private JComboBox<Course> courseComboBox;
    private JButton openCourseButton;
    private JButton editCourseButton;
    private JButton deleteCourseButton;
    private JButton createCourseButton;
    private JButton logoutButton;

    private JTable studentsTable;
    private JButton assignGradeButton;
    private JTextField gradeField;
    private DefaultTableModel studentsTableModel;

    public ProfessorPanel(Professor professor, StudentDAO studentDAO, CourseDAO courseDAO, ProfessorDAO professorDAO) {
        this.professor = professor;
        this.studentDAO = studentDAO;
        this.courseDAO = courseDAO;
        this.professorDAO = professorDAO;

        setTitle("Professor Panel - " + professor.getUserName());
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JLabel welcomeLabel = new JLabel("Welcome, Professor " + professor.getUserName() + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(welcomeLabel);
        add(topPanel, BorderLayout.NORTH);

        JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        mainSplit.setDividerLocation(250);
        add(mainSplit, BorderLayout.CENTER);

        JPanel coursePanel = new JPanel(new BorderLayout(10, 10));
        coursePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JLabel coursesLabel = new JLabel("Your Courses:");
        coursesLabel.setFont(new Font("Arial", Font.BOLD, 14));
        coursePanel.add(coursesLabel, BorderLayout.NORTH);

        // Professor'un kurslarını veritabanından çekmek isterseniz professorDAO ile yapılmalı
        // Şu an professor objesinde mevcut.
        courseComboBox = new JComboBox<>();
        try {
            List<Course> courses = professorDAO.getCoursesByProfessor(professor.getUserID());
            for (Course course : courses) {
                System.out.println(course.getCourseName());
                System.out.println("1");
                courseComboBox.addItem(course);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching courses: " + ex.getMessage());
        }
        coursePanel.add(courseComboBox, BorderLayout.CENTER);

        JPanel courseActionsPanel = new JPanel(new GridLayout(4, 1, 5, 5));

        openCourseButton = new JButton("Open Course");
        editCourseButton = new JButton("Edit Course");
        deleteCourseButton = new JButton("Delete Course");
        createCourseButton = new JButton("Create Course");
        logoutButton = new JButton("Log Out");
        openCourseButton.setToolTipText("View and manage students enrolled in this course");
        createCourseButton.setToolTipText("Add a new course to your teaching list");
        deleteCourseButton.setToolTipText("Remove the selected course");
        courseActionsPanel.add(openCourseButton);
        courseActionsPanel.add(editCourseButton);
        courseActionsPanel.add(deleteCourseButton);
        courseActionsPanel.add(createCourseButton);
        coursePanel.add(courseActionsPanel, BorderLayout.SOUTH);

        topPanel.add(logoutButton,BorderLayout.EAST);


        mainSplit.setLeftComponent(coursePanel);

        JPanel studentsPanel = new JPanel(new BorderLayout(10, 10));
        studentsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JLabel enrolledLabel = new JLabel("Enrolled Students:");
        enrolledLabel.setFont(new Font("Arial", Font.BOLD, 14));
        studentsPanel.add(enrolledLabel, BorderLayout.NORTH);

        studentsTableModel = new DefaultTableModel(new Object[]{"Student ID", "Student Name", "Current Grade"}, 0);
        studentsTable = new JTable(studentsTableModel);
        studentsPanel.add(new JScrollPane(studentsTable), BorderLayout.CENTER);

        JPanel assignGradePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        assignGradePanel.add(new JLabel("Assign Grade:"));
        gradeField = new JTextField(5);
        assignGradePanel.add(gradeField);
        assignGradeButton = new JButton("Assign");
        assignGradePanel.add(assignGradeButton);

        studentsPanel.add(assignGradePanel, BorderLayout.SOUTH);

        mainSplit.setRightComponent(studentsPanel);

        openCourseButton.addActionListener(e -> {
            Course selectedCourse = (Course) courseComboBox.getSelectedItem();
            if (selectedCourse == null) {
                JOptionPane.showMessageDialog(this, "No course selected.");
                return;
            }

            studentsTableModel.setRowCount(0);

            try {
                List<Student> enrolledStudents = studentDAO.getStudentsEnrolledInCourse(selectedCourse.getCourseId());
                for (Student student : enrolledStudents) {
                    studentsTableModel.addRow(new Object[]{
                            student.getUserID(),
                            student.getUserName(),
                            student.viewLetterGrade(selectedCourse)
                    });
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            // DONE: DAO logic: Enrollment_Table üzerinden bu kursa kayıtlı öğrencileri çek
            // Örneğin studentDAO içinde getStudentsEnrolledInCourse metodu yazılabilir.
            // List<Student> enrolledStudents = studentDAO.getStudentsEnrolledInCourse(selectedCourse.getCourseId());
            // for (Student s : enrolledStudents) {
            //     // s.viewLetterGrade(selectedCourse) ile notu çekebilirsiniz
            //     studentsTableModel.addRow(new Object[]{s.getUserID(), s.getUserName(), s.viewLetterGrade(selectedCourse)});
            // }

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
                // TODO: DAO logic to update the course in the DB: courseDAO.updateCourse(selectedCourse)
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
            // TODO: DAO logic to delete course from DB: courseDAO.deleteCourse(selectedCourse.getCourseId())

            if (choice == JOptionPane.YES_OPTION) {
                try {
                    courseDAO.deleteCourse(selectedCourse.getCourseId());
                    professorDAO.unassignCourseFromProfessor(selectedCourse.getCourseId());
                    courseComboBox.removeItem(selectedCourse);
                    JOptionPane.showMessageDialog(this, "Course deleted successfully.");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Failed to delete course: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
            courseComboBox.removeItem(selectedCourse);
            JOptionPane.showMessageDialog(this, "Course deleted successfully.");
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

            // TODO: DAO logic to assign grade in Enrollment_Table
            // studentDAO.assignGrade(studentId, courseId, grade);

            studentsTableModel.setValueAt(grade.toUpperCase(), selectedRow, 2);
            JOptionPane.showMessageDialog(this, "Grade assigned successfully.");
        });

        createCourseButton.addActionListener(e -> {
            JPanel inputPanel = new JPanel(new GridLayout(9, 2, 5, 5));

            JTextField courseIdField = new JTextField();
            JTextField courseNameField = new JTextField();
            JTextField creditsField = new JTextField();
            JTextField quotaField = new JTextField();
            JTextField courseDayField = new JTextField();
            JTextField startTimeField = new JTextField();
            JTextField endTimeField = new JTextField();
            JTextField syllabusField = new JTextField();

            inputPanel.add(new JLabel("Course ID:"));
            inputPanel.add(courseIdField);
            inputPanel.add(new JLabel("Course Name:"));
            inputPanel.add(courseNameField);
            inputPanel.add(new JLabel("Credits:"));
            inputPanel.add(creditsField);
            inputPanel.add(new JLabel("Quota:"));
            inputPanel.add(quotaField);
            inputPanel.add(new JLabel("Course Day:"));
            inputPanel.add(courseDayField);
            inputPanel.add(new JLabel("Start Time (HH:MM):"));
            inputPanel.add(startTimeField);
            inputPanel.add(new JLabel("End Time (HH:MM):"));
            inputPanel.add(endTimeField);
            inputPanel.add(new JLabel("Syllabus:"));
            inputPanel.add(syllabusField);

            int result = JOptionPane.showConfirmDialog(this, inputPanel, "Create New Course", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    int courseId = Integer.parseInt(courseIdField.getText().trim());
                    String courseName = courseNameField.getText().trim();
                    int credits = Integer.parseInt(creditsField.getText().trim());
                    int quota = Integer.parseInt(quotaField.getText().trim());
                    String courseDay_str = courseDayField.getText().trim();
                    String startTime_str = startTimeField.getText().trim();
                    String endTime_str = endTimeField.getText().trim();
                    String syllabus = syllabusField.getText().trim();

                    if (courseName.isEmpty() || syllabus.isEmpty() || courseDay_str.isEmpty() || startTime_str.isEmpty() || endTime_str.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Please fill all fields.", "Missing Input", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    WeekDays courseDay = WeekDays.valueOf(courseDay_str.toUpperCase());
                    LocalTime startTime = LocalTime.parse(startTime_str);
                    LocalTime endTime = LocalTime.parse(endTime_str);

                    Course newCourse = new Course(courseId, courseName, quota, credits, startTime, endTime, courseDay, syllabus, new ArrayList<>());

                    // TODO: DAO logic to add course: courseDAO.addCourse(newCourse)
                    courseDAO.addCourse(newCourse);
                    professorDAO.assignCourseToProfessor(professor.getUserID(), courseId);
                    this.courseComboBox.addItem(newCourse);
                    JOptionPane.showMessageDialog(this, "Course created successfully!");

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid numeric input for ID, credits, or quota.");
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid day or time format.");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error assigning course to professor: " + ex.getMessage());
                    throw new RuntimeException(ex);
                }
            }
        });
        setLocationRelativeTo(null);
        setVisible(true);
    }
}