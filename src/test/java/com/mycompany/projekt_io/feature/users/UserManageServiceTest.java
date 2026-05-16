package com.mycompany.projekt_io.feature.users;

import com.mycompany.projekt_io.datamodel.Permission;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import static org.junit.jupiter.api.Assertions.*;

public class UserManageServiceTest {

    private UserManageService userService;

    @BeforeEach
    void setUp() {
        // Przygotowanie danych testowych: ID 1, login "testUser", hasło "tajne123"
        userService = new UserManageService(1, "testUser", "tajne123", new Permission(1, "ADMIN"));
    }

    /**
     * Test 1: Sprawdza, czy haszowanie zmienia hasło na bezpieczny ciąg znaków
     */
    @Test
    public void testHashPassword_Success() {
        // Test hashowania
        // Sprawdza czy haslo jest poprawnie hashowane
        
        boolean success = userService.hashPassword();
        
        assertTrue(success, "Haszowanie powinno się udać dla poprawnego hasła");
        assertNotNull(userService.getHashedPass(), "Hasz nie może być pusty");
        assertNotEquals("tajne123", userService.getHashedPass(), "Hasło jawne nie może być przechowywane w hashedPass");
        
        // Weryfikacja za pomocą BCrypt - sprawdza czy hasło pasuje do hasha
        assertTrue(BCrypt.checkpw("tajne123", userService.getHashedPass()), "Hash musi być poprawny względem hasła");
    }

    
    @Test
    public void testHashPassword_EmptyPassword_ShouldFail() {
        // Test hashowania
        // Sprawdza czy mozna hashowac puste haslo, powinno byc False
        userService.setPass(""); // Ustawiamy puste hasło
        boolean success = userService.hashPassword();
        
        assertFalse(success, "Haszowanie pustego hasła powinno zwrócić false");
    }

    @Test
    public void testHashPassword_NullPassword_ShouldFail() {
        // Test hashowania
        // Sprawdza czy mozna hashowac haslo ktore jest "null"
        userService.setPass(null); // Ustawiamy null
        boolean success = userService.hashPassword();
        
        assertFalse(success, "Haszowanie hasła o wartości null powinno zwrócić false");
    }

    @Test
    public void testDeleteUser_InvalidId_ShouldFail() {
        // Ustawiamy ID na 0, co według logiki kodu powinno zwrócić false
        UserManageService invalidUser = new UserManageService(0); 
        
        boolean deleted = invalidUser.deleteUser();
        assertFalse(deleted, "Usuwanie użytkownika z ID <= 0 powinno być niemożliwe");
    }
}