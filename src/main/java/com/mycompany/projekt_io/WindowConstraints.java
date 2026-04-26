package com.mycompany.projekt_io;

import javafx.stage.Stage;

/**
 * Klasa narzędziowa przechowująca i stosująca minimalne wymiary okien aplikacji.
 * <p>
 * Zapewnia spójność ograniczeń rozmiaru okna w całej aplikacji — każdy kontroler
 * powinien wywoływać {@link #applyMinSize(Stage)} podczas inicjalizacji widoku,
 * aby użytkownik nie mógł zmniejszyć okna poniżej dopuszczalnego minimum.
 * </p>
 */
public class WindowConstraints {

    /**
     * Minimalna szerokość okna aplikacji w pikselach.
     */
    public static final double MIN_WIDTH = 600.0;

    /**
     * Minimalna wysokość okna aplikacji w pikselach.
     */
    public static final double MIN_HEIGHT = 450.0;

    /**
     * Ustawia minimalne wymiary podanego okna na wartości zdefiniowane
     * w {@link #MIN_WIDTH} i {@link #MIN_HEIGHT}.
     * <p>
     * Metoda powinna być wywoływana w kontrolerach po załadowaniu sceny,
     * najlepiej wewnątrz bloku {@code Platform.runLater()} w metodzie
     * {@code initialize()}, gdy obiekt {@link Stage} jest już dostępny.
     * </p>
     *
     * @param stage okno JavaFX, dla którego zostaną ustawione minimalne wymiary
     */
    public static void applyMinSize(Stage stage) {
        stage.setMinWidth(MIN_WIDTH);
        stage.setMinHeight(MIN_HEIGHT);
    }
}