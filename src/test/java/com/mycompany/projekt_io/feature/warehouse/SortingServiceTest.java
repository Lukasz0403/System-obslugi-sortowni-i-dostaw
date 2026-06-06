package com.mycompany.projekt_io.feature.warehouse;

import com.mycompany.projekt_io.core.database.PackageDAO;
import com.mycompany.projekt_io.datamodel.*;
import com.mycompany.projekt_io.datamodel.Package;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testy jednostkowe klasy {@link SortingService}.
 * <p>
 * Weryfikują poprawność algorytmu przypisywania paczek do regałów — przydział
 * do preferowanego regału, obsługę przepełnienia strefy, przekierowanie do
 * strefy zapasowej oraz działanie wzorca Observer przy przekroczeniu progu
 * zajętości. Testy wykorzystują mocki obiektów {@link PackageDAO} i
 * {@link WarehouseObserver} eliminując zależność od bazy danych.
 * </p>
 * 
 * @author Paweł Pietrusiak
 */
public class SortingServiceTest {

    private SortingService sortingService;
    private PackageDAO mockPackageDao;

    /**
     * Inicjalizuje mock {@link PackageDAO} i wstrzykuje go do testowanego
     * serwisu przed każdym testem.
     */
    @BeforeEach
    void setUp() {
        mockPackageDao = mock(PackageDAO.class);
        sortingService = new SortingService(mockPackageDao);
    }

    /**
     * Sprawdza czy algorytm poprawnie przypisuje paczkę do dedykowanego regału
     * gdy jest w nim wystarczająca ilość wolnych slotów.
     */
    @Test
    public void testAssignShelves_ShouldAssignToPreferredRack() {
        Region region = new Region(1, "WAW");
        Zone zone = new Zone(1);
        Rack preferredRack = new Rack(10, zone);

        Format format = mock(Format.class);
        when(format.getSlot_coverage()).thenReturn(50);
        Package pkg = new Package(100, null, null, null, region, null, null, 0, 0, 0, 0);
        pkg.setPackage_format(format);

        when(mockPackageDao.getPackages()).thenReturn(Collections.singletonList(pkg));
        when(mockPackageDao.getRacks()).thenReturn(Collections.singletonList(preferredRack));
        when(mockPackageDao.getRegions()).thenReturn(Collections.singletonList(region));
        when(mockPackageDao.changePackage(any(Package.class))).thenReturn(true);

        sortingService.assignShelvesToPackages();

        assertNotNull(pkg.getPackage_rack(), "Paczka powinna mieć przypisany regał");
        assertEquals(10, pkg.getPackage_rack().getRack_id(), "Paczka powinna trafić na regał o ID 10");
        verify(mockPackageDao).changePackage(pkg);
    }

    /**
     * Sprawdza czy algorytm przypisuje paczkę do alternatywnego regału w tej
     * samej strefie gdy preferowany regał jest już zapełniony.
     */
    @Test
    public void testAssignShelves_PreferredRackFull_ShouldAssignToAlternativeRackInSameZone() {
        Region region = new Region(1, "KRK");
        Zone zone = new Zone(1);

        Rack fullPreferredRack = new Rack(10, zone);
        Rack alternativeRack = new Rack(20, zone);

        Format fullFormat = mock(Format.class);
        when(fullFormat.getSlot_coverage()).thenReturn(1050);
        Package oldPkg = new Package(1, null, null, null, region, null, null, 0, 0, 0, 0);
        oldPkg.setPackage_format(fullFormat);
        oldPkg.setPackage_rack(fullPreferredRack);

        Format newFormat = mock(Format.class);
        when(newFormat.getSlot_coverage()).thenReturn(100);
        Package newPkg = new Package(2, null, null, null, region, null, null, 0, 0, 0, 0);
        newPkg.setPackage_format(newFormat);

        when(mockPackageDao.getPackages()).thenReturn(Arrays.asList(oldPkg, newPkg));
        when(mockPackageDao.getRacks()).thenReturn(Arrays.asList(fullPreferredRack, alternativeRack));
        when(mockPackageDao.getRegions()).thenReturn(Collections.singletonList(region));
        when(mockPackageDao.changePackage(any(Package.class))).thenReturn(true);

        sortingService.assignShelvesToPackages();

        assertNotNull(newPkg.getPackage_rack(), "Nowa paczka powinna znaleźć miejsce");
        assertEquals(20, newPkg.getPackage_rack().getRack_id());
    }

