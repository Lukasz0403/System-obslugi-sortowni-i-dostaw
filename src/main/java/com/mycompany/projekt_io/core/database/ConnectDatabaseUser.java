package com.mycompany.projekt_io.core.database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author mateu
 */
public final class ConnectDatabaseUser {
//    private static Connection conn = null;
//    
//    static {
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
////            conn = DriverManager.getConnection("jdbc:mysql://192.168.0.73/login_credentials","root","1234");
//            conn = DriverManager.getConnection("jdbc:mysql://localhost/login_credentials","root","");
//        }  catch (ClassNotFoundException | SQLException ex) {
//            ex.printStackTrace();
//        } 
//    }
//    
//    public static Connection getConnection() {
//        return conn;
//    }
        
            private static Connection conn = null;

    static {
        connect();
    }

    private static void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            //conn = DriverManager.getConnection("jdbc:mysql://192.168.0.73/login_credentials","root","1234");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/login_credentials", "root", "");
            conn.setAutoCommit(true);
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static Connection getConnection() {
        try {
            if (conn == null || conn.isClosed() || !conn.isValid(2)) {
                connect();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            connect();
        }
        return conn;
    }
}