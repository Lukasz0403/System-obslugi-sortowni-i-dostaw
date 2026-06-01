package com.mycompany.projekt_io;

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
import com.mycompany.projekt_io.core.database.UserDAO;
import com.mycompany.projekt_io.core.database.UserDAOInterface;
import com.mycompany.projekt_io.datamodel.User;
import com.mycompany.projekt_io.feature.login.AppCloser;
import com.mycompany.projekt_io.feature.login.AppSession;
import com.mycompany.projekt_io.feature.users.UserManageService;

/**
 * Kontroler okna zarządzania użytkownikami systemu.
 * <p>
 * Wyświetla dynamicznie budowaną listę użytkowników filtrowaną według uprawnień
 * zalogowanego użytkownika. Umożliwia dodawanie nowych kont przez dialog
 * modalny, edycję istniejących kont oraz usuwanie użytkowników z
 * zabezpieczeniem przed usunięciem ostatniego administratora.
 * </p>
 *
 * @author Mateusz Gojny, Ida Wszoła
 */
public class UserManageWindowController implements Initializable {

    @FXML
    private Label timeLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private VBox userListContainer;
    @FXML
    private Button logOut, closeApp;
    @FXML
    private Button addUserButton;

    private UserDAOInterface userDAO = new UserDAO();

    /**
     * Inicjalizuje kontroler — uruchamia zegar, ustawia minimalne wymiary okna,
     * ukrywa przycisk dodawania dla pracowników i ładuje listę użytkowników.
     *
     * @param url ścieżka do pliku FXML (nieużywana bezpośrednio)
     * @param rb zasoby lokalizacyjne (nieużywane bezpośrednio)
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
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

        Platform.runLater(() -> {
            Stage stage = (Stage) timeLabel.getScene().getWindow();
            WindowConstraints.applyMinSize(stage);
        });

        if (AppSession.getCurrentUser().getPermission().getPermission_id() == 1) {
            addUserButton.setVisible(false);
        }

        Platform.runLater(this::loadUsers);

        logOut.setOnAction(e -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Logout");
            confirm.setHeaderText(null);
            confirm.setContentText("Are you sure you want to log out?");
            confirm.initOwner(timeLabel.getScene().getWindow());
            confirm.showAndWait().ifPresent(response -> {
                if (response == javafx.scene.control.ButtonType.OK) {
                    AppSession.logout();
                    loadWindow("/com/mycompany/projekt_io/loginWindow.fxml");
                }
            });
        });

        closeApp.setOnAction(e -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Close Application");
            confirm.setHeaderText(null);
            confirm.setContentText("Are you sure you want to close the application?");
            confirm.initOwner(timeLabel.getScene().getWindow());
            confirm.showAndWait().ifPresent(response -> {
                if (response == javafx.scene.control.ButtonType.OK) {
                    AppCloser.closeApp();
                }
            });
        });
    }

    /**
     * Pobiera listę użytkowników z bazy i buduje dynamicznie widok listy.
     * <p>
     * Filtruje wyświetlanych użytkowników według uprawnień zalogowanego
     * użytkownika — pracownik widzi tylko własne konto, kierownik nie widzi
     * kierowników, administrator widzi wszystkich. Wiersze listy mają
     * naprzemienne tło.
     * </p>
     */
    public void loadUsers() {
        List<User> users = userDAO.getUsers();
        userListContainer.getChildren().clear();
        boolean alternate = false;

        if (AppSession.getCurrentUser().getPermission().getPermission_id() == 1) {
            for (User user : users) {
                if (user.getPermission().getPermission_id() != 1) {
                    continue;
                }
                userListContainer.getChildren().add(
                        buildUserRow(user.getUser_id(), user.getLogin(),
                                user.getPermission().getName(), alternate));
                alternate = !alternate;
            }
        } else if (AppSession.getCurrentUser().getPermission().getPermission_id() == 3) {
            for (User user : users) {
                if (user.getPermission().getPermission_id() == 2) {
                    continue;
                }
                userListContainer.getChildren().add(
                        buildUserRow(user.getUser_id(), user.getLogin(),
                                user.getPermission().getName(), alternate));
                alternate = !alternate;
            }
        } else {
            for (User user : users) {
                userListContainer.getChildren().add(
                        buildUserRow(user.getUser_id(), user.getLogin(),
                                user.getPermission().getName(), alternate));
                alternate = !alternate;
            }
        }
    }

    /**
     * Buduje wiersz listy użytkowników z przyciskami Edit i Delete.
     * <p>
     * Przyciski są ukrywane dla użytkowników bez uprawnień do edycji. Kierownik
     * nie może edytować ani usuwać innych kierowników.
     * </p>
     *
     * @param id identyfikator użytkownika
     * @param login login użytkownika
     * @param role nazwa roli użytkownika
     * @param alternate czy wiersz ma mieć naprzemienne tło
     * @return gotowy komponent {@link HBox} reprezentujący wiersz listy
     */
    private HBox buildUserRow(int id, String login, String role, boolean alternate) {
        HBox row = new HBox();
        row.setPrefHeight(40.0);
        row.setStyle("-fx-padding: 0 15 0 15; -fx-background-color: "
                + (alternate ? "#efefea" : "#F7F5E6") + ";");
        row.setAlignment(Pos.CENTER_LEFT);

        Label loginLabel = new Label(login);
        loginLabel.setPrefWidth(150.0);
        Label roleLabel = new Label(role);
        roleLabel.setPrefWidth(120.0);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button editBtn = new Button("Edit");
        editBtn.setStyle("-fx-background-color: #52658F; -fx-text-fill: white; "
                + "-fx-cursor: hand; -fx-font-size: 10px;");
        editBtn.setOnAction(e -> handleEditUser(id, login));
        if (AppSession.getCurrentUser().getPermission().getPermission_id() == 1) {
            editBtn.setVisible(false);
        }
        if (AppSession.getCurrentUser().getPermission().getPermission_id() == 3
                && role.equals("kierownik")) {
            editBtn.setVisible(false);
        }

        Button deleteBtn = new Button("Delete");
        deleteBtn.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white; "
                + "-fx-cursor: hand; -fx-font-size: 10px;");
        deleteBtn.setOnAction(e -> handleDeleteUser(id, login));
        if (AppSession.getCurrentUser().getPermission().getPermission_id() == 1) {
            deleteBtn.setVisible(false);
        }
        if (AppSession.getCurrentUser().getPermission().getPermission_id() == 3
                && role.equals("kierownik")) {
            deleteBtn.setVisible(false);
        }

