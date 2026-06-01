package com.mycompany.projekt_io.feature.login;

import com.mycompany.projekt_io.datamodel.Permission;
import com.mycompany.projekt_io.datamodel.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testy jednostkowe klasy {@link AuthorizationService}.
 * <p>
 * Weryfikują poprawność metod autoryzacji użytkowników — sprawdzanie roli
 * administratora oraz weryfikację uprawnień z uwzględnieniem wielkości liter.
 * Klasa nie wymaga mocków ponieważ nie komunikuje się z bazą danych.
 * </p>
 * 
 * @author Paweł Pietrusiak
 */
public class AuthorizationServiceTest {

    private AuthorizationService authService;

    /**
     * Inicjalizuje instancję {@link AuthorizationService} przed każdym testem.
     */
    @BeforeEach
    void setUp() {
        authService = new AuthorizationService();
    }

    /**
     * Sprawdza czy metoda {@code isAdmin()} zwraca true dla użytkownika z
     * uprawnieniem o nazwie "administrator".
     */
    @Test
    public void testIsAdmin_Success() {
        Permission adminPerm = new Permission(1, "administrator");
        User adminUser = new User("boss", "hash", adminPerm);

        boolean result = authService.isAdmin(adminUser);

        assertTrue(result, "Powinno zwrócić true dla uprawnienia 'administrator'");
    }

    /**
     * Sprawdza czy metoda {@code isAdmin()} zwraca false dla użytkownika z
     * uprawnieniem innym niż administrator.
     */
    @Test
    public void testIsAdmin_Fail_WrongRole() {
        Permission workerPerm = new Permission(2, "pracownik");
        User normalUser = new User("kowalski", "hash", workerPerm);

        boolean result = authService.isAdmin(normalUser);

        assertFalse(result, "Powinno zwrócić false, bo pracownik to nie administrator");
    }

    /**
     * Sprawdza czy metoda {@code hasPermission()} rozróżnia wielkość liter w
     * nazwie roli — "Magazynier" i "magazynier" to dwie różne wartości.
     */
    @Test
    public void testHasPermission_CaseSensitiveSuccess() {
        Permission p = new Permission(3, "Magazynier");
        User user = new User("stasiu", "hash", p);

        assertTrue(authService.hasPermission(user, "Magazynier"));
        assertFalse(authService.hasPermission(user, "magazynier"), "Wielkość liter powinna mieć znaczenie!");
    }

    /**
     * Sprawdza czy metoda {@code isAdmin()} zwraca false bez rzucania wyjątku
     * {@code NullPointerException} gdy użytkownik jest null.
     */
    @Test
    public void testIsAdmin_NullUser() {
        assertFalse(authService.isAdmin(null), "Dla null zawsze powinno być false");
    }
}
