package com.mycompany.projekt_io.datamodel;
/**
 * Model danych reprezentujący regał magazynowy.
 * <p>
 * Każdy regał należy do jednej strefy magazynowej ({@link Zone}). Paczki są
 * przypisywane do regałów po przyjęciu na magazyn.
 * </p>
 *
 * @author Radosław Kruczek
 */
public class Rack {

    int rack_id;
    Zone zone;

    /**
     * Tworzy obiekt regału magazynowego.
     *
     * @param rack_id unikalny identyfikator regału
     * @param zone strefa magazynowa, do której należy regał
     */
    public Rack(int rack_id, Zone zone) {
        this.rack_id = rack_id;
        this.zone = zone;
    }

    /**
     * @return unikalny identyfikator regału
     */
    public int getRack_id() {
        return rack_id;
    }

    /**
     * @return strefa magazynowa, do której należy regał
     */
    public Zone getZone() {
        return zone;
    }

    /**
     * @param rack_id unikalny identyfikator regału
     */
    public void setRack_id(int rack_id) {
        this.rack_id = rack_id;
    }

    /**
     * @param zone strefa magazynowa regału
     */
    public void setZone(Zone zone) {
        this.zone = zone;
    }

    @Override
    public String toString() {
        return "Rack{" + "rack_id=" + rack_id + ", zone=" + zone + '}';
    }
}
