/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projekt_io.datamodel;

/**
 * Model danych reprezentujący region kurierski.
 * <p>
 * Każda paczka posiada przypisany region nadania oraz region docelowy.
 * Region identyfikowany jest trzyliterowym kodem (np. "WAW", "KRK")
 * odpowiadającym miastu obsługiwanemu przez dany oddział kurierski.
 * </p>
 *
 * @author Radosław Kruczek
 */
public class Region {

    int region_id;
    String region_name;

    /**
     * Tworzy obiekt regionu kurierskiego.
     *
     * @param region_id   unikalny identyfikator regionu
     * @param region_name trzyliterowy kod regionu (np. "WAW", "GDA")
     */
    public Region(int region_id, String region_name) {
        this.region_id = region_id;
        this.region_name = region_name;
    }

    /** @return unikalny identyfikator regionu */
    public int getRegion_id() { return region_id; }

    /** @return trzyliterowy kod regionu kurierskiego */
    public String getRegion_name() { return region_name; }

    /** @param region_id unikalny identyfikator regionu */
    public void setRegion_id(int region_id) { this.region_id = region_id; }

    /** @param region_name trzyliterowy kod regionu */
    public void setRegion_name(String region_name) { this.region_name = region_name; }

    @Override
    public String toString() {
        return "Region{" + "region_id=" + region_id + ", region_name=" + region_name + '}';
    }
}
