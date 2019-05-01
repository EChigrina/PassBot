package com.elizavetachigrina;

import java.sql.*;

public class DatabaseConnector {
    private static Connection dbConnection = null;

    public static int connect() {
        final String DB_URL = "jdbc:postgresql://127.0.0.1:5432/passbot";
        final String USER = "admin";
        final String PASS = "admin";

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver is not found. Include it in your library path ");
            e.printStackTrace();
            return 1;
        }

        System.out.println("PostgreSQL JDBC Driver successfully connected");
//        dbConnection = null;

        try {
            dbConnection = DriverManager
                    .getConnection(DB_URL, USER, PASS);

        } catch (SQLException e) {
            System.out.println("Connection Failed");
            e.printStackTrace();
            return 1;
        }

        if (dbConnection != null) {
            System.out.println("You successfully connected to database now");
            return 0;
        } else {
            System.out.println("Failed to make connection to database");
            return 1;
        }
    }

    public static void createDbTables() throws SQLException {
        Statement statement = null;

        String createTablePersonSQL = "CREATE TABLE USERS("
                + "VKID VARCHAR(20) PRIMARY KEY, "
                + "PASSWORD VARCHAR(20)"
                + ")";

        String createTablePasswordsSQL = "CREATE TABLE PASSWORDS("
                + "VKID VARCHAR(20) REFERENCES USERS(VKID) ON DELETE CASCADE, "
                + "NAME VARCHAR(50) NOT NULL, "
                + "PASSWORD VARCHAR(20) NOT NULL,"
                + "PRIMARY KEY (VKID, NAME)"
                + ")";

        if (dbConnection == null) {
            System.out.println("Connection hasn't been established");
            return;
        }
        try {
            statement = dbConnection.createStatement();

            // выполнить SQL запрос
            if (statement.execute(createTablePersonSQL)) {
                System.out.println("Error creating table \"USERS\"");
            } else {
                System.out.println("Table \"USERS\" is created!");
            }
            if (statement.execute(createTablePasswordsSQL)) {
                System.out.println("Error creating table \"PASSWORDS\"");
            } else {
                System.out.println("Table \"PASSWORDS\" is created!");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    public static void addPerson(String vkid) throws  SQLException {
        String insert = "INSERT INTO USERS(VKID) VALUES(?);";
        PreparedStatement ps = dbConnection.prepareStatement(insert);

        ps.setString(1, vkid);

        ResultSet rs = ps.executeQuery();
    }

    public static void addPerson(String vkid, String password) throws  SQLException {
        String insert = "INSERT INTO USERS(VKID,PASSWORD) VALUES(?, ?);";
        PreparedStatement ps = dbConnection.prepareStatement(insert);

        ps.setString(1, vkid);
        ps.setString(2, password);

        ResultSet rs = ps.executeQuery();
    }

    /**
     * @param vkid Id пользователя
     * @param password новый мастер-пароль
     * @throws SQLException
     * Метод обновляет мастер-пароль пользователя для доступа к сохранённым паролям
     */
    public static void updatePerson(String vkid, String password) throws  SQLException {
        String query = "UPDATE USERS " +
                "SET PASSWORD = ? " +
                "WHERE VKID = ?;";
        PreparedStatement ps = dbConnection.prepareStatement(query);

        ps.setString(1, password);
        ps.setString(2, vkid);

        ps.executeUpdate();
    }

    public static void addPassword(String vkid, String name, String password) throws  SQLException {
        String insert = "INSERT INTO PASSWORDS(VKID, NAME, PASSWORD) VALUES(?, ?, ?);";
        PreparedStatement ps = dbConnection.prepareStatement(insert);

        ps.setString(1, vkid);
        ps.setString(2, name);
        ps.setString(3, password);

        ResultSet rs = ps.executeQuery();
    }

    public static void updatePassword(String vkid, String name, String password) throws  SQLException {
        String query = "UPDATE PASSWORDS " +
                "SET PASSWORD = ? " +
                "WHERE VKID = ? " +
                "AND NAME = ?;";
        PreparedStatement ps = dbConnection.prepareStatement(query);

        ps.setString(1, password);
        ps.setString(2, vkid);
        ps.setString(3, name);

        ps.executeUpdate();
    }

    public static boolean ifPersonExists(String vkid) throws  SQLException {
        String exists = "SELECT EXISTS(SELECT 1 FROM USERS WHERE vkid LIKE \'" + vkid + "\')";
        Statement statement = dbConnection.createStatement();
        ResultSet rs = statement.executeQuery(exists);
        rs.next();
        return rs.getBoolean("exists");
    }

    public static boolean ifPasswordExists(String vkid, String name) throws  SQLException {
        String exists = "SELECT EXISTS(SELECT 1 FROM PASSWORDS " +
                "WHERE VKID LIKE \'" + vkid + "\' " +
                "AND NAME LIKE \'" + name + "\')";
        Statement statement = dbConnection.createStatement();
        ResultSet rs = statement.executeQuery(exists);
        rs.next();
        return rs.getBoolean("exists");
    }

    public static void closeConnection() {
        try {
            if (dbConnection != null) {
                dbConnection.close();
            }
        } catch (SQLException e) {
            System.out.println("Error closing connection");
            System.out.println(e.getMessage());
        }
    }
}
