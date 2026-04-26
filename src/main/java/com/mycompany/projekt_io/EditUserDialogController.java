package com.mycompany.projekt_io;

import com.mycompany.projekt_io.core.database.UserDAO;
import com.mycompany.projekt_io.core.database.UserDAOInterface;
import com.mycompany.projekt_io.datamodel.Permission;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Kontroler okna dialogowego edycji danych istniejącego użytkownika.
 * <p>
 * Wyświetla formularz z loginem, nowym hasłem oraz listą wyboru uprawnień.
 * Okno otwierane jest jako dialog modalny z poziomu widoku zarządzania
 * użytkownikami. Zapis zmian nie jest jeszcze zaimplementowany — metoda
 * {@link #handleSave()} wyświetla stosowny komunikat zastępczy.
 * </p>
 */
public class EditUserDialogController implements Initializable {

    @FXML private TextField loginField;
    @FXML private PasswordField passwordField;
    @FXML private ChoiceBox<Permission> permissionChoiceBox;
    @FXML private Label errorLabel;

    private UserDAOInterface userDAO = new UserDAO();

    /**
     * Wypełnia pole loginu danymi edytowanego użytkownika.
     * <p>
     * Wywoływana przez kontroler nadrzędny bezpośrednio po załadowaniu
     * okna dialogowego, przed jego wyświetleniem użytkownikowi.
     * </p>
     *
     * @param login login użytkownika, którego dane mają być edytowane
     */
    public void setUser(String login) {
        loginField.setText(login);
    }

    /**
     * Inicjalizuje kontroler po załadowaniu widoku FXML.
     * <p>
     * Pobiera listę dostępnych uprawnień z bazy danych, wypełnia nią listę
     * rozwijaną i ustawia domyślnie pierwsze uprawnienie z listy.
     * Konfiguruje również konwerter wyświetlający nazwę uprawnienia
     * zamiast domyślnej reprezentacji obiektu {@link Permission}.
     * </p>
     *
     * @param url lokalizacja używana do rozwiązywania względnych ścieżek
     *            do obiektu głównego lub {@code null} jeśli nieznana
     * @param rb  zasoby używane do lokalizacji obiektu głównego
     *            lub {@code null} jeśli nieznane
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        List<Permission> permissions = userDAO.getPermissions();
        permissionChoiceBox.getItems().addAll(permissions);
        if (!permissions.isEmpty()) {
            permissionChoiceBox.setValue(permissions.get(0));
        }
        permissionChoiceBox.setConverter(new javafx.util.StringConverter<Permission>() {
            @Override
            public String toString(Permission p) {
                return p != null ? p.getName() : "";
            }
            @Override
            public Permission fromString(String s) { return null; }
        });
    }

    /**
     * Obsługuje kliknięcie przycisku zapisu zmian.
     * <p>
     * Metoda nie jest jeszcze zaimplementowana — wyświetla komunikat
     * informujący o konieczności kontaktu z administratorem bazy danych.
     * </p>
     */
    @FXML
    private void handleSave() {
        errorLabel.setText("Method not finished - contact database administrator.");
        errorLabel.setVisible(true);
    }

    /**
     * Obsługuje kliknięcie przycisku anulowania — zamyka okno dialogowe
     * bez zapisywania żadnych zmian.
     */
    @FXML
    private void handleCancel() {
        Stage stage = (Stage) loginField.getScene().getWindow();
        stage.close();
    }
}