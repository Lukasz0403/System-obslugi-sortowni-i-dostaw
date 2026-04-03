/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projekt_io.datamodel;

/**
 *
 * @author mateu
 */
public class Recipient {
    
    int recipient_id;
    String recipient_name, recipient_city, recipient_street, recipient_postcode, recipient_email, recipient_phone;

    public int getRecipient_id() {
        return recipient_id;
    }

    public String getRecipient_name() {
        return recipient_name;
    }

    public String getRecipient_city() {
        return recipient_city;
    }

    public String getRecipient_street() {
        return recipient_street;
    }

    public String getRecipient_postcode() {
        return recipient_postcode;
    }

    public String getRecipient_email() {
        return recipient_email;
    }

    public String getRecipient_phone() {
        return recipient_phone;
    }

    public void setRecipient_id(int recipient_id) {
        this.recipient_id = recipient_id;
    }

    public void setRecipient_name(String recipient_name) {
        this.recipient_name = recipient_name;
    }

    public void setRecipient_city(String recipient_city) {
        this.recipient_city = recipient_city;
    }

    public void setRecipient_street(String recipient_street) {
        this.recipient_street = recipient_street;
    }

    public void setRecipient_postcode(String recipient_postcode) {
        this.recipient_postcode = recipient_postcode;
    }

    public void setRecipient_email(String recipient_email) {
        this.recipient_email = recipient_email;
    }

    public void setRecipient_phone(String recipient_phone) {
        this.recipient_phone = recipient_phone;
    }

    public Recipient(int recipient_id, String recipient_name, String recipient_city, String recipient_street, String recipient_postcode, String recipient_email, String recipient_phone) {
        this.recipient_id = recipient_id;
        this.recipient_name = recipient_name;
        this.recipient_city = recipient_city;
        this.recipient_street = recipient_street;
        this.recipient_postcode = recipient_postcode;
        this.recipient_email = recipient_email;
        this.recipient_phone = recipient_phone;
    }

    @Override
    public String toString() {
        return "Recipient{" + "recipient_id=" + recipient_id + ", recipient_name=" + recipient_name + ", recipient_city=" + recipient_city + ", recipient_street=" + recipient_street + ", recipient_postcode=" + recipient_postcode + ", recipient_email=" + recipient_email + ", recipient_phone=" + recipient_phone + '}';
    }
    
    
}
