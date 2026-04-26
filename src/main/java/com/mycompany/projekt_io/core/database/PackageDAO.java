/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projekt_io.core.database;

import com.mycompany.projekt_io.datamodel.Format;
import com.mycompany.projekt_io.datamodel.Package;
import com.mycompany.projekt_io.datamodel.Rack;
import com.mycompany.projekt_io.datamodel.Recipient;
import com.mycompany.projekt_io.datamodel.Region;
import com.mycompany.projekt_io.datamodel.Sender;
import com.mycompany.projekt_io.datamodel.Zone;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementacja metod DAO dla bazy danych z paczkami.
 * <p>
 * Klasa realizuje operacje CRUD (tworzenie, odczyt, aktualizacja, usuwanie) dla
 * paczek, nadawców, odbiorców, regałów, stref, regionów kurierskich oraz
 * formatów paczek. Połączenie z bazą danych pobierane jest za pośrednictwem
 * {@link ConnectDatabasePackage}.
 * </p>
 *
 * @author Radosław Kruczek
 */
public class PackageDAO implements PackageDAOInterface {

    
    /**
     * Pobiera listę wszystkich paczek z bazy danych wraz z pełnymi danymi
     * powiązanych obiektów.
     * <p>
     * Zapytanie łączy tabele: {@code packages}, {@code senders},
     * {@code recipients}, {@code courier_regions} (dwukrotnie — dla regionu
     * nadania i docelowego), {@code package_formats}, {@code racks} oraz
     * {@code zones}. Użycie {@code LEFT JOIN} dla tabel {@code racks} i
     * {@code zones} pozwala na pobranie paczek bez przypisanego regału (kolumna
     * {@code package_rack} może mieć wartość {@code NULL}).
     * </p>
     *
     * @return lista obiektów {@link Package} reprezentujących wszystkie paczki
     * w bazie danych; pusta lista jeśli brak paczek lub wystąpił błąd
     * 
     * @auhtor Radosław Kruczek
     */
    @Override
    public List<Package> getPackages() {
        
        List<Package> packages = new ArrayList<>();
        
        try {
            
            Connection conn = ConnectDatabasePackage.getConnection();
            
            String sql = "SELECT * FROM packages "
                    + "JOIN senders ON package_sender = sender_id "
                    + "JOIN recipients ON package_recipient = recipient_id "
                    + "JOIN courier_regions r1 ON package_region = r1.region_id "
                    + "JOIN courier_regions r2 ON package_dest_region = r2.region_id "
                    + "JOIN package_formats ON package_format = format_id "
                    + "LEFT JOIN racks ON package_rack = rack_id "
                    + "LEFT JOIN zones ON racks.zone = zones.zone_id ";
                    
            
            Statement s = conn.createStatement();
            
            ResultSet rs = s.executeQuery(sql);
            

            
            while (rs.next()) {
                 
                Sender sender = new Sender(rs.getInt("sender_id"), 
                                            rs.getString("sender_name"), 
                                            rs.getString("sender_city"), 
                                            rs.getString("sender_street"), 
                                            rs.getString("sender_postcode"), 
                                            rs.getString("sender_email"), 
                                            rs.getString("sender_phone"));

                Recipient recipient = new Recipient(rs.getInt("recipient_id"), 
                                            rs.getString("recipient_name"), 
                                            rs.getString("recipient_city"), 
                                            rs.getString("recipient_street"), 
                                            rs.getString("recipient_postcode"), 
                                            rs.getString("recipient_email"), 
                                            rs.getString("recipient_phone"));

                Region region = new Region(rs.getInt("r1.region_id"), rs.getString("r1.region_name"));

                Region destRegion = new Region(rs.getInt("r2.region_id"), rs.getString("r2.region_name"));

                Format format = new Format(rs.getString("format_id"), 
                                            rs.getInt("max_format_width"), 
                                            rs.getInt("max_format_height"), 
                                            rs.getInt("max_format_depth"),
                                            rs.getInt("max_weight"),
                                            rs.getInt("slot_coverage"));

                //Rack rack = new Rack(rs.getInt("rack_id"), new Zone(rs.getInt("zone_id")));   
                
                Rack rack = null;

                int rackId = rs.getInt("rack_id");
                if (!rs.wasNull()) {
                    int zoneId = rs.getInt("zone_id");
                    rack = new Rack(rackId, new Zone(zoneId));
                }
                
                Package pack = new Package(rs.getInt("package_id"), sender, recipient, region, destRegion, format, rack,
                rs.getInt("width"),   
                rs.getInt("height"),  
                rs.getInt("depth"),   
                rs.getInt("weight") );
                
                
                 
                 
                packages.add(pack);              
             }
            
        } catch (SQLException ex) {
        }
        
        return packages;
    }

    
    /**
     * Pobiera pojedynczą paczkę z bazy danych na podstawie jej identyfikatora.
     * <p>
     * Zapytanie jest analogiczne do {@link #getPackages()}, z dodatkowym
     * warunkiem {@code WHERE package_id = ?}. Jeśli paczka o podanym ID nie
     * istnieje, metoda zwraca {@code null}.
     * </p>
     *
     * @param id identyfikator paczki do pobrania
     * @return obiekt {@link Package} z danymi paczki lub {@code null} jeśli
     * paczka nie istnieje lub wystąpił błąd bazy danych
     * 
     * @auhtor Radosław Kruczek
     */
    @Override
    public Package getPackage(int id) {
        
        
        Package pack = null;
        
        try {
            
            Connection conn = ConnectDatabasePackage.getConnection();
            
            String sql = "SELECT * FROM packages "
                    + "JOIN senders ON package_sender = sender_id "
                    + "JOIN recipients ON package_recipient = recipient_id "
                    + "JOIN courier_regions r1 ON package_region = r1.region_id "
                    + "JOIN courier_regions r2 ON package_dest_region = r2.region_id "
                    + "JOIN package_formats ON package_format = format_id "
                    + "LEFT JOIN racks ON package_rack = rack_id "
                    + "LEFT JOIN zones ON zone = zone_id "
                    + "WHERE package_id = ?";
            
            PreparedStatement ps = conn.prepareStatement(sql);
            
            ps.setInt(1, id);
            
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()) {
                
                Sender sender = new Sender(rs.getInt("sender_id"), 
                                            rs.getString("sender_name"), 
                                            rs.getString("sender_city"), 
                                            rs.getString("sender_street"), 
                                            rs.getString("sender_postcode"), 
                                            rs.getString("sender_email"), 
                                            rs.getString("sender_phone"));

                Recipient recipient = new Recipient(rs.getInt("recipient_id"), 
                                            rs.getString("recipient_name"), 
                                            rs.getString("recipient_city"), 
                                            rs.getString("recipient_street"), 
                                            rs.getString("recipient_postcode"), 
                                            rs.getString("recipient_email"), 
                                            rs.getString("recipient_phone"));

                Region region = new Region(rs.getInt("r1.region_id"), rs.getString("r1.region_name"));

                Region destRegion = new Region(rs.getInt("r2.region_id"), rs.getString("r2.region_name"));

                Format format = new Format(rs.getString("format_id"), 
                                            rs.getInt("max_format_width"), 
                                            rs.getInt("max_format_height"), 
                                            rs.getInt("max_format_depth"),
                                            rs.getInt("max_weight"),
                                            rs.getInt("slot_coverage"));

                Rack rack = new Rack(rs.getInt("rack_id"), new Zone(rs.getInt("zone_id")));   

                pack = new Package(rs.getInt("package_id"), sender, recipient, region, destRegion, format, rack,
                        rs.getInt("width"), 
                        rs.getInt("height"), 
                        rs.getInt("depth"), 
                        rs.getInt("weight"));   
            }     
        
            
        } catch (SQLException ex) {
        }
        
