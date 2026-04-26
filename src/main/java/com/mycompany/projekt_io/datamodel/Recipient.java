/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projekt_io.datamodel;

/**
 * Model danych reprezentujący odbiorcę paczki.
 * <p>
 * Przechowuje dane adresowe i kontaktowe odbiorcy. Każda paczka posiada
 * powiązanego odbiorcę zapisanego w tabeli {@code recipients} bazy danych.
 * </p>
 *
 * @author Radosław Kruczek
 */
public class Recipient {

    int recipient_id;
    String recipient_name, recipient_city, recipient_street,
            recipient_postcode, recipient_email, recipient_phone;

    /**
     * Tworzy obiekt odbiorcy paczki.
     *
     * @param recipient_id unikalny identyfikator odbiorcy
     * @param recipient_name imię i nazwisko lub nazwa firmy odbiorcy
     * @param recipient_city miasto odbiorcy
     * @param recipient_street ulica i numer domu odbiorcy
     * @param recipient_postcode kod pocztowy odbiorcy (format XX-XXX)
     * @param recipient_email adres e-mail odbiorcy; może być {@code null}
     * @param recipient_phone numer telefonu odbiorcy; może być {@code null}
     */
    public Recipient(int recipient_id, String recipient_name, String recipient_city,
            String recipient_street, String recipient_postcode,
            String recipient_email, String recipient_phone) {
        this.recipient_id = recipient_id;
        this.recipient_name = recipient_name;
        this.recipient_city = recipient_city;
        this.recipient_street = recipient_street;
        this.recipient_postcode = recipient_postcode;
        this.recipient_email = recipient_email;
        this.recipient_phone = recipient_phone;
    }

    /**
     * @return unikalny identyfikator odbiorcy
     */
    public int getRecipient_id() {
        return recipient_id;
    }

    /**
     * @return imię i nazwisko lub nazwa firmy odbiorcy
     */
    public String getRecipient_name() {
        return recipient_name;
    }

    /**
     * @return miasto odbiorcy
     */
    public String getRecipient_city() {
        return recipient_city;
    }

    /**
     * @return ulica i numer domu odbiorcy
     */
    public String getRecipient_street() {
        return recipient_street;
    }

    /**
     * @return kod pocztowy odbiorcy
     */
    public String getRecipient_postcode() {
        return recipient_postcode;
    }

    /**
     * @return adres e-mail odbiorcy
     */
    public String getRecipient_email() {
        return recipient_email;
    }

    /**
     * @return numer telefonu odbiorcy
     */
    public String getRecipient_phone() {
        return recipient_phone;
    }

    /**
     * @param recipient_id unikalny identyfikator odbiorcy
     */
    public void setRecipient_id(int recipient_id) {
        this.recipient_id = recipient_id;
    }

    /**
     * @param recipient_name imię i nazwisko lub nazwa firmy
     */
    public void setRecipient_name(String recipient_name) {
        this.recipient_name = recipient_name;
    }

    /**
     * @param recipient_city miasto odbiorcy
     */
    public void setRecipient_city(String recipient_city) {
        this.recipient_city = recipient_city;
    }

    /**
     * @param recipient_street ulica i numer domu
     */
    public void setRecipient_street(String recipient_street) {
        this.recipient_street = recipient_street;
    }

    /**
     * @param recipient_postcode kod pocztowy (format XX-XXX)
     */
    public void setRecipient_postcode(String recipient_postcode) {
        this.recipient_postcode = recipient_postcode;
    }

    /**
     * @param recipient_email adres e-mail
     */
    public void setRecipient_email(String recipient_email) {
        this.recipient_email = recipient_email;
    }

    /**
     * @param recipient_phone numer telefonu
     */
    public void setRecipient_phone(String recipient_phone) {
        this.recipient_phone = recipient_phone;
    }

    @Override
    public String toString() {
        return "Recipient{" + "recipient_id=" + recipient_id + ", recipient_name=" + recipient_name
                + ", recipient_city=" + recipient_city + ", recipient_street=" + recipient_street
                + ", recipient_postcode=" + recipient_postcode + ", recipient_email=" + recipient_email
                + ", recipient_phone=" + recipient_phone + '}';
    }
}
