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
import com.mycompany.projekt_io.datamodel.Rack;

public class PackageService {

    private final PackageDAO dao = new PackageDAO();



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

        int sendRegionId = findRegionIdByName(sendRegionName);
        int receiveRegionId = findRegionIdByName(receiveRegionName);

        Sender sender = new Sender(1, senderName, "", senderStreet, senderPostcode, senderEmail, senderPhone);
        Recipient recipient = new Recipient(1, recipientName, "", recipientStreet, recipientPostcode, recipientEmail, recipientPhone);

        Region region = new Region(sendRegionId, sendRegionName);
        Region destRegion = new Region(receiveRegionId, receiveRegionName);

        Format format = new Format(
                size,
                (int) width,
                (int) height,
                (int) depth,
                (int) weight,
                mapSizeToSlot(size)
        );
        Rack rack = null;

        Package pack = new Package(
                0,
                sender,
                recipient,
                region,
                destRegion,
                format,
                rack,
                (int) width, 
                (int) height, 
                (int) depth, 
                (int) weight
        );

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
        
        if (width < 2 || height < 10 || depth < 10) {
            System.out.println("Paczka zbyt mała! Minimum: 2x10x10 cm");
            return false;
        }

        // AUTOMATYCZNE PRZYPISANIE GABARYTU
         size = assignFormat(width, height, depth);
        if (size == null) {
            System.out.println("Paczka zbyt duża! Maksymalny gabaryt to C (40x40x60 cm)");
            return false;
        }

        Sender sender = new Sender(0, senderName, senderCity,
                senderStreet, senderPostcode, senderEmail, senderPhone);
        int senderId = dao.addSender(sender);

        Recipient recipient = new Recipient(0, recipientName, recipientCity,
                recipientStreet, recipientPostcode, recipientEmail, recipientPhone);
        int recipientId = dao.addRecipient(recipient);

        if (senderId == -1 || recipientId == -1) {
            return false;
        }

        Region region = new Region(findRegionIdByName(sendRegion), sendRegion);
        Region destRegion = new Region(findRegionIdByName(receiveRegion), receiveRegion);

        Format format = new Format(
                size,
                (int) width,
                (int) height,
                (int) depth,
                (int) weight,
                mapSizeToSlot(size)
        );

        Package pack = new Package(
                0,
                new Sender(senderId, null, null, null, null, null, null),
                new Recipient(recipientId, null, null, null, null, null, null),
                region,
                destRegion,
                format,
                null,
                (int) width, 
                (int) height, 
                (int) depth, 
                (int) weight
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
            int senderId,
            String senderName,
            String senderCity,
            String senderStreet,
            String senderPostcode,
            String senderEmail,
            String senderPhone,
            int recipientId,
            String recipientName,
            String recipientCity,
            String recipientStreet,
            String recipientPostcode,
            String recipientEmail,
            String recipientPhone,
            Rack rack
    ) {

        Sender sender = new Sender(
                senderId, senderName, senderCity,
                senderStreet, senderPostcode, senderEmail, senderPhone
        );

        Recipient recipient = new Recipient(
                recipientId, recipientName, recipientCity,
                recipientStreet, recipientPostcode, recipientEmail, recipientPhone
        );

        Region region = new Region(findRegionIdByName(sendRegion), sendRegion);
        Region destRegion = new Region(findRegionIdByName(receiveRegion), receiveRegion);

        Format format = new Format(
                size,
                (int) width,
                (int) height,
                (int) depth,
                (int) weight,
                mapSizeToSlot(size)
        );

        Package pack = new Package(
                packageId,
                sender,
                recipient,
                region,
                destRegion,
                format,
                rack,
                (int) width, // ← NOWE
                (int) height, // ← NOWE
                (int) depth, // ← NOWE
                (int) weight
        );

        boolean senderUpdated = dao.updateSender(sender);
        boolean recipientUpdated = dao.updateRecipient(recipient);
        boolean packageUpdated = dao.changePackage(pack);

        return senderUpdated && recipientUpdated && packageUpdated;
    }
    
    
    public boolean deletePackage(int packageId) {
        return dao.deletePackage(packageId);
    }
    
    private int findRegionIdByName(String name) {
        String code = mapRegionNameToCode(name);

        return dao.getRegions().stream()
                .filter(r -> r.getRegion_name().equals(code))
                .map(Region::getRegion_id)
                .findFirst()
                .orElse(1);
    }
    
    private int mapSizeToSlot(String size) {
        switch (size) {
            case "A":
                return 1;
            case "B":
                return 2;
            case "C":
                return 4;
            default:
                return 1;
        }
    }
    
    private String mapRegionNameToCode(String name) {
        switch (name) {
            case "Białystok":
                return "BIA";
            case "Bydgoszcz":
                return "BYD";
            case "Częstochowa":
                return "CZE";
            case "Gdańsk":
                return "GDA";
            case "Gdynia":
                return "GDY";
            case "Katowice":
                return "KAT";
            case "Kielce":
                return "KIE";
            case "Kraków":
                return "KRK";
            case "Łódź":
                return "LOD";
            case "Lublin":
                return "LUB";
            case "Olsztyn":
                return "OLS";
            case "Opole":
                return "OPL";
            case "Poznań":
                return "POZ";
            case "Rzeszów":
                return "RZE";
            case "Sopot":
                return "SOP";
            case "Szczecin":
                return "SZC";
            case "Toruń":
                return "TOR";
            case "Warszawa":
                return "WAW";
            case "Wrocław":
                return "WRO";
            case "Zielona Góra":
                return "ZIE";
            default:
                return "WAW";
        }
    }
    
    private String assignFormat(double width, double height, double depth) {
       

        if (width <= 10 && height <= 40 && depth <= 60) {
            return "A";
        }
        if (width <= 20 && height <= 40 && depth <= 60) {
            return "B";
        }
        if (width <= 40 && height <= 40 && depth <= 60) {
            return "C";
        }

        return null; // za duża
    }
}
