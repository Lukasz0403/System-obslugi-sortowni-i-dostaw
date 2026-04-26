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


/**
 * Serwis odpowiedzialny za zarządzanie paczkami w systemie.
 * <p>
 * Udostępnia metody do dodawania, aktualizowania i usuwania paczek, a także
 * pomocnicze metody do mapowania regionów i automatycznego przypisywania
 * gabarytów na podstawie wymiarów paczki. 
 * </p>
 * 
 * @author Mateusz Gojny
 */

public class PackageService {

    private final PackageDAO dao = new PackageDAO();


    /**
     * Dodaje nową paczkę do bazy danych 
     * <p>
     * Metoda przeznaczona do szybkiego dodawania paczek bez pełnych danych
     * adresowych. Gabaryt ({@code size}) jest przekazywany jawnie. Paczka nie
     * ma przypisanego regału ({@code rack = null}).
     * </p>
     *
     * @param size identyfikator gabarytu paczki ("A", "B" lub "C")
     * @param sendRegionName nazwa miasta regionu nadania (np. "Warszawa")
     * @param receiveRegionName nazwa miasta regionu docelowego (np. "Kraków")
     * @param weight waga paczki w kilogramach
     * @param width szerokość paczki w centymetrach
     * @param height wysokość paczki w centymetrach
     * @param depth głębokość paczki w centymetrach
     * @param senderName imię i nazwisko lub nazwa nadawcy
     * @param senderStreet ulica nadawcy
     * @param senderPostcode kod pocztowy nadawcy (format XX-XXX)
     * @param senderEmail adres e-mail nadawcy
     * @param senderPhone numer telefonu nadawcy
     * @param recipientName imię i nazwisko lub nazwa odbiorcy
     * @param recipientStreet ulica odbiorcy
     * @param recipientPostcode kod pocztowy odbiorcy (format XX-XXX)
     * @param recipientEmail adres e-mail odbiorcy
     * @param recipientPhone numer telefonu odbiorcy
     * @return {@code true} jeśli paczka została pomyślnie dodana do bazy,
     * {@code false} w przypadku błędu
     */

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
    
    /**
     * Dodaje nową paczkę do bazy danych wraz z pełnymi danymi nadawcy i
     * odbiorcy.
     * <p>
     * Nadawca i odbiorca są najpierw zapisywani jako nowe rekordy w bazie
     * danych, a ich wygenerowane ID są następnie przypisywane do paczki.
     * Gabaryt jest przypisywany automatycznie na podstawie wymiarów paczki przy
     * użyciu metody {@link #assignFormat(double, double, double)}. Przed
     * dodaniem paczki sprawdzane są minimalne dopuszczalne wymiary (min. 2 x 10
     * x 10 cm) oraz maksymalne (gabaryt C: 40 x 40 x 60 cm). Paczka nie ma
     * przypisanego regału ({@code rack = null}).
     * </p>
     *
     * @param size parametr ignorowany — gabaryt jest przypisywany automatycznie
     * @param sendRegion nazwa miasta regionu nadania (np. "Warszawa")
     * @param receiveRegion nazwa miasta regionu docelowego (np. "Kraków")
     * @param weight waga paczki w kilogramach
     * @param width szerokość paczki w centymetrach
     * @param height wysokość paczki w centymetrach
     * @param depth głębokość paczki w centymetrach
     * @param senderName imię i nazwisko lub nazwa nadawcy
     * @param senderCity miasto nadawcy
     * @param senderStreet ulica nadawcy
     * @param senderPostcode kod pocztowy nadawcy (format XX-XXX)
     * @param senderEmail adres e-mail nadawcy
     * @param senderPhone numer telefonu nadawcy
     * @param recipientName imię i nazwisko lub nazwa odbiorcy
     * @param recipientCity miasto odbiorcy
     * @param recipientStreet ulica odbiorcy
     * @param recipientPostcode kod pocztowy odbiorcy (format XX-XXX)
     * @param recipientEmail adres e-mail odbiorcy
     * @param recipientPhone numer telefonu odbiorcy
     * @return {@code true} jeśli paczka wraz z nadawcą i odbiorcą zostały
     * pomyślnie zapisane w bazie, {@code false} jeśli wymiary są nieprawidłowe,
     * gabaryt niemożliwy do przypisania lub wystąpił błąd zapisu w bazie danych
     */
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
    
