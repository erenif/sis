package DAOs;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDAO extends AbstractDB {

    public AdminDAO(Connection connection) {
        super(connection);
    }

    // Admin kimlik doğrulama
    public boolean verifyAdminCredentials(String username, String password) throws SQLException {
        String query = "SELECT admin_id FROM Admin_Table WHERE admin_name = ? AND password = ?";
        ResultSet resultSet = executeQuery(query, username, password);
        return resultSet.next();
    }

    // Yeni bir admin ekle
    public void addAdmin(int adminId, String adminName, String password) throws SQLException {
        String query = "INSERT INTO Admin_Table (admin_id, admin_name, password) VALUES (?, ?, ?)";
        executeUpdate(query, adminId, adminName, password);
    }

    // Admin bilgilerini güncelle
    public void updateAdmin(int adminId, String adminName, String password) throws SQLException {
        String query = "UPDATE Admin_Table SET admin_name = ?, password = ? WHERE admin_id = ?";
        executeUpdate(query, adminName, password, adminId);
    }

    // Admin'i sil
    public void deleteAdmin(int adminId) throws SQLException {
        String query = "DELETE FROM Admin_Table WHERE admin_id = ?";
        executeUpdate(query, adminId);
    }
}
