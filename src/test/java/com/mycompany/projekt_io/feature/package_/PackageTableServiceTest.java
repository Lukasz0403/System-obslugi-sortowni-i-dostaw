package com.mycompany.projekt_io.feature.package_;

import com.mycompany.projekt_io.datamodel.Package;
import com.mycompany.projekt_io.datamodel.Rack;
import com.mycompany.projekt_io.datamodel.Zone;
import com.mycompany.projekt_io.datamodel.Format;
import com.mycompany.projekt_io.datamodel.Region;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testy jednostkowe klasy {@link PackageTableService}.
 * <p>
 * Weryfikują poprawność formatowania danych paczki na potrzeby wyświetlania w
 * komponencie TableView. Testy wykorzystują mocki obiektów
 * {@link Package}, {@link Rack} i {@link Format}.
 * </p>
 * 
 * @author Paweł Pietrusiak
 */
public class PackageTableServiceTest {

    /**
     * Sprawdza czy pole shelf jest poprawnie formatowane jako
     * {@code "rackId / zoneId"} gdy paczka ma przypisany regał.
     */
    @Test
    public void testShelfFormattingWithRack() {
        Package mockPackage = mock(Package.class);
        Rack mockRack = mock(Rack.class);
        Zone mockZone = mock(Zone.class);
        Format mockFormat = mock(Format.class);
        Region mockRegion = mock(Region.class);

        when(mockPackage.getPackage_rack()).thenReturn(mockRack);
        when(mockRack.getRack_id()).thenReturn(5);
        when(mockRack.getZone()).thenReturn(mockZone);
        when(mockZone.getZone_id()).thenReturn(2);
        when(mockPackage.getPackage_format()).thenReturn(mockFormat);
        when(mockFormat.getFormat_id()).thenReturn("A");
        when(mockPackage.getPackage_region()).thenReturn(mockRegion);
        when(mockRegion.getRegion_name()).thenReturn("WAW");
        when(mockPackage.getPackage_dest_region()).thenReturn(mockRegion);

        PackageTableService tableRow = new PackageTableService(mockPackage);

        assertEquals("5 / 2", tableRow.getShelf(), "Format regału powinien być '5 / 2'");
    }

    /**
     * Sprawdza czy pole shelf zwraca wartość {@code "0"} gdy paczka nie ma
     * przypisanego regału (rack jest null).
     */
    @Test
    public void testShelfFormattingWithoutRack() {
        Package mockPackage = mock(Package.class);
        Format mockFormat = mock(Format.class);
        Region mockRegion = mock(Region.class);

        when(mockPackage.getPackage_rack()).thenReturn(null);
        when(mockPackage.getPackage_format()).thenReturn(mockFormat);
        when(mockFormat.getFormat_id()).thenReturn("B");
        when(mockPackage.getPackage_region()).thenReturn(mockRegion);
        when(mockRegion.getRegion_name()).thenReturn("KRK");
        when(mockPackage.getPackage_dest_region()).thenReturn(mockRegion);

        PackageTableService tableRow = new PackageTableService(mockPackage);

        assertEquals("0", tableRow.getShelf(), "Jeśli brak regału, powinno być '0'");
    }

    /**
     * Sprawdza czy wszystkie pola obiektu {@link PackageTableService} są
     * poprawnie mapowane z danych obiektu {@link Package}.
     */
    @Test
    public void testCorrectMappingOfFields() {
        Package p = mock(Package.class);
        Format f = mock(Format.class);
        Region r1 = mock(Region.class);
        Region r2 = mock(Region.class);

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

        PackageTableService tableRow = new PackageTableService(p);

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