        return pack;
    }

    /**
     * Pobiera listę wszystkich dostępnych formatów paczek z bazy danych.
     *
     * @return lista obiektów {@link Format} reprezentujących wszystkie formaty
     * zdefiniowane w tabeli {@code package_formats}; pusta lista jeśli brak
     * formatów lub wystąpił błąd
     * 
     * @auhtor Radosław Kruczek
     */
    @Override
    public List<Format> getFormats() {
        
        List<Format> formats = new ArrayList<>();
        
        try {
            
            Connection conn = ConnectDatabasePackage.getConnection();
            
            String sql = "SELECT * FROM package_formats";
                    
            
            Statement s = conn.createStatement();
            
            ResultSet rs = s.executeQuery(sql);
            

            
            while (rs.next()) {

                Format format = new Format(rs.getString("format_id"), 
                                            rs.getInt("max_format_width"), 
                                            rs.getInt("max_format_height"), 
                                            rs.getInt("max_format_depth"),
                                            rs.getInt("max_weight"),
                                            rs.getInt("slot_coverage"));
                 
                 
                formats.add(format);              
             }
            
        } catch (SQLException ex) {
        }
        
        return formats;
    }

    /**
     * Pobiera listę wszystkich nadawców z bazy danych.
     *
     * @return lista obiektów {@link Sender} z tabeli {@code senders}; pusta
     * lista jeśli brak nadawców lub wystąpił błąd
     * 
     * @auhtor Radosław Kruczek
     */
    @Override
    public List<Sender> getSenders() {
        
        List<Sender> senders = new ArrayList<>();
        
        try {
            
            Connection conn = ConnectDatabasePackage.getConnection();
            
            String sql = "SELECT * FROM senders";
                    
            
            Statement s = conn.createStatement();
            
            ResultSet rs = s.executeQuery(sql);
            

            
            while (rs.next()) {

                Sender sender = new Sender(rs.getInt("sender_id"), 
                                            rs.getString("sender_name"), 
                                            rs.getString("sender_city"), 
                                            rs.getString("sender_street"), 
                                            rs.getString("sender_postcode"), 
                                            rs.getString("sender_email"), 
                                            rs.getString("sender_phone"));
                 
                 
                senders.add(sender);              
             }
            
        } catch (SQLException ex) {
        }
        
        return senders;
    }

    /**
     * Pobiera listę wszystkich odbiorców z bazy danych.
     *
     * @return lista obiektów {@link Recipient} z tabeli {@code recipients};
     * pusta lista jeśli brak odbiorców lub wystąpił błąd
     * 
     * @auhtor Radosław Kruczek
     */
    @Override
    public List<Recipient> getRecipient() {
        
        List<Recipient> recipients = new ArrayList<>();
        
        try {
            
            Connection conn = ConnectDatabasePackage.getConnection();
            
            String sql = "SELECT * FROM recipients";
                    
            
            Statement s = conn.createStatement();
            
            ResultSet rs = s.executeQuery(sql);
            

            
            while (rs.next()) {

                Recipient recipient = new Recipient(rs.getInt("recipient_id"), 
                                            rs.getString("recipient_name"), 
                                            rs.getString("recipient_city"), 
                                            rs.getString("recipient_street"), 
                                            rs.getString("recipient_postcode"), 
                                            rs.getString("recipient_email"), 
                                            rs.getString("recipient_phone"));
                 
                 
                recipients.add(recipient);              
             }
            
        } catch (SQLException ex) {
        }
        
        return recipients;
    }

    
    /**
     * Pobiera listę wszystkich regionów kurierskich z bazy danych.
     *
     * @return lista obiektów {@link Region} z tabeli {@code courier_regions};
     *         pusta lista jeśli brak regionów lub wystąpił błąd
     * 
     * @auhtor Radosław Kruczek
     */
    @Override
    public List<Region> getRegions() {
        
        List<Region> regions = new ArrayList<>();
        
        try {
            
            Connection conn = ConnectDatabasePackage.getConnection();
            
            String sql = "SELECT * FROM courier_regions";
                    
            
            Statement s = conn.createStatement();
            
            ResultSet rs = s.executeQuery(sql);
            

            
            while (rs.next()) {

                Region region = new Region(rs.getInt("region_id"), rs.getString("region_name"));
                 
                 
                regions.add(region);              
             }
            
        } catch (SQLException ex) {
        }
        
        return regions;
    }

    
    /**
     * Pobiera listę wszystkich regałów magazynowych z bazy danych.
     *
     * @return lista obiektów {@link Rack} z tabeli {@code racks},
     *         każdy z przypisaną strefą {@link Zone};
     *         pusta lista jeśli brak regałów lub wystąpił błąd
     * 
     * @auhtor Radosław Kruczek
     */
    @Override
    public List<Rack> getRacks() {
        
        List<Rack> shelves = new ArrayList<>();
        
        try {
            
            Connection conn = ConnectDatabasePackage.getConnection();
            
            String sql = "SELECT * FROM racks";
                    
            
            Statement s = conn.createStatement();
            
            ResultSet rs = s.executeQuery(sql);
            

            
            while (rs.next()) {

                Rack rack = new Rack(rs.getInt("rack_id"), new Zone(rs.getInt("zone_id")));   
                 
                 
                shelves.add(rack);              
             }
            
        } catch (SQLException ex) {
        }
        
        return shelves;
    }

    
    /**
     * Pobiera listę wszystkich stref magazynowych z bazy danych.
     *
     * @return lista obiektów {@link Zone} z tabeli {@code zones};
     *         pusta lista jeśli brak stref lub wystąpił błąd
     * 
     * @auhtor Radosław Kruczek
     */
    @Override
    public List<Zone> getZones() {
        
        List<Zone> zones = new ArrayList<>();
        
        try {
            
            Connection conn = ConnectDatabasePackage.getConnection();
            
            String sql = "SELECT * FROM zones";
                    
            
            Statement s = conn.createStatement();
            
            ResultSet rs = s.executeQuery(sql);
            

            
            while (rs.next()) {

                Zone zone = new Zone(rs.getInt("zone_id"));   
                 
                 
                zones.add(zone);              
             }
            
        } catch (SQLException ex) {
        }
        
        return zones;
    }


    
    /**
     * Dodaje nową paczkę do bazy danych.
     * <p>
     * Jeśli paczka ma przypisany regał ({@code package_rack != null}), jego
     * identyfikator jest zapisywany w bazie. W przeciwnym razie kolumna
     * {@code package_rack} przyjmuje wartość {@code NULL}.
     * </p>
     *
     * @param p obiekt {@link Package} zawierający dane nowej paczki; nadawca i
     * odbiorca muszą już istnieć w bazie danych
     * @return {@code true} jeśli paczka została pomyślnie dodana, {@code false}
     * jeśli wystąpił błąd bazy danych
     * 
     * @auhtor Radosław Kruczek
     */
    @Override
