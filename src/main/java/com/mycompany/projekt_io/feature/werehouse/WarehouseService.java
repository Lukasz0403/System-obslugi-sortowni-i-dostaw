package com.mycompany.projekt_io.feature.werehouse;

import com.mycompany.projekt_io.core.database.PackageDAO;
import com.mycompany.projekt_io.datamodel.Package;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Łukasz
 */
public class WarehouseService {

    private final PackageDAO packageDAO;
    private static final int MAX_RACK_CAPACITY = 1050;

    public WarehouseService(PackageDAO packageDAO) {
        this.packageDAO = packageDAO;
    }

    /**
     * Przygotowuje dane do wykresu kołowego dla konkretnego regału.
     * Zwraca mapę, gdzie kluczem jest etykieta (np. "Occupied"), a wartością liczba slotów.
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
     * Pobiera listę wszystkich paczek, które znajdują się na wybranym regale.
     * Te dane trafią bezpośrednio do tabeli w widoku szczegółów.
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