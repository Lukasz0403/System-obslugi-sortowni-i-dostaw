/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projekt_io.core.database;

import com.mycompany.projekt_io.datamodel.Format;
import com.mycompany.projekt_io.datamodel.Package;
import com.mycompany.projekt_io.datamodel.Recipient;
import com.mycompany.projekt_io.datamodel.Region;
import com.mycompany.projekt_io.datamodel.Sender;
import com.mycompany.projekt_io.datamodel.Shelf;
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
                    + "JOIN shelves ON package_rack = shelf_id "
                    + "JOIN zones ON zone = zone_id";
            
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

                Region region = new Region(rs.getString("package_region"));

                Region destRegion = new Region(rs.getString("package_dest_region"));

                Format format = new Format(rs.getString("format_id"), 
                                            rs.getInt("max_format_width"), 
                                            rs.getInt("max_format_height"), 
                                            rs.getInt("max_format_depth"),
                                            rs.getInt("max_wage"));

                Shelf shelf = new Shelf(rs.getInt("shelf_id"), new Zone(rs.getInt("zone_id")));   
                
                Package pack = new Package(rs.getInt("package_id"), sender, recipient, region, destRegion, format, shelf);
                 
                 
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
                    + "JOIN shelves ON package_rack = shelf_id "
                    + "JOIN zones ON zone = zone_id WHERE package_id = ?";
            
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

                Region region = new Region(rs.getString("package_region"));

                Region destRegion = new Region(rs.getString("package_dest_region"));

                Format format = new Format(rs.getString("format_id"), 
                                            rs.getInt("max_format_width"), 
                                            rs.getInt("max_format_height"), 
                                            rs.getInt("max_format_depth"),
                                            rs.getInt("max_wage"));

                Shelf shelf = new Shelf(rs.getInt("shelf_id"), new Zone(rs.getInt("zone_id")));   

                pack = new Package(rs.getInt("package_id"), sender, recipient, region, destRegion, format, shelf);   
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
                                            rs.getInt("max_wage"));
                 
                 
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

                Region region = new Region(rs.getString("package_region"));
                 
                 
                regions.add(region);              
             }
            
        } catch (SQLException ex) {
        }
        
        return regions;
    }

    @Override
    public List<Shelf> getShelves() {
        
        List<Shelf> shelves = new ArrayList<>();
        
        try {
            
            Connection conn = ConnectDatabasePackage.getConnection();
            
            String sql = "SELECT * FROM shelves";
                    
            
            Statement s = conn.createStatement();
            
            ResultSet rs = s.executeQuery(sql);
            

            
            while (rs.next()) {

                Shelf shelf = new Shelf(rs.getInt("shelf_id"), new Zone(rs.getInt("zone_id")));   
                 
                 
                shelves.add(shelf);              
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
        
        try {
            
            Connection conn = ConnectDatabasePackage.getConnection();
            
            String sql = "INSERT INTO packages(package_sender, package_recipient, package_region, package_dest_region, package_format, package_rack) VALUES(?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = conn.prepareStatement(sql);
            
            ps.setInt(1, p.getPackage_sender().getSender_id());
            
            ps.setInt(2, p.getPackage_recipient().getRecipient_id());
            
            ps.setString(3, p.getPackage_region().getRegion());
            
            ps.setString(4, p.getPackage_dest_region().getRegion());
            
            ps.setString(5, p.getPackage_format().getFormat_id());
            
            ps.setInt(6, p.getPackage_rack().getShelf_id());
            
            ResultSet rs = ps.executeQuery();
            
            return true;
            
        } catch (SQLException ex) {
        } finally {
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
            
            ps.setString(3, p.getPackage_region().getRegion());
            
            ps.setString(4, p.getPackage_dest_region().getRegion());
            
            ps.setString(5, p.getPackage_format().getFormat_id());
            
            ps.setInt(6, p.getPackage_rack().getShelf_id());
            
            ps.setInt(7, p.getPackage_id());
            
            ps.executeUpdate(sql);
            
            return true;
            
        } catch (SQLException ex) {
        } finally {
            return false;  
        }     
    }
    
    
    
}
