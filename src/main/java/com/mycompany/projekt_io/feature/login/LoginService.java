    /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projekt_io.feature.login;

import com.mycompany.projekt_io.core.database.UserDAO;
import com.mycompany.projekt_io.core.database.UserDAOInterface;
import com.mycompany.projekt_io.datamodel.User;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Serwis odpowiedzialny za uwierzytelnianie użytkowników w systemie.
 * <p>
 * Pobiera dane użytkownika z bazy danych za pośrednictwem
 * {@link UserDAOInterface}, a następnie weryfikuje podane hasło przy użyciu
 * algorytmu BCrypt.
 * </p>
 *
 * @author Łukasz Motyka
 */
public class LoginService {
    
    private final UserDAOInterface userDAO = new UserDAO();

    /**
     * Uwierzytelnia użytkownika na podstawie loginu i hasła.
     * <p>
     * Metoda pobiera użytkownika z bazy danych po podanym loginie, a następnie
     * porównuje podane hasło jawne z hashem BCrypt przechowywanym w bazie.
     * Jeśli hasło jest poprawne, zwraca obiekt użytkownika. W przypadku
     * niepoprawnego formatu hasha w bazie danych błąd jest logowany na
     * standardowe wyjście błędów, a metoda zwraca {@code null}.
     * </p>
     *
     * @param login login użytkownika używany do wyszukania go w bazie danych
     * @param plainPassword hasło w postaci jawnej (niezaszyfrowanej) podane
     * przez użytkownika
     * @return obiekt {@link User} jeśli uwierzytelnianie zakończyło się
     * sukcesem, {@code null} jeśli użytkownik nie istnieje, hasło jest
     * niepoprawne lub hash w bazie ma nieprawidłowy format
     */
    public User authenticate(String login, String plainPassword) {
        User user = userDAO.getUser(login);

        if (user != null) {
            try {
                if (BCrypt.checkpw(plainPassword, user.getPassword())) {
                    return user;
                }
            } catch (IllegalArgumentException e) {
                System.err.println("Error: Invalid BCrypt hash format in database!");
            }
        }
        return null;
    }
    
}
