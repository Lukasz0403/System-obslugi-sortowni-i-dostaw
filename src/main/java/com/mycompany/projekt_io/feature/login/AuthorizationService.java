/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projekt_io.feature.login;

import com.mycompany.projekt_io.datamodel.User;

/**
 *
 * @author mateu
 */
public class AuthorizationService {
    
//  Metoda sprawdzająca czy użytkownikiem jest admin - Łukasz
    public boolean isAdmin(User user) {
        return user != null && "administrator".equalsIgnoreCase(user.getPermission().getName());
    }

//  Metoda sprawdzająca uprawnienia użytkownika - Łukasz
    public boolean hasPermission(User user, String requiredPermission) {
        return user != null && user.getPermission().getName().equals(requiredPermission);
    }
    
}
