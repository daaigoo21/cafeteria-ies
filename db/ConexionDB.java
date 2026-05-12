package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {

    private static final String URL = "jdbc:mysql://localhost:3306/cafeteria_ies?useSSL=false&serverTimezone=Europe/Madrid";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    private ConexionDB() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void iniciarTransaccion(Connection con) throws SQLException {
        con.setAutoCommit(false);
    }

    public static void confirmar(Connection con) throws SQLException {
        con.commit();
    }

    public static void revertir(Connection con) {
        if (con != null) {
            try {
                con.rollback();
            } catch (SQLException ignored) {
            }
        }
    }
}
