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
import javafx.scene.control.Alert;
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
    private int id;

    public void setUser(String login) {
        loginField.setText(login);
    }
    
    public void setId(int id) {
        this.id = id;
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
                confirm.setHeaderText("New user has been added: " + login + "?");
                confirm.showAndWait();
                closeDialog();
            }
        }
        errorLabel.setText("Method not finished - contact database administrator.");
        errorLabel.setVisible(true);
    }
    
    @FXML
    private void handleCancel() {
        closeDialog();
    }

    private void closeDialog() {
        Stage stage = (Stage) loginField.getScene().getWindow();
        stage.close();
    }
}
