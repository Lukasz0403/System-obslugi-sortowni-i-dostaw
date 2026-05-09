package com.mycompany.projekt_io;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.io.IOException;
import javafx.application.Platform;

public class MainWindowController implements Initializable {

    
        @Override
    public void initialize(URL url, ResourceBundle rb) {
        // aktualizacja czasu i daty
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
        
        // Inicjalizacja danych dashboardu
        userNameLabel.setText("Jan Kowalski"); 
        userRoleLabel.setText("Administrator Systemu");
        userIdLabel.setText("#001");
        sessionStartLabel.setText(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        
        //rozmiar min okna
        Platform.runLater(() -> {
            Stage stage = (Stage) magButton.getScene().getWindow();
            WindowConstraints.applyMinSize(stage);
            stage.centerOnScreen();
        });
    }
    
    
    @FXML
    private Label timeLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private Button homeButton;
    
    @FXML
    private Button magButton;

    @FXML
    private Button pacButton;

    @FXML
    private Button addButton;

    // Pola Dashboardu
    @FXML private Label userNameLabel;
    @FXML private Label userRoleLabel;
    @FXML private Label userIdLabel;
    @FXML private Label sessionStartLabel;

    @FXML
    private void handleHomeButton() {
        loadWindow("/com/mycompany/projekt_io/mainWindow.fxml");
    }
    
    @FXML
    private void handleMagButton() {
        loadWindow("/com/mycompany/projekt_io/werehouseMainWindow.fxml");
    }

    @FXML
    private void handlePacButton() {
        loadWindow("/com/mycompany/projekt_io/packageTableWindow.fxml");
    }

    @FXML
    private void handleUserButton() {
        loadWindow("/com/mycompany/projekt_io/userManageWindow.fxml");
    }

    // Metoda pomocnicza do ładowania nowego FXML
    private void loadWindow(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            // Pobranie aktualnego Stage z jednego z labeli
            Stage stage = (Stage) timeLabel.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}