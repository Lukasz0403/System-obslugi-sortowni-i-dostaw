package com.mycompany.projekt_io.datamodel;

/**
 * Model danych reprezentujący strefę magazynową.
 * <p>
 * Magazyn podzielony jest na strefy, z których każda zawiera określoną liczbę
 * regałów ({@link Rack}). Strefa identyfikowana jest numerycznym
 * identyfikatorem.
 * </p>
 *
 * @author Radosław Kruczek
 */
public class Zone {

    /**
     * Unikalny identyfikator strefy magazynowej.
     */
    int zone_id;

    /**
     * Tworzy obiekt strefy magazynowej.
     *
     * @param zone_id unikalny identyfikator strefy
     */
    public Zone(int zone_id) {
        this.zone_id = zone_id;
    }

    /**
     * Zwraca unikalny identyfikator strefy magazynowej.
     *
     * @return unikalny identyfikator strefy magazynowej
     */
    public int getZone_id() {
        return zone_id;
    }

    /**
     * Ustawia unikalny identyfikator strefy magazynowej.
     *
     * @param zone_id unikalny identyfikator strefy magazynowej
     */
    public void setZone_id(int zone_id) {
        this.zone_id = zone_id;
    }

    @Override
    public String toString() {
        return "Zone{" + "zone_id=" + zone_id + '}';
    }
}
