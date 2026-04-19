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

public class EditUserDialogController implements Initializable {

    @FXML private TextField loginField;
    @FXML private PasswordField passwordField;
    @FXML private ChoiceBox<Permission> permissionChoiceBox;
    @FXML private Label errorLabel;

    private UserDAOInterface userDAO = new UserDAO();

    public void setUser(String login) {
        loginField.setText(login);
    }

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

    @FXML
    private void handleSave() {
        errorLabel.setText("Method not finished - contact database administrator.");
        errorLabel.setVisible(true);
    }

    @FXML
    private void handleCancel() {
        Stage stage = (Stage) loginField.getScene().getWindow();
        stage.close();
    }
}
