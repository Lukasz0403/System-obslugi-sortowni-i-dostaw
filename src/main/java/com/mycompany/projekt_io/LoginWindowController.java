package com.mycompany.projekt_io;

import com.mycompany.projekt_io.feature.login.*;
import com.mycompany.projekt_io.*;
import com.mycompany.projekt_io.core.database.UserDAO;
import com.mycompany.projekt_io.core.database.UserDAOInterface;
import com.mycompany.projekt_io.datamodel.User;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.util.Duration;


public class LoginWindowController implements Initializable {

    @FXML
    private Label dateTimeLabel;

    @FXML
    private Button devButton;
    
    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField usernameField;
    
    @FXML
    private Label errorLabel;

    @FXML
    private Button logInButton;

    private UserDAOInterface userDAO = new UserDAO();
    
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
        
        logInButton.setDefaultButton(true);
        
        Platform.runLater(() -> {
            Stage stage = (Stage) logInButton.getScene().getWindow();
            WindowConstraints.applyMinSize(stage);
        });
    }

    @FXML
    private void handleDevButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/projekt_io/MainWindow.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) devButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private final LoginService loginService = new LoginService();
    
//  Logika przycisku logowania - Łukasz
    @FXML
    void loginAction(ActionEvent event) {
        String login = usernameField.getText();
        String plainPassword = passwordField.getText();

        if (login.isEmpty() || plainPassword.isEmpty()) {
            showError("Wprowadź login i hasło.");
            return;
        }

        User user = loginService.authenticate(login, plainPassword);

        if (user != null) {
            System.out.println("Zalogowano: " + user.getLogin());
            errorLabel.setVisible(false);
            loadMainWindow();
        } else {
            showError("Nieprawidłowy login lub hasło.");
            passwordField.clear();
        }
    }

//  obsługa labela showError - Łukasz
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
    
//  Przełącza widok aplikacji z okna logowania do oknagłównego - Łukasz
    private void loadMainWindow() {
        try { 
            Stage stage = (Stage) devButton.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/projekt_io/MainWindow.fxml"));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}