package View.UserPanel;

import DAOs.CourseDAO;
import DAOs.StudentDAO;
import Entities.Course;
import Entities.Student;
import Utils.DatabaseConnection;
import View.LoginPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class StudentPanel extends JFrame {
    private Student student;
    private StudentDAO studentDAO;
    private CourseDAO courseDAO;

    private JTabbedPane tabbedPane;

    private JTable enrolledCoursesTable;
    private DefaultTableModel enrolledCoursesTableModel;
    private JTable availableCoursesTable;
    private DefaultTableModel availableCoursesTableModel;
    private JButton addCourseButton;
    private JButton dropCourseButton;
    private JButton logoutButton;


    private JTable scheduleTable;
    private DefaultTableModel scheduleTableModel;

    private JLabel gpaLabel;
    private JButton calculateGPAButton;

    public StudentPanel(Student student, StudentDAO studentDAO, CourseDAO courseDAO, Connection connection) {
        this.student = student;
        this.studentDAO = studentDAO;
        this.courseDAO = courseDAO;

        setTitle("Student Panel - " + student.getUserName());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10,10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBorder(new EmptyBorder(10,10,10,10));
        JLabel welcomeLabel = new JLabel("Welcome, " + student.getUserName() + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(welcomeLabel);

        add(topPanel, BorderLayout.NORTH);

        logoutButton = new JButton("Logout");
        topPanel.add(logoutButton, BorderLayout.EAST);

        tabbedPane = new JTabbedPane();
        add(tabbedPane, BorderLayout.CENTER);

        setupCoursesTab();
        setupScheduleTab();
        setupGpaTab();

        loadDataFromDatabase();

        logoutButton.addActionListener(e -> {
            // Close this AdminPanel
            dispose();

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

        addCourseButton.addActionListener(e -> {
            final int selectedRow = availableCoursesTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Select a course from the available courses table to add.");
                return;
            }

            final int courseId = (int) availableCoursesTableModel.getValueAt(selectedRow, 0);

            try {
                studentDAO.enrollInCourse(student.getUserID(), courseId);
                JOptionPane.showMessageDialog(this, "Course added successfully!");
                loadDataFromDatabase();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error adding course: " + ex.getMessage());
            }
        });

        dropCourseButton.addActionListener(e -> {
            final int selectedRow = enrolledCoursesTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Select a course from the enrolled courses table to drop.");
                return;
            }

            final int courseId = (int) enrolledCoursesTableModel.getValueAt(selectedRow, 0);

            try {
                studentDAO.dropCourse(student.getUserID(), courseId);
                JOptionPane.showMessageDialog(this, "Course dropped successfully!");
                loadDataFromDatabase();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error dropping course: " + ex.getMessage());
            }
        });

        calculateGPAButton.addActionListener(e -> {
            try {
                Student updatedStudent = studentDAO.getStudentById(student.getUserID());
                if (updatedStudent != null) {
                    this.student = updatedStudent;
                }
                double currentGPA = student.viewGPA();
                student.setGpa(currentGPA);
                gpaLabel.setText("Your current GPA: " + currentGPA);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error retrieving GPA: " + ex.getMessage());
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void setupCoursesTab() {
        JPanel coursesPanel = new JPanel(new BorderLayout(10,10));
        coursesPanel.setBorder(new EmptyBorder(10,10,10,10));
        tabbedPane.addTab("Courses", coursesPanel);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(200);
        coursesPanel.add(splitPane, BorderLayout.CENTER);

        JPanel enrolledPanel = new JPanel(new BorderLayout(5,5));
        enrolledPanel.setBorder(new EmptyBorder(5,5,5,5));
        enrolledPanel.add(new JLabel("Enrolled Courses:"), BorderLayout.NORTH);
        enrolledCoursesTableModel = new DefaultTableModel(new Object[]{"Course ID", "Course Name", "Day", "Time", "Credits"}, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tablonun düzenlenemez olmasını sağlıyor
            }
        };
        enrolledCoursesTable = new JTable(enrolledCoursesTableModel);
        enrolledPanel.add(new JScrollPane(enrolledCoursesTable), BorderLayout.CENTER);

        JPanel availablePanel = new JPanel(new BorderLayout(5,5));
        availablePanel.setBorder(new EmptyBorder(5,5,5,5));
        availablePanel.add(new JLabel("Available Courses:"), BorderLayout.NORTH);
        availableCoursesTableModel = new DefaultTableModel(new Object[]{"Course ID", "Course Name", "Quota", "Day", "Time", "Credits"}, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tablonun düzenlenemez olmasını sağlıyor
            }
        };
        availableCoursesTable = new JTable(availableCoursesTableModel);
        availablePanel.add(new JScrollPane(availableCoursesTable), BorderLayout.CENTER);

        JPanel courseButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10,10));
        addCourseButton = new JButton("Add Course");
        dropCourseButton = new JButton("Drop Course");
        courseButtonPanel.add(addCourseButton);
        courseButtonPanel.add(dropCourseButton);
        availablePanel.add(courseButtonPanel, BorderLayout.SOUTH);

        splitPane.setTopComponent(enrolledPanel);
        splitPane.setBottomComponent(availablePanel);
    }

    private void setupScheduleTab() {
        JPanel schedulePanel = new JPanel(new BorderLayout(10,10));
        schedulePanel.setBorder(new EmptyBorder(10,10,10,10));
        tabbedPane.addTab("Schedule", schedulePanel);

        JLabel scheduleLabel = new JLabel("Weekly Schedule:");
        scheduleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        schedulePanel.add(scheduleLabel, BorderLayout.NORTH);

        // Create a DefaultTableModel that always returns false for isCellEditable
        scheduleTableModel = new DefaultTableModel(
                new Object[]{"Time", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;  // Make the entire table read-only
            }
        };
        scheduleTable = new JTable(scheduleTableModel);
        schedulePanel.add(new JScrollPane(scheduleTable), BorderLayout.CENTER);
        loadEmptySchedule();
    }

    private void setupGpaTab() {
        JPanel gpaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,10,10));
        tabbedPane.addTab("GPA", gpaPanel);

        gpaLabel = new JLabel("Your current GPA: N/A");
        gpaLabel.setFont(new Font("Arial", Font.BOLD, 14));
        calculateGPAButton = new JButton("Calculate GPA");

        gpaPanel.add(gpaLabel);
        gpaPanel.add(calculateGPAButton);
    }

    private void loadEmptySchedule() {

        String[] times = {"9:00-10:00", "10:00-11:00", "11:00-12:00", "13:00-14:00", "14:00-15:00"};
        for (String time : times) {
            scheduleTableModel.addRow(new Object[]{time, "", "", "", "", ""});
        }
    }

    private void loadDataFromDatabase() {
        if (studentDAO == null || courseDAO == null) {
            return;
        }

        try {
            Student freshStudent = studentDAO.getStudentById(student.getUserID());
            if (freshStudent != null) {
                this.student = freshStudent;
            }

            enrolledCoursesTableModel.setRowCount(0);
            for (Course c : student.getCourseList()) {
                enrolledCoursesTableModel.addRow(new Object[]{
                        c.getCourseId(),
                        c.getCourseName(),
                        c.getCourse_day().toString(),
                        c.getStartTime() + "-" + c.getEndTime(),
                        c.getCredits()
                });
            }

            List<Course> allCourses = courseDAO.getAllCourses();
            availableCoursesTableModel.setRowCount(0);
            for (Course c : allCourses) {
                if (!student.getCourseList().contains(c) && c.getQuota() > 0) {
                    availableCoursesTableModel.addRow(new Object[]{
                            c.getCourseId(),
                            c.getCourseName(),
                            c.getQuota(),
                            c.getCourse_day().toString(),
                            c.getStartTime() + "-" + c.getEndTime(),
                            c.getCredits()
                    });
                }
            }

            updateSchedule();

            double currentGPA = student.viewGPA();
            student.setGpa(currentGPA);
            gpaLabel.setText("Your current GPA: " + currentGPA);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data from database: " + e.getMessage());
        }
    }

    private void updateSchedule() {
        for (int row = 0; row < scheduleTableModel.getRowCount(); row++) {
            for (int col = 1; col < scheduleTableModel.getColumnCount(); col++) {
                scheduleTableModel.setValueAt("", row, col);
            }
        }

        for (int i = 0; i < enrolledCoursesTableModel.getRowCount(); i++) {
            String day = (String) enrolledCoursesTableModel.getValueAt(i, 2);
            String time = (String) enrolledCoursesTableModel.getValueAt(i, 3);
            String courseName = (String) enrolledCoursesTableModel.getValueAt(i, 1);

            String[] timeParts = time.split("-");
            String start = timeParts[0].trim();
            int row = findRowByTime(start);
            int col = findColumnByDay(day);
            if (row != -1 && col != -1) {
                scheduleTableModel.setValueAt(courseName, row, col);
            }
        }
    }

    private int findRowByTime(String startTime) {
        String[] parts = startTime.split(":");
        int hour = Integer.parseInt(parts[0]);
        for (int i = 0; i < scheduleTableModel.getRowCount(); i++) {
            String timeRange = (String) scheduleTableModel.getValueAt(i, 0);
            String startRange = timeRange.split("-")[0].trim(); // "9:00"
            String[] rangeParts = startRange.split(":");
            int rangeHour = Integer.parseInt(rangeParts[0]);
            if (rangeHour == hour) {
                return i;
            }
        }
        return -1;
    }

    private int findColumnByDay(String day) {
        day = day.toUpperCase();
        return switch (day) {
            case "MONDAY" -> 1;
            case "TUESDAY" -> 2;
            case "WEDNESDAY" -> 3;
            case "THURSDAY" -> 4;
            case "FRIDAY" -> 5;
            default -> -1;
        };
    }
}
