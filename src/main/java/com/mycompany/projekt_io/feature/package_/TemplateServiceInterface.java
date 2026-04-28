/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.projekt_io.feature.package_;

import com.mycompany.projekt_io.feature.package_.SenderTemplate;

/**
 * Interfejs definiujący kontrakt dla operacji na szablonie nadawcy.
 * <p>
 * Umożliwia podmianę implementacji przechowywania szablonu bez konieczności
 * modyfikacji klas korzystających z serwisu. Obecnie implementowany przez
 * {@link TemplateService}, który wykorzystuje Java Preferences API.
 * </p>
 *
 * @author Mateusz Gojny
 */
public interface TemplateServiceInterface {

    /**
     * Zapisuje dane nadawcy jako szablon.
     *
     * @param template obiekt z danymi nadawcy do zapisania
     */
    void saveTemplate(SenderTemplate template);

    /**
     * Wczytuje zapisany szablon nadawcy.
     *
     * @return obiekt {@link SenderTemplate} z zapisanymi danymi lub obiekt z
     * pustymi polami jeśli brak szablonu
     */
    SenderTemplate loadTemplate();

    /**
     * Sprawdza czy szablon jest zapisany i w pełni uzupełniony.
     *
     * @return {@code true} jeśli wszystkie pola szablonu są niepuste,
     * {@code false} w przeciwnym razie
     */
    boolean isTemplateSaved();

    /**
     * Usuwa zapisany szablon.
     */
    void clearTemplate();
}
