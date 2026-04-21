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
 *
 * @author mateu
 */
public class LoginService {
    
    private final UserDAOInterface userDAO = new UserDAO();

    //Funkcja do logowania - Łukasz
    public User authenticate(String login, String plainPassword) {
        User user = userDAO.getUser(login);

        if (user != null) {
            try {
                if (BCrypt.checkpw(plainPassword, user.getPassword())) {
                    return user;
                }
            } catch (IllegalArgumentException e) {
                System.err.println("Błąd: Format hasha w bazie jest niepoprawny!");
            }
        }
        return null;
    }
    
}
