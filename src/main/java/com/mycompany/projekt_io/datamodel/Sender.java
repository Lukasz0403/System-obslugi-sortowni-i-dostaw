/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projekt_io.datamodel;

/**
 *
 * @author mateu
 */
public class Sender {
    
    int sender_id;
    String sender_name, sender_city, sender_street, sender_postcode, sender_email, sender_phone;

    public int getSender_id() {
        return sender_id;
    }

    public String getSender_name() {
        return sender_name;
    }

    public String getSender_city() {
        return sender_city;
    }

    public String getSender_street() {
        return sender_street;
    }

    public String getSender_postcode() {
        return sender_postcode;
    }

    public String getSender_email() {
        return sender_email;
    }

    public String getSender_phone() {
        return sender_phone;
    }

    public void setSender_id(int sender_id) {
        this.sender_id = sender_id;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public void setSender_city(String sender_city) {
        this.sender_city = sender_city;
    }

    public void setSender_street(String sender_street) {
        this.sender_street = sender_street;
    }

    public void setSender_postcode(String sender_postcode) {
        this.sender_postcode = sender_postcode;
    }

    public void setSender_email(String sender_email) {
        this.sender_email = sender_email;
    }

    public void setSender_phone(String sender_phone) {
        this.sender_phone = sender_phone;
    }

    public Sender(int sender_id, String sender_name, String sender_city, String sender_street, String sender_postcode, String sender_email, String sender_phone) {
        this.sender_id = sender_id;
        this.sender_name = sender_name;
        this.sender_city = sender_city;
        this.sender_street = sender_street;
        this.sender_postcode = sender_postcode;
        this.sender_email = sender_email;
        this.sender_phone = sender_phone;
    }

    @Override
    public String toString() {
        return "Sender{" + "sender_id=" + sender_id + ", sender_name=" + sender_name + ", sender_city=" + sender_city + ", sender_street=" + sender_street + ", sender_postcode=" + sender_postcode + ", sender_email=" + sender_email + ", sender_phone=" + sender_phone + '}';
    }
    
    
}