    /**
     * Sprawdza czy algorytm natychmiast kończy działanie bez wykonywania
     * żadnych zapisów gdy baza danych nie zawiera regałów ani regionów.
     */
    @Test
    public void testAssignShelves_EmptyDatabase_ShouldReturnImmediately() {
        when(mockPackageDao.getRacks()).thenReturn(new ArrayList<>());
        when(mockPackageDao.getRegions()).thenReturn(new ArrayList<>());
        when(mockPackageDao.getPackages()).thenReturn(new ArrayList<>());

        sortingService.assignShelvesToPackages();

        verify(mockPackageDao, never()).changePackage(any(Package.class));
    }

    /**
     * Sprawdza czy algorytm przekierowuje paczkę do strefy zapasowej (ID=5) gdy
     * wszystkie regały w strefie głównej są zapełnione.
     */
    @Test
    public void testAssignShelves_MainZoneFull_ShouldAssignToBackupZone() {
        Region region = new Region(1, "WRO");
        Zone mainZone = new Zone(1);
        Zone backupZone = new Zone(5);

        Rack fullMainRack = new Rack(10, mainZone);
        Rack backupRack = new Rack(99, backupZone);

        Format fullFormat = mock(Format.class);
        when(fullFormat.getSlot_coverage()).thenReturn(1050);
        Package oldPkg = new Package(1, null, null, null, region, null, null, 0, 0, 0, 0);
        oldPkg.setPackage_format(fullFormat);
        oldPkg.setPackage_rack(fullMainRack);

        Format newFormat = mock(Format.class);
        when(newFormat.getSlot_coverage()).thenReturn(200);
        Package newPkg = new Package(2, null, null, null, region, null, null, 0, 0, 0, 0);
        newPkg.setPackage_format(newFormat);

        when(mockPackageDao.getPackages()).thenReturn(Arrays.asList(oldPkg, newPkg));
        when(mockPackageDao.getRacks()).thenReturn(Arrays.asList(fullMainRack, backupRack));
        when(mockPackageDao.getRegions()).thenReturn(Collections.singletonList(region));
        when(mockPackageDao.changePackage(any(Package.class))).thenReturn(true);

        sortingService.assignShelvesToPackages();

        assertNotNull(newPkg.getPackage_rack(), "Paczka powinna zostać przypisana");
        assertEquals(99, newPkg.getPackage_rack().getRack_id(),
                "Gdy strefa główna jest pełna, paczka musi trafić na regał zapasowy (ID 99)");
    }

