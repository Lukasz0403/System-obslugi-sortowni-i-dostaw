/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projekt_io.feature.warehouse;

/**
 * Interfejs obserwatora stanu magazynu.
 * <p>
 * Implementowany przez klasy które chcą być powiadamiane o przekroczeniu progu
 * zajętości strefy magazynowej.
 * </p>
 * 
 * @author Mateusz Gojny
 */
public interface WarehouseObserver {

    /**
     * Wywoływane gdy zajętość strefy przekroczy zdefiniowany próg.
     *
     * @param zoneId identyfikator strefy magazynowej
     * @param occupancy aktualna zajętość strefy w slotach
     * @param maxCapacity maksymalna pojemność strefy w slotach
     */
    void onZoneOverloaded(int zoneId, int occupancy, int maxCapacity);
}
