package com.mycompany.projekt_io.feature.package_;

/**
 * Interfejs definiujący kontrakt dla klas generujących etykiety kurierskie.
 * <p>
 * Implementacje tego interfejsu odpowiadają za tworzenie etykiet wysyłkowych w
 * formacie PDF na podstawie danych paczki. Umożliwia dobór odpowiedniego wyglądu etykiety oraz zestawu informacji
 * w zależności od potrzeb.
 * </p>
 *
 * @author Radosław Kruczek
 */
public interface LabelFactoryInterface {

    /**
     * Generuje etykietę kurierską i zapisuje ją jako plik PDF.
     *
     * @return {@code true} jeśli etykieta została pomyślnie wygenerowana,
     * {@code false} w przypadku błędu
     */
    public boolean printLabel();
}
