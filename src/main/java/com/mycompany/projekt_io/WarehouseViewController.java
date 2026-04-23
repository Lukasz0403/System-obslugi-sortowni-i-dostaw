package com.mycompany.projekt_io;

import com.mycompany.projekt_io.datamodel.Rack;
import com.mycompany.projekt_io.feature.werehouse.SortingService;

//HUH? - Ida

/**
 *
 * @author lukas
 */
public class WarehouseViewController {

    private Rack warehouse;
    private SortingService sortingService;

    

    public WarehouseViewController(Rack warehouse, SortingService sortingService) {
        this.warehouse = warehouse;
        this.sortingService = sortingService;
    }
    
    public void updateView() {
        
    }

    public void showStatistics() {
        
    }

    public void goToMainWindow() {
        
    }
}