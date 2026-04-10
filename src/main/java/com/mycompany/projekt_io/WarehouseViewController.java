package com.mycompany.projekt_io;

import com.mycompany.projekt_io.datamodel.Warehouse;
import com.mycompany.projekt_io.feature.werehouse.SortingService;

/**
 *
 * @author lukas
 */
public class WarehouseViewController {

    private Warehouse warehouse;
    private SortingService sortingService;

    

    public WarehouseViewController(Warehouse warehouse, SortingService sortingService) {
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