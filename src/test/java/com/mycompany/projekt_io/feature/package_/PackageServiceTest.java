package com.mycompany.projekt_io.feature.package_;

import com.mycompany.projekt_io.core.database.PackageDAO;
import com.mycompany.projekt_io.datamodel.Package;
import com.mycompany.projekt_io.datamodel.Format;
import com.mycompany.projekt_io.datamodel.Region;
import com.mycompany.projekt_io.datamodel.Sender;
import com.mycompany.projekt_io.datamodel.Recipient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testy jednostkowe klasy {@link PackageService}.
 * <p>
 * Weryfikują poprawność walidacji wymiarów paczki, automatycznego przypisywania
 * gabarytu oraz delegowania operacji do warstwy DAO. Testy wykorzystują mock
 * obiektu {@link PackageDAO} eliminując zależność od bazy danych.
 * </p>
 * 
 * @author Paweł Pietrusiak
 */
public class PackageServiceTest {

    private PackageService packageService;
    private PackageDAO mockDao;

    /**
     * Inicjalizuje mock {@link PackageDAO} z testową listą regionów i
     * wstrzykuje go do testowanego serwisu.
     */
    @BeforeEach
    void setUp() {
        mockDao = mock(PackageDAO.class);
        packageService = new PackageService(mockDao);

        List<Region> mockRegions = Arrays.asList(
                new Region(1, "WAW"),
                new Region(2, "KRK"),
                new Region(3, "GDA"),
                new Region(4, "KAT")
        );
        when(mockDao.getRegions()).thenReturn(mockRegions);
    }

    /**
     * Sprawdza czy serwis odrzuca paczkę o wymiarach poniżej dopuszczalnego
     * minimum i nie wykonuje zapisu do bazy.
     */
    @Test
    public void testAddPackageFull_ShouldFailForTooSmallPackage() {
        boolean result = packageService.addPackageFull(
                "A", "Warszawa", "Kraków", 1.0,
                1.0, 5.0, 5.0,
                "N", "C", "S", "00-000", "e", "1",
                "O", "C", "S", "00-000", "e", "2"
        );

        assertFalse(result);
        verify(mockDao, never()).addPackage(any());
    }

    /**
     * Sprawdza czy serwis odrzuca paczkę o wadze przekraczającej maksymalne 20
     * kg i nie wykonuje zapisu do bazy.
     */
    @Test
    public void testAddPackageFull_ShouldFailForTooHeavyPackage() {
        boolean result = packageService.addPackageFull(
                null, "Gdańsk", "Warszawa", 25.0,
                20.0, 20.0, 20.0,
                "N", "C", "S", "00-000", "e", "1",
                "O", "C", "S", "00-000", "e", "2"
        );

        assertFalse(result);
        verify(mockDao, never()).addPackage(any());
    }

    /**
     * Sprawdza czy serwis poprawnie przetwarza dane paczki przed zapisem —
     * weryfikuje automatyczne przypisanie gabarytu, liczbę slotów, mapowanie
     * regionu oraz identyfikatory nadawcy i odbiorcy zwrócone przez DAO.
     */
    @Test
    public void testAddPackageFull_CheckWhatIsSentToDatabase() {
        when(mockDao.addSender(any(Sender.class))).thenReturn(101);
        when(mockDao.addRecipient(any(Recipient.class))).thenReturn(202);
        when(mockDao.addPackage(any(Package.class))).thenReturn(true);

        boolean result = packageService.addPackageFull(
                null, "Gdańsk", "Katowice", 5.0,
                15.0, 15.0, 15.0,
                "Jan Kowalski", "Gdańsk", "Długa 1", "80-001", "jan@wp.pl", "111",
                "Anna Nowak", "Katowice", "Mariacka 5", "40-001", "ania@wp.pl", "222"
        );

        assertTrue(result);

        ArgumentCaptor<Package> captor = ArgumentCaptor.forClass(Package.class);
        verify(mockDao).addPackage(captor.capture());
        Package capturedPackage = captor.getValue();

        assertEquals("B", capturedPackage.getPackage_format().getFormat_id(), "Powinien być gabaryt B");
        assertEquals(2, capturedPackage.getPackage_format().getSlot_coverage(), "Gabaryt B = 2 sloty");
        assertEquals("Gdańsk", capturedPackage.getPackage_region().getRegion_name(), "Miasto Gdańsk = GDA");
        assertEquals(101, capturedPackage.getPackage_sender().getSender_id(), "ID nadawcy = 101");
    }

    /**
     * Sprawdza czy metoda {@code deletePackage()} poprawnie deleguje operację
     * usunięcia do DAO i zwraca true gdy baza potwierdzi powodzenie.
     */
    @Test
    public void testDeletePackage_Success() {
        when(mockDao.deletePackage(777)).thenReturn(true);

        assertTrue(packageService.deletePackage(777));
        verify(mockDao).deletePackage(777);
    }
}
