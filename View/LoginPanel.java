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

        // ENTER BUTTON
        JButton enterButton = new JButton("Login");
        enterButton.setPreferredSize(new Dimension(180, 40));
        enterButton.setBackground(new Color(255, 255, 255));
        enterButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        userPanel.add(enterButton);

        // ADD USER PANEL TO FRAME USING GRIDBAG LAYOUT
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 10, 10, 10); // Margins around the panel
        gbc.anchor = GridBagConstraints.CENTER;
        add(userPanel, gbc);

        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String role = (String) roleSelector.getSelectedItem();
                // Perform login logic here
                if (role.equals("Admin") && username.equals("admin") && password.equals("admin123")) {
                    parentFrame.dispose(); // Close the login frame
                    JFrame adminFrame = new JFrame("Admin Panel");
                    adminFrame.setSize(400, 300);
                    adminFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    adminFrame.add(new JLabel("Welcome to Admin Panel", SwingConstants.CENTER));
                    adminFrame.setVisible(true);
                } else if (role.equals("User") && username.equals("user") && password.equals("user123")) {
                    parentFrame.dispose(); // Close the login frame
                    JFrame userFrame = new JFrame("User Panel");
                    userFrame.setSize(400, 300);
                    userFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    userFrame.add(new JLabel("Welcome to User Panel", SwingConstants.CENTER));
                    userFrame.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Login failed. Please check your credentials and role.");
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw background color
        g.setColor(new Color(240, 240, 240));
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}




