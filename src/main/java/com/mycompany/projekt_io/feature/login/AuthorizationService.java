/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projekt_io.feature.login;

import com.mycompany.projekt_io.datamodel.User;

/**
 * Serwis odpowiedzialny za autoryzację użytkowników w systemie.
 * <p>
 * Udostępnia metody do weryfikacji uprawnień zalogowanego użytkownika, w tym
 * sprawdzanie czy użytkownik posiada rolę administratora lub inne wymagane
 * uprawnienia.
 * </p>
 *
 * @author Łukasz Motyka
 */
public class AuthorizationService {
    
    /**
     * Sprawdza czy podany użytkownik posiada uprawnienia administratora.
     * <p>
     * Porównanie nazwy uprawnienia jest niewrażliwe na wielkość liter, dzięki
     * czemu wartości takie jak "Administrator" lub "ADMINISTRATOR" są
     * traktowane równoważnie.
     * </p>
     *
     * @param user użytkownik do sprawdzenia; może być {@code null}
     * @return {@code true} jeśli użytkownik istnieje i posiada uprawnienie o
     * nazwie "administrator" (bez względu na wielkość liter), {@code false} w
     * przeciwnym wypadku
     */
    public boolean isAdmin(User user) {
        return user != null && "administrator".equalsIgnoreCase(user.getPermission().getName());
    }

    /**
     * Sprawdza czy podany użytkownik posiada konkretne, wymagane uprawnienie.
     * <p>
     * Porównanie nazwy uprawnienia jest wrażliwe na wielkość liter. Metoda jest
     * wykorzystywana do kontroli dostępu do poszczególnych funkcji systemu na
     * podstawie roli użytkownika.
     * </p>
     *
     * @param user użytkownik do sprawdzenia; może być {@code null}
     * @param requiredPermission nazwa wymaganego uprawnienia (wielkość liter ma
     * znaczenie)
     * @return {@code true} jeśli użytkownik istnieje i jego uprawnienie
     * dokładnie odpowiada wartości {@code requiredPermission}, {@code false} w
     * przeciwnym wypadku
     */
    public boolean hasPermission(User user, String requiredPermission) {
        return user != null && user.getPermission().getName().equals(requiredPermission);
    }
    
}
