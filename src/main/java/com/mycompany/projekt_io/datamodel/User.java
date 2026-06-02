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

    /**
     * Unikalny identyfikator użytkownika.
     */
    int user_id;

    /**
     * Login użytkownika służący do uwierzytelniania.
     */
    String login;

    /**
     * Zahashowane hasło użytkownika.
     */
    String password;

    /**
     * Obiekt uprawnień przypisany do użytkownika.
     */
    Permission permission;

    /**
     * Zwraca login użytkownika.
     *
     * @return login użytkownika
     */
    public String getLogin() {
        return login;
    }

    /**
     * Zwraca zahashowane hasło użytkownika.
     *
     * @return zahashowane hasło użytkownika
     */
    public String getPassword() {
        return password;
    }

    /**
     * Zwraca uprawnienia przypisane do użytkownika.
     *
     * @return uprawnienia użytkownika
     */
    public Permission getPermission() {
        return permission;
    }

    /**
     * Zwraca unikalny identyfikator użytkownika.
     *
     * @return unikalny identyfikator użytkownika
     */
    public int getUser_id() {
        return user_id;
    }

    /**
     * Ustawia login użytkownika.
     *
     * @param login nowy login użytkownika
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * Ustawia zahashowane hasło użytkownika.
     *
     * @param password nowe zahashowane hasło użytkownika
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Przypisuje nowe uprawnienia dla użytkownika.
     *
     * @param permission nowy obiekt uprawnień
     */
    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    /**
     * Tworzy obiekt użytkownika bez określania identyfikatora (np. przed
     * zapisem w bazie).
     *
     * @param login login użytkownika
     * @param password zahashowane hasło użytkownika
     * @param permission uprawnienia użytkownika
     */
    public User(String login, String password, Permission permission) {
        this.login = login;
        this.password = password;
        this.permission = permission;
    }

    /**
     * Tworzy obiekt użytkownika wraz z jego identyfikatorem z bazy danych.
     *
     * @param user_id unikalny identyfikator użytkownika
     * @param login login użytkownika
     * @param password zahashowane hasło użytkownika
     * @param permission uprawnienia użytkownika
     */
    public User(int user_id, String login, String password, Permission permission) {
        this.user_id = user_id;
        this.login = login;
        this.password = password;
        this.permission = permission;
    }

    @Override
    public String toString() {
        return "User{" + "login=" + login + ", password=" + password
                + ", permission=" + permission + '}';
    }
}
