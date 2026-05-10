/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projekt_io.feature.login;

import com.mycompany.projekt_io.core.database.ConnectDatabasePackage;
import com.mycompany.projekt_io.core.database.ConnectDatabaseUser;
import javafx.application.Platform;
import java.sql.SQLException;

/**
 * Klasa narzędziowa odpowiedzialna za bezpieczne zamknięcie aplikacji.
 * <p>
 * Zamyka połączenia z bazami danych, wylogowuje użytkownika
 * i kończy działanie aplikacji JavaFX.
 * </p>
 */
public class AppCloser {

    /**
     * Bezpiecznie zamyka aplikację.
     * <p>
     * Kolejność operacji:
     * </p>
     * <ol>
     *   <li>Wylogowanie użytkownika ({@link AppSession#logout()})</li>
     *   <li>Zamknięcie połączenia z bazą paczek</li>
     *   <li>Zamknięcie połączenia z bazą użytkowników</li>
     *   <li>Zakończenie działania platformy JavaFX ({@link Platform#exit()})</li>
     * </ol>
     */
    public static void closeApp() {
        // Wyloguj użytkownika
        AppSession.logout();

        // Zamknij połączenia z bazą
        try {
            if (ConnectDatabasePackage.getConnection() != null
                    && !ConnectDatabasePackage.getConnection().isClosed()) {
                ConnectDatabasePackage.getConnection().close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (ConnectDatabaseUser.getConnection() != null
                    && !ConnectDatabaseUser.getConnection().isClosed()) {
                ConnectDatabaseUser.getConnection().close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Zamknij aplikację
        Platform.exit();
    }
}
