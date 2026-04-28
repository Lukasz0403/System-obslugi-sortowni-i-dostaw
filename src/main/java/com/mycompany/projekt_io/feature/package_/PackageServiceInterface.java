package com.mycompany.projekt_io.feature.package_;

import com.mycompany.projekt_io.datamodel.Rack;

/**
 * Interfejs definiujący kontrakt dla operacji biznesowych na paczkach.
 * <p>
 * Określa zestaw operacji dostępnych w warstwie serwisowej systemu
 * magazynowego. Implementowany przez {@link PackageService}.
 * </p>
 * @author Mateusz Gojny
 */
public interface PackageServiceInterface {

    boolean addPackage(String size, String sendRegionName, String receiveRegionName,
                       double weight, double width, double height, double depth,
                       String senderName, String senderStreet, String senderPostcode,
                       String senderEmail, String senderPhone,
                       String recipientName, String recipientStreet, String recipientPostcode,
                       String recipientEmail, String recipientPhone);

    boolean addPackageFull(String size, String sendRegion, String receiveRegion,
                           double weight, double width, double height, double depth,
                           String senderName, String senderCity, String senderStreet,
                           String senderPostcode, String senderEmail, String senderPhone,
                           String recipientName, String recipientCity, String recipientStreet,
                           String recipientPostcode, String recipientEmail, String recipientPhone);

    boolean updatePackageFull(int packageId, String size, String sendRegion, String receiveRegion,
                              double weight, double width, double height, double depth,
                              int senderId, String senderName, String senderCity, String senderStreet,
                              String senderPostcode, String senderEmail, String senderPhone,
                              int recipientId, String recipientName, String recipientCity,
                              String recipientStreet, String recipientPostcode,
                              String recipientEmail, String recipientPhone, Rack rack);

    boolean deletePackage(int packageId);
    
}