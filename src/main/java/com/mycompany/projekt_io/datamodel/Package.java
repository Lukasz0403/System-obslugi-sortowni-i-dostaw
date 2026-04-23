/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projekt_io.datamodel;

/**
 *
 * @author mateu
 */
public class Package {
    
    int package_id;
    Sender package_sender;
    Recipient package_recipient;
    Region package_region, package_dest_region;
    Format package_format;
    Rack package_rack;

    public int getPackage_id() {
        return package_id;
    }

    public Sender getPackage_sender() {
        return package_sender;
    }

    public Recipient getPackage_recipient() {
        return package_recipient;
    }

    public Region getPackage_region() {
        return package_region;
    }

    public Region getPackage_dest_region() {
        return package_dest_region;
    }

    public Format getPackage_format() {
        return package_format;
    }

    public Rack getPackage_rack() {
        return package_rack;
    }

    public void setPackage_rack(Rack package_rack) {
        this.package_rack = package_rack;
    }
    

    public void setPackage_id(int package_id) {
        this.package_id = package_id;
    }

    public void setPackage_sender(Sender package_sender) {
        this.package_sender = package_sender;
    }

    public void setPackage_recipient(Recipient package_recipient) {
        this.package_recipient = package_recipient;
    }

    public void setPackage_region(Region package_region) {
        this.package_region = package_region;
    }

    public void setPackage_dest_region(Region package_dest_region) {
        this.package_dest_region = package_dest_region;
    }

    public void setPackage_format(Format package_format) {
        this.package_format = package_format;
    }

    public Package(int package_id, Sender package_sender, Recipient package_recipient, Region package_region, Region package_dest_region, Format package_format, Rack package_rack) {
        this.package_id = package_id;
        this.package_sender = package_sender;
        this.package_recipient = package_recipient;
        this.package_region = package_region;
        this.package_dest_region = package_dest_region;
        this.package_format = package_format;
        this.package_rack = package_rack;
    }

    @Override
    public String toString() {
        return "Package{" + "package_id=" + package_id + ", package_sender=" + package_sender + ", package_recipient=" + package_recipient + ", package_region=" + package_region + ", package_dest_region=" + package_dest_region + ", package_format=" + package_format + ", package_rack=" + package_rack + '}';
    }
}
