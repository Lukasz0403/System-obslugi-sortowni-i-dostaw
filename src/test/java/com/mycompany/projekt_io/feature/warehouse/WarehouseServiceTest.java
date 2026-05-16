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

public class WarehouseServiceTest {

    private WarehouseService warehouseService;
    private PackageDAO mockPackageDao;

    @BeforeEach
    void setUp() {
        // Tworzymy mocka dla warstwy dostępu do bazy danych
        mockPackageDao = mock(PackageDAO.class);
        // Wstrzykujemy mocka przez konstruktor do testowanego serwisu
        warehouseService = new WarehouseService(mockPackageDao);
    }

    /**
     * Testuje poprawność obliczania zajętości regału dla wykresu kołowego.
     */
    @Test
    public void testGetRackOccupancyData_ShouldCalculateCorrectSlots() {
        // Arrange: Przygotowanie danych (Mamy regał o ID 10)
        int targetRackId = 10;
        
        // Tworzymy dwie paczki leżące na regale 10 (zajmujące kolejno 3 i 5 slotów)
        Package p1 = createMockPackage(targetRackId, 3);
        Package p2 = createMockPackage(targetRackId, 5);
        // Tworzymy paczkę leżącą na zupełnie innym regale (powinna zostać zignorowana)
        Package p3 = createMockPackage(99, 10);

        // Nakazujemy mockowi zwrócić naszą listę paczek przy wywołaniu metody getPackages()
        when(mockPackageDao.getPackages()).thenReturn(Arrays.asList(p1, p2, p3));

        // Act: Wywołanie testowanej metody
        Map<String, Integer> chartData = warehouseService.getRackOccupancyData(targetRackId);

        // Assert: Weryfikacja wyników
        assertNotNull(chartData);
        // Oczekiwana zajętość: 3 + 5 = 8 slotów
        assertEquals(8, chartData.get("Occupied"), "Suma zajętych slotów na regale powinna wynosić 8");
        // Oczekiwana dostępność: MAX (1050) - 8 = 1042 sloty
        assertEquals(1042, chartData.get("Available"), "Suma dostępnych slotów powinna wynosić 1042");
    }

    /**
     * Testuje czy metoda zwraca 0 zajętych slotów, gdy żaden regał nie pasuje lub baza jest pusta.
     */
    @Test
    public void testGetRackOccupancyData_EmptyDatabase_ShouldReturnFullAvailable() {
        // Arrange: Baza zwraca pustą listę paczek
        when(mockPackageDao.getPackages()).thenReturn(Collections.emptyList());

        // Act
        Map<String, Integer> chartData = warehouseService.getRackOccupancyData(10);

        // Assert
        assertEquals(0, chartData.get("Occupied"), "Dla pustej bazy zajętość musi wynosić 0");
        assertEquals(1050, chartData.get("Available"), "Dla pustej bazy cała pojemność (1050) musi być dostępna");
    }

    /**
     * Testuje pobieranie i filtrowanie listy paczek znajdujących się na konkretnym regale.
     */
    @Test
    public void testGetPackagesOnRack_ShouldFilterCorrectPackages() {
        // Arrange
        int targetRackId = 7;
        Package p1 = createMockPackage(targetRackId, 1);
        Package p2 = createMockPackage(targetRackId, 2);
        Package p3 = createMockPackage(22, 4); // Paczka na innym regale

        when(mockPackageDao.getPackages()).thenReturn(Arrays.asList(p1, p2, p3));

        // Act
        List<Package> filteredPackages = warehouseService.getPackagesOnRack(targetRackId);

        // Assert
        assertEquals(2, filteredPackages.size(), "Na regale o ID 7 powinny znajdować się dokładnie 2 paczki");
        assertTrue(filteredPackages.contains(p1), "Lista powinna zawierać paczkę p1");
        assertTrue(filteredPackages.contains(p2), "Lista powinna zawierać paczkę p2");
        assertFalse(filteredPackages.contains(p3), "Lista NIE może zawierać paczki p3, która leży na regale 22");
    }

    /**
     * Metoda pomocnicza do dynamicznego budowania spójnych mocków paczek, regałów i formatów.
     * Pozwala uniknąć powtarzania kilkunastu linijek konfiguracji Mockito w każdym teście.
     */
    private Package createMockPackage(int rackId, int slotCoverage) {
        Package mockPackage = mock(Package.class);
        Rack mockRack = mock(Rack.class);
        Format mockFormat = mock(Format.class);

        // Konfiguracja struktury: Paczka -> Regał -> ID regału
        when(mockRack.getRack_id()).thenReturn(rackId);
        when(mockPackage.getPackage_rack()).thenReturn(mockRack);

        // Konfiguracja struktury: Paczka -> Format -> Liczba zajmowanych slotów
        when(mockFormat.getSlot_coverage()).thenReturn(slotCoverage);
        when(mockPackage.getPackage_format()).thenReturn(mockFormat);

        return mockPackage;
    }
}