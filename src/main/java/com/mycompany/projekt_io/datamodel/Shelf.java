/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projekt_io.datamodel;

/**
 *
 * @author mateu
 */
public class Shelf {
    
    int shelf_id;
    Zone zone;

    public int getShelf_id() {
        return shelf_id;
    }

    public Zone getZone() {
        return zone;
    }

    public void setShelf_id(int shelf_id) {
        this.shelf_id = shelf_id;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public Shelf(int shelf_id, Zone zone) {
        this.shelf_id = shelf_id;
        this.zone = zone;
    }

    @Override
    public String toString() {
        return "Shelf{" + "shelf_id=" + shelf_id + ", zone=" + zone + '}';
    }
    
    
    
}
