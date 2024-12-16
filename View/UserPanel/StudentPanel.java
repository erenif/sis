package View.UserPanel;

import Model.Student;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StudentPanel extends JFrame {
    private Student student;

    // Tabs
    private JTabbedPane tabbedPane;

    // Courses Tab Components
    private JTable enrolledCoursesTable;
    private DefaultTableModel enrolledCoursesTableModel;
    private JTable availableCoursesTable;
    private DefaultTableModel availableCoursesTableModel;
    private JButton addCourseButton;
    private JButton dropCourseButton;

    // Schedule Tab Components
    // We'll represent the schedule as a simple table: columns = Monday-Friday, rows = hours (example)
    private JTable scheduleTable;
    private DefaultTableModel scheduleTableModel;

    // GPA Tab Components
    private JLabel gpaLabel;
    private JButton calculateGPAButton;

    public StudentPanel(Student student) {
        this.student = student;
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

        tabbedPane = new JTabbedPane();
        add(tabbedPane, BorderLayout.CENTER);

        // Set up the three tabs:
        setupCoursesTab();
        setupScheduleTab();
        setupGpaTab();

        // Load mock data
        loadMockData();

        // Action listeners (no DAO logic, just comments)
        addCourseButton.addActionListener(e -> {
            int selectedRow = availableCoursesTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Select a course from the available courses table to add.");
                return;
            }

            int courseId = (int) availableCoursesTableModel.getValueAt(selectedRow, 0);
            int quota = (int) availableCoursesTableModel.getValueAt(selectedRow, 2);

            if (quota <= 0) {
                JOptionPane.showMessageDialog(this, "Cannot add course. Quota full.");
                return;
            }

            // TODO: DAO logic to enroll student in course if credits and quota allow
            // After successful enrollment:
            // Decrement quota in available courses table
            availableCoursesTableModel.setValueAt(quota - 1, selectedRow, 2);

            // Add the course to enrolled courses table
            Object[] courseData = new Object[]{
                    courseId,
                    availableCoursesTableModel.getValueAt(selectedRow, 1), // course name
                    availableCoursesTableModel.getValueAt(selectedRow, 3), // day
                    availableCoursesTableModel.getValueAt(selectedRow, 4), // time
                    availableCoursesTableModel.getValueAt(selectedRow, 5)  // credits
            };
            enrolledCoursesTableModel.addRow(courseData);

            // Refresh schedule (TODO: in real scenario, recalculate schedule from enrolled courses)
            updateSchedule();
            JOptionPane.showMessageDialog(this, "Course added successfully!");
        });

        dropCourseButton.addActionListener(e -> {
            int selectedRow = enrolledCoursesTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Select a course from the enrolled courses table to drop.");
                return;
            }

            int courseId = (int) enrolledCoursesTableModel.getValueAt(selectedRow, 0);

            // TODO: DAO logic to drop the course from the student's schedule and update credits
            enrolledCoursesTableModel.removeRow(selectedRow);

            // Update the available courses quota if needed
            // For example, find the course in availableCoursesTable and increment its quota by 1
            // In a real scenario, you would know which row in availableCoursesTable matches this courseId.
            // Here, just for demonstration:
            for (int i = 0; i < availableCoursesTableModel.getRowCount(); i++) {
                int avCourseId = (int) availableCoursesTableModel.getValueAt(i, 0);
                if (avCourseId == courseId) {
                    int currentQuota = (int) availableCoursesTableModel.getValueAt(i, 2);
                    availableCoursesTableModel.setValueAt(currentQuota + 1, i, 2);
                    break;
                }
            }

            // Refresh schedule
            updateSchedule();
            JOptionPane.showMessageDialog(this, "Course dropped successfully.");
        });

        calculateGPAButton.addActionListener(e -> {
            // TODO: DAO logic or model logic to calculate GPA from completed courses and grades
            // For now, just mock
            double mockGpa = 3.4; // just a placeholder
            gpaLabel.setText("Your current GPA: " + mockGpa);
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

        // Top: Enrolled Courses
        JPanel enrolledPanel = new JPanel(new BorderLayout(5,5));
        enrolledPanel.setBorder(new EmptyBorder(5,5,5,5));
        enrolledPanel.add(new JLabel("Enrolled Courses:"), BorderLayout.NORTH);
        enrolledCoursesTableModel = new DefaultTableModel(new Object[]{"Course ID", "Course Name", "Day", "Time", "Credits"}, 0);
        enrolledCoursesTable = new JTable(enrolledCoursesTableModel);
        enrolledPanel.add(new JScrollPane(enrolledCoursesTable), BorderLayout.CENTER);

        // Bottom: Available Courses + add/drop buttons
        JPanel availablePanel = new JPanel(new BorderLayout(5,5));
        availablePanel.setBorder(new EmptyBorder(5,5,5,5));
        availablePanel.add(new JLabel("Available Courses:"), BorderLayout.NORTH);
        availableCoursesTableModel = new DefaultTableModel(new Object[]{"Course ID", "Course Name", "Quota", "Day", "Time", "Credits"}, 0);
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

        // Columns: Monday to Friday
        // Rows: Just a few time slots as example
        scheduleTableModel = new DefaultTableModel(new Object[]{"Time", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"}, 0);
        scheduleTable = new JTable(scheduleTableModel);
        schedulePanel.add(new JScrollPane(scheduleTable), BorderLayout.CENTER);

        // Initially load empty schedule
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
        // Example timeslots
        String[] times = {"9:00-10:00", "10:00-11:00", "11:00-12:00", "1:00-2:00", "2:00-3:00"};
        for (String time : times) {
            scheduleTableModel.addRow(new Object[]{time, "", "", "", "", ""});
        }
    }

    private void updateSchedule() {
        // TODO: In real scenario, use student's enrolled courses to fill the schedule
        // Clear current schedule
        for (int row = 0; row < scheduleTableModel.getRowCount(); row++) {
            for (int col = 1; col < scheduleTableModel.getColumnCount(); col++) {
                scheduleTableModel.setValueAt("", row, col);
            }
        }

        // Mock: Fill schedule based on enrolled courses
        // For each enrolled course, parse its day & time and place it on schedule
        // Here we just show a mock process:
        for (int i = 0; i < enrolledCoursesTableModel.getRowCount(); i++) {
            String day = (String) enrolledCoursesTableModel.getValueAt(i, 2);
            String time = (String) enrolledCoursesTableModel.getValueAt(i, 3);
            String courseName = (String) enrolledCoursesTableModel.getValueAt(i, 1);

            // In a real scenario, parse day & time and find the matching slot in schedule
            // For mock, if day = "Wednesday" and time = "11.00-12.00" we put it in schedule
            // Just a demo: If it's "Wednesday, 11.00-12.00" (from the example format)
            // find that row and column.

            // We'll just do a simple match:
            String[] timeParts = time.split("-");
            String start = timeParts[0].trim();
            // Convert start to row index
            int row = findRowByTime(start);
            int col = findColumnByDay(day);
            if (row != -1 && col != -1) {
                scheduleTableModel.setValueAt(courseName, row, col);
            }
        }
    }

    private int findRowByTime(String startTime) {
        // Example: "11.00"
        // Our schedule rows are 9:00-10:00, 10:00-11:00, etc.
        // Just match first hour
        int hour = Integer.parseInt(startTime.split("\\.")[0]);
        for (int i = 0; i < scheduleTableModel.getRowCount(); i++) {
            String timeRange = (String) scheduleTableModel.getValueAt(i, 0);
            if (timeRange.startsWith(String.valueOf(hour))) {
                return i;
            }
        }
        return -1;
    }

    private int findColumnByDay(String day) {
        // Columns: Time, Monday, Tuesday, Wednesday, Thursday, Friday
        switch (day) {
            case "Monday": return 1;
            case "Tuesday": return 2;
            case "Wednesday": return 3;
            case "Thursday": return 4;
            case "Friday": return 5;
            default: return -1;
        }
    }

    private void loadMockData() {
        // Mock enrolled courses
        // TODO: Replace with DAO call to get currently enrolled courses
        enrolledCoursesTableModel.addRow(new Object[]{201, "Data Structures", "Wednesday", "11.00-12.00", 3});
        enrolledCoursesTableModel.addRow(new Object[]{202, "Algorithms", "Friday", "10.00-11.00", 3});

        // Mock available courses
        // TODO: Replace with DAO call to get all available courses
        availableCoursesTableModel.addRow(new Object[]{301, "Database Systems", 10, "Monday", "9.00-10.00", 3});
        availableCoursesTableModel.addRow(new Object[]{302, "Operating Systems", 5, "Tuesday", "11.00-12.00", 4});

        // Update schedule based on enrolled courses
        updateSchedule();
    }

    // Test main
    /*
    public static void main(String[] args) {
        // Mock a student
        Student mockStudent = new Student(101, "John Doe");
        SwingUtilities.invokeLater(() -> new StudentPanel(mockStudent));
    }

     */
}