public Boolean addPackage(Package p) {
    String sql = "INSERT INTO packages("
            + "package_sender, "
            + "package_recipient, "
            + "package_region, "
            + "package_dest_region, "
            + "package_format, "
            + "package_rack, "
            + "width, "
            + "height, "
            + "depth, "
            + "weight"
            + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    try (Connection conn = ConnectDatabasePackage.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

       
        ps.setInt(1, p.getPackage_sender().getSender_id());
        ps.setInt(2, p.getPackage_recipient().getRecipient_id());
        ps.setInt(3, p.getPackage_region().getRegion_id());
        ps.setInt(4, p.getPackage_dest_region().getRegion_id());
        ps.setString(5, p.getPackage_format().getFormat_id());

       
        if (p.getPackage_rack() != null) {
            ps.setInt(6, p.getPackage_rack().getRack_id());
        } else {
            ps.setNull(6, java.sql.Types.INTEGER);
        }

        
        ps.setInt(7, p.getWidth());
        ps.setInt(8, p.getHeight());
        ps.setInt(9, p.getDepth());
        ps.setInt(10, p.getWeight());

        int rows = ps.executeUpdate();

        return rows > 0;

    } catch (SQLException ex) {
        ex.printStackTrace();
        return false;
    }
}

    /**
     * Aktualizuje dane istniejącej paczki w bazie danych.
     * <p>
     * Aktualizowane są: nadawca, odbiorca, region nadania, region docelowy,
     * format oraz przypisany regał. Jeśli paczka nie ma przypisanego regału,
     * kolumna {@code package_rack} przyjmuje wartość {@code NULL}.
     * </p>
     *
     * @param p obiekt {@link Package} z zaktualizowanymi danymi; musi zawierać
     * poprawne {@code package_id}
     * @return {@code true} jeśli aktualizacja powiodła się, {@code false} jeśli
     * wystąpił błąd bazy danych
     * 
     * @author Mateusz Gojny
     */
    @Override
    public Boolean changePackage(Package p) {
        try {
            Connection conn = ConnectDatabasePackage.getConnection();

            String sql = "UPDATE packages SET package_sender = ?, package_recipient = ?, package_region = ?, package_dest_region = ?, package_format = ?, package_rack = ? WHERE package_id = ?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, p.getPackage_sender().getSender_id());
            ps.setInt(2, p.getPackage_recipient().getRecipient_id());
            ps.setInt(3, p.getPackage_region().getRegion_id());
            ps.setInt(4, p.getPackage_dest_region().getRegion_id());
            ps.setString(5, p.getPackage_format().getFormat_id());

            // 
            if (p.getPackage_rack() != null) {
                ps.setInt(6, p.getPackage_rack().getRack_id());
            } else {
                ps.setNull(6, java.sql.Types.INTEGER);
            }

            ps.setInt(7, p.getPackage_id());

            int rows = ps.executeUpdate();

            return rows > 0; 

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    /**
     * Zapisuje nowego nadawcę w bazie danych i zwraca jego wygenerowany identyfikator.
     * <p>
     * Model bazy danych wymaga istnienia rekordu w tabeli {@code senders}
     * przed dodaniem paczki. Zwrócony identyfikator jest następnie używany
     * przy tworzeniu obiektu paczki w {@link com.mycompany.projekt_io.feature.package_.PackageService}.
     * </p>
     *
     * @param s obiekt {@link Sender} z danymi nowego nadawcy
     * @return wygenerowany identyfikator nadawcy ({@code sender_id})
     *         lub {@code -1} jeśli zapis się nie powiódł
     * 
     * @author Mateusz Gojny
     */
    @Override
    public int addSender(Sender s) {
        try {
            Connection conn = ConnectDatabasePackage.getConnection();

            String sql = "INSERT INTO senders(sender_name, sender_city, sender_street, sender_postcode, sender_email, sender_phone) VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, s.getSender_name());
            ps.setString(2, s.getSender_city());
            ps.setString(3, s.getSender_street());
            ps.setString(4, s.getSender_postcode());
            ps.setString(5, s.getSender_email());
            ps.setString(6, s.getSender_phone());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); 
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }
    
    /**
     * Zapisuje nowego odbiorcę w bazie danych i zwraca jego wygenerowany
     * identyfikator.
     * <p>
     * Analogicznie do {@link #addSender(Sender)}, paczka wymaga istniejącego
     * rekordu w tabeli {@code recipients} przed jej dodaniem do bazy.
     * </p>
     *
     * @param r obiekt {@link Recipient} z danymi nowego odbiorcy
     * @return wygenerowany identyfikator odbiorcy ({@code recipient_id}) lub
     * {@code -1} jeśli zapis się nie powiódł
     * 
     * @author Mateusz Gojny
     */
    @Override
    public int addRecipient(Recipient r) {
        try {
            Connection conn = ConnectDatabasePackage.getConnection();

            String sql = "INSERT INTO recipients(recipient_name, recipient_city, recipient_street, recipient_postcode, recipient_email, recipient_phone) VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, r.getRecipient_name());
            ps.setString(2, r.getRecipient_city());
            ps.setString(3, r.getRecipient_street());
            ps.setString(4, r.getRecipient_postcode());
            ps.setString(5, r.getRecipient_email());
            ps.setString(6, r.getRecipient_phone());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    /**
     * Zwraca liczbę paczek przypisanych do wskazanego regału.
     *
     * @param rackId identyfikator regału
     * @return liczba paczek przypisanych do regału o podanym ID; {@code 0}
     * jeśli regał jest pusty lub wystąpił błąd
     * 
     * @author Ida Wszoła
     */
    @Override
    public int getPackageCountForShelf(int rackId) {
        int count = 0;
        try (Connection conn = ConnectDatabasePackage.getConnection(); PreparedStatement ps = conn.prepareStatement(
                "SELECT COUNT(*) FROM packages WHERE package_rack = ?")) {
            ps.setInt(1, rackId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return count;
    }


    /**
     * Zwraca identyfikator ostatnio dodanej paczki na wskazanym regale.
     * <p>
     * Wyznaczany jest jako maksymalna wartość {@code package_id} spośród paczek
     * przypisanych do danego regału.
     * </p>
     *
     * @param rackId identyfikator regału
     * @return identyfikator ostatniej paczki na regale lub {@code -1} jeśli
     * regał jest pusty lub wystąpił błąd
     * 
     * @author Ida Wszoła
     */
    @Override
    public int getLastPackageIdForShelf(int rackId) {
        int id = -1;
        try (Connection conn = ConnectDatabasePackage.getConnection(); PreparedStatement ps = conn.prepareStatement(
                "SELECT MAX(package_id) FROM packages WHERE package_rack = ?")) {
            ps.setInt(1, rackId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return id;
    }


    /**
     * Zwraca łączną liczbę wszystkich paczek w systemie magazynowym.
     *
     * @return całkowita liczba rekordów w tabeli {@code packages}; {@code 0}
     * jeśli brak paczek lub wystąpił błąd
     * 
     * @author Ida Wszoła
     */
    @Override
    public int getTotalPackageCount() {
        int count = 0;
        Connection conn = ConnectDatabasePackage.getConnection();
        try {
            String sql = "SELECT COUNT(*) FROM packages";
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery(sql);
            if (rs.next()) count = rs.getInt(1);
        } catch (SQLException ex) {
        }
        return count;
    }


    /**
     * Zwraca liczbę paczek przypisanych do regałów w danej strefie magazynowej.
     *
     * @param zoneId identyfikator strefy magazynowej
     * @return liczba paczek w strefie o podanym ID; {@code 0} jeśli strefa jest
     * pusta lub wystąpił błąd
     * 
     * @author Ida Wszoła
     */
    @Override
    public int getPackageCountForZone(int zoneId) {
        int count = 0;
        try (Connection conn = ConnectDatabasePackage.getConnection(); PreparedStatement ps = conn.prepareStatement(
                "SELECT COUNT(*) FROM packages "
                + "JOIN racks ON package_rack = rack_id "
                + "WHERE racks.zone = ?")) {
            ps.setInt(1, zoneId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return count;
    }


    /**
     * Zwraca identyfikator najbardziej obciążonego regału w magazynie.
     * <p>
     * Wyznaczany jest regał z największą liczbą przypisanych paczek.
     * Pod uwagę brane są tylko paczki z niepustą kolumną {@code package_rack}.
     * </p>
     *
     * @return identyfikator najbardzej obciążonego regału lub {@code -1}
     *         jeśli żadna paczka nie ma przypisanego regału bądź wystąpił błąd
     * 
     * @author Ida Wszoła
     */
    @Override
    public int getMostLoadedShelfId() {
        int rackId = -1;
        try (Connection conn = ConnectDatabasePackage.getConnection(); Statement s = conn.createStatement()) {
            String sql = "SELECT package_rack, COUNT(*) as cnt FROM packages "
                    + "WHERE package_rack IS NOT NULL "
                    + "GROUP BY package_rack ORDER BY cnt DESC LIMIT 1";
            ResultSet rs = s.executeQuery(sql);
            if (rs.next()) {
                rackId = rs.getInt("package_rack");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return rackId;
    }
    
    /**
     * Aktualizuje dane istniejącego nadawcy w bazie danych.
     *
     * @param s obiekt {@link Sender} z zaktualizowanymi danymi; musi zawierać
     * poprawne {@code sender_id}
     * @return {@code true} jeśli aktualizacja powiodła się, {@code false} jeśli
     * wystąpił błąd bazy 
     * 
     * @author Mateusz Gojny
     */
    @Override
    public boolean updateSender(Sender s) {
        try {
            Connection conn = ConnectDatabasePackage.getConnection();

            String sql = "UPDATE senders SET sender_name=?, sender_city=?, sender_street=?, sender_postcode=?, sender_email=?, sender_phone=? WHERE sender_id=?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, s.getSender_name());
            ps.setString(2, s.getSender_city());
            ps.setString(3, s.getSender_street());
            ps.setString(4, s.getSender_postcode());
            ps.setString(5, s.getSender_email());
            ps.setString(6, s.getSender_phone());
            ps.setInt(7, s.getSender_id());

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Aktualizuje dane istniejącego odbiorcy w bazie danych.
     *
     * @param r obiekt {@link Recipient} z zaktualizowanymi danymi;
     *          musi zawierać poprawne {@code recipient_id}
     * @return {@code true} jeśli aktualizacja powiodła się,
     *         {@code false} jeśli wystąpił błąd bazy danych
     * 
     * @author Mateusz Gojny
     */
    @Override
    public boolean updateRecipient(Recipient r) {
        try {
            Connection conn = ConnectDatabasePackage.getConnection();

            String sql = "UPDATE recipients SET recipient_name=?, recipient_city=?, recipient_street=?, recipient_postcode=?, recipient_email=?, recipient_phone=? WHERE recipient_id=?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, r.getRecipient_name());
            ps.setString(2, r.getRecipient_city());
            ps.setString(3, r.getRecipient_street());
            ps.setString(4, r.getRecipient_postcode());
            ps.setString(5, r.getRecipient_email());
            ps.setString(6, r.getRecipient_phone());
            ps.setInt(7, r.getRecipient_id());

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Usuwa paczkę z bazy danych na podstawie jej identyfikatora.
     *
     * @param packageId identyfikator paczki do usunięcia
     * @return {@code true} jeśli paczka została pomyślnie usunięta,
     * {@code false} jeśli paczka o podanym ID nie istnieje lub wystąpił błąd
     * bazy danych
     * 
     * @author Mateusz Gojny
     */
    @Override
    public boolean deletePackage(int packageId) {
        try {
            Connection conn = ConnectDatabasePackage.getConnection();

            String sql = "DELETE FROM packages WHERE package_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, packageId);

            int rows = ps.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
