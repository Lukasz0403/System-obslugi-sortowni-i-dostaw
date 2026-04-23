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

public class UserDAO implements UserDAOInterface {

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

    @Override
    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        try {
            Connection conn = ConnectDatabaseUser.getConnection();
            String sql = "SELECT * FROM users JOIN permissions ON permission = permission_id";
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                users.add(new User(rs.getString("login"), rs.getString("password"), new Permission(rs.getInt("permission_id"), rs.getString("name"))));
            }
            conn.close();
        } catch (SQLException ex) {
        }
        return users;
    }

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
}