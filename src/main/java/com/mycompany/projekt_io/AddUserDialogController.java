package com.mycompany.projekt_io;


import com.mycompany.projekt_io.datamodel.Permission;
import com.mycompany.projekt_io.feature.users.UserManageService;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import com.mycompany.projekt_io.core.database.UserDAO;
import com.mycompany.projekt_io.core.database.UserDAOInterface;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.control.Alert;


/**
 * Kontroler okna dialogowego dodawania nowego użytkownika systemu.
 * <p>
 * Wyświetla formularz z loginem, hasłem oraz listą wyboru uprawnień.
 * Okno otwierane jest jako dialog modalny z poziomu widoku zarządzania
 * użytkownikami. Po pomyślnym dodaniu użytkownika wywoływany jest opcjonalny
 * callback {@link #onUserAdded}, który pozwala kontrolerowi nadrzędnemu
 * odświeżyć listę użytkowników.
 * </p>
 * <p>
 * Właściwy zapis do bazy danych nie jest jeszcze zaimplementowany —
 * metoda {@link #handleAddUser()} wyświetla komunikat zastępczy (TODO).
 * </p>
 */
public class AddUserDialogController implements Initializable {

    @FXML private TextField loginField;
    @FXML private PasswordField passwordField;
    @FXML private ChoiceBox<Permission> permissionChoiceBox;
    @FXML private Label errorLabel;

    private UserDAOInterface userDAO = new UserDAO();
    private Runnable onUserAdded;

    /**
     * Rejestruje callback wywoływany po pomyślnym dodaniu nowego użytkownika.
     * <p>
     * Powinien być ustawiony przez kontroler nadrzędny zaraz po załadowaniu
     * okna dialogowego. Typowe zastosowanie to odświeżenie listy użytkowników
     * w widoku zarządzania.
     * </p>
     *
     * @param callback akcja wykonywana po dodaniu użytkownika;
     *                 może być {@code null} jeśli odświeżenie nie jest wymagane
     */
    public void setOnUserAdded(Runnable callback) {
        this.onUserAdded = callback;
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
     * Obsługuje kliknięcie przycisku dodania użytkownika.
     * <p>
     * Waliduje kompletność formularza — sprawdza, czy login, hasło
     * i uprawnienie zostały podane. Właściwy zapis do bazy danych
     * nie jest jeszcze zaimplementowany (oznaczony jako TODO).
     * </p>
     */
    @FXML
    private void handleAddUser() {
        String login = loginField.getText().trim();
        String password = passwordField.getText();
        Permission selected = permissionChoiceBox.getValue();

        if (login.isEmpty() || password.isEmpty() || selected == null) {
            errorLabel.setText("All fields are required.");
            errorLabel.setVisible(true);
            return;
        } else {
            
            UserManageService u = new UserManageService(login, password, selected);
            
            if(!u.addUser()) {
                errorLabel.setText("Error adding user..");
            } else {
                Alert confirm = new Alert(Alert.AlertType.INFORMATION);
                confirm.setTitle("Add User");
                confirm.setHeaderText("New user has been added: " + login + ".");
                confirm.showAndWait();
                closeDialog();
            }
        }
    }
    

    @FXML
    private void handleCancel() {
        closeDialog();
    }

    /**
     * Zamyka okno dialogowe.
     */
    private void closeDialog() {
        Stage stage = (Stage) loginField.getScene().getWindow();
        stage.close();
    }
}
