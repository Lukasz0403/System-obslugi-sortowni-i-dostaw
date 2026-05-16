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

public class PackageServiceTest {

    private PackageService packageService;
    private PackageDAO mockDao;

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

    @Test
    public void testAddPackageFull_ShouldFailForTooSmallPackage() {
       // Test dodawania paczki
       // Powinien zwrocic False, poniewaz paczka jest zbyt mala
        
       // Dodawanie paczki
        boolean result = packageService.addPackageFull(
                "A", "Warszawa", "Kraków", 1.0, 
                1.0, 5.0, 5.0, 
                "N", "C", "S", "00-000", "e", "1", 
                "O", "C", "S", "00-000", "e", "2"
        );
        // Test czy zwrocilo False
        assertFalse(result);
        // Sprawdzenie czy byla proba zapisu do bazy
        verify(mockDao, never()).addPackage(any());
    }

    @Test
    public void testAddPackageFull_ShouldFailForTooHeavyPackage() {
        // Test dodawania paczki
        // Powinien zwrocic False, poniewaz paczka jest zbyt cieka
        
        // Dodawanie packzi
        boolean result = packageService.addPackageFull(
                null, "Gdańsk", "Warszawa", 25.0, 
                20.0, 20.0, 20.0, 
                "N", "C", "S", "00-000", "e", "1", 
                "O", "C", "S", "00-000", "e", "2"
        );
        
        // Test czy zwrocilo False
        assertFalse(result);
        // Sprawdzenie czy byla proba zapisu do bazy
        verify(mockDao, never()).addPackage(any());
    }

    @Test
    public void testAddPackageFull_CheckWhatIsSentToDatabase() {
        // Test dodawania paczki
        // Sprawdzamy czy i co dodal
        
        // Programowanie Mocka
        // Gdy serwis zapyta o zapis nadawcy, mock ma udawać sukces i zwrócić testowe ID = 101
        when(mockDao.addSender(any(Sender.class))).thenReturn(101);
        // Gdy serwis zapyta o zapis odbiorcy, mock ma udawać sukces i zwrócić testowe ID = 202
        when(mockDao.addRecipient(any(Recipient.class))).thenReturn(202);
        // Gdy serwis spróbuje zapisać całą paczkę, mock ma potwierdzić powodzenie operacji (true)
        when(mockDao.addPackage(any(Package.class))).thenReturn(true);

        // Dodanie paczki
        boolean result = packageService.addPackageFull(
                null, "Gdańsk", "Katowice", 5.0, 
                15.0, 15.0, 15.0, 
                "Jan Kowalski", "Gdańsk", "Długa 1", "80-001", "jan@wp.pl", "111",
                "Anna Nowak", "Katowice", "Mariacka 5", "40-001", "ania@wp.pl", "222"
        );
        // Test czy zwrocilo True
        assertTrue(result);
        
        // Przygotowanie przechwycenia zapisywanej paczki
        ArgumentCaptor<Package> captor = ArgumentCaptor.forClass(Package.class);
        verify(mockDao).addPackage(captor.capture());
        Package capturedPackage = captor.getValue();
        
        // Sprawdzanie poprawnosci paczki
        assertEquals("B", capturedPackage.getPackage_format().getFormat_id(), "Powinien być gabaryt B");
        assertEquals(2, capturedPackage.getPackage_format().getSlot_coverage(), "Gabaryt B = 2 sloty");
        assertEquals("Gdańsk", capturedPackage.getPackage_region().getRegion_name(), "Miasto Gdańsk = GDA");
        assertEquals(101, capturedPackage.getPackage_sender().getSender_id(), "ID nadawcy = 101");
    }

    @Test
    public void testDeletePackage_Success() {
        // Test usuwania paczki
        when(mockDao.deletePackage(777)).thenReturn(true);
        assertTrue(packageService.deletePackage(777));
        verify(mockDao).deletePackage(777);
    }
    
}