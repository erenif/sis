package View;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class LoginPanel extends JPanel {
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
        roleSelector = new JComboBox<>(new String[]{"Admin", "User"});
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


    }
}



