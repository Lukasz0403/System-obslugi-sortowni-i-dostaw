package com.mycompany.projekt_io.feature.werehouse;

import com.mycompany.projekt_io.core.database.PackageDAO;
import com.mycompany.projekt_io.datamodel.Package;
import com.mycompany.projekt_io.datamodel.Rack;
import com.mycompany.projekt_io.datamodel.Region;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Łukasz
 */
public class SortingService {

    private final PackageDAO packageDAO;
    private static final int MAX_RACK_CAPACITY = 1050; 
    private static final int BACKUP_ZONE_ID = 5;

    private Map<Integer, Integer> regionToRackMapping = new HashMap<>();
    private Map<Integer, Integer> rackOccupancyMap = new HashMap<>();
    private List<Rack> mainRacks = new ArrayList<>();
    private List<Rack> backupRacks = new ArrayList<>();

    public SortingService(PackageDAO packageDAO) {
        this.packageDAO = packageDAO;
    }

    /**
     * Główna metoda sterująca procesem przydzielania regałów do paczek.
     */
    public void assignShelvesToPackages() {
        List<Package> allPackages = packageDAO.getPackages();
        List<Rack> allRacks = packageDAO.getRacks();
        List<Region> allRegions = packageDAO.getRegions();

        if (allRacks.isEmpty() || allRegions.isEmpty()) {
            return;
        }

        setupRacks(allRacks);

        calculateCurrentOccupancy(allPackages);

        mapRegionsToRacks(allRegions);

        processPackages(allPackages, allRacks);
    }

    /**
     * Czyści listy i mapy oraz dzieli regały na główne i zapasowe na podstawie ID strefy.
     */
    private void setupRacks(List<Rack> allRacks) {
        mainRacks.clear();
        backupRacks.clear();
        rackOccupancyMap.clear();

        for (int i = 0; i < allRacks.size(); i++) {
            Rack r = allRacks.get(i);
            if (r.getZone().getZone_id() == BACKUP_ZONE_ID) {
                backupRacks.add(r);
            } else {
                mainRacks.add(r);
            }
            rackOccupancyMap.put(r.getRack_id(), 0);
        }
    }

    /**
     * Sprawdza paczki już przypisane do regałów i sumuje zajęte przez nie sloty w mapie zajętości.
     */    
    private void calculateCurrentOccupancy(List<Package> allPackages) {
        for (int i = 0; i < allPackages.size(); i++) {
            Package p = allPackages.get(i);
            if (p.getPackage_rack() != null && p.getPackage_rack().getRack_id() > 0) {
                int rackId = p.getPackage_rack().getRack_id();
                int slots = p.getPackage_format().getSlot_coverage();
                
                int current = rackOccupancyMap.get(rackId);
                rackOccupancyMap.put(rackId, current + slots);
            }
        }
    }

    /**
     * Tworzy stałe powiązanie regionu z konkretnym regałem głównym.
     */
    private void mapRegionsToRacks(List<Region> allRegions) {
        regionToRackMapping.clear();
        for (int i = 0; i < allRegions.size(); i++) {
            int regionId = allRegions.get(i).getRegion_id();
            int index = i % mainRacks.size();
            int rackId = mainRacks.get(index).getRack_id();
            regionToRackMapping.put(regionId, rackId);
        }
    }

    /**
     * Przechodzi przez listę nieprzypisanych paczek i znajduje dla nich miejsce na magazynie.
     */
    private void processPackages(List<Package> allPackages, List<Rack> allRacks) {
        for (int i = 0; i < allPackages.size(); i++) {
            Package pkg = allPackages.get(i);

            if (pkg.getPackage_rack() == null || pkg.getPackage_rack().getRack_id() <= 0) {
                
                int regionId = pkg.getPackage_dest_region().getRegion_id();
                int slots = pkg.getPackage_format().getSlot_coverage();
                int prefRackId = regionToRackMapping.get(regionId);

                Rack bestRack = findBestAvailableRack(prefRackId, slots, allRacks);

                if (bestRack != null) {
                    saveAssignment(pkg, bestRack, slots);
                } else {
                    System.out.println("No space for Package ID: " + pkg.getPackage_id());
                }
            }
        }
    }

    /**
     * Szuka wolnego regału w trzech krokach: 1. Dedykowany, 2. Ta sama strefa, 3. Strefa zapasowa.
     */
    private Rack findBestAvailableRack(int prefRackId, int slots, List<Rack> allRacks) {
        if (rackOccupancyMap.get(prefRackId) + slots <= MAX_RACK_CAPACITY) {
            return getRackFromList(allRacks, prefRackId);
        }

        Rack prefObject = getRackFromList(allRacks, prefRackId);
        int zoneId = prefObject.getZone().getZone_id();

        for (int i = 0; i < mainRacks.size(); i++) {
            Rack r = mainRacks.get(i);
            if (r.getZone().getZone_id() == zoneId) {
                if (rackOccupancyMap.get(r.getRack_id()) + slots <= MAX_RACK_CAPACITY) {
                    return r;
                }
            }
        }

        for (int i = 0; i < backupRacks.size(); i++) {
            Rack r = backupRacks.get(i);
            if (rackOccupancyMap.get(r.getRack_id()) + slots <= MAX_RACK_CAPACITY) {
                return r;
            }
        }

        return null;
    }

    /**
     * Zapisuje przypisanie paczki do regału w bazie danych i aktualizuje licznik zajętych slotów.
     */
    private void saveAssignment(Package pkg, Rack rack, int slots) {
        pkg.setPackage_rack(rack);
        if (packageDAO.changePackage(pkg)) {
            int newTotal = rackOccupancyMap.get(rack.getRack_id()) + slots;
            rackOccupancyMap.put(rack.getRack_id(), newTotal);
            
            System.out.println("Przydzielono: Paczka " + pkg.getPackage_id() + " do regalu " + rack.getRack_id() + 
                               " (" + newTotal + "/1050 slotów)");
        }
    }

    /**
     * Przeszukuje listę obiektów Rack, aby znaleźć ten o konkretnym numerze ID.
     */
    private Rack getRackFromList(List<Rack> list, int id) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getRack_id() == id) {
                return list.get(i);
            }
        }
        return null;
    }
}