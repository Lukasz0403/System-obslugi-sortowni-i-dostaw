/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projekt_io.datamodel;

/**
 *
 * @author mateu
 */
public class Region {
    int region_id;
    String region_name;

    public int getRegion_id() {
        return region_id;
    }

    public String getRegion_name() {
        return region_name;
    }

    public void setRegion_id(int region_id) {
        this.region_id = region_id;
    }

    public void setRegion_name(String region_name) {
        this.region_name = region_name;
    }

    public Region(int region_id, String region_name) {
        this.region_id = region_id;
        this.region_name = region_name;
    }

    @Override
    public String toString() {
        return "Region{" + "region_id=" + region_id + ", region_name=" + region_name + '}';
    } 
}
