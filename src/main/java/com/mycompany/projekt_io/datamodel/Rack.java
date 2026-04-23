package com.mycompany.projekt_io.datamodel;
/**
 *
 * @author lukas
 */
public class Rack {
    int rack_id;
    Zone zone;

    public int getRack_id() {
        return rack_id;
    }

    public void setRack_id(int rack_id) {
        this.rack_id = rack_id;
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public Rack(int rack_id, Zone zone) {
        this.rack_id = rack_id;
        this.zone = zone;
    }

    @Override
    public String toString() {
        return "Rack{" + "rack_id=" + rack_id + ", zone=" + zone + '}';
    } 
}
