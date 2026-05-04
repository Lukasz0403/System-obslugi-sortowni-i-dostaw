package com.mycompany.projekt_io.core.database;

import com.mycompany.projekt_io.datamodel.Permission;
import com.mycompany.projekt_io.datamodel.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementacja interfejsu {@link UserDAOInterface} zapewniająca dostęp do
 * danych użytkowników w bazie danych MySQL.
 * <p>
 * Klasa realizuje operacje pobierania użytkowników, pobierania uprawnień oraz
 * dodawania nowych użytkowników. Połączenie z bazą danych pobierane jest za
 * pośrednictwem {@link ConnectDatabaseUser}.
 * </p>
 */

public class UserDAO implements UserDAOInterface {

    /**
     * Pobiera użytkownika z bazy danych na podstawie jego loginu.
     * <p>
     * Zapytanie łączy tabele {@code users} i {@code permissions}, aby pobrać
     * jednocześnie dane użytkownika i jego uprawnienia.
     * </p>
     *
     * @param login login użytkownika do wyszukania
     * @return obiekt {@link User} z danymi użytkownika i jego uprawnieniami lub
     * {@code null} jeśli użytkownik o podanym loginie nie istnieje bądź
     * wystąpił błąd bazy danych
     */
    @Override
    public User getUser(String login) {
        User user = null;
        Connection conn = ConnectDatabaseUser.getConnection();
        try {
            String sql = "SELECT * FROM users JOIN permissions ON permission = permission_id WHERE login = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, login);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                user = new User(rs.getString("login"), rs.getString("password"), new Permission(rs.getInt("permission_id"), rs.getString("name")));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return user;
    }

    /**
     * Pobiera listę wszystkich użytkowników systemu wraz z ich uprawnieniami.
     * <p>
     * Zapytanie łączy tabele {@code users} i {@code permissions}.
     * </p>
     *
     * @return lista obiektów {@link User} reprezentujących wszystkich
     * użytkowników systemu; pusta lista jeśli brak użytkowników lub wystąpił
     * błąd bazy danych
     */
    @Override
    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        try {
            Connection conn = ConnectDatabaseUser.getConnection();
            String sql = "SELECT * FROM users JOIN permissions ON permission = permission_id";
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                users.add(new User(rs.getInt("user_id"), rs.getString("login"), rs.getString("password"), new Permission(rs.getInt("permission_id"), rs.getString("name"))));
            }
            conn.close();
        } catch (SQLException ex) {
        }
        return users;
    }

    /**
     * Pobiera listę wszystkich dostępnych uprawnień z bazy danych.
     *
     * @return lista obiektów {@link Permission} z tabeli {@code permissions};
     * pusta lista jeśli brak uprawnień lub wystąpił błąd bazy danych
     */
    @Override
    public List<Permission> getPermissions() {
        List<Permission> permissions = new ArrayList<>();
        Connection conn = ConnectDatabaseUser.getConnection();
        try {
            String sql = "SELECT * FROM permissions";
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                permissions.add(new Permission(rs.getInt("permission_id"), rs.getString("name")));
            }
        } catch (SQLException ex) {
        } 
        return permissions;
    }


    /**
     * Dodaje nowego użytkownika do bazy danych.
     * <p>
     * Hasło musi być przekazane już w postaci zahashowanej algorytmem BCrypt —
     * metoda nie wykonuje samego hashowania. Hashowanie realizowane jest w
     * warstwie serwisowej przed wywołaniem tej metody.
     * </p>
     *
     * @param login login nowego użytkownika; musi być unikalny w tabeli
     * {@code users}
     * @param hashedPassword hasło użytkownika zahashowane algorytmem BCrypt
     * @param permissionId identyfikator uprawnienia z tabeli
     * {@code permissions}
     * @return {@code true} jeśli użytkownik został pomyślnie dodany,
     * {@code false} jeśli wystąpił błąd bazy danych (np. duplikat loginu)
     */
    @Override
    public boolean addUser(String login, String hashedPassword, int permissionId) {
        Connection conn = ConnectDatabaseUser.getConnection();
        try {
            String sql = "INSERT INTO users (login, password, permission) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, login);
            ps.setString(2, hashedPassword);
            ps.setInt(3, permissionId);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean changeUser(int id, String login, String pass, int permId) {
        try {
            Connection conn = ConnectDatabaseUser.getConnection();

            String sql = "UPDATE users SET login = ?, password = ?, permission = ? WHERE user_id = ?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, login);
            ps.setString(2, pass);
            ps.setInt(3, permId);
            ps.setInt(4, id);

            int rows = ps.executeUpdate();

            return rows > 0; 

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    @Override
    public boolean changeUser(int id, String login, int permId) {
        try {
            Connection conn = ConnectDatabaseUser.getConnection();

            String sql = "UPDATE users SET login = ?, permission = ? WHERE user_id = ?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, login);
            ps.setInt(2, permId);
            ps.setInt(3, id);

            int rows = ps.executeUpdate();

            return rows > 0; 

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }   
    }
    
    @Override
    public boolean deleteUser(int id) {

        try {
            Connection conn = ConnectDatabaseUser.getConnection();

            String sql = "DELETE FROM users WHERE user_id = ?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, id);

            int rows = ps.executeUpdate();

            return rows > 0; 

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }   
    }
}