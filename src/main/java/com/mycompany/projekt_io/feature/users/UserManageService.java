package com.mycompany.projekt_io.feature.users;

import com.mycompany.projekt_io.core.database.UserDAO;
import com.mycompany.projekt_io.datamodel.Permission;
import com.mycompany.projekt_io.datamodel.User;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Serwis odpowiedzialny za zarządzanie kontami użytkowników systemu.
 * <p>
 * Realizuje operacje dodawania, edycji i usuwania użytkowników z automatycznym
 * hashowaniem haseł algorytmem BCrypt przed zapisem do bazy danych.
 * </p>
 *
 * @author Radosław Kruczek
 */
public class UserManageService {

    /**
     * ID wybranego użytkownika, domyślnie ID ustawiane na 0
     */
    int user_id = 0;
    
    /**
     * Dane użytkwonika
     */
    String login, pass, hashedPass;
    /**
     * Uprawnienia użytkownika
     */
    Permission perm;

    /**
     * Zwraca login użytkownika.
     *
     * @return login użytkownika
     */
    public String getLogin() {
        return login;
    }

    /**
     * Zwraca hasło jawne użytkownika.
     *
     * @return hasło w postaci jawnej
     */
    public String getPass() {
        return pass;
    }

    /**
     * Zwraca zahashowane hasło użytkownika.
     *
     * @return hash hasła BCrypt
     */
    public String getHashedPass() {
        return hashedPass;
    }

    /**
     * Zwraca uprawnienia użytkownika.
     *
     * @return obiekt {@link Permission}
     */
    public Permission getPerm() {
        return perm;
    }

    /**
     * Zwraca identyfikator użytkownika.
     *
     * @return identyfikator użytkownika
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
     * Ustawia hasło jawne użytkownika.
     *
     * @param pass hasło w postaci jawnej
     */
    public void setPass(String pass) {
        this.pass = pass;
    }

    /**
     * Ustawia zahashowane hasło użytkownika.
     *
     * @param hashedPass hash hasła BCrypt
     */
    public void setHashedPass(String hashedPass) {
        this.hashedPass = hashedPass;
    }

    /**
     * Ustawia uprawnienia użytkownika.
     *
     * @param perm obiekt {@link Permission}
     */
    public void setPerm(Permission perm) {
        this.perm = perm;
    }

    /**
     * Tworzy serwis dla operacji edycji istniejącego użytkownika.
     *
     * @param user_id identyfikator użytkownika w bazie danych
     * @param login nowy login użytkownika
     * @param pass nowe hasło w postaci jawnej lub {@code null} jeśli bez zmiany
     * @param perm nowe uprawnienia użytkownika
     */
    public UserManageService(int user_id, String login, String pass, Permission perm) {
        this.user_id = user_id;
        this.login = login;
        this.pass = pass;
        this.perm = perm;
    }

    /**
     * Tworzy serwis dla operacji dodawania nowego użytkownika.
     *
     * @param login login nowego użytkownika
     * @param pass hasło w postaci jawnej
     * @param perm uprawnienia nowego użytkownika
     */
    public UserManageService(String login, String pass, Permission perm) {
        this.login = login;
        this.pass = pass;
        this.perm = perm;
    }

    /**
     * Tworzy serwis dla operacji usunięcia użytkownika na podstawie jego ID.
     *
     * @param user_id identyfikator użytkownika do usunięcia
     */
    public UserManageService(int user_id) {
        this.user_id = user_id;
    }

    /**
     * Hashuje hasło jawne algorytmem BCrypt z współczynnikiem kosztu 12.
     * <p>
     * Metoda zwraca {@code false} i nie wykonuje hashowania jeśli hasło jest
     * puste lub ma wartość {@code null}.
     * </p>
     *
     * @return {@code true} jeśli hashowanie zakończyło się sukcesem,
     * {@code false} jeśli hasło jest puste lub null
     */
    public boolean hashPassword() {
        if ("".equals(this.pass) || this.pass == null) {
            return false;
        } else {
            this.hashedPass = BCrypt.hashpw(this.pass, BCrypt.gensalt(12));
            return true;
        }
    }

    /**
     * Dodaje nowego użytkownika do bazy danych.
     * <p>
     * Przed zapisem hashuje hasło algorytmem BCrypt. Zwraca {@code false} jeśli
     * hashowanie się nie powiedzie.
     * </p>
     *
     * @return {@code true} jeśli użytkownik został pomyślnie dodany,
     * {@code false} w przypadku błędu
     */
    public boolean addUser() {
        if (!hashPassword()) {
            return false;
        }
        UserDAO dao = new UserDAO();
        return dao.addUser(login, hashedPass, perm.getPermission_id());
    }

    /**
     * Aktualizuje dane istniejącego użytkownika w bazie danych.
     * <p>
     * Jeśli hasło jest {@code null} aktualizuje tylko login i uprawnienia bez
     * zmiany hasła. Jeśli hasło jest podane — hashuje je przed zapisem.
     * </p>
     *
     * @return {@code true} jeśli aktualizacja zakończyła się sukcesem,
     * {@code false} jeśli ID jest nieprawidłowe lub wystąpił błąd
     */
    public boolean changeUser() {
        UserDAO dao = new UserDAO();
        if (user_id <= 0) {
            return false;
        }
        if (this.pass == null) {
            return dao.changeUser(user_id, login, perm.getPermission_id());
        } else {
            if (!hashPassword()) {
                return false;
            }
            return dao.changeUser(user_id, login, hashedPass, perm.getPermission_id());
        }
    }

    /**
     * Usuwa użytkownika z bazy danych na podstawie jego identyfikatora.
     *
     * @return {@code true} jeśli użytkownik został pomyślnie usunięty,
     * {@code false} jeśli ID jest nieprawidłowe (mniejsze lub równe 0) lub
     * wystąpił błąd bazy danych
     */
    public boolean deleteUser() {
        UserDAO dao = new UserDAO();
        if (user_id <= 0) {
            return false;
        } else {
            return dao.deleteUser(user_id);
        }
    }
}
