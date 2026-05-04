/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projekt_io.feature.users;

import com.mycompany.projekt_io.core.database.UserDAO;
import com.mycompany.projekt_io.datamodel.Permission;
import com.mycompany.projekt_io.datamodel.User;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author mateu
 */
public class UserManageService {
    
    int user_id = 0;
    String login, pass, hashedPass;
    Permission perm;

    public String getLogin() {
        return login;
    }

    public String getPass() {
        return pass;
    }

    public String getHashedPass() {
        return hashedPass;
    }

    public Permission getPerm() {
        return perm;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setHashedPass(String hashedPass) {
        this.hashedPass = hashedPass;
    }

    public void setPerm(Permission perm) {
        this.perm = perm;
    }

    public UserManageService(int user_id, String login, String pass, Permission perm) {
        this.user_id = user_id;
        this.login = login;
        this.pass = pass;
        this.perm = perm;
    }

    public UserManageService(String login, String pass, Permission perm) {
        this.login = login;
        this.pass = pass;
        this.perm = perm;
    }

    public UserManageService(int user_id) {
        this.user_id = user_id;
    }
    
    public boolean hashPassword() {
        
        if("".equals(this.pass) || this.pass == null) {
            return false;
        } else {
            
            this.hashedPass = BCrypt.hashpw(this.pass, BCrypt.gensalt(12));
            
            return true;
        }
    }
    
    
    public boolean addUser() {
        
        if(!hashPassword()) {
            return false;
        }
        
        UserDAO dao = new UserDAO();
        
        return dao.addUser(login, hashedPass, perm.getPermission_id());
    }
    
    public boolean changeUser() {
        
        UserDAO dao = new UserDAO();
        
        if(user_id <= 0) return false;
        
        if(this.pass == null) {
            return dao.changeUser(user_id, login, perm.getPermission_id());
        } else {
            if(!hashPassword()) {
                return false;
            }
            return dao.changeUser(user_id, login, hashedPass, perm.getPermission_id());
        }
    }
    
    public boolean deleteUser() {
        
        UserDAO dao = new UserDAO();
        
        if(user_id <= 0) {
            return false;
        } else {
            return dao.deleteUser(user_id);
        }    
    }
}
