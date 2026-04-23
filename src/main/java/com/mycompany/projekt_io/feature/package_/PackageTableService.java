/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projekt_io.feature.package_;
import com.mycompany.projekt_io.datamodel.Package;

/**
 *
 * @author mateu
 */
public class PackageTableService {
    
    private int id;
    private String shelf;
    private String size;
    private String senderRegion;
    private String receiverRegion;
     private double weight;

    public PackageTableService(Package p) {
        this.id = p.getPackage_id();
        if (p.getPackage_rack() != null) {
            this.shelf = String.valueOf(p.getPackage_rack().getRack_id());
        } else {
            this.shelf = "0";
        }
        this.size = p.getPackage_format().getFormat_id();
        this.senderRegion = p.getPackage_region().getRegion_name();
        this.receiverRegion = p.getPackage_dest_region().getRegion_name();
        this.weight = p.getPackage_format().getMax_wage();
    }
    
    public int getId() { return id; }
    public String getShelf() { return shelf; }
    public String getSize() { return size; }
    public String getSenderRegion() { return senderRegion; }
    public String getReceiverRegion() { return receiverRegion; }
    public double getWeight() { return weight; }
    
    
    
        
}
