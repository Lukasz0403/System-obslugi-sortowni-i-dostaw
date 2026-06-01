package com.mycompany.projekt_io.feature.login;

import com.mycompany.projekt_io.core.database.UserDAOInterface;
import com.mycompany.projekt_io.datamodel.User;
import com.mycompany.projekt_io.datamodel.Permission;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mindrot.jbcrypt.BCrypt;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testy jednostkowe klasy {@link LoginService}.
 * <p>
 * Weryfikują poprawność uwierzytelniania użytkowników — logowanie z poprawnymi
 * danymi, nieistniejącym loginem oraz błędnym hasłem. Testy wykorzystują mock
 * obiektu {@link UserDAOInterface} eliminując zależność od bazy danych.
 * </p>
 * 
 * @author Paweł Pietrusiak
 */
public class LoginServiceTest {

    private LoginService loginService;
    private UserDAOInterface mockUserDAO;

    /**
     * Inicjalizuje mock {@link UserDAOInterface} i wstrzykuje go do testowanego
     * serwisu przez konstruktor.
     */
    @BeforeEach
    void setUp() {
        mockUserDAO = mock(UserDAOInterface.class);
        loginService = new LoginService(mockUserDAO);
    }

    /**
     * Sprawdza czy metoda {@code authenticate()} zwraca obiekt {@link User} gdy
     * login istnieje w bazie i hasło zgadza się z hashem BCrypt. Dodatkowo
     * weryfikuje przez {@code ArgumentCaptor} że do bazy przekazano dokładnie
     * ten login który podał użytkownik.
     */
    @Test
    public void testAuthenticate_Success() {
        String login = "lukasz";
        String plainPassword = "mojeHaslo123";
        String hashedInDatabase = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
        User user = new User(login, hashedInDatabase, null);

        when(mockUserDAO.getUser(login)).thenReturn(user);

        User result = loginService.authenticate(login, plainPassword);

        assertNotNull(result, "Logowanie powinno się udać");
        assertEquals(login, result.getLogin(), "Login powinien być taki sam");

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(mockUserDAO).getUser(captor.capture());
        System.out.println("Złapany login wysłany do bazy: " + captor.getValue());
    }

    /**
     * Sprawdza czy metoda {@code authenticate()} zwraca null gdy użytkownik o
     * podanym loginie nie istnieje w bazie danych. Weryfikuje że serwis wykonał
     * dokładnie jedno zapytanie do bazy.
     */
    @Test
    public void testAuthenticate_UserNotFound() {
        String login = "nieistniejacy_user";
        String password = "jakiesHaslo";

        when(mockUserDAO.getUser(login)).thenReturn(null);

        User result = loginService.authenticate(login, password);

        assertNull(result, "Powinien zwrócić null, gdy użytkownik nie istnieje w bazie");
        verify(mockUserDAO, times(1)).getUser(login);
    }

    /**
     * Sprawdza czy metoda {@code authenticate()} zwraca null gdy użytkownik
     * istnieje w bazie ale podane hasło nie zgadza się z przechowywanym hashem
     * BCrypt.
     */
    @Test
    public void testAuthenticate_WrongPassword() {
        String login = "tester";
        String correctPassword = "dobreHaslo";
        String wrongPassword = "zleHaslo";
        String hashedInDatabase = BCrypt.hashpw(correctPassword, BCrypt.gensalt());
        User user = new User(login, hashedInDatabase, null);

        when(mockUserDAO.getUser(login)).thenReturn(user);

        User result = loginService.authenticate(login, wrongPassword);

        assertNull(result, "Powinien zwrócić null, bo hasło jest niepoprawne");
        verify(mockUserDAO).getUser(login);
    }
}
