/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projekt_io.core.database;

import com.mycompany.projekt_io.datamodel.Format;
import java.util.List;
import com.mycompany.projekt_io.datamodel.Package;
import com.mycompany.projekt_io.datamodel.Rack;
import com.mycompany.projekt_io.datamodel.Recipient;
import com.mycompany.projekt_io.datamodel.Region;
import com.mycompany.projekt_io.datamodel.Sender;
import com.mycompany.projekt_io.datamodel.Zone;

/**
 *
 * @author Radosław
 */
public interface PackageDAOInterface {
    
    List<Package> getPackages();
    Package getPackage(int id);
    Boolean addPackage(Package p);
    Boolean changePackage(Package p);
    List<Format> getFormats();
    List<Sender> getSenders();
    List<Recipient> getRecipient();
    List<Region> getRegions();
    List<Rack> getRacks();
    List<Zone> getZones();
    int addSender(Sender s);
    int addRecipient(Recipient r);
    int getPackageCountForShelf(int shelfId);
    int getLastPackageIdForShelf(int shelfId);
    int getTotalPackageCount();
    int getPackageCountForZone(int zoneId);
    int getMostLoadedShelfId();
    boolean updateSender(Sender s);
    boolean updateRecipient(Recipient r);
    boolean deletePackage(int packageId);
}


