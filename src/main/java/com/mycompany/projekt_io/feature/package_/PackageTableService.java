/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projekt_io.feature.package_;
import com.mycompany.projekt_io.datamodel.Package;

/**
 * Model wiersza tabeli paczek wyświetlanej w interfejsie użytkownika.
 * <p>
 * Klasa pełni rolę obiektu transferowego.
 * Przygotowuje dane w formacie gotowym do wyświetlenia w
 * komponencie {@code TableView} JavaFX. Każda instancja tej klasy odpowiada
 * jednemu wierszowi w tabeli paczek.
 * </p>
 * 
 * @author Mateusz Gojny
 */
public class PackageTableService {
    
    private int id;
    private String shelf;
    private String size;
    private String senderRegion;
    private String receiverRegion;
    private double weight;
    private int width;   
    private int height;  
    private int depth; 

    
    /**
     * Tworzy obiekt wiersza tabeli na podstawie pełnego obiektu paczki.
     * <p>
     * Jeśli paczka ma przypisany regał, pole {@code shelf} przyjmuje format
     * {@code "rack_id / zone_id"} (np. {@code "5 / 2"}). Jeśli paczka nie ma
     * przypisanego regału, pole {@code shelf} przyjmuje wartość {@code "0"}.
     * </p>
     *
     * @param p obiekt paczki {@link Package}, na podstawie którego tworzony
     * jest wiersz tabeli; nie może być {@code null}
     */
    public PackageTableService(Package p) {
        this.id = p.getPackage_id();
        if (p.getPackage_rack() != null) {
            this.shelf = String.valueOf(p.getPackage_rack().getRack_id())
                    + " / "
                    + String.valueOf(p.getPackage_rack().getZone().getZone_id());
        } else {
            this.shelf = "0";
        }
        this.size = p.getPackage_format().getFormat_id();
        this.senderRegion = p.getPackage_region().getRegion_name();
        this.receiverRegion = p.getPackage_dest_region().getRegion_name();
        this.weight = p.getWeight();
        this.width = p.getWidth();   
        this.height = p.getHeight(); 
        this.depth = p.getDepth(); 
    }
    
    /**
     * Zwraca identyfikator paczki.
     *
     * @return unikalny identyfikator paczki z bazy danych
     */
    public int getId() {
        return id;
    }

    /**
     * Zwraca informację o przypisanym regale i strefie.
     * <p>
     * Format: {@code "rack_id / zone_id"} (np. {@code "5 / 2"}) lub {@code "0"}
     * jeśli paczka nie ma przypisanego regału.
     * </p>
     *
     * @return ciąg znaków z numerem regału i strefy lub {@code "0"}
     */
    public String getShelf() {
        return shelf;
    }

    /**
     * Zwraca identyfikator gabarytu paczki.
     *
     * @return identyfikator gabarytu ("A", "B" lub "C")
     */
    public String getSize() {
        return size;
    }

    /**
     * Zwraca trzyliterowy kod regionu nadania paczki.
     *
     * @return kod regionu nadania (np. "WAW", "KRK")
     */
    public String getSenderRegion() {
        return senderRegion;
    }

    /**
     * Zwraca trzyliterowy kod regionu docelowego paczki.
     *
     * @return kod regionu docelowego (np. "GDA", "WRO")
     */
    public String getReceiverRegion() {
        return receiverRegion;
    }

    /**
     * Zwraca wagę paczki.
     *
     * @return waga paczki w kilogramach
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Zwraca szerokość paczki.
     *
     * @return szerokość paczki w centymetrach
     */
    public int getWidth() {
        return width;
    }

    /**
     * Zwraca wysokość paczki.
     *
     * @return wysokość paczki w centymetrach
     */
    public int getHeight() {
        return height;
    }

    /**
     * Zwraca głębokość paczki.
     *
     * @return głębokość paczki w centymetrach
     */
    public int getDepth() {
        return depth;
    }
    
    
    
        
}
