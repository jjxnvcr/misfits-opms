package app.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;

public class Connect {
    private static final String server = "localhost";
    private static final String instance = "SQLEXPRESS";
    private static final String database = "Misfits";
    private static final boolean windowsAuth = true;
    private static final boolean encrypted = false;
    private static final boolean trustCert = true;

    public static Connection openConnection() throws SQLException, SQLTimeoutException {
        String connUrl = String.format("jdbc:sqlserver://%s\\%s;databaseName=%s;integratedSecurity=%b;trustServerCertificate=%b;encrypt=%b", server, instance, database, windowsAuth, trustCert, encrypted);

        return DriverManager.getConnection(connUrl);
    }

    public static void closeConnection(Connection connection) throws SQLException {
        connection.close();
    }
}
