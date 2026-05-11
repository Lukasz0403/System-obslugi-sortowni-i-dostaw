package com.mycompany.projekt_io;

import com.mycompany.projekt_io.core.database.PackageDAO;
import com.mycompany.projekt_io.datamodel.Format;
import com.mycompany.projekt_io.datamodel.Recipient;
import com.mycompany.projekt_io.datamodel.Region;
import com.mycompany.projekt_io.datamodel.Sender;
import com.mycompany.projekt_io.datamodel.Rack;
import com.mycompany.projekt_io.feature.package_.PackageService;
import com.mycompany.projekt_io.datamodel.Package;
import com.mycompany.projekt_io.feature.login.AppCloser;
import com.mycompany.projekt_io.feature.login.AppSession;
import com.mycompany.projekt_io.feature.package_.PackageServiceInterface;
import com.mycompany.projekt_io.feature.package_.SenderTemplate;
import com.mycompany.projekt_io.feature.package_.TemplateService;
import com.mycompany.projekt_io.feature.package_.TemplateServiceInterface;
import com.mycompany.projekt_io.feature.warehouse.SortingService;
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
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.IOException;
import javafx.scene.control.ChoiceBox;
import java.util.List;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import java.util.function.UnaryOperator;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.text.Text;

/**
 * Kontroler okna dodawania nowej paczki do systemu magazynowego.
 * <p>
 * Obsługuje formularz wprowadzania danych paczki: wymiarów, danych nadawcy
 * i odbiorcy oraz regionów nadania i odbioru. Kontroler odpowiada za:
 * </p>
 * <ul>
 *   <li>Inicjalizację i walidację pól formularza (filtry tekstowe, wybór regionów)</li>
 *   <li>Wyświetlanie aktualnej godziny i daty w pasku bocznym</li>
 *   <li>Nawigację pomiędzy widokami aplikacji</li>
 *   <li>Przekazanie zebranych danych do {@link PackageService} i zapis w bazie</li>
 * </ul>
 *
 * @author Mateusz Gojny
 */
public class PackageAddWindowController implements Initializable {

    @FXML private Label timeLabel;
    @FXML private Label dateLabel;
    @FXML private Button homeButton, logOut, closeApp;
    @FXML private Button magButton;
    @FXML private Button pacButton;
    @FXML private Button addButton;
    @FXML private Button addPacButton;
    @FXML private Button saveTempButton;
    @FXML private Button deleteButton;
    @FXML private TextField weightField;
    @FXML private TextField heightField;
    @FXML private TextField widthField;
    @FXML private TextField depthField;
    @FXML private TextField senderNameField;
    @FXML private TextField senderStreetField;
    @FXML private TextField senderPostcodeField;
    @FXML private TextField senderEmailField;
    @FXML private TextField senderNumberField;
    @FXML private TextField recipientNameField;
    @FXML private TextField recipientStreetField;
    @FXML private TextField recipientPostcodeField;
    @FXML private TextField recipientEmailField;
    @FXML private TextField recipientNumberField;
    @FXML private Text codeText;
    @FXML private Button loadTempButton;
    @FXML private Text mailText;
    @FXML private Text nameText;
    @FXML private Text numberText;
    @FXML private Text streetText;
    @FXML private Text cityText;
    @FXML private ChoiceBox<String> sendRegionChoiceBox;
    @FXML private ChoiceBox<String> receiveRegionChoiceBox;

    private final TemplateServiceInterface templateService = new TemplateService();

