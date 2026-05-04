package com.mycompany.projekt_io;

import com.mycompany.projekt_io.core.database.UserDAO;
import com.mycompany.projekt_io.core.database.UserDAOInterface;
import com.mycompany.projekt_io.datamodel.User;
import com.mycompany.projekt_io.feature.users.UserManageService;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class UserManageWindowController implements Initializable {

    @FXML private Label timeLabel;
    @FXML private Label dateLabel;
    @FXML private VBox userListContainer;

    private UserDAOInterface userDAO = new UserDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TIME AND DATE
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(1), event -> {
                LocalDateTime now = LocalDateTime.now();
                timeLabel.setText(now.format(timeFormatter));
                dateLabel.setText(now.format(dateFormatter));
            })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        // WINDOW CONSTRAINTS
        Platform.runLater(() -> {
            Stage stage = (Stage) timeLabel.getScene().getWindow();
            WindowConstraints.applyMinSize(stage);
        });

        loadUsers();
    }

    public void loadUsers() {
        List<User> users = userDAO.getUsers();
        userListContainer.getChildren().clear();
        boolean alternate = false;
        for (User user : users) {
            userListContainer.getChildren().add(
                buildUserRow(user.getUser_id(), user.getLogin(), user.getPermission().getName(), alternate)
            );
            alternate = !alternate;
        }
    }

    private HBox buildUserRow(int id, String login, String role, boolean alternate) {
        HBox row = new HBox();
        row.setPrefHeight(40.0);
        row.setStyle("-fx-padding: 0 15 0 15; -fx-background-color: " + (alternate ? "#efefea" : "#F7F5E6") + ";");
        row.setAlignment(Pos.CENTER_LEFT);

        Label loginLabel = new Label(login);
        loginLabel.setPrefWidth(150.0);
        Label roleLabel = new Label(role);
        roleLabel.setPrefWidth(120.0);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button editBtn = new Button("Edit");
        editBtn.setStyle("-fx-background-color: #52658F; -fx-text-fill: white; -fx-cursor: hand; -fx-font-size: 10px;");
        editBtn.setOnAction(e -> handleEditUser(id, login));

        Button deleteBtn = new Button("Delete");
        deleteBtn.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white; -fx-cursor: hand; -fx-font-size: 10px;");
        deleteBtn.setOnAction(e -> handleDeleteUser(id, login));

        HBox actions = new HBox(5, editBtn, deleteBtn);
        actions.setAlignment(Pos.CENTER_LEFT);

        row.getChildren().addAll(loginLabel, roleLabel, spacer, actions);
        return row;
    }
    
        @FXML
    private void handleAddUser() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/projekt_io/addUserDialog.fxml"));
            Parent root = loader.load();

            AddUserDialogController controller = loader.getController();
            controller.setOnUserAdded(() -> Platform.runLater(this::loadUsers)); // refresh list after add

            Stage dialog = new Stage();
            dialog.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            dialog.initOwner(timeLabel.getScene().getWindow());
            dialog.setTitle("Add User");
            dialog.setResizable(false);
            dialog.setScene(new Scene(root));
            dialog.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleEditUser(int id, String login) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/projekt_io/editUserDialog.fxml"));
            Parent root = loader.load();
            EditUserDialogController controller = loader.getController();
            
            // Przekazanie danych do edycji
            controller.setId(id);
            controller.setUser(login);
            
            // odświeżenie listy
            controller.setOnUserUpdated(() -> Platform.runLater(this::loadUsers));

            showDialog(root, "Edit User");
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void showDialog(Parent root, String title) {
        Stage dialog = new Stage();
        dialog.initModality(javafx.stage.Modality.APPLICATION_MODAL);
        dialog.initOwner(timeLabel.getScene().getWindow());
        dialog.setTitle(title);
        dialog.setResizable(false);
        dialog.setScene(new Scene(root));
        dialog.showAndWait();
    }

    private void handleDeleteUser(int id, String login) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete User");
        confirm.setHeaderText("Are you sure you want to delete: " + login + "?");
        confirm.setContentText("This action cannot be undone.");
        confirm.initOwner(timeLabel.getScene().getWindow());

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                System.out.println("Delete confirmed: " + login);
                UserManageService u = new UserManageService(id);
                u.deleteUser();
                // TODO: delete via UserDAO
                loadUsers();
            }
        });
    }

    @FXML private void handleMagButton() { loadWindow("/com/mycompany/projekt_io/werehouseMainWindow.fxml"); }
    @FXML private void handlePacButton() { loadWindow("/com/mycompany/projekt_io/packageTableWindow.fxml"); }
    @FXML private void handleUserButton() { loadWindow("/com/mycompany/projekt_io/userManageWindow.fxml"); }

    private void loadWindow(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) timeLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) { e.printStackTrace(); }
    }
}