/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projekt_io.datamodel;

/**
 * Model danych reprezentujący użytkownika systemu.
 * <p>
 * Przechowuje dane logowania oraz uprawnienia użytkownika. Hasło przechowywane
 * jest w postaci zahashowanej algorytmem BCrypt.
 * </p>
 *
 * @author Radosław Kruczek
 */
public class User {

    String login, password;
    Permission permission;

    /**
     * Tworzy obiekt użytkownika systemu.
     *
     * @param login login użytkownika używany do uwierzytelniania
     * @param password hasło użytkownika w postaci hasha BCrypt
     * @param permission uprawnienia przypisane do użytkownika
     */
    public User(String login, String password, Permission permission) {
        this.login = login;
        this.password = password;
        this.permission = permission;
    }

    /**
     * @return login użytkownika
     */
    public String getLogin() {
        return login;
    }

    /**
     * @return hasło użytkownika w postaci hasha BCrypt
     */
    public String getPassword() {
        return password;
    }

    /**
     * @return uprawnienia użytkownika
     */
    public Permission getPermission() {
        return permission;
    }

    /**
     * @param login login użytkownika
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * @param password hasło w postaci hasha BCrypt
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @param permission uprawnienia użytkownika
     */
    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    @Override
    public String toString() {
        return "User{" + "login=" + login + ", password=" + password
                + ", permission=" + permission + '}';
    }
}
