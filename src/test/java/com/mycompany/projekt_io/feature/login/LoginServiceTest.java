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

public class LoginServiceTest {

    private LoginService loginService;
    private UserDAOInterface mockUserDAO;
    
    
    
    @BeforeEach
    void setUp() {
        // Tworzymy falszywe DAO
        mockUserDAO = mock(UserDAOInterface.class);
        // podpinsmy je pod nasza klase
        loginService = new LoginService(mockUserDAO);
    }

    @Test
    public void testAuthenticate_Success() {
        // Test poprawnego logowania 
        
        // Tworzymy uzytkownika
        // Login i haslo
        String login = "lukasz";
        String plainPassword = "mojeHaslo123";
        // Musimy zahashować hasło, bo serwis wywoła BCrypt.checkpw
        String hashedInDatabase = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
        // uzytkownik
        User User = new User(login, hashedInDatabase, null); 

        // Programowanie mocka : Gdy spytają o login, oddaj fakeUsera
        when(mockUserDAO.getUser(login)).thenReturn(User);

        // logowanie
        User result = loginService.authenticate(login, plainPassword);

        // Test czy logowanie poszlo pomyslnie
        assertNotNull(result, "Logowanie powinno się udać");
        assertEquals(login, result.getLogin(), "Login powinien być taki sam");
        
        // Sprawdzenie czy przeslany login jest poprawny
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(mockUserDAO).getUser(captor.capture());
        
        System.out.println("Złapany login wysłany do bazy: " + captor.getValue());
    }
    
    @Test
    public void testAuthenticate_UserNotFound() {
        // test gdy nie ma uzytkownika w bazie 

        // Przygotowanie uzytkownika
        String login = "nieistniejacy_user";
        String password = "jakiesHaslo";

        // Programowanie mocka : gdy spytaja o login, nic nie zwracaj
        when(mockUserDAO.getUser(login)).thenReturn(null);

        // Logowanie
        User result = loginService.authenticate(login, password);

        // 3. 
        // Test logowania
        assertNull(result, "Powinien zwrócić null, gdy użytkownik nie istnieje w bazie");

        // Sprawdzamy, czy serwis w ogóle zapytał bazę o tego usera
        verify(mockUserDAO, times(1)).getUser(login);
    }
        @Test
        public void testAuthenticate_WrongPassword() {
            // test gdy podajemy zle haslo

            // Tworzenie uzytkownika
            String login = "tester";
            String correctPassword = "dobreHaslo";
            String wrongPassword = "zleHaslo";

            // Generujemy hash z DOBREGO hasła
            String hashedInDatabase = BCrypt.hashpw(correctPassword, BCrypt.gensalt());

            // Tworzymy usera
            User user = new User(login, hashedInDatabase, null);

            // Programujemy mocka : Gdy spytaja o usera, zwroc im usera
            when(mockUserDAO.getUser(login)).thenReturn(user);

            // Logowanie zlym haslem
            User result = loginService.authenticate(login, wrongPassword);

            // Test co zwrocilo logowanie
            assertNull(result, "Powinien zwrócić null, bo hasło jest niepoprawne");

            // Weryfikujemy czy serwis w ogóle sprawdził tego usera w bazie
            verify(mockUserDAO).getUser(login);
        }
}