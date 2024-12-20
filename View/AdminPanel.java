package View;

import DAOs.ProfessorDAO;
import DAOs.StudentDAO;
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
    private JButton createStudentButton;
    private JButton editStudentButton;
    private JButton deleteStudentButton;

    private JTable professorsTable;
    private DefaultTableModel professorsTableModel;
    private JButton createProfessorButton;
    private JButton editProfessorButton;
    private JButton deleteProfessorButton;

    private StudentDAO studentDAO;
    private ProfessorDAO professorDAO;

    public AdminPanel(StudentDAO studentDAO, ProfessorDAO professorDAO) {
        this.studentDAO = studentDAO;
        this.professorDAO = professorDAO;

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

        JTabbedPane tabbedPane = new JTabbedPane();
        JPanel studentsPanel = new JPanel(new BorderLayout(10,10));
        studentsPanel.setBorder(new EmptyBorder(10,10,10,10));
        tabbedPane.addTab("Students", studentsPanel);

        JPanel professorsPanel = new JPanel(new BorderLayout(10,10));
        professorsPanel.setBorder(new EmptyBorder(10,10,10,10));
        tabbedPane.addTab("Professors", professorsPanel);

        add(tabbedPane, BorderLayout.CENTER);

        // Students Panel
        JLabel studentsLabel = new JLabel("Manage Students:");
        studentsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        studentsPanel.add(studentsLabel, BorderLayout.NORTH);

        studentsTableModel = new DefaultTableModel(new Object[]{"Student ID", "Student Name", "GPA", "Available Credits"}, 0);
        studentsTable = new JTable(studentsTableModel);
        studentsPanel.add(new JScrollPane(studentsTable), BorderLayout.CENTER);

        JPanel studentButtonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,10,10));
        createStudentButton = new JButton("Create Student");
        editStudentButton = new JButton("Edit Student");
        deleteStudentButton = new JButton("Delete Student");
        studentButtonsPanel.add(createStudentButton);
        studentButtonsPanel.add(editStudentButton);
        studentButtonsPanel.add(deleteStudentButton);
        studentsPanel.add(studentButtonsPanel, BorderLayout.SOUTH);

        // Professors Panel
        JLabel professorsLabel = new JLabel("Manage Professors:");
        professorsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        professorsPanel.add(professorsLabel, BorderLayout.NORTH);

        professorsTableModel = new DefaultTableModel(new Object[]{"Professor ID", "Professor Name"}, 0);
        professorsTable = new JTable(professorsTableModel);
        professorsPanel.add(new JScrollPane(professorsTable), BorderLayout.CENTER);

        JPanel professorButtonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,10,10));
        createProfessorButton = new JButton("Create Professor");
        editProfessorButton = new JButton("Edit Professor");
        deleteProfessorButton = new JButton("Delete Professor");
        professorButtonsPanel.add(createProfessorButton);
        professorButtonsPanel.add(editProfessorButton);
        professorButtonsPanel.add(deleteProfessorButton);
        professorsPanel.add(professorButtonsPanel, BorderLayout.SOUTH);

        loadDataFromDatabase();

        // TODO: ActionListener'larda DAO çağrıları yaparak student ve professor ekleme/silme/güncelleme
        // Şimdilik bunlar TODO olarak kalıyor.

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loadDataFromDatabase() {
        try {
            studentsTableModel.setRowCount(0);
            List<Student> students = studentDAO.getAllStudents();
            for (Student s : students) {
                studentsTableModel.addRow(new Object[]{ s.getUserID(), s.getUserName(), s.getGpa(), s.getAvailableCredits() });
            }

            professorsTableModel.setRowCount(0);
            List<Professor> professors = professorDAO.getAllProfessors();
            for (Professor p : professors) {
                professorsTableModel.addRow(new Object[]{ p.getUserID(), p.getUserName() });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage());
        }
    }
}
