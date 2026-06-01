package com.mycompany.projekt_io.feature.warehouse;

import com.mycompany.projekt_io.core.database.PackageDAO;
import com.mycompany.projekt_io.datamodel.Format;
import com.mycompany.projekt_io.datamodel.Package;
import com.mycompany.projekt_io.datamodel.Rack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testy jednostkowe klasy {@link WarehouseService}.
 * <p>
 * Weryfikują poprawność obliczania zajętości regałów oraz filtrowania paczek.
 * Testy wykorzystują mock obiektu {@link PackageDAO} eliminując zależność od
 * bazy danych.
 * </p>
 * 
 * @author Paweł Pietrusiak
 */
public class WarehouseServiceTest {

    private WarehouseService warehouseService;
    private PackageDAO mockPackageDao;

    /**
     * Inicjalizuje mock {@link PackageDAO} i wstrzykuje go do testowanego
     * serwisu.
     */
    @BeforeEach
    void setUp() {
        mockPackageDao = mock(PackageDAO.class);
        warehouseService = new WarehouseService(mockPackageDao);
    }

    /**
     * Sprawdza czy metoda {@code getRackOccupancyData()} poprawnie sumuje
     * zajęte sloty dla wskazanego regału i ignoruje paczki z innych regałów.
     */
    @Test
    public void testGetRackOccupancyData_ShouldCalculateCorrectSlots() {
        int targetRackId = 10;

        Package p1 = createMockPackage(targetRackId, 3);
        Package p2 = createMockPackage(targetRackId, 5);
        Package p3 = createMockPackage(99, 10);

        when(mockPackageDao.getPackages()).thenReturn(Arrays.asList(p1, p2, p3));

        Map<String, Integer> chartData = warehouseService.getRackOccupancyData(targetRackId);

        assertNotNull(chartData);
        assertEquals(8, chartData.get("Occupied"), "Suma zajętych slotów na regale powinna wynosić 8");
        assertEquals(1042, chartData.get("Available"), "Suma dostępnych slotów powinna wynosić 1042");
    }

    /**
     * Sprawdza czy metoda {@code getRackOccupancyData()} zwraca 0 zajętych
     * slotów i pełną pojemność gdy baza danych jest pusta.
     */
    @Test
    public void testGetRackOccupancyData_EmptyDatabase_ShouldReturnFullAvailable() {
        when(mockPackageDao.getPackages()).thenReturn(Collections.emptyList());

        Map<String, Integer> chartData = warehouseService.getRackOccupancyData(10);

        assertEquals(0, chartData.get("Occupied"), "Dla pustej bazy zajętość musi wynosić 0");
        assertEquals(1050, chartData.get("Available"), "Dla pustej bazy cała pojemność (1050) musi być dostępna");
    }

    /**
     * Sprawdza czy metoda {@code getPackagesOnRack()} zwraca tylko paczki
     * przypisane do wskazanego regału i pomija paczki z innych regałów.
     */
    @Test
    public void testGetPackagesOnRack_ShouldFilterCorrectPackages() {
        int targetRackId = 7;
        Package p1 = createMockPackage(targetRackId, 1);
        Package p2 = createMockPackage(targetRackId, 2);
        Package p3 = createMockPackage(22, 4);

        when(mockPackageDao.getPackages()).thenReturn(Arrays.asList(p1, p2, p3));

        List<Package> filteredPackages = warehouseService.getPackagesOnRack(targetRackId);

        assertEquals(2, filteredPackages.size(), "Na regale o ID 7 powinny znajdować się dokładnie 2 paczki");
        assertTrue(filteredPackages.contains(p1), "Lista powinna zawierać paczkę p1");
        assertTrue(filteredPackages.contains(p2), "Lista powinna zawierać paczkę p2");
        assertFalse(filteredPackages.contains(p3), "Lista NIE może zawierać paczki p3, która leży na regale 22");
    }

    /**
     * Metoda pomocnicza tworząca mock obiektu {@link Package} z przypisanym
     * regałem i formatem o podanej liczbie slotów.
     *
     * @param rackId identyfikator regału przypisanego do paczki
     * @param slotCoverage liczba slotów zajmowanych przez paczkę
     * @return skonfigurowany mock obiektu {@link Package}
     */
    private Package createMockPackage(int rackId, int slotCoverage) {
        Package mockPackage = mock(Package.class);
        Rack mockRack = mock(Rack.class);
        Format mockFormat = mock(Format.class);

        when(mockRack.getRack_id()).thenReturn(rackId);
        when(mockPackage.getPackage_rack()).thenReturn(mockRack);

        when(mockFormat.getSlot_coverage()).thenReturn(slotCoverage);
        when(mockPackage.getPackage_format()).thenReturn(mockFormat);

        return mockPackage;
    }
}
