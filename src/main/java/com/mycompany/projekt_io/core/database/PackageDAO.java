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
 *
 * @author mateu
 */
public class PackageDAO implements PackageDAOInterface {

    
    /**
     * Zmieniono INNER JOIN na LEFT JOIN dla tabel shelves oraz zones.
     *
     * W bazie danych kolumna package_rack może mieć wartość NULL. W
     * przypadku użycia INNER JOIN rekordy z NULL w package_rack nie były
     * zwracane w wyniku zapytania, co powodowało brak wyświetlania nowo
     * dodanych paczek w tabeli.
     *
     * LEFT JOIN pozwala na pobranie rekordów nawet jeśli nie istnieje powiązana
     * półka (shelf), co jest zgodne z modelem danych.
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
                
                Package pack = new Package(rs.getInt("package_id"), sender, recipient, region, destRegion, format, rack);
                 
                 
                packages.add(pack);              
             }
            
        } catch (SQLException ex) {
        }
        
        return packages;
    }

    
    
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

                pack = new Package(rs.getInt("package_id"), sender, recipient, region, destRegion, format, rack);   
            }     
        
            
        } catch (SQLException ex) {
        }
        
        return pack;
    }

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

        
        ps.setInt(7, p.getPackage_format().getMax_format_width());
        ps.setInt(8, p.getPackage_format().getMax_format_height());
        ps.setInt(9, p.getPackage_format().getMax_format_depth());
        ps.setInt(10, p.getPackage_format().getMax_wage());

        int rows = ps.executeUpdate();

        return rows > 0;

    } catch (SQLException ex) {
        ex.printStackTrace();
        return false;
    }
}

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
     * Dodano metodę zapisu nadawcy (Sender) do bazy danych.
     *
     * Model bazy danych wymaga istnienia rekordu w tabeli senders przed
     * dodaniem paczki. Metoda zwraca wygenerowany identyfikator (sender_id), który jest
     * następnie używany przy tworzeniu paczki.
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
     * Dodano metodę zapisu odbiorcy (Recipient) do bazy danych.
     *
     * Analogicznie do nadawcy, paczka wymaga istniejącego rekordu w
     * tabeli recipients .
     *
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

    @Override
    public int getPackageCountForShelf(int shelfId) {
        int count = 0;
        Connection conn = ConnectDatabasePackage.getConnection();
        try {
            String sql = "SELECT COUNT(*) FROM packages WHERE package_shelf = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, shelfId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) count = rs.getInt(1);
        } catch (SQLException ex) {
        }
        return count;
    }


    @Override
    public int getLastPackageIdForShelf(int shelfId) {
        int id = -1;
        Connection conn = ConnectDatabasePackage.getConnection();
        try {
            String sql = "SELECT MAX(package_id) FROM packages WHERE package_shelf = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, shelfId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) id = rs.getInt(1);
        } catch (SQLException ex) {
        } 
        return id;
    }


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


    @Override
    public int getPackageCountForZone(int zoneId) {
        int count = 0;
        Connection conn = ConnectDatabasePackage.getConnection();
        try {
            String sql = "SELECT COUNT(*) FROM packages JOIN shelves ON package_shelf = shelf_id JOIN racks ON rack= rack_id JOIN zones ON zone = zone_id WHERE zone = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, zoneId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) count = rs.getInt(1);
        } catch (SQLException ex) {
        } 
        return count;
    }


    @Override
    public int getMostLoadedShelfId() {
        int shelfId = -1;
        Connection conn = ConnectDatabasePackage.getConnection();
        try {
            String sql = "SELECT package_rack, COUNT(*) as cnt FROM packages WHERE package_shelf IS NOT NULL GROUP BY package_shelf ORDER BY cnt DESC LIMIT 1";
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery(sql);
            if (rs.next()) shelfId = rs.getInt("package_rack");
        } catch (SQLException ex) {
        }
        return shelfId;
    }
    
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
