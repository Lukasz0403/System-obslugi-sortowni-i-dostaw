/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projekt_io.datamodel;

/**
 *
 * @author mateu
 */
public class Zone {
    
    int zone_id;

    public int getZone_id() {
        return zone_id;
    }

    public void setZone_id(int zone_id) {
        this.zone_id = zone_id;
    }

    public Zone(int zone_id) {
        this.zone_id = zone_id;
    }

    @Override
    public String toString() {
        return "Zone{" + "zone_id=" + zone_id + '}';
    }
    
    
}
