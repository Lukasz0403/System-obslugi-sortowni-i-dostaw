/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projekt_io.core.database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Klasa odpowiedzialna za zarządzanie połączeniem z bazą danych MySQL systemu
 * magazynowego.
 * <p>
 * Implementuje wzorzec Singleton - w całej aplikacji istnieje tylko jedno
 * współdzielone połączenie z bazą danych. Połączenie jest nawiązywane
 * automatycznie przy pierwszym użyciu klasy (blok statyczny) i odnawiane w
 * razie potrzeby przy każdym wywołaniu {@link #getConnection()}. Klasa jest
 * oznaczona jako {@code final}.
 * </p>
 *
 * @author Radosław Kruczek
 */
public final class ConnectDatabasePackage {

//    private static Connection conn = null;
//
//    static {
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//            //conn = DriverManager.getConnection("jdbc:mysql://192.168.0.73/package_storage","root","1234");
//            conn = DriverManager.getConnection("jdbc:mysql://localhost/package_storage","root","");
//            //conn = DriverManager.getConnection("jdbc:mysql://localhost/package_storage","root","");
//        } catch (ClassNotFoundException | SQLException ex) {
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

    
    
    /**
     * Nawiązuje połączenie z bazą danych i przechowuje je w statycznym polu.
     * <p>
     * Ładuje sterownik MySQL, tworzy nowe
     * połączenie z lokalną bazą danych {@code package_storage} i ustawia tryb
     * automatycznego zatwierdzania transakcji ({@code autoCommit = true}). W
     * przypadku błędu ładowania sterownika lub błędu połączenia wyjątek jest
     * logowany na standardowe wyjście błędów.
     * </p>
     */
    private static void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            //conn = DriverManager.getConnection("jdbc:mysql://192.168.0.73/package_storage","root","1234");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/package_storage", "root", "1234");
            conn.setAutoCommit(true);
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }

    
    
    /**
     * Zwraca aktywne połączenie z bazą danych.
     * <p>
     * Przed zwróceniem połączenia sprawdza czy jest ono wciąż aktywne czy nie
     * jest {@code null}, czy nie zostało zamknięte oraz czy odpowiada na ping w
     * ciągu 2 sekund ({@code isValid(2)}). Jeśli którykolwiek z warunków nie
     * jest spełniony, połączenie jest automatycznie odnawiane przez ponowne
     * wywołanie {@link #connect()}. Dzięki temu aplikacja jest odporna na
     * zrywanie połączenia bez konieczności restartu.
     * </p>
     *
     * @return aktywne połączenie {@link Connection} z bazą danych, lub
     * {@code null} jeśli ponowne połączenie również się nie powiodło
     */
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