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

/**
 * Kontroler okna logowania do systemu magazynowego.
 * <p>
 * Obsługuje formularz logowania użytkownika: pobiera login i hasło,
 * przekazuje je do {@link LoginService} w celu uwierzytelnienia,
 * a po pomyślnym logowaniu przełącza widok na główne okno aplikacji.
 * Kontroler odpowiada również za wyświetlanie aktualnej godziny i daty
 * oraz ustawianie minimalnych wymiarów okna.
 * </p>
 *
 * @author Łukasz
 */
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
    private final LoginService loginService = new LoginService();

    /**
     * Inicjalizuje kontroler po załadowaniu widoku FXML.
     * <p>
     * Uruchamia animację zegara aktualizującą etykietę daty i czasu co sekundę,
     * ustawia przycisk logowania jako domyślny (reagujący na klawisz Enter)
     * oraz stosuje minimalne wymiary okna za pomocą {@link WindowConstraints}.
     * </p>
     *
     * @param url lokalizacja używana do rozwiązywania względnych ścieżek
     *            do obiektu głównego lub {@code null} jeśli nieznana
     * @param rb  zasoby używane do lokalizacji obiektu głównego
     *            lub {@code null} jeśli nieznane
     */
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

    /**
     * Obsługuje przycisk developerski umożliwiający pominięcie logowania.
     * <p>
     * Ładuje bezpośrednio główne okno aplikacji ({@code MainWindow.fxml})
     * bez weryfikacji danych uwierzytelniających. Przeznaczony wyłącznie
     * do użytku podczas prac developerskich.
     * </p>
     *
     * @param event zdarzenie akcji wywołane kliknięciem przycisku
     */
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

    /**
     * Obsługuje zdarzenie kliknięcia przycisku logowania.
     * <p>
     * Pobiera login i hasło z pól formularza, a następnie:
     * </p>
     * <ol>
     *   <li>Sprawdza, czy oba pola są wypełnione — jeśli nie, wyświetla komunikat błędu</li>
     *   <li>Przekazuje dane do {@link LoginService#authenticate(String, String)}</li>
     *   <li>Przy pomyślnym uwierzytelnieniu ukrywa etykietę błędu i wywołuje {@link #loadMainWindow()}</li>
     *   <li>Przy niepowodzeniu wyświetla komunikat błędu i czyści pole hasła</li>
     * </ol>
     *
     * @param event zdarzenie akcji wywołane kliknięciem przycisku lub naciśnięciem Enter
     */
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

    /**
     * Wyświetla komunikat błędu w etykiecie formularza logowania.
     *
     * @param message treść komunikatu błędu do wyświetlenia użytkownikowi
     */
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    /**
     * Przełącza widok aplikacji z okna logowania na okno główne.
     * <p>
     * Ładuje plik {@code MainWindow.fxml} i zastępuje nim bieżącą scenę
     * w tym samym oknie aplikacji.
     * </p>
     */
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