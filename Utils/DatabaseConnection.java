package Utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class DatabaseConnection {
    private static final HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/ucss"); // Veritabanı URL'si
        config.setUsername("root"); // Veritabanı kullanıcı adı
        config.setPassword("new_password"); // Veritabanı şifresi
        config.setMaximumPoolSize(200); // Maksimum bağlantı sayısı
        config.setIdleTimeout(30000); // Boşta bekleyen bağlantıların süre aşımı (ms)
        config.setConnectionTimeout(30000); // Bağlantı oluşturma zaman aşımı (ms)
        config.setLeakDetectionThreshold(2000); // Bağlantı sızıntı tespiti (ms)
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");

        dataSource = new HikariDataSource(config);
    }

    public static DataSource getDataSource() {
        return dataSource;
    }
}