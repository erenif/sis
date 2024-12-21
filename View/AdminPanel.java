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
import java.sql.SQLException;
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

    public AdminPanel(StudentDAO studentDAO, ProfessorDAO professorDAO, CourseDAO courseDAO) {
        this.studentDAO = studentDAO;
        this.professorDAO = professorDAO;
        this.courseDAO = courseDAO;

        setTitle("Admin Panel");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JLabel welcomeLabel = new JLabel("Welcome, Admin!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(welcomeLabel);
        add(topPanel, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();

        // Students Tab
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

        // Professors Tab
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

        // Courses Tab
        JPanel coursesPanel = new JPanel(new BorderLayout(10, 10));
        coursesPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        tabbedPane.addTab("Courses", coursesPanel);

        coursesTableModel = new DefaultTableModel(new Object[]{"Course ID", "Name", "Quota", "Credits", "Start", "End", "Day", "Syllabus"}, 0);
        coursesTable = new JTable(coursesTableModel);
        coursesPanel.add(new JScrollPane(coursesTable), BorderLayout.CENTER);

        JPanel courseButtonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JButton createCourseButton = new JButton("Create Course");
        JButton deleteCourseButton = new JButton("Delete Course");
        courseButtonsPanel.add(createCourseButton);
        courseButtonsPanel.add(deleteCourseButton);
        coursesPanel.add(courseButtonsPanel, BorderLayout.SOUTH);

        add(tabbedPane, BorderLayout.CENTER);

        loadDataFromDatabase();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loadDataFromDatabase() {
        try {
            // Load Students
            studentsTableModel.setRowCount(0);
            List<Student> students = studentDAO.getAllStudents();
            for (Student s : students) {
                studentsTableModel.addRow(new Object[]{s.getUserID(), s.getUserName(), s.getGpa(), s.getAvailableCredits()});
            }

            // Load Professors
            professorsTableModel.setRowCount(0);
            List<Professor> professors = professorDAO.getAllProfessors();
            for (Professor p : professors) {
                professorsTableModel.addRow(new Object[]{p.getUserID(), p.getUserName()});
            }

            // Load Courses
            coursesTableModel.setRowCount(0);
            List<Course> courses = courseDAO.getAllCourses();
            for (Course c : courses) {
                coursesTableModel.addRow(new Object[]{
                        c.getCourseId(), c.getCourseName(), c.getQuota(), c.getCredits(),
                        c.getStartTime(), c.getEndTime(), c.getCourse_day(), c.getSyllabus()
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage());
        }
    }
}
