package com.mycompany.projekt_io;

import com.mycompany.projekt_io.feature.login.*;
import com.mycompany.projekt_io.*;
import com.mycompany.projekt_io.core.database.UserDAO;
import com.mycompany.projekt_io.core.database.UserDAOInterface;
import com.mycompany.projekt_io.datamodel.User;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import org.mindrot.jbcrypt.BCrypt;


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
    
    @FXML
    void loginAction(ActionEvent event) {
        String login = usernameField.getText();
        String plainPassword = passwordField.getText();

        if (login.isEmpty() || plainPassword.isEmpty()) {
            errorLabel.setText("Wprowadź login i hasło.");
            return;
        }

        User user = userDAO.getUser(login);

        if (user == null) {
            errorLabel.setText("Nieprawidłowy login lub hasło.");
            return;
        }

        boolean passwordMatches = false;
        try {
            passwordMatches = BCrypt.checkpw(plainPassword, user.getPassword());
        } catch (IllegalArgumentException e) {
             System.out.println("Blad: Haslo w bazie nie jest hashem Bcrypt!");
             errorLabel.setText("Błąd krytyczny logowania.");
             return;
        }

        if (passwordMatches) {
            System.out.println("Zalogowano pomyslnie uzytkownika: " + user.getLogin());
            
            loadMainWindow();
                       
        } else {
            errorLabel.setText("Nieprawidłowy login lub hasło.");
            passwordField.clear(); 
        }
    }
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