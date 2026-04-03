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
    
}