    /**
     * Aktualizuje dane istniejącej paczki w bazie danych wraz z danymi nadawcy
     * i odbiorcy.
     * <p>
     * Metoda aktualizuje jednocześnie trzy rekordy w bazie: paczkę, jej nadawcę
     * oraz odbiorcę. Zwraca {@code true} tylko jeśli wszystkie trzy operacje
     * zakończyły się sukcesem. Regał paczki pozostaje niezmieniony —
     * przekazywany jest bezpośrednio z istniejącego obiektu paczki.
     * </p>
     *
     * @param packageId identyfikator aktualizowanej paczki
     * @param size identyfikator gabarytu paczki ("A", "B" lub "C")
     * @param sendRegion nazwa miasta regionu nadania
     * @param receiveRegion nazwa miasta regionu docelowego
     * @param weight waga paczki w kilogramach
     * @param width szerokość paczki w centymetrach
     * @param height wysokość paczki w centymetrach
     * @param depth głębokość paczki w centymetrach
     * @param senderId identyfikator nadawcy w bazie danych
     * @param senderName imię i nazwisko lub nazwa nadawcy
     * @param senderCity miasto nadawcy
     * @param senderStreet ulica nadawcy
     * @param senderPostcode kod pocztowy nadawcy (format XX-XXX)
     * @param senderEmail adres e-mail nadawcy
     * @param senderPhone numer telefonu nadawcy
     * @param recipientId identyfikator odbiorcy w bazie danych
     * @param recipientName imię i nazwisko lub nazwa odbiorcy
     * @param recipientCity miasto odbiorcy
     * @param recipientStreet ulica odbiorcy
     * @param recipientPostcode kod pocztowy odbiorcy (format XX-XXX)
     * @param recipientEmail adres e-mail odbiorcy
     * @param recipientPhone numer telefonu odbiorcy
     * @param rack regał, do którego przypisana jest paczka; może być
     * {@code null} jeśli paczka nie ma regału
     * @return {@code true} jeśli aktualizacja paczki, nadawcy i odbiorcy
     * zakończyła się sukcesem, {@code false} jeśli którakolwiek z operacji nie
     * powiodła się
     */
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
    
    /**
     * Usuwa paczkę z bazy danych na podstawie jej identyfikatora.
     *
     * @param packageId identyfikator paczki do usunięcia
     * @return {@code true} jeśli paczka została pomyślnie usunięta,
     * {@code false} jeśli paczka o podanym ID nie istnieje lub wystąpił błąd
     * bazy danych
     */
    public boolean deletePackage(int packageId) {
        return dao.deletePackage(packageId);
    }
    
    /**
     * Wyszukuje identyfikator regionu kurierskiego na podstawie nazwy miasta.
     * <p>
     * Najpierw konwertuje pełną nazwę miasta na trzyliterowy kod regionu przy
     * użyciu {@link #mapRegionNameToCode(String)}, a następnie przeszukuje
     * listę regionów pobraną z bazy danych. Jeśli region nie zostanie
     * znaleziony, zwracana jest wartość {@code 1} jako domyślna.
     * </p>
     *
     * @param name pełna nazwa miasta (np. "Warszawa", "Kraków")
     * @return identyfikator regionu z bazy danych lub {@code 1} jeśli nie
     * znaleziono dopasowania
     */
    private int findRegionIdByName(String name) {
        String code = mapRegionNameToCode(name);

        return dao.getRegions().stream()
                .filter(r -> r.getRegion_name().equals(code))
                .map(Region::getRegion_id)
                .findFirst()
                .orElse(1);
    }
    
    /**
     * Mapuje identyfikator gabarytu na liczbę zajmowanych slotów magazynowych.
     * <p>
     * Gabaryt A - 1 slot, B - 2 sloty, C - 4 sloty. Dla nierozpoznanego
     * gabarytu zwracana jest wartość domyślna {@code 1}.
     * </p>
     *
     * @param size identyfikator gabarytu ("A", "B" lub "C")
     * @return liczba slotów zajmowanych przez dany gabaryt
     */
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
    
    /**
     * Konwertuje pełną nazwę miasta na trzyliterowy kod regionu kurierskiego.
     * <p>
     * Obsługuje wszystkie 20 regionów kurierskich zdefiniowanych w bazie danych.
     * Jeśli nazwa miasta nie zostanie rozpoznana, zwracany jest domyślny
     * kod {@code "WAW"} (Warszawa).
     * </p>
     *
     * @param name pełna nazwa miasta (np. "Gdańsk", "Wrocław")
     * @return trzyliterowy kod regionu kurierskiego (np. "GDA", "WRO")
     *         lub {@code "WAW"} jeśli nazwa nie została rozpoznana
     */
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
    
    
    /**
     * Automatycznie przypisuje gabaryt paczki na podstawie jej wymiarów.
     *
     * @param width szerokość paczki w centymetrach
     * @param height wysokość paczki w centymetrach
     * @param depth głębokość paczki w centymetrach
     * @return identyfikator gabarytu ("A", "B" lub "C") lub {@code null} jeśli
     * paczka jest zbyt duża dla żadnego gabarytu
     */
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
