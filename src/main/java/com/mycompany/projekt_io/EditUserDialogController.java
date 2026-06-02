package com.mycompany.projekt_io;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import com.mycompany.projekt_io.core.database.UserDAO;
import com.mycompany.projekt_io.core.database.UserDAOInterface;
import com.mycompany.projekt_io.datamodel.Permission;
import com.mycompany.projekt_io.feature.users.UserManageService;

/**
 * Kontroler okna dialogowego edycji danych istniejącego użytkownika.
 * <p>
 * Wyświetla formularz z loginem, opcjonalnym nowym hasłem oraz listą wyboru
 * uprawnień. Umożliwia zachowanie aktualnego hasła przez zaznaczenie checkboxa.
 * Okno otwierane jest jako dialog modalny z poziomu widoku zarządzania
 * użytkownikami. Po pomyślnym zapisie wywoływany jest callback
 * {@link #onUserUpdated} odświeżający listę użytkowników w widoku nadrzędnym.
 * </p>
 *
 * @author Ida Wszoła
 */
public class EditUserDialogController implements Initializable {

    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private CheckBox keepCurrentPasswordCheckbox;
    @FXML
    private ChoiceBox<Permission> permissionChoiceBox;
    @FXML
    private Label errorLabel;

    private final UserDAOInterface userDAO = new UserDAO();
    private int userId;
    private Runnable onUserUpdated;

    /**
     * Ustawia identyfikator edytowanego użytkownika.
     *
     * @param id identyfikator użytkownika w bazie danych
     */
    public void setId(int id) {
        this.userId = id;
    }

    /**
     * Wypełnia pole loginu danymi edytowanego użytkownika.
     *
     * @param login aktualny login użytkownika
     */
    public void setUser(String login) {
        loginField.setText(login);
    }

    /**
     * Rejestruje callback wywoływany po pomyślnym zapisie zmian.
     *
     * @param callback akcja odświeżająca listę użytkowników w widoku nadrzędnym
     */
    public void setOnUserUpdated(Runnable callback) {
        this.onUserUpdated = callback;
    }

    /**
     * Inicjalizuje kontroler — pobiera listę uprawnień z bazy, wypełnia listę
     * rozwijaną i ustawia konwerter wyświetlający nazwy uprawnień.
     *
     * @param url ścieżka do pliku FXML (nieużywana bezpośrednio)
     * @param rb zasoby lokalizacyjne (nieużywane bezpośrednio)
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        List<Permission> permissions = userDAO.getPermissions();
        permissionChoiceBox.getItems().addAll(permissions);
        if (!permissions.isEmpty()) {
            permissionChoiceBox.setValue(permissions.get(0));
        }
        permissionChoiceBox.setConverter(new StringConverter<Permission>() {
            @Override
            public String toString(Permission p) {
                return p != null ? p.getName() : "";
            }

            @Override
            public Permission fromString(String s) {
                return null;
            }
        });
    }

    /**
     * Obsługuje zmianę stanu checkboxa zachowania aktualnego hasła.
     * <p>
     * Gdy checkbox jest zaznaczony — blokuje pole hasła i czyści jego
     * zawartość. Gdy odznaczony — odblokowuje pole umożliwiając wpisanie nowego
     * hasła.
     * </p>
     */
    @FXML
    private void handleKeepPasswordAction() {
        boolean keep = keepCurrentPasswordCheckbox.isSelected();
        passwordField.setDisable(keep);
        if (keep) {
            passwordField.clear();
        }
    }

    /**
     * Obsługuje kliknięcie przycisku zapisu zmian.
     * <p>
     * Waliduje kompletność formularza i zapisuje zmiany przez
     * {@link UserManageService#changeUser()}. Jeśli zaznaczono zachowanie hasła
     * serwis aktualizuje tylko login i uprawnienia. Po pomyślnym zapisie
     * wywołuje callback i zamyka dialog.
     * </p>
     */
    @FXML
    private void handleSave() {
        String login = loginField.getText().trim();
        String password = passwordField.getText();
        Permission selected = permissionChoiceBox.getValue();
        boolean keepOld = keepCurrentPasswordCheckbox.isSelected();
        UserManageService u;

        if (login.isEmpty() || selected == null || (!keepOld && password.isEmpty())) {
            errorLabel.setText("All fields are required.");
            errorLabel.setVisible(true);
            return;
        }

        if (keepOld) {
            u = new UserManageService(userId, login, null, selected);
        } else {
            u = new UserManageService(userId, login, password, selected);
        }

        try {
            boolean success = u.changeUser();
            if (success) {
                if (onUserUpdated != null) {
                    onUserUpdated.run();
                }
                closeDialog();
            } else {
                errorLabel.setText("SAVE ERROR (Check for duplicates)");
                errorLabel.setVisible(true);
            }
        } catch (Exception e) {
            errorLabel.setText("SQL ERROR: " + e.getMessage());
            errorLabel.setVisible(true);
        }
    }

    /**
     * Obsługuje kliknięcie przycisku anulowania, zamyka dialog bez zapisu.
     */
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
