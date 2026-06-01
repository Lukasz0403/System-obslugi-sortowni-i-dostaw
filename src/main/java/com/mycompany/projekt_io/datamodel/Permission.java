package com.mycompany.projekt_io.datamodel;

/**
 * Model danych reprezentujący uprawnienie użytkownika w systemie.
 * <p>
 * Określa poziom dostępu użytkownika do funkcji aplikacji.
 * </p>
 *
 * @author Radosław Kruczek
 */
public class Permission {

    int permission_id;
    String name;

    /**
     * Tworzy obiekt uprawnienia.
     *
     * @param permission_id unikalny identyfikator uprawnienia
     * @param name nazwa uprawnienia (np. "administrator", "pracownik")
     */
    public Permission(int permission_id, String name) {
        this.permission_id = permission_id;
        this.name = name;
    }

    /** @return unikalny identyfikator uprawnienia */
    public int getPermission_id() {
        return permission_id; 
    }

    /** @return nazwa uprawnienia */
    public String getName() {
        return name; 
    }

    /** @param permission_id unikalny identyfikator uprawnienia */
    public void setPermission_id(int permission_id) {
        this.permission_id = permission_id;
    }

    /** @param name nazwa uprawnienia */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Permission{" + "permission_id=" + permission_id + ", name=" + name + '}';
    }
}