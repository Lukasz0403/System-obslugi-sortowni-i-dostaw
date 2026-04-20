/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projekt_io.feature.package_;
import com.mycompany.projekt_io.core.database.PackageDAO;
import com.mycompany.projekt_io.datamodel.Format;
import com.mycompany.projekt_io.datamodel.Package;
import com.mycompany.projekt_io.datamodel.Recipient;
import com.mycompany.projekt_io.datamodel.Region;
import com.mycompany.projekt_io.datamodel.Sender;
import com.mycompany.projekt_io.datamodel.Shelf;

public class PackageService {

    private final PackageDAO dao = new PackageDAO();

    private String mapRegionToCode(String region) {
        switch (region) {
            case "Katowice":
                return "CZ1";
            case "Gliwice":
                return "DE1";
            case "Zabrze":
                return "GA2";
            case "Bytom":
                return "PL1";
            case "Chorzów":
                return "PL2";
            case "Ruda Śląska":
                return "PL3";
            case "Tychy":
                return "RB4";
            case "Dąbrowa Górnicza":
                return "WA8";
            case "Sosnowiec":
                return "CZ1";
            default:
                return "CZ1";
        }
    }

    public boolean addPackage(
            String size,
            String sendRegionName,
            String receiveRegionName,
            double weight,
            double width,
            double height,
            double depth,
            String senderName,
            String senderStreet,
            String senderPostcode,
            String senderEmail,
            String senderPhone,
            String recipientName,
            String recipientStreet,
            String recipientPostcode,
            String recipientEmail,
            String recipientPhone
    ) {

        String sendRegionCode = mapRegionToCode(sendRegionName);
        String receiveRegionCode = mapRegionToCode(receiveRegionName);

        Sender sender = new Sender(1, senderName, "", senderStreet, senderPostcode, senderEmail, senderPhone);
        Recipient recipient = new Recipient(1, recipientName, "", recipientStreet, recipientPostcode, recipientEmail, recipientPhone);

        Region region = new Region(sendRegionCode);
        Region destRegion = new Region(receiveRegionCode);

        Format format = new Format(size, (int) width, (int) height, (int) depth, (int) weight);
        Shelf shelf = new Shelf(1, null);

        Package pack = new Package(0, sender, recipient, region, destRegion, format, shelf);

        return dao.addPackage(pack);
    }
    
    
    public boolean addPackageFull(
            String size,
            String sendRegion,
            String receiveRegion,
            double weight,
            double width,
            double height,
            double depth,
            String senderName,
            String senderCity,
            String senderStreet,
            String senderPostcode,
            String senderEmail,
            String senderPhone,
            String recipientName,
            String recipientCity,
            String recipientStreet,
            String recipientPostcode,
            String recipientEmail,
            String recipientPhone
    ) {

        PackageDAO dao = new PackageDAO();

        
        Sender sender = new Sender(0, senderName, senderCity, senderStreet, senderPostcode, senderEmail, senderPhone);
        int senderId = dao.addSender(sender);

        
        Recipient recipient = new Recipient(0, recipientName, recipientCity, recipientStreet, recipientPostcode, recipientEmail, recipientPhone);
        int recipientId = dao.addRecipient(recipient);

        if (senderId == -1 || recipientId == -1) {
            return false;
        }

        
        Region region = new Region(mapRegionToCode(sendRegion));
        Region destRegion = new Region(mapRegionToCode(receiveRegion));

        
        Format format = new Format(size, (int) width, (int) height, (int) depth, (int) weight);

        
        Shelf shelf = null;

        
        Package pack = new Package(
                0,
                new Sender(senderId, null, null, null, null, null, null),
                new Recipient(recipientId, null, null, null, null, null, null),
                region,
                destRegion,
                format,
                shelf
        );

        return dao.addPackage(pack);
    }
    
    
    public boolean updatePackageFull(
            int packageId,
            String size,
            String sendRegion,
            String receiveRegion,
            double weight,
            double width,
            double height,
            double depth,
            // sender
            int senderId,
            String senderName,
            String senderCity,
            String senderStreet,
            String senderPostcode,
            String senderEmail,
            String senderPhone,
            // recipient
            int recipientId,
            String recipientName,
            String recipientCity,
            String recipientStreet,
            String recipientPostcode,
            String recipientEmail,
            String recipientPhone,
            Shelf shelf
            
    ) {

        PackageDAO dao = new PackageDAO();

        // 🔹 update sender
        Sender sender = new Sender(
                senderId,
                senderName,
                senderCity,
                senderStreet,
                senderPostcode,
                senderEmail,
                senderPhone
        );

        // 🔹 update recipient
        Recipient recipient = new Recipient(
                recipientId,
                recipientName,
                recipientCity,
                recipientStreet,
                recipientPostcode,
                recipientEmail,
                recipientPhone
        );

        // 🔹 regiony
        Region region = new Region(mapRegionToCode(sendRegion));
        Region destRegion = new Region(mapRegionToCode(receiveRegion));
        

        // 🔹 format
        Format format = new Format(
                size,
                (int) width,
                (int) height,
                (int) depth,
                (int) weight
        );

        // 🔹 package
        Package pack = new Package(
                packageId,
                sender,
                recipient,
                region,
                destRegion,
                format,
                shelf
        );

        // 🔥 WYWOŁANIA DAO
        boolean senderUpdated = dao.updateSender(sender);
        boolean recipientUpdated = dao.updateRecipient(recipient);
        boolean packageUpdated = dao.changePackage(pack);

        return senderUpdated && recipientUpdated && packageUpdated;
    }
    
    
    public boolean deletePackage(int packageId) {
        return dao.deletePackage(packageId);
    }
}
