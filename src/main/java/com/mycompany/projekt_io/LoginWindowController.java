package com.mycompany.projekt_io;

import com.mycompany.projekt_io.feature.login.*;
import com.mycompany.projekt_io.*;
import java.io.IOException;
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
import javafx.event.ActionEvent;
import javafx.util.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;


public class LoginWindowController implements Initializable {

    @FXML
    private Label dateTimeLabel;

    @FXML
    private Button devButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm    yyyy-MM-dd");

        Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(1), event -> {
                dateTimeLabel.setText(LocalDateTime.now().format(formatter));
            })
        );

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    @FXML
    private void handleDevButton(ActionEvent event) {
        try {
            // Ładowanie FXML MainWindow
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/projekt_io/MainWindow.fxml"));
            Parent root = loader.load();

            // Pobranie aktualnego Stage z przycisku
            Stage stage = (Stage) devButton.getScene().getWindow();

            // Ustawienie nowej sceny
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}