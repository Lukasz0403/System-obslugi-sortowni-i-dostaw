package com.mycompany.projekt_io.feature.login;

import com.mycompany.projekt_io.datamodel.Permission;
import com.mycompany.projekt_io.datamodel.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AuthorizationServiceTest {

    private AuthorizationService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthorizationService();
    }

    @Test
    public void testIsAdmin_Success() {
        // Test autoryzacji
        // Uzytkownik jest adminem, wiec autoryzacja admina powinna zwrocic TRUE
        
        // Tworzy uprawnienia i uzytkownika
        Permission adminPerm = new Permission(1, "administrator");
        User adminUser = new User("boss", "hash", adminPerm);
        // Test autoryzacji 
        boolean result = authService.isAdmin(adminUser);
        // Sprawdza czy test zwrocil True
        assertTrue(result, "Powinno zwrócić true dla uprawnienia 'administrator'");
    }

    @Test
    public void testIsAdmin_Fail_WrongRole() {
        // Test autoryzacji 
        // Uzytkownik nie jest adminem, wiec autoryzacja admina powinna zwrocic FALSE
        
        // Tworzenie uprawnien i uzytkownika
        Permission workerPerm = new Permission(2, "pracownik");
        User normalUser = new User("kowalski", "hash", workerPerm);

        // Test autoryzacji
        boolean result = authService.isAdmin(normalUser);

        // Sprawdza czy test zwrocil False
        assertFalse(result, "Powinno zwrócić false, bo pracownik to nie administrator");
    }

    @Test
    public void testHasPermission_CaseSensitiveSuccess() {
        // Test czy autoryzacja przyjmuje nazwe roli niezaleznie od wielkosci liter
        
        Permission p = new Permission(3, "Magazynier");
        
        User user = new User("stasiu", "hash", p);

        // "Magazynier" == "Magazynier" -> True
        assertTrue(authService.hasPermission(user, "Magazynier"));
        
        // "Magazynier" == "magazynier" -> False
        assertFalse(authService.hasPermission(user, "magazynier"), "Wielkość liter powinna mieć znaczenie!");
    }

    @Test
    public void testIsAdmin_NullUser() {
        // Testujemy czy serwis nie zwróciNullPointerException gdy user jest null
        assertFalse(authService.isAdmin(null), "Dla null zawsze powinno być false");
    }
}