        HBox actions = new HBox(5, editBtn, deleteBtn);
        actions.setAlignment(Pos.CENTER_LEFT);
        row.getChildren().addAll(loginLabel, roleLabel, spacer, actions);
        return row;
    }

    /**
     * Obsługuje przycisk dodawania nowego użytkownika.
     * <p>
     * Otwiera dialog modalny z formularzem dodawania konta. Po zamknięciu
     * dialogu lista użytkowników jest odświeżana przez callback.
     * </p>
     */
    @FXML
    private void handleAddUser() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com/mycompany/projekt_io/addUserDialog.fxml"));
            Parent root = loader.load();
            AddUserDialogController controller = loader.getController();
            controller.setOnUserAdded(() -> Platform.runLater(this::loadUsers));
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

    /**
     * Obsługuje kliknięcie przycisku Edit dla wybranego użytkownika.
     * <p>
     * Otwiera dialog modalny z formularzem edycji przekazując ID i login
     * użytkownika. Po zamknięciu dialogu lista jest odświeżana przez callback.
     * </p>
     *
     * @param id identyfikator edytowanego użytkownika
     * @param login login edytowanego użytkownika
     */
    private void handleEditUser(int id, String login) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com/mycompany/projekt_io/editUserDialog.fxml"));
            Parent root = loader.load();
            EditUserDialogController controller = loader.getController();
            controller.setId(id);
            controller.setUser(login);
            controller.setOnUserUpdated(() -> Platform.runLater(this::loadUsers));
            showDialog(root, "Edit User");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Wyświetla okno dialogowe jako modal.
     *
     * @param root korzeń sceny dialogu
     * @param title tytuł okna dialogowego
     */
    private void showDialog(Parent root, String title) {
        Stage dialog = new Stage();
        dialog.initModality(javafx.stage.Modality.APPLICATION_MODAL);
        dialog.initOwner(timeLabel.getScene().getWindow());
        dialog.setTitle(title);
        dialog.setResizable(false);
        dialog.setScene(new Scene(root));
        dialog.showAndWait();
    }

    /**
     * Obsługuje kliknięcie przycisku Delete dla wybranego użytkownika.
     * <p>
     * Sprawdza czy usuwany użytkownik jest ostatnim administratorem — jeśli
     * tak, wyświetla alert i przerywa operację. W przeciwnym razie wyświetla
     * potwierdzenie i po zatwierdzeniu usuwa użytkownika z bazy.
     * </p>
     *
     * @param id identyfikator usuwanego użytkownika
     * @param login login usuwanego użytkownika wyświetlany w komunikacie
     */
    private void handleDeleteUser(int id, String login) {
        User userToDelete = userDAO.getUsers().stream()
                .filter(u -> u.getUser_id() == id)
                .findFirst()
                .orElse(null);

        if (userToDelete != null && userToDelete.getPermission().getPermission_id() == 2) {
            long adminCount = userDAO.getUsers().stream()
                    .filter(u -> u.getPermission().getPermission_id() == 2)
                    .count();
            if (adminCount <= 1) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Cannot Delete");
                alert.setHeaderText(null);
                alert.setContentText("Cannot delete the last administrator. "
                        + "At least one administrator must remain in the system.");
                alert.initOwner(timeLabel.getScene().getWindow());
                alert.showAndWait();
                return;
            }
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete User");
        confirm.setHeaderText("Are you sure you want to delete: " + login + "?");
        confirm.setContentText("This action cannot be undone.");
        confirm.initOwner(timeLabel.getScene().getWindow());
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                UserManageService u = new UserManageService(id);
                u.deleteUser();
                loadUsers();
            }
        });
    }

    /**
     * Przechodzi do okna głównego.
     */
    @FXML
    private void handleHomeButton() {
        loadWindow("/com/mycompany/projekt_io/mainWindow.fxml");
    }

    /**
     * Przechodzi do okna magazynu.
     */
    @FXML
    private void handleMagButton() {
        loadWindow("/com/mycompany/projekt_io/werehouseMainWindow.fxml");
    }

    /**
     * Przechodzi do okna tabeli paczek.
     */
    @FXML
    private void handlePacButton() {
        loadWindow("/com/mycompany/projekt_io/packageTableWindow.fxml");
    }

    /**
     * Odświeża bieżące okno zarządzania użytkownikami.
     */
    @FXML
    private void handleUserButton() {
        loadWindow("/com/mycompany/projekt_io/userManageWindow.fxml");
    }

    /**
     * Ładuje wskazane okno FXML i zastępuje nim bieżącą scenę.
     *
     * @param fxmlPath ścieżka do pliku FXML okna docelowego
     */
    private void loadWindow(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) timeLabel.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
