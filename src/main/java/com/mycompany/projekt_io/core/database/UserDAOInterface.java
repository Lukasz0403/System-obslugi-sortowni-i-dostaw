package com.mycompany.projekt_io.core.database;

import com.mycompany.projekt_io.datamodel.Permission;
import com.mycompany.projekt_io.datamodel.User;
import java.util.List;

/**
 * Interfejs definiujący kontrakt dla operacji na danych użytkowników systemu.
 * <p>
 * Określa operacje pobierania, dodawania użytkowników oraz pobierania
 * dostępnych uprawnień. Implementowany przez {@link UserDAO}.
 * </p>
 * 
 * @author Radosław Kruczek
 */
public interface UserDAOInterface {
    User getUser(String login);
    List<User> getUsers();
    List<Permission> getPermissions();
    boolean addUser(String login, String hashedPassword, int permissionId);
    boolean changeUser(int id, String login, String pass, int permId);
    boolean changeUser(int id, String login, int permId);
    boolean deleteUser(int id);
}
