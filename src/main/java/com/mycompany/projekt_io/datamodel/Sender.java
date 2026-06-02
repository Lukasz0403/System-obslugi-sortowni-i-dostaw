package com.mycompany.projekt_io.datamodel;

/**
 * Model danych reprezentujący nadawcę paczki.
 * <p>
 * Przechowuje dane adresowe i kontaktowe nadawcy. Każda paczka posiada
 * powiązanego nadawcę zapisanego w tabeli {@code senders} bazy danych.
 * </p>
 *
 * @author Radosław Kruczek
 */
public class Sender {

    /**
     * Unikalny identyfikator nadawcy.
     */
    int sender_id;

    /**
     * Imię i nazwisko lub nazwa firmy nadawcy.
     */
    String sender_name;

    /**
     * Miasto zamieszkania lub siedziby nadawcy.
     */
    String sender_city;

    /**
     * Ulica oraz numer domu/lokalu nadawcy.
     */
    String sender_street;

    /**
     * Kod pocztowy nadawcy (w formacie XX-XXX).
     */
    String sender_postcode;

    /**
     * Adres e-mail nadawcy.
     */
    String sender_email;

    /**
     * Numer telefonu kontaktowego nadawcy.
     */
    String sender_phone;

    /**
     * Tworzy obiekt nadawcy paczki.
     *
     * @param sender_id unikalny identyfikator nadawcy
     * @param sender_name imię i nazwisko lub nazwa firmy nadawcy
     * @param sender_city miasto nadawcy
     * @param sender_street ulica i numer domu nadawcy
     * @param sender_postcode kod pocztowy nadawcy (format XX-XXX)
     * @param sender_email adres e-mail nadawcy; może być {@code null}
     * @param sender_phone numer telefonu nadawcy; może być {@code null}
     */
    public Sender(int sender_id, String sender_name, String sender_city,
            String sender_street, String sender_postcode,
            String sender_email, String sender_phone) {
        this.sender_id = sender_id;
        this.sender_name = sender_name;
        this.sender_city = sender_city;
        this.sender_street = sender_street;
        this.sender_postcode = sender_postcode;
        this.sender_email = sender_email;
        this.sender_phone = sender_phone;
    }

    /**
     * Zwraca unikalny identyfikator nadawcy.
     *
     * @return unikalny identyfikator nadawcy
     */
    public int getSender_id() {
        return sender_id;
    }

    /**
     * Zwraca imię i nazwisko lub nazwę firmy nadawcy.
     *
     * @return imię i nazwisko lub nazwa firmy nadawcy
     */
    public String getSender_name() {
        return sender_name;
    }

    /**
     * Zwraca miasto nadawcy.
     *
     * @return miasto nadawcy
     */
    public String getSender_city() {
        return sender_city;
    }

    /**
     * Zwraca ulicę i numer domu nadawcy.
     *
     * @return ulica i numer domu nadawcy
     */
    public String getSender_street() {
        return sender_street;
    }

    /**
     * Zwraca kod pocztowy nadawcy.
     *
     * @return kod pocztowy nadawcy
     */
    public String getSender_postcode() {
        return sender_postcode;
    }

    /**
     * Zwraca adres e-mail nadawcy.
     *
     * @return adres e-mail nadawcy
     */
    public String getSender_email() {
        return sender_email;
    }

    /**
     * Zwraca numer telefonu nadawcy.
     *
     * @return numer telefonu nadawcy
     */
    public String getSender_phone() {
        return sender_phone;
    }

    /**
     * Ustawia unikalny identyfikator nadawcy.
     *
     * @param sender_id unikalny identyfikator nadawcy
     */
    public void setSender_id(int sender_id) {
        this.sender_id = sender_id;
    }

    /**
     * Ustawia imię i nazwisko lub nazwę firmy nadawcy.
     *
     * @param sender_name imię i nazwisko lub nazwa firmy
     */
    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    /**
     * Ustawia miasto nadawcy.
     *
     * @param sender_city miasto nadawcy
     */
    public void setSender_city(String sender_city) {
        this.sender_city = sender_city;
    }

    /**
     * Ustawia ulicę i numer domu nadawcy.
     *
     * @param sender_street ulica i numer domu
     */
    public void setSender_street(String sender_street) {
        this.sender_street = sender_street;
    }

    /**
     * Ustawia kod pocztowy nadawcy.
     *
     * @param sender_postcode kod pocztowy (format XX-XXX)
     */
    public void setSender_postcode(String sender_postcode) {
        this.sender_postcode = sender_postcode;
    }

    /**
     * Ustawia adres e-mail nadawcy.
     *
     * @param sender_email adres e-mail
     */
    public void setSender_email(String sender_email) {
        this.sender_email = sender_email;
    }

    /**
     * Ustawia numer telefonu nadawcy.
     *
     * @param sender_phone numer telefonu
     */
    public void setSender_phone(String sender_phone) {
        this.sender_phone = sender_phone;
    }

    @Override
    public String toString() {
        return "Sender{" + "sender_id=" + sender_id + ", sender_name=" + sender_name
                + ", sender_city=" + sender_city + ", sender_street=" + sender_street
                + ", sender_postcode=" + sender_postcode + ", sender_email=" + sender_email
                + ", sender_phone=" + sender_phone + '}';
    }
}
