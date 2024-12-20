import Utils.DatabaseConnection;
import View.LoginPanel;

import javax.swing.*;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try (Connection connection = DatabaseConnection.getDataSource().getConnection()) {
                JFrame frame = new JFrame("Login");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(400, 300);
                frame.setLocationRelativeTo(null);
                frame.add(new LoginPanel(frame, connection));
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
