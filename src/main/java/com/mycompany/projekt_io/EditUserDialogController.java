package com.mycompany.projekt_io;

import com.mycompany.projekt_io.core.database.UserDAO;
import com.mycompany.projekt_io.core.database.UserDAOInterface;
import com.mycompany.projekt_io.datamodel.Permission;
import com.mycompany.projekt_io.feature.users.UserManageService;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

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
    @FXML private CheckBox keepCurrentPasswordCheckbox; // Musi być w FXML!
    @FXML private ChoiceBox<Permission> permissionChoiceBox;
    @FXML private Label errorLabel;

    private UserDAOInterface userDAO = new UserDAO();
    private int userId;
    private Runnable onUserUpdated;

    public void setId(int id) { this.userId = id; }
    public void setUser(String login) { loginField.setText(login); }
    public void setOnUserUpdated(Runnable callback) { this.onUserUpdated = callback; }

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
        if (!permissions.isEmpty()) permissionChoiceBox.setValue(permissions.get(0));

        permissionChoiceBox.setConverter(new StringConverter<Permission>() {
            @Override public String toString(Permission p) { return p != null ? p.getName() : ""; }
            @Override public Permission fromString(String s) { return null; }
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
    private void handleKeepPasswordAction() {
        boolean keep = keepCurrentPasswordCheckbox.isSelected();
        passwordField.setDisable(keep);
        if (keep) passwordField.clear();
    }

    /**
     * Obsługuje kliknięcie przycisku anulowania — zamyka okno dialogowe
     * bez zapisywania żadnych zmian.
     */
    @FXML
    private void handleSave() {
        String login = loginField.getText().trim();
        String password = passwordField.getText();
        Permission selected = permissionChoiceBox.getValue();
        boolean keepOld = keepCurrentPasswordCheckbox.isSelected();

        if (login.isEmpty() || selected == null || (!keepOld && password.isEmpty())) {
            errorLabel.setText("Uzupełnij dane!");
            errorLabel.setVisible(true);
            return;
        }

        // Jeśli zaznaczono "Keep password", wysyłamy cokolwiek (np. "OLD_PWD"), 
        String passwordToSend = keepOld ? "BRAK_ZMIAN" : password;

        try {
            UserManageService u = new UserManageService(login, passwordToSend, selected);
            boolean success = u.addUser(); // TRZEBA ZROBIĆ METODE UPDAYE IDK CZY JEST

            if (success) {
                if (onUserUpdated != null) onUserUpdated.run();
                closeDialog();
            } else {
                errorLabel.setText("Błąd zapisu (może duplikujesz login?)");
                errorLabel.setVisible(true);
            }
        } catch (Exception e) {
            errorLabel.setText("Błąd SQL: " + e.getMessage());
            errorLabel.setVisible(true);
        }
    }

    @FXML private void handleCancel() { closeDialog(); }

    private void closeDialog() {
        Stage stage = (Stage) loginField.getScene().getWindow();
        stage.close();
    }
   
}
