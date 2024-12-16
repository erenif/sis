package DAOs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class AbstractDB {
    protected Connection connection;

    // Constructor to initialize the database connection
    public AbstractDB(Connection connection) {
        this.connection = connection;
    }
    protected ResultSet executeQuery(String query, Object... params) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        setParameters(preparedStatement, params);
        return preparedStatement.executeQuery();
    }

    protected int executeUpdate(String query, Object... params) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        setParameters(preparedStatement, params);
        return preparedStatement.executeUpdate();
    }

    private void setParameters(PreparedStatement preparedStatement, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            preparedStatement.setObject(i + 1, params[i]);
        }
    }
}
