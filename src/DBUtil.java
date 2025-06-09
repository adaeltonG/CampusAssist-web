package src;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    private static String DB_URL;

    public static void setDbUrl(String dbPath) {
        DB_URL = "jdbc:sqlite:" + dbPath;
    }

    static {
        try {
            System.out.println("Loading SQLite JDBC driver...");
            Class.forName("org.sqlite.JDBC");
            System.out.println("Loaded SQLite JDBC driver successfully!");
        } catch (ClassNotFoundException e) {
            System.err.println("ClassNotFoundException: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to load SQLite JDBC driver.", e);
        } catch (Throwable t) {
            System.err.println("Unexpected error in DBUtil static block: " + t.getMessage());
            t.printStackTrace();
            throw new RuntimeException("Unexpected error in DBUtil static block.", t);
        }
    }

    public static Connection getConnection() throws SQLException {
        if (DB_URL == null) {
            throw new IllegalStateException("DB URL not set!");
        }
        return DriverManager.getConnection(DB_URL);
    }
} 