    /**
     * Inicjalizuje kontroler po załadowaniu widoku FXML.
     *
     * @param url lokalizacja używana do rozwiązywania względnych ścieżek
     *            do obiektu głównego lub {@code null} jeśli nieznana
     * @param rb  zasoby używane do lokalizacji obiektu głównego
     *            lub {@code null} jeśli nieznane
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

        List<String> cities = List.of(
            "--SENDERS REGION--",
            "Białystok", "Bydgoszcz", "Częstochowa", "Gdańsk", "Gdynia",
            "Katowice", "Kielce", "Kraków", "Łódź", "Lublin", "Olsztyn",
            "Opole", "Poznań", "Rzeszów", "Sopot", "Szczecin", "Toruń",
            "Warszawa", "Wrocław", "Zielona Góra"
        );

        List<String> cities2 = List.of(
            "--RECIPIENTS REGION--",
            "Białystok", "Bydgoszcz", "Częstochowa", "Gdańsk", "Gdynia",
            "Katowice", "Kielce", "Kraków", "Łódź", "Lublin", "Olsztyn",
            "Opole", "Poznań", "Rzeszów", "Sopot", "Szczecin", "Toruń",
            "Warszawa", "Wrocław", "Zielona Góra"
        );

        sendRegionChoiceBox.getItems().addAll(cities);
        sendRegionChoiceBox.setValue("--SENDERS REGION--");
        receiveRegionChoiceBox.getItems().addAll(cities2);
        receiveRegionChoiceBox.setValue("--RECIPIENTS REGION--");

        UnaryOperator<Change> postcodeFilterAutoDash = change -> {
            String digitsOnly = change.getControlNewText().replaceAll("[^\\d]", "");
            if (digitsOnly.length() > 5) return null;
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < digitsOnly.length(); i++) {
                if (i == 2) result.append('-');
                result.append(digitsOnly.charAt(i));
            }
            change.setText(result.toString());
            change.setRange(0, change.getControlText().length());
            change.selectRange(result.length(), result.length());
            return change;
        };
        senderPostcodeField.setTextFormatter(new TextFormatter<>(postcodeFilterAutoDash));
        recipientPostcodeField.setTextFormatter(new TextFormatter<>(postcodeFilterAutoDash));

        UnaryOperator<Change> numberFilter = change -> {
            if (change.getControlNewText().matches("\\d{0,9}")) return change;
            return null;
        };
        senderNumberField.setTextFormatter(new TextFormatter<>(numberFilter));
        recipientNumberField.setTextFormatter(new TextFormatter<>(numberFilter));

        UnaryOperator<Change> doubleFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.isEmpty()) return change;
            if (newText.matches("\\d*([\\.]\\d*)?")) return change;
            return null;
        };
        weightField.setTextFormatter(new TextFormatter<>(doubleFilter));
        heightField.setTextFormatter(new TextFormatter<>(doubleFilter));
        widthField.setTextFormatter(new TextFormatter<>(doubleFilter));
        depthField.setTextFormatter(new TextFormatter<>(doubleFilter));

        if (templateService.isTemplateSaved()) {
            updateTemplateDisplay(templateService.loadTemplate());
        }

        logOut.setOnAction(e -> {
            AppSession.logout();
            loadWindow("/com/mycompany/projekt_io/loginWindow.fxml");
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
     * Wczytuje zapisany szablon nadawcy i wypełnia nim pola formularza.
     *
     * @param event zdarzenie akcji przycisku "Load Template"
     */
    @FXML
    void loadTemplate(ActionEvent event) {
        if (!templateService.isTemplateSaved()) {
            showAlert(Alert.AlertType.WARNING, "No Template", "No template has been saved yet.");
            return;
        }
        SenderTemplate template = templateService.loadTemplate();
        senderNameField.setText(template.getName());
        senderStreetField.setText(template.getStreet());
        senderPostcodeField.setText(template.getPostcode());
        senderEmailField.setText(template.getEmail());
        senderNumberField.setText(template.getPhone());
        if (!template.getCity().isEmpty()) {
            sendRegionChoiceBox.setValue(template.getCity());
        }
    }

    /**
     * Zapisuje dane nadawcy z formularza jako szablon wielokrotnego użytku.
     *
     * @param event zdarzenie akcji przycisku "Save Template"
     */
    @FXML
    void saveTemplate(ActionEvent event) {
        if (senderNameField.getText().isEmpty()
                || senderStreetField.getText().isEmpty()
                || senderPostcodeField.getText().isEmpty()
                || senderEmailField.getText().isEmpty()
                || senderNumberField.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Error", "Please fill in all sender fields before saving the template.");
            return;
        }
        SenderTemplate template = new SenderTemplate(
                senderNameField.getText(),
                senderStreetField.getText(),
                senderPostcodeField.getText(),
                senderEmailField.getText(),
                senderNumberField.getText(),
                sendRegionChoiceBox.getValue().equals("--SENDERS REGION--") ? "" : sendRegionChoiceBox.getValue()
        );
        templateService.saveTemplate(template);
        updateTemplateDisplay(template);
        showAlert(Alert.AlertType.INFORMATION, "Success", "Sender template has been saved.");
    }

    /**
     * Usuwa zapisany szablon nadawcy i resetuje panel podglądu do stanu domyślnego.
     *
     * @param event zdarzenie akcji przycisku "Clear Template"
     */
    @FXML
    void clearTemplate(ActionEvent event) {
        templateService.clearTemplate();
        streetText.setText("None");
        cityText.setText("None");
        codeText.setText("None");
        numberText.setText("None");
        mailText.setText("None");
        nameText.setText("None");
    }

    /**
     * Czyści wszystkie pola formularza i przywraca domyślne wartości list rozwijanych.
     */
    @FXML
    private void handleDeleteButton() {
        weightField.clear();
        heightField.clear();
        widthField.clear();
        depthField.clear();
        senderNameField.clear();
        senderStreetField.clear();
        senderPostcodeField.clear();
        senderEmailField.clear();
        senderNumberField.clear();
        recipientNameField.clear();
        recipientStreetField.clear();
        recipientPostcodeField.clear();
        recipientEmailField.clear();
        recipientNumberField.clear();
        sendRegionChoiceBox.setValue("--SENDERS REGION--");
        receiveRegionChoiceBox.setValue("--RECIPIENTS REGION--");
    }

    /** Przechodzi do okna głównego. */
    @FXML
    private void handleHomeButton() {
        loadWindow("/com/mycompany/projekt_io/mainWindow.fxml");
    }

    /** Przechodzi do okna głównego magazynu. */
    @FXML
    private void handleMagButton() {
        loadWindow("/com/mycompany/projekt_io/werehouseMainWindow.fxml");
    }

    /** Przechodzi do okna tabeli paczek. */
    @FXML
    private void handlePacButton() {
        loadWindow("/com/mycompany/projekt_io/packageTableWindow.fxml");
    }

    /** Przechodzi do okna zarządzania użytkownikami. */
    @FXML
    private void handleUserButton() {
        loadWindow("/com/mycompany/projekt_io/userManageWindow.fxml");
    }

    /** Wraca do okna tabeli paczek. */
    @FXML
    private void handleRetButton() {
        loadWindow("/com/mycompany/projekt_io/packageTableWindow.fxml");
    }

    /**
     * Obsługuje zdarzenie dodania nowej paczki do systemu.
     */
    @FXML
    private void handleAddPackage() {
        if (sendRegionChoiceBox.getValue().equals("--SENDERS REGION--")
                || receiveRegionChoiceBox.getValue().equals("--RECIPIENTS REGION--")) {
            showAlert(Alert.AlertType.WARNING, "Error", "Please select sender and recipient regions.");
            return;
        }

        if (weightField.getText().isEmpty()
                || widthField.getText().isEmpty()
                || heightField.getText().isEmpty()
                || depthField.getText().isEmpty()
                || senderNameField.getText().isEmpty()
                || senderStreetField.getText().isEmpty()
                || senderPostcodeField.getText().isEmpty()
                || senderEmailField.getText().isEmpty()
                || senderNumberField.getText().isEmpty()
                || recipientNameField.getText().isEmpty()
                || recipientStreetField.getText().isEmpty()
                || recipientPostcodeField.getText().isEmpty()
                || recipientEmailField.getText().isEmpty()
                || recipientNumberField.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Error", "Please fill in all required fields.");
            return;
        }

        PackageServiceInterface service = new PackageService();

        boolean success = service.addPackageFull(
                "",
                sendRegionChoiceBox.getValue(),
                receiveRegionChoiceBox.getValue(),
                Double.parseDouble(weightField.getText()),
                Double.parseDouble(widthField.getText()),
                Double.parseDouble(heightField.getText()),
                Double.parseDouble(depthField.getText()),
                senderNameField.getText(),
                "",
                senderStreetField.getText(),
                senderPostcodeField.getText(),
                senderEmailField.getText(),
                senderNumberField.getText(),
                recipientNameField.getText(),
                "",
                recipientStreetField.getText(),
                recipientPostcodeField.getText(),
                recipientEmailField.getText(),
                recipientNumberField.getText()
        );

        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Package has been added successfully.");
            handleDeleteButton();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to add package. Please check your data or database connection.");
        }

        PackageDAO dao = new PackageDAO();
        SortingService algorithm = new SortingService(dao);
        algorithm.assignShelvesToPackages();
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
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Wyświetla modalne okno dialogowe z komunikatem dla użytkownika.
     *
     * @param type    typ alertu
     * @param title   tytuł okna dialogowego
     * @param content treść komunikatu
     */
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.initOwner(timeLabel.getScene().getWindow());
        alert.showAndWait();
    }

    /**
     * Aktualizuje pola tekstowe w panelu podglądu szablonu.
     *
     * @param template szablon którego dane mają być wyświetlone w panelu
     */
    private void updateTemplateDisplay(SenderTemplate template) {
        nameText.setText(template.getName());
        streetText.setText(template.getStreet());
        codeText.setText(template.getPostcode());
        mailText.setText(template.getEmail());
        numberText.setText(template.getPhone());
        cityText.setText(template.getCity());
    }
}