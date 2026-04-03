/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projekt_io.datamodel;

/**
 *
 * @author Radosław
 */
public class Permission {
    
    int permission_id;
    String name;

    public int getPermission_id() {
        return permission_id;
    }

    public String getName() {
        return name;
    }

    public void setPermission_id(int permission_id) {
        this.permission_id = permission_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Permission(int permission_id, String name) {
        this.permission_id = permission_id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Permission{" + "permission_id=" + permission_id + ", name=" + name + '}';
    }
    
    
}
