package com.mycompany.projekt_io.datamodel;
/**
 *
 * @author lukas
 */
public class Warehouse extends Zone {
    
    
    private String[] zonesID;
    private int capacity;

    public Warehouse(String[] zonesID, int capacity, int zone_id) {
        super(zone_id);
        this.zonesID = zonesID;
        this.capacity = capacity;
    }

    public String[] getZonesID() {
        return zonesID;
    }

    public void setZonesID(String[] zonesID) {
        this.zonesID = zonesID;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getZone_id() {
        return zone_id;
    }

    public void setZone_id(int zone_id) {
        this.zone_id = zone_id;
    }

    
}
