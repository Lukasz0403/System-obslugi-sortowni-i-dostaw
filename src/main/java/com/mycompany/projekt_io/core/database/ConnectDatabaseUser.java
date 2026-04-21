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
    
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
//            conn = DriverManager.getConnection("jdbc:mysql://192.168.0.73/login_credentials","root","1234");
//            conn = DriverManager.getConnection("jdbc:mysql://localhost/login_credentials","root","1234");
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } 
    }
    
    public static Connection getConnection() {
        try {
            //Wywaliłem zmienną conn i wywołuję połączenie bezpośrednio w tym miejscu. 
            //Był problem po wpisaniu błędnego hasła taki że w kolejnej próbie nawet poprawnym hasłem nie dało się zalogować.
            //W klasie DAO użytkownik zamyka połączenie i w przypadku logowania kolejnego użytkownika i tak by to nie działało.
            //W razie potrzeby zostawiam zakomentowane poprzednie rozwiązanie.
            return 
//                  DriverManager.getConnection("jdbc:mysql://192.168.0.73/login_credentials","root","1234");
                    DriverManager.getConnection("jdbc:mysql://localhost/login_credentials", "root", "1234");
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    

}