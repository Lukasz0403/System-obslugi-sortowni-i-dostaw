package com.mycompany.projekt_io;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.util.Duration;
import com.mycompany.projekt_io.feature.login.AppCloser;
import com.mycompany.projekt_io.feature.login.AppSession;

/**
 * Kontroler głównego okna aplikacji (dashboard).
 * <p>
 * Wyświetla informacje o zalogowanym użytkowniku — login, rolę i godzinę
 * rozpoczęcia sesji. Obsługuje nawigację do pozostałych okien aplikacji oraz
 * przyciski wylogowania i zamknięcia aplikacji z potwierdzeniem.
 * </p>
 *
 * @author Mateusz Gojny, Ida Wszoła
 */
public class MainWindowController implements Initializable {

    @FXML
    private Label timeLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Button magButton;
    @FXML
    private Button logOut;
    @FXML
    private Button closeApp;
    @FXML
    private Label userNameLabel;
    @FXML
    private Label userRoleLabel;
    @FXML
    private Label userIdLabel;
    @FXML
    private Label sessionStartLabel;

    /**
     * Inicjalizuje kontroler — uruchamia zegar, wypełnia dane dashboardu danymi
     * zalogowanego użytkownika z {@link AppSession} oraz ustawia minimalne
     * wymiary okna i obsługę przycisków wylogowania i zamknięcia.
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

        if (AppSession.isLoggedIn()) {
            userNameLabel.setText(AppSession.getCurrentUser().getLogin());
            userRoleLabel.setText(AppSession.getCurrentUser().getPermission().getName());
            userIdLabel.setText("#" + AppSession.getCurrentUser().getLogin());
            sessionStartLabel.setText(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        } else {
            userNameLabel.setText("Unknown");
            userRoleLabel.setText("Unknown");
            userIdLabel.setText("-");
            sessionStartLabel.setText(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        }

        Platform.runLater(() -> {
            Stage stage = (Stage) magButton.getScene().getWindow();
            WindowConstraints.applyMinSize(stage);
            stage.centerOnScreen();
        });

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
     * Odświeża bieżące okno główne.
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
     * Przechodzi do okna zarządzania użytkownikami.
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
