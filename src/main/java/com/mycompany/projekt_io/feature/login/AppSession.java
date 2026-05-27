package com.mycompany.projekt_io.feature.login;

import com.mycompany.projekt_io.datamodel.User;

/**
 * Klasa przechowująca dane sesji zalogowanego użytkownika.
 * <p>
 * Singleton zapewniający dostęp do aktualnie zalogowanego użytkownika
 * oraz umożliwiający wylogowanie przez wyczyszczenie sesji.
 * </p>
 */
public class AppSession {

    private static User currentUser = null;

    /**
     * Ustawia zalogowanego użytkownika.
     *
     * @param user zalogowany użytkownik
     */
    public static void login(User user) {
        currentUser = user;
    }

    /**
     * Zwraca aktualnie zalogowanego użytkownika.
     *
     * @return obiekt {@link User} lub {@code null} jeśli nikt nie jest zalogowany
     */
    public static User getCurrentUser() {
        return currentUser;
    }

    /**
     * Wylogowuje użytkownika — czyści dane sesji.
     */
    public static void logout() {
        currentUser = null;
    }

    /**
     * Sprawdza czy użytkownik jest zalogowany.
     *
     * @return {@code true} jeśli sesja jest aktywna
     */
    public static boolean isLoggedIn() {
        return currentUser != null;
    }
}