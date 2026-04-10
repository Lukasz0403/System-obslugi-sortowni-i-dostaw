/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projekt_io.core.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author mateu
 */
public final class ConnectDatabasePackage {
    
    private static Connection conn = null;
    
    static {
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://192.168.0.73/package_storage","root","1234");
            //conn = DriverManager.getConnection("jdbc:mysql://localhost/package_storage","root","1234");
//          /*Baza danych bez hasła ;p */conn = DriverManager.getConnection("jdbc:mysql://localhost/package_storage","root","");
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        } 
    }
    
    public static Connection getConnection() {
       
        return conn;
    }

    private ConnectDatabasePackage() {
    }
}
