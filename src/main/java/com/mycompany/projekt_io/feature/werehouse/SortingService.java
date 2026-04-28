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
 * Serwis odpowiedzialny za automatyczne przydzielanie regałów magazynowych do
 * paczek które nie mają jeszcze przypisanego miejsca w magazynie.
 * <p>
 * Implementuje interfejs {@link SortingServiceInterface} oraz wzorzec Observer
 * do powiadamiania o przekroczeniu progu zajętości stref magazynowych. Algorytm
 * przydziału działa w trzech krokach:
 * </p>
 * <ol>
 * <li>Próba przydziału do regału dedykowanego dla regionu docelowego
 * paczki</li>
 * <li>Próba przydziału do innego regału w tej samej strefie</li>
 * <li>Próba przydziału do regału w strefie zapasowej (strefa
 * {@value #BACKUP_ZONE_ID})</li>
 * </ol>
 * <p>
 * Pojemność każdego regału wynosi {@value #MAX_RACK_CAPACITY} slotów. Alert
 * jest wysyłany do obserwatorów gdy zajętość strefy przekroczy
 * {@value #OVERLOAD_THRESHOLD} * 100% pojemności.
 * </p>
 *
 * @author Łukasz
 */
public class SortingService implements SortingServiceInterface {

    private final PackageDAO packageDAO;

    /**
     * Maksymalna liczba slotów na jeden regał.
     */
    private static final int MAX_RACK_CAPACITY = 1050;

    /**
     * Identyfikator strefy zapasowej używanej gdy główne strefy są pełne.
     */
    private static final int BACKUP_ZONE_ID = 5;

    /**
     * Próg zajętości strefy (0.0 - 1.0) po przekroczeniu którego wysyłane jest
     * powiadomienie.
     */
    private static final double OVERLOAD_THRESHOLD = 0.9;

    private Map<Integer, Integer> regionToRackMapping = new HashMap<>();
    private Map<Integer, Integer> rackOccupancyMap = new HashMap<>();
    private List<Rack> mainRacks = new ArrayList<>();
    private List<Rack> backupRacks = new ArrayList<>();
    private final List<WarehouseObserver> observers = new ArrayList<>();

    /**
     * Tworzy instancję serwisu sortowania.
     *
     * @param packageDAO obiekt dostępu do danych używany do pobierania i
     * aktualizowania paczek oraz regałów w bazie danych
     */
    public SortingService(PackageDAO packageDAO) {
        this.packageDAO = packageDAO;
    }

    /**
     * Główna metoda sterująca procesem przydzielania regałów do paczek.
     * <p>
     * Pobiera wszystkie paczki, regały i regiony z bazy danych, a następnie
     * kolejno: inicjalizuje struktury danych, oblicza aktualną zajętość
     * regałów, tworzy mapowanie regionów do regałów i przetwarza nieprzypisane
     * paczki. Po zakończeniu powiadamia obserwatorów o stanie zajętości stref.
     * </p>
     */
    @Override
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
        notifyObserversIfNeeded();
    }

    /**
     * Inicjalizuje struktury danych — czyści listy i mapy oraz dzieli regały na
     * główne i zapasowe na podstawie identyfikatora strefy.
     * <p>
     * Regały należące do strefy o ID {@value #BACKUP_ZONE_ID} trafiają do listy
     * regałów zapasowych, pozostałe do listy głównych. Wszystkie regały
     * inicjalizowane są z zerową zajętością.
     * </p>
     *
     * @param allRacks lista wszystkich regałów pobranych z bazy danych
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
     * Oblicza aktualną zajętość regałów na podstawie już przypisanych paczek.
     * <p>
     * Dla każdej paczki posiadającej przypisany regał sumuje liczbę zajmowanych
     * przez nią slotów ({@code slot_coverage} z formatu paczki) i aktualizuje
     * mapę zajętości {@code rackOccupancyMap}.
     * </p>
     *
     * @param allPackages lista wszystkich paczek pobranych z bazy danych
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
     * Tworzy stałe powiązanie każdego regionu kurierskiego z konkretnym regałem
     * głównym.
     * <p>
     * Regiony są przypisywane do regałów metodą modulo — jeśli jest więcej
     * regionów niż regałów głównych, kolejne regiony są przypisywane od
     * początku listy regałów.
     * </p>
     *
     * @param allRegions lista wszystkich regionów kurierskich z bazy danych
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
     * Przetwarza listę paczek i przydziela regały tym które jeszcze ich nie
     * mają.
     * <p>
     * Dla każdej nieprzypisanej paczki wyznaczany jest preferowany regał na
     * podstawie regionu docelowego, a następnie wyszukiwany jest najlepszy
     * dostępny regał przy użyciu {@link #findBestAvailableRack}. Jeśli nie ma
     * miejsca dla paczki, informacja jest logowana na standardowe wyjście.
     * </p>
     *
     * @param allPackages lista wszystkich paczek
     * @param allRacks lista wszystkich regałów
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
     * Wyszukuje najlepszy dostępny regał dla paczki o podanej liczbie slotów.
     * <p>
     * Przeszukiwanie odbywa się w trzech krokach:
     * </p>
     * <ol>
     * <li>Sprawdzenie regału dedykowanego dla regionu docelowego paczki</li>
     * <li>Przeszukanie innych regałów w tej samej strefie co regał
     * dedykowany</li>
     * <li>Przeszukanie regałów w strefie zapasowej
     * ({@value #BACKUP_ZONE_ID})</li>
     * </ol>
     *
     * @param prefRackId identyfikator preferowanego regału (dedykowany dla
     * regionu)
     * @param slots liczba slotów wymaganych przez paczkę
     * @param allRacks lista wszystkich regałów
     * @return najlepszy dostępny {@link Rack} lub {@code null} jeśli brak
     * miejsca
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
     * Zapisuje przypisanie paczki do regału w bazie danych i aktualizuje
     * licznik zajętych slotów w mapie zajętości.
     * <p>
     * Jeśli zapis w bazie się nie powiedzie, mapa zajętości nie jest
     * aktualizowana — przypisanie jest traktowane jako nieudane.
     * </p>
     *
     * @param pkg paczka do przypisania
     * @param rack regał do którego paczka ma być przypisana
     * @param slots liczba slotów zajmowanych przez paczkę
     */
    private void saveAssignment(Package pkg, Rack rack, int slots) {
        pkg.setPackage_rack(rack);
        if (packageDAO.changePackage(pkg)) {
            int newTotal = rackOccupancyMap.get(rack.getRack_id()) + slots;
            rackOccupancyMap.put(rack.getRack_id(), newTotal);
            System.out.println("Przydzielono: Paczka " + pkg.getPackage_id()
                    + " do regalu " + rack.getRack_id()
                    + " (" + newTotal + "/1050 slotów)");
        }
    }

    /**
     * Przeszukuje listę regałów i zwraca regał o podanym identyfikatorze.
     *
     * @param list lista regałów do przeszukania
     * @param id identyfikator szukanego regału
     * @return obiekt {@link Rack} o podanym ID lub {@code null} jeśli nie
     * znaleziono
     */
    private Rack getRackFromList(List<Rack> list, int id) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getRack_id() == id) {
                return list.get(i);
            }
        }
        return null;
    }

    /**
     * Rejestruje obserwatora który będzie powiadamiany o przekroczeniu progu
     * zajętości strefy magazynowej.
     *
     * @param observer obserwator do zarejestrowania
     */
    @Override
    public void addObserver(WarehouseObserver observer) {
        observers.add(observer);
    }

    /**
     * Wyrejestrowuje obserwatora — od tej pory nie będzie otrzymywał
     * powiadomień.
     *
     * @param observer obserwator do usunięcia
     */
    @Override
    public void removeObserver(WarehouseObserver observer) {
        observers.remove(observer);
    }

    /**
     * Sprawdza zajętość wszystkich stref i powiadamia zarejestrowanych
     * obserwatorów jeśli któraś przekroczyła próg {@value #OVERLOAD_THRESHOLD}.
     * <p>
     * Zajętość strefy obliczana jest jako suma slotów zajętych przez wszystkie
     * regały należące do danej strefy. Maksymalna pojemność strefy wynosi
     * {@value #MAX_RACK_CAPACITY} * 4 slotów (4 regały na strefę).
     * </p>
     */
    private void notifyObserversIfNeeded() {
        Map<Integer, Integer> zoneOccupancy = new HashMap<>();

        for (Map.Entry<Integer, Integer> entry : rackOccupancyMap.entrySet()) {
            int rackId = entry.getKey();
            int slots = entry.getValue();

            Rack rack = getRackFromList(mainRacks, rackId);
            if (rack == null) {
                rack = getRackFromList(backupRacks, rackId);
            }
            if (rack == null) {
                continue;
            }

            int zoneId = rack.getZone().getZone_id();
            zoneOccupancy.merge(zoneId, slots, Integer::sum);
        }

        int maxPerZone = MAX_RACK_CAPACITY * 4;

        for (Map.Entry<Integer, Integer> entry : zoneOccupancy.entrySet()) {
            int zoneId = entry.getKey();
            int occupancy = entry.getValue();

            if (occupancy >= maxPerZone * OVERLOAD_THRESHOLD) {
                for (WarehouseObserver observer : observers) {
                    observer.onZoneOverloaded(zoneId, occupancy, maxPerZone);
                }
            }
        }
    }
}
