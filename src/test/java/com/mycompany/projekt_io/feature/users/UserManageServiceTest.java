package com.mycompany.projekt_io.feature.users;

import com.mycompany.projekt_io.datamodel.Permission;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testy jednostkowe klasy {@link UserManageService}.
 * <p>
 * Weryfikują poprawność hashowania haseł algorytmem BCrypt oraz walidację
 * operacji na użytkownikach. Testy nie wymagają połączenia z bazą danych.
 * </p>
 * 
 * @author Paweł Pietrusiak
 */
public class UserManageServiceTest {

    private UserManageService userService;

    /**
     * Inicjalizuje serwis z testowymi danymi użytkownika przed każdym testem.
     */
    @BeforeEach
    void setUp() {
        userService = new UserManageService(1, "testUser", "tajne123", new Permission(1, "ADMIN"));
    }

    /**
     * Sprawdza czy metoda {@code hashPassword()} poprawnie hashuje hasło
     * algorytmem BCrypt i zwraca hash weryfikowalny przez
     * {@code BCrypt.checkpw()}.
     */
    @Test
    public void testHashPassword_Success() {
        boolean success = userService.hashPassword();

        assertTrue(success, "Haszowanie powinno się udać dla poprawnego hasła");
        assertNotNull(userService.getHashedPass(), "Hasz nie może być pusty");
        assertNotEquals("tajne123", userService.getHashedPass(), "Hasło jawne nie może być przechowywane w hashedPass");
        assertTrue(BCrypt.checkpw("tajne123", userService.getHashedPass()), "Hash musi być poprawny względem hasła");
    }

    /**
     * Sprawdza czy metoda {@code hashPassword()} zwraca false gdy hasło jest
     * pustym ciągiem znaków.
     */
    @Test
    public void testHashPassword_EmptyPassword_ShouldFail() {
        userService.setPass("");

        boolean success = userService.hashPassword();

        assertFalse(success, "Haszowanie pustego hasła powinno zwrócić false");
    }

    /**
     * Sprawdza czy metoda {@code hashPassword()} zwraca false gdy hasło ma
     * wartość null.
     */
    @Test
    public void testHashPassword_NullPassword_ShouldFail() {
        userService.setPass(null);

        boolean success = userService.hashPassword();

        assertFalse(success, "Haszowanie hasła o wartości null powinno zwrócić false");
    }

    /**
     * Sprawdza czy metoda {@code deleteUser()} zwraca false gdy identyfikator
     * użytkownika wynosi 0 lub mniej.
     */
    @Test
    public void testDeleteUser_InvalidId_ShouldFail() {
        UserManageService invalidUser = new UserManageService(0);

        boolean deleted = invalidUser.deleteUser();

        assertFalse(deleted, "Usuwanie użytkownika z ID <= 0 powinno być niemożliwe");
    }
}
