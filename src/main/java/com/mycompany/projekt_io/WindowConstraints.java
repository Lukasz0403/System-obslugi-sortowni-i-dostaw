package com.mycompany.projekt_io;

import javafx.stage.Stage;

/**
 * Klasa narzędziowa przechowująca minimalne wymiary okna aplikacji.
 * <p>
 * Zapewnia spójność rozmiarów okien we wszystkich widokach aplikacji przez
 * udostępnienie stałych i metody ustawiającej minimalne wymiary dla podanego
 * obiektu {@link Stage}.
 * </p>
 *
 * @author Ida Wszoła
 */
public class WindowConstraints {

    /**
     * Minimalna szerokość okna aplikacji w pikselach.
     */
    public static final double MIN_WIDTH = 950.0;

    /**
     * Minimalna wysokość okna aplikacji w pikselach.
     */
    public static final double MIN_HEIGHT = 750.0;

    /**
     * Ustawia minimalne wymiary podanego okna na wartości zdefiniowane przez
     * {@link #MIN_WIDTH} i {@link #MIN_HEIGHT}.
     *
     * @param stage okno JavaFX którego minimalne wymiary mają być ustawione
     */
    public static void applyMinSize(Stage stage) {
        stage.setMinWidth(MIN_WIDTH);
        stage.setMinHeight(MIN_HEIGHT);
    }
}
