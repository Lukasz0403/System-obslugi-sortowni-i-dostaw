package com.mycompany.projekt_io.feature.package_;

import com.mycompany.projekt_io.datamodel.Package;
import com.mycompany.projekt_io.datamodel.Rack;
import com.mycompany.projekt_io.datamodel.Zone;
import com.mycompany.projekt_io.datamodel.Format;
import com.mycompany.projekt_io.datamodel.Region;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PackageTableServiceTest {

    @Test
    public void testShelfFormattingWithRack() {
        // Przygotowanie mocków
        Package mockPackage = mock(Package.class);
        Rack mockRack = mock(Rack.class);
        Zone mockZone = mock(Zone.class);
        Format mockFormat = mock(Format.class);
        Region mockRegion = mock(Region.class);

        // Konfiguracja mocków (Ustawiamy zachowanie)
        when(mockPackage.getPackage_rack()).thenReturn(mockRack);
        when(mockRack.getRack_id()).thenReturn(5);
        when(mockRack.getZone()).thenReturn(mockZone);
        when(mockZone.getZone_id()).thenReturn(2);
        
        // Mockowanie pozostałych pól wymaganych przez konstruktor
        when(mockPackage.getPackage_format()).thenReturn(mockFormat);
        when(mockFormat.getFormat_id()).thenReturn("A");
        when(mockPackage.getPackage_region()).thenReturn(mockRegion);
        when(mockRegion.getRegion_name()).thenReturn("WAW");
        when(mockPackage.getPackage_dest_region()).thenReturn(mockRegion);

        // Akcja
        PackageTableService tableRow = new PackageTableService(mockPackage);

        // Weryfikacja
        assertEquals("5 / 2", tableRow.getShelf(), "Format regału powinien być '5 / 2'");
    }

    @Test
    public void testShelfFormattingWithoutRack() {
        // Przygotowanie mocków
        Package mockPackage = mock(Package.class);
        Format mockFormat = mock(Format.class);
        Region mockRegion = mock(Region.class);

        // Regał jest nullem
        when(mockPackage.getPackage_rack()).thenReturn(null);
        
        // Mockowanie reszty potrzebnej do konstruktora
        when(mockPackage.getPackage_format()).thenReturn(mockFormat);
        when(mockFormat.getFormat_id()).thenReturn("B");
        when(mockPackage.getPackage_region()).thenReturn(mockRegion);
        when(mockRegion.getRegion_name()).thenReturn("KRK");
        when(mockPackage.getPackage_dest_region()).thenReturn(mockRegion);

        // Akcja
        PackageTableService tableRow = new PackageTableService(mockPackage);

        // Weryfikacja
        assertEquals("0", tableRow.getShelf(), "Jeśli brak regału, powinno być '0'");
    }

    @Test
    public void testCorrectMappingOfFields() {
        // Przygotowanie danych
        Package p = mock(Package.class);
        Format f = mock(Format.class);
        Region r1 = mock(Region.class);
        Region r2 = mock(Region.class);

        // Mockowanie danych
        when(p.getPackage_id()).thenReturn(100);
        when(p.getPackage_format()).thenReturn(f);
        when(f.getFormat_id()).thenReturn("C");
        when(p.getPackage_region()).thenReturn(r1);
        when(r1.getRegion_name()).thenReturn("GDA");
        when(p.getPackage_dest_region()).thenReturn(r2);
        when(r2.getRegion_name()).thenReturn("WRO");
        when(p.getWeight()).thenReturn(15);
        when(p.getWidth()).thenReturn(40);
        when(p.getHeight()).thenReturn(30);
        when(p.getDepth()).thenReturn(60);

        // Akcja
        PackageTableService tableRow = new PackageTableService(p);

        // Weryfikacja
        assertEquals(100, tableRow.getId());
        assertEquals("C", tableRow.getSize());
        assertEquals("GDA", tableRow.getSenderRegion());
        assertEquals("WRO", tableRow.getReceiverRegion());
        assertEquals(15.0, tableRow.getWeight());
        assertEquals(40, tableRow.getWidth());
        assertEquals(30, tableRow.getHeight());
        assertEquals(60, tableRow.getDepth());
    }
}