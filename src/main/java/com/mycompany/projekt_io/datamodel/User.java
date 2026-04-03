/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projekt_io.datamodel;

/**
 *
 * @author mateu
 */
public class User {
    
    String login, password;
    
    Permission permission;

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    public User(String login, String password, Permission permission) {
        this.login = login;
        this.password = password;
        this.permission = permission;
    }

    @Override
    public String toString() {
        return "User{" + "login=" + login + ", password=" + password + ", permission=" + permission + '}';
    }
    
    
}