    /**
     * Sprawdza czy paczka pozostaje bez przypisanego regału gdy zarówno strefa
     * główna jak i strefa zapasowa są całkowicie zapełnione.
     */
    @Test
    public void testAssignShelves_NoSpaceAtAll_ShouldNotAssignAnything() {
        Region region = new Region(1, "POZ");
        Zone mainZone = new Zone(1);
        Zone backupZone = new Zone(5);

        Rack mainRack = new Rack(10, mainZone);
        Rack backupRack = new Rack(99, backupZone);

        Format fullFormat = mock(Format.class);
        when(fullFormat.getSlot_coverage()).thenReturn(1050);

        Package oldPkg1 = new Package(1, null, null, null, region, null, null, 0, 0, 0, 0);
        oldPkg1.setPackage_format(fullFormat);
        oldPkg1.setPackage_rack(mainRack);

        Package oldPkg2 = new Package(2, null, null, null, region, null, null, 0, 0, 0, 0);
        oldPkg2.setPackage_format(fullFormat);
        oldPkg2.setPackage_rack(backupRack);

        Format newFormat = mock(Format.class);
        when(newFormat.getSlot_coverage()).thenReturn(50);
        Package newPkg = new Package(3, null, null, null, region, null, null, 0, 0, 0, 0);
        newPkg.setPackage_format(newFormat);

        when(mockPackageDao.getPackages()).thenReturn(Arrays.asList(oldPkg1, oldPkg2, newPkg));
        when(mockPackageDao.getRacks()).thenReturn(Arrays.asList(mainRack, backupRack));
        when(mockPackageDao.getRegions()).thenReturn(Collections.singletonList(region));

        sortingService.assignShelvesToPackages();

        assertNull(newPkg.getPackage_rack(), "Magazyn jest pełny, regał powinien pozostać nullem");
        verify(mockPackageDao, never()).changePackage(newPkg);
    }

    /**
     * Sprawdza czy mechanizm modulo poprawnie mapuje regiony do regałów gdy
     * liczba regionów przekracza liczbę dostępnych regałów głównych — paczki z
     * różnych regionów powinny trafiać na ten sam regał.
     */
    @Test
    public void testAssignShelves_ModuloMapping_ShouldWrapAroundRacks() {
        Region region1 = new Region(1, "SZC");
        Region region2 = new Region(2, "RZE");

        Zone zone = new Zone(1);
        Rack singleMainRack = new Rack(77, zone);

        Format format = mock(Format.class);
        when(format.getSlot_coverage()).thenReturn(10);

        Package pkg1 = new Package(10, null, null, null, region1, null, null, 0, 0, 0, 0);
        pkg1.setPackage_format(format);

        Package pkg2 = new Package(11, null, null, null, region2, null, null, 0, 0, 0, 0);
        pkg2.setPackage_format(format);

        when(mockPackageDao.getPackages()).thenReturn(Arrays.asList(pkg1, pkg2));
        when(mockPackageDao.getRacks()).thenReturn(Collections.singletonList(singleMainRack));
        when(mockPackageDao.getRegions()).thenReturn(Arrays.asList(region1, region2));
        when(mockPackageDao.changePackage(any(Package.class))).thenReturn(true);

        sortingService.assignShelvesToPackages();

        assertEquals(77, pkg1.getPackage_rack().getRack_id(), "Paczka 1 powinna trafić na jedyny regał");
        assertEquals(77, pkg2.getPackage_rack().getRack_id(), "Paczka 2 również powinna tam trafić przez mechanizm modulo");
    }

    /**
     * Sprawdza czy wzorzec Observer poprawnie powiadamia zarejestrowanego
     * obserwatora gdy zajętość strefy przekroczy próg 90% pojemności.
     */
    @Test
    public void testNotifyObservers_ZoneOverloaded_ShouldTriggerAlert() {
        Region region = new Region(1, "GDA");
        Zone zone = new Zone(2);
        Rack rack = new Rack(15, zone);

        Format hugeFormat = mock(Format.class);
        when(hugeFormat.getSlot_coverage()).thenReturn(3800);

        Package pkg = new Package(9, null, null, null, region, null, null, 0, 0, 0, 0);
        pkg.setPackage_format(hugeFormat);
        pkg.setPackage_rack(rack);

        when(mockPackageDao.getPackages()).thenReturn(Collections.singletonList(pkg));
        when(mockPackageDao.getRacks()).thenReturn(Collections.singletonList(rack));
        when(mockPackageDao.getRegions()).thenReturn(Collections.singletonList(region));

        WarehouseObserver mockObserver = mock(WarehouseObserver.class);
        sortingService.addObserver(mockObserver);

        sortingService.assignShelvesToPackages();

        verify(mockObserver, times(1)).onZoneOverloaded(eq(2), eq(3800), eq(4200));
    }
}
