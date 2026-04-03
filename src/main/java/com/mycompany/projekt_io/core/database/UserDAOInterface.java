/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projekt_io.core.database;

import com.mycompany.projekt_io.datamodel.User;
import java.util.List;

/**
 *
 * @author Radosław
 */
public interface UserDAOInterface {
    User getUser(String login);
    List<User> getUsers();
}
