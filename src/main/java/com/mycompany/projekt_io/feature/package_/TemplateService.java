/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projekt_io.feature.package_;

import java.util.prefs.Preferences;

/**
 * Serwis zarządzający szablonem danych nadawcy.
 * <p>
 * Umożliwia zapis i odczyt danych nadawcy przy użyciu Java Preferences API,
 * które przechowuje dane w rejestrze systemowym (Windows) lub katalogu domowym
 * użytkownika. Dane są dostępne między sesjami aplikacji bez
 * potrzeby tworzenia plików zewnętrznych.
 * </p>
 * <p>
 * Klasa zaprojektowana z myślą o rozszerzalności - dodanie nowego pola wymaga
 * jedynie dodania nowej stałej klucza i odpowiednich metod get/set w klasie
 * {@link SenderTemplate}.
 * </p>
 * 
 * @author Mateusz Gojny
 */
public class TemplateService implements TemplateServiceInterface {

    private static final Preferences prefs
            = Preferences.userNodeForPackage(TemplateService.class);

    // Klucze przechowywanych wartości
    private static final String KEY_NAME = "template_sender_name";
    private static final String KEY_STREET = "template_sender_street";
    private static final String KEY_POSTCODE = "template_sender_postcode";
    private static final String KEY_EMAIL = "template_sender_email";
    private static final String KEY_PHONE = "template_sender_phone";
    private static final String KEY_CITY = "template_sender_city";

    /**
     * Zapisuje dane nadawcy jako szablon.
     * <p>
     * Wszystkie pola obiektu {@link SenderTemplate} są zapisywane w Java
     * Preferences API. Wartości {@code null} są zastępowane pustym ciągiem
     * znaków.
     * </p>
     *
     * @param template obiekt z danymi nadawcy do zapisania; nie może być
     * {@code null}
     */
    public void saveTemplate(SenderTemplate template) {
        prefs.put(KEY_NAME, nvl(template.getName()));
        prefs.put(KEY_STREET, nvl(template.getStreet()));
        prefs.put(KEY_POSTCODE, nvl(template.getPostcode()));
        prefs.put(KEY_EMAIL, nvl(template.getEmail()));
        prefs.put(KEY_PHONE, nvl(template.getPhone()));
        prefs.put(KEY_CITY, nvl(template.getCity()));
    }

    /**
     * Wczytuje zapisany szablon nadawcy.
     * <p>
     * Jeśli żaden szablon nie był wcześniej zapisany, wszystkie pola zwróconego
     * obiektu będą pustymi ciągami znaków.
     * </p>
     *
     * @return obiekt {@link SenderTemplate} z zapisanymi danymi nadawcy
     */
    public SenderTemplate loadTemplate() {
        return new SenderTemplate(
                prefs.get(KEY_NAME, ""),
                prefs.get(KEY_STREET, ""),
                prefs.get(KEY_POSTCODE, ""),
                prefs.get(KEY_EMAIL, ""),
                prefs.get(KEY_PHONE, ""),
                prefs.get(KEY_CITY, "")
        );
    }

    /**
     * Sprawdza czy szablon jest zapisany i w pełni uzupełniony.
     * <p>
     * Szablon jest uznawany za kompletny jeśli żadne z wymaganych pól (nazwa,
     * ulica, kod pocztowy, email, telefon, miasto) nie jest puste.
     * </p>
     *
     * @return {@code true} jeśli wszystkie pola szablonu są niepuste,
     * {@code false} jeśli szablon nie istnieje lub jest niekompletny
     */
    public boolean isTemplateSaved() {
        SenderTemplate t = loadTemplate();
        return !t.getName().isEmpty()
                && !t.getStreet().isEmpty()
                && !t.getPostcode().isEmpty()
                && !t.getEmail().isEmpty()
                && !t.getPhone().isEmpty()
                && !t.getCity().isEmpty();
    }

    /**
     * Usuwa zapisany szablon — wszystkie pola są kasowane z Preferences.
     */
    public void clearTemplate() {
        prefs.remove(KEY_NAME);
        prefs.remove(KEY_STREET);
        prefs.remove(KEY_POSTCODE);
        prefs.remove(KEY_EMAIL);
        prefs.remove(KEY_PHONE);
        prefs.remove(KEY_CITY);
    }

    private String nvl(String value) {
        return value != null ? value : "";
    }
}
