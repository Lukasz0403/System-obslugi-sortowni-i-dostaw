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
    
    int user_id;
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

    public UserManageService(String login, String pass, Permission perm) {
        this.login = login;
        this.pass = pass;
        this.perm = perm;
    }
    
    public boolean hashPassword() {
        
        if(this.pass == "") {
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
        
        if(dao.addUser(login, hashedPass, perm.getPermission_id())) {
            return true;
        } else {
            return false;
        }
    }
    
    public boolean changeUser() {
        
        if(!hashPassword()) {
            return false;
        }
        
        UserDAO dao = new UserDAO();
        
        if(dao.changeUser(user_id, login, hashedPass, perm.getPermission_id())) {
            return true;
        } else {
            return false;
        }
    }
    
}
