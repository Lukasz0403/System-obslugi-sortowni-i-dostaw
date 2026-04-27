/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projekt_io.feature.package_;

/**
 * Model danych szablonu nadawcy.
 * <p>
 * Przechowuje dane adresowe i kontaktowe nadawcy zapisywane jako szablon
 * wielokrotnego użytku. Zaprojektowany z myślą o rozszerzalności — dodanie
 * nowego pola wymaga jedynie rozszerzenia tej klasy i dodania odpowiedniego
 * klucza w {@link TemplateService}.
 * </p>
 */
public class SenderTemplate {

    private String name;
    private String street;
    private String postcode;
    private String email;
    private String phone;
    private String city;

    /**
     * Tworzy obiekt szablonu nadawcy.
     *
     * @param name imię i nazwisko lub nazwa firmy nadawcy
     * @param street ulica i numer domu
     * @param postcode kod pocztowy (format XX-XXX)
     * @param email adres e-mail
     * @param phone numer telefonu
     * @param city miasto
     */
    public SenderTemplate(String name, String street, String postcode,
            String email, String phone, String city) {
        this.name = name;
        this.street = street;
        this.postcode = postcode;
        this.email = email;
        this.phone = phone;
        this.city = city;
    }

    /**
     * @return imię i nazwisko lub nazwa firmy nadawcy
     */
    public String getName() {
        return name;
    }

    /**
     * @return ulica i numer domu
     */
    public String getStreet() {
        return street;
    }

    /**
     * @return kod pocztowy
     */
    public String getPostcode() {
        return postcode;
    }

    /**
     * @return adres e-mail
     */
    public String getEmail() {
        return email;
    }

    /**
     * @return numer telefonu
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @return miasto nadawcy
     */
    public String getCity() {
        return city;
    }

    /**
     * @param name imię i nazwisko lub nazwa firmy
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param street ulica i numer domu
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * @param postcode kod pocztowy
     */
    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    /**
     * @param email adres e-mail
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @param phone numer telefonu
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @param city miasto
     */
    public void setCity(String city) {
        this.city = city;
    }
}
