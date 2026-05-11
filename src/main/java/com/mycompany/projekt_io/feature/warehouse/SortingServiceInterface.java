/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.projekt_io.feature.warehouse;

/**
 * Interfejs definiujący kontrakt dla algorytmów przydzielania regałów do paczek
 * w systemie magazynowym.
 * <p>
 * Umożliwia podmianę algorytmu sortowania bez modyfikacji klas korzystających z
 * serwisu. Przykładowe implementacje mogą różnić się strategią przydziału
 * regałów, np.:
 * </p>
 * <ul>
 * <li>{@link SortingService} — przydzielanie na podstawie regionu
 * docelowego</li>
 * <li>Implementacja FIFO — przydzielanie po kolei do pierwszego wolnego
 * regału</li>
 * <li>Implementacja wagowa — priorytetyzacja według wagi paczki</li>
 * </ul>
 */
public interface SortingServiceInterface {

    /**
     * Przydziela regały do paczek które jeszcze ich nie mają.
     * <p>
     * Implementacja powinna pobrać nieprzypisane paczki z bazy danych, znaleźć
     * dla nich odpowiednie miejsce w magazynie i zapisać przypisanie w bazie.
     * </p>
     */
    void assignShelvesToPackages();

    /**
     * Rejestruje obserwatora który będzie powiadamiany o przekroczeniu progu
     * zajętości strefy magazynowej.
     *
     * @param observer obserwator do zarejestrowania
     */
    void addObserver(WarehouseObserver observer);

    /**
     * Wyrejestrowuje obserwatora.
     *
     * @param observer obserwator do usunięcia
     */
    void removeObserver(WarehouseObserver observer);
}
