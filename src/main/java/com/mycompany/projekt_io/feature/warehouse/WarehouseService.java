package com.mycompany.projekt_io.feature.warehouse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static com.mycompany.projekt_io.feature.warehouse.SortingService.MAX_RACK_CAPACITY;
import com.mycompany.projekt_io.core.database.PackageDAO;
import com.mycompany.projekt_io.datamodel.Package;

/**
 * Serwis dostarczający dane o stanie regałów magazynowych.
 * <p>
 * Oblicza zajętość slotów dla konkretnego regału oraz pobiera listę paczek
 * przypisanych do wskazanego regału. Dane te są wykorzystywane przez
 * {@code WarehouseInfoWindowController} do wyświetlania wykresu kołowego i
 * tabeli szczegółów regału.
 * </p>
 *
 * @author Łukasz Motyka
 */
public class WarehouseService {

    private final PackageDAO packageDAO;

    /**
     * Tworzy instancję serwisu z podanym obiektem dostępu do danych.
     *
     * @param packageDAO obiekt DAO używany do pobierania danych paczek
     */
    public WarehouseService(PackageDAO packageDAO) {
        this.packageDAO = packageDAO;
    }

    /**
     * Przygotowuje dane zajętości regału do wykresu kołowego.
     * <p>
     * Sumuje sloty zajmowane przez wszystkie paczki przypisane do wskazanego
     * regału i oblicza liczbę wolnych slotów jako różnicę między pojemnością
     * maksymalną a zajętą.
     * </p>
     *
     * @param rackId identyfikator regału którego zajętość ma być obliczona
     * @return mapa z kluczami {@code "Occupied"} i {@code "Available"}
     * zawierająca liczbę zajętych i wolnych slotów
     */
    public Map<String, Integer> getRackOccupancyData(int rackId) {
        List<Package> allPackages = packageDAO.getPackages();
        int occupiedSlots = 0;

        for (int i = 0; i < allPackages.size(); i++) {
            Package p = allPackages.get(i);
            if (p.getPackage_rack() != null && p.getPackage_rack().getRack_id() == rackId) {
                occupiedSlots += p.getPackage_format().getSlot_coverage();
            }
        }

        int availableSlots = MAX_RACK_CAPACITY - occupiedSlots;
        Map<String, Integer> chartData = new HashMap<>();
        chartData.put("Occupied", occupiedSlots);
        chartData.put("Available", availableSlots);
        return chartData;
    }

    /**
     * Pobiera listę wszystkich paczek przypisanych do wskazanego regału.
     * <p>
     * Dane zwracane przez tę metodę są wyświetlane bezpośrednio w tabeli
     * szczegółów regału w oknie {@code WarehouseInfoWindowController}.
     * </p>
     *
     * @param rackId identyfikator regału którego paczki mają być pobrane
     * @return lista paczek znajdujących się na wskazanym regale, pusta lista
     * jeśli regał nie zawiera żadnych paczek
     */
    public List<Package> getPackagesOnRack(int rackId) {
        List<Package> allPackages = packageDAO.getPackages();
        List<Package> rackPackages = new ArrayList<>();

        for (int i = 0; i < allPackages.size(); i++) {
            Package p = allPackages.get(i);
            if (p.getPackage_rack() != null && p.getPackage_rack().getRack_id() == rackId) {
                rackPackages.add(p);
            }
        }
        return rackPackages;
    }
}
