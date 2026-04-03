/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
 *
 * @author mateu
 */
public class UserDAO implements UserDAOInterface{
    
    @Override
    public User getUser(String login) {
        
        User user = null;
        
        Connection conn = ConnectDatabaseUser.getConnection();
        
        String sql = "SELECT * FROM users JOIN permissions ON permission = permission_id WHERE login = ?";
         
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            
            ps.setString(1, login);
            
            ResultSet rs = ps.executeQuery();
            
            
            while(rs.next()) {
                
                user = new User(rs.getString("login"), rs.getString("password"), new Permission(rs.getInt("permission_id"), rs.getString("name")));
            }
            
        } catch (SQLException ex) {
            System.getLogger(UserDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
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
    
    
    
    
    
}
