package com.mycompany.projekt_io.core.database;

import com.mycompany.projekt_io.datamodel.Permission;
import com.mycompany.projekt_io.datamodel.User;
import java.util.List;

public interface UserDAOInterface {
    User getUser(String login);
    List<User> getUsers();
    
    // EXPERIMENTAL, REMOVE IF ALREADY EXISTS
    List<Permission> getPermissions();
    // EXPERIMENTAL, REMOVE IF ALREADY EXISTS
    boolean addUser(String login, String hashedPassword, int permissionId);
}
