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

    /**
     * Unikalny identyfikator regału.
     */
    int rack_id;

    /**
     * Strefa magazynowa, do której przypisany jest regał.
     */
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
     * Zwraca unikalny identyfikator regału.
     *
     * * @return unikalny identyfikator regału
     */
    public int getRack_id() {
        return rack_id;
    }

    /**
     * Zwraca strefu magazynową, do której należy regał.
     *
     * * @return strefa magazynowa, do której należy regał
     */
    public Zone getZone() {
        return zone;
    }

    /**
     * Ustawia unikalny identyfikator regału.
     *
     * * @param rack_id unikalny identyfikator regału
     */
    public void setRack_id(int rack_id) {
        this.rack_id = rack_id;
    }

    /**
     * Ustawia strefę magazynową regału.
     *
     * * @param zone strefa magazynowa regału
     */
    public void setZone(Zone zone) {
        this.zone = zone;
    }

    @Override
    public String toString() {
        return "Rack{" + "rack_id=" + rack_id + ", zone=" + zone + '}';
    }
}
