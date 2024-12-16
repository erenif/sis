package View;

import Model.Admin;
import javax.swing.*;
import java.awt.*;

public class AdminPanel extends JFrame {
    private Admin admin;

    public AdminPanel(Admin admin) {
        this.admin = admin;
        setTitle("Admin Panel - " + admin.getAdminName());
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Welcome to the Admin Panel, " + admin.getAdminName(), SwingConstants.CENTER);
        add(welcomeLabel, BorderLayout.CENTER);

        // Additional admin-related UI and controls

        setLocationRelativeTo(null);
        setVisible(true);
    }
}

