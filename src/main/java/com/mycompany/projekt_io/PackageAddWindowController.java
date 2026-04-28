package com.mycompany.projekt_io;

//IMPORTS

import com.mycompany.projekt_io.core.database.PackageDAO;
import com.mycompany.projekt_io.datamodel.Format;
import com.mycompany.projekt_io.datamodel.Recipient;
import com.mycompany.projekt_io.datamodel.Region;
import com.mycompany.projekt_io.datamodel.Sender;
import com.mycompany.projekt_io.datamodel.Rack;
import com.mycompany.projekt_io.feature.package_.PackageService;
import com.mycompany.projekt_io.datamodel.Package;
import com.mycompany.projekt_io.feature.package_.PackageServiceInterface;
import com.mycompany.projekt_io.feature.package_.SenderTemplate;
import com.mycompany.projekt_io.feature.package_.TemplateService;
import com.mycompany.projekt_io.feature.werehouse.SortingService;
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


//-------

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

    //SIDEBAR-------------------------------------------------------------------
    @FXML
    private Label timeLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Button magButton;
    @FXML
    private Button pacButton;
    @FXML
    private Button addButton;
    
    //FORM BUTTONS--------------------------------------------------------------
    @FXML
    private Button addPacButton;
    @FXML
    private Button saveTempButton;
    @FXML
    private Button deleteButton;

    // TEXTFIELDS---------------------------------------------------------------
    @FXML
    private TextField weightField;
    @FXML
    private TextField heightField;
    @FXML
    private TextField widthField;
    @FXML
    private TextField depthField;

    @FXML
    private TextField senderNameField;
    @FXML
    private TextField senderStreetField;
    @FXML
    private TextField senderPostcodeField;
    @FXML
    private TextField senderEmailField;
    @FXML
    private TextField senderNumberField;

    @FXML
    private TextField recipientNameField;
    @FXML
    private TextField recipientStreetField;
    @FXML
    private TextField recipientPostcodeField;
    @FXML
    private TextField recipientEmailField;
    @FXML
    private TextField recipientNumberField;
    @FXML
    private Text codeText;
    @FXML
    private Button loadTempButton;
    @FXML
    private Text mailText;
    @FXML
    private Text nameText;
    @FXML
    private Text numberText;
    @FXML
    private Text streetText;
    @FXML
    private Text cityText;
    
    private final TemplateService templateService = new TemplateService();

    // COMBOBOXES---------------------------------------------------------------
    @FXML
    private ChoiceBox<String> sendRegionChoiceBox;
    @FXML
    private ChoiceBox<String> receiveRegionChoiceBox;

    /**
     * Inicjalizuje kontroler po załadowaniu widoku FXML.
     * <p>
     * Wykonywane operacje:
     * </p>
     * <ul>
     *   <li>Uruchomienie animacji zegara aktualizującego etykiety czasu i daty co sekundę</li>
     *   <li>Ustawienie minimalnych wymiarów okna za pomocą {@link WindowConstraints}</li>
     *   <li>Wypełnienie list rozwijanych regionów nadania i odbioru</li>
     *   <li>Ustawienie filtrów tekstowych na polach kodu pocztowego (format XX-XXX
     *       z automatycznym wstawianiem myślnika), numeru telefonu (max 9 cyfr)
     *       oraz wymiarów paczki (liczby dziesiętne)</li>
     * </ul>
     *
     * @param url            lokalizacja używana do rozwiązywania względnych ścieżek
     *                       do obiektu głównego lub {@code null} jeśli nieznana
     * @param rb             zasoby używane do lokalizacji obiektu głównego
     *                       lub {@code null} jeśli nieznane
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    // TIME AND DATE--------------------------------------------------------
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

    // WINDOW CONSTRAINTS ---------------------------------------------------
        Platform.runLater(() -> {
            Stage stage = (Stage) timeLabel.getScene().getWindow();
            WindowConstraints.applyMinSize(stage);
        });

    // CHOICEBOXES INIT -------------------------------------------------------

        // CITY LIST
        List<String> cities = List.of(
            "--SENDERS REGION--",
                "Białystok",
                "Bydgoszcz",
                "Częstochowa",
                "Gdańsk",
                "Gdynia",
                "Katowice",
                "Kielce",
                "Kraków",
                "Łódź",
                "Lublin",
                "Olsztyn",
                "Opole",
                "Poznań",
                "Rzeszów",
                "Sopot",
                "Szczecin",
                "Toruń",
                "Warszawa",
                "Wrocław",
                "Zielona Góra"
        );
        
        List<String> cities2 = List.of(
            "--RECIPIENTS REGION--",
                "Białystok",
                "Bydgoszcz",
                "Częstochowa",
                "Gdańsk",
                "Gdynia",
                "Katowice",
                "Kielce",
                "Kraków",
                "Łódź",
                "Lublin",
                "Olsztyn",
                "Opole",
                "Poznań",
                "Rzeszów",
                "Sopot",
                "Szczecin",
                "Toruń",
                "Warszawa",
                "Wrocław",
                "Zielona Góra"
        );

        sendRegionChoiceBox.getItems().addAll(cities);
        sendRegionChoiceBox.setValue("--SENDERS REGION--");

        receiveRegionChoiceBox.getItems().addAll(cities2);
        receiveRegionChoiceBox.setValue("--RECIPIENTS REGION--");
        
    // FILTERS ----------------------------------------------------------------
        
        // POSTCODE FILTER (XX-XXX) WITH AUTOMATIC DASH
        UnaryOperator<Change> postcodeFilterAutoDash = change -> {
            String text = change.getControlNewText();

            String digitsOnly = text.replaceAll("[^\\d]", "");

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
        
        // PHONE NUMBER FILTER
        UnaryOperator<Change> numberFilter = change -> {
            String text = change.getControlNewText();
            if (text.matches("\\d{0,9}")) {
                return change;
            }
            return null;
        };
        senderNumberField.setTextFormatter(new TextFormatter<>(numberFilter));
        recipientNumberField.setTextFormatter(new TextFormatter<>(numberFilter));

        // NUMBER FILTER
        UnaryOperator<Change> doubleFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.isEmpty()) return change;
            if (newText.matches("\\d*([\\.]\\d*)?")) {
                return change;
            }
            return null;
        };

        weightField.setTextFormatter(new TextFormatter<>(doubleFilter));
        heightField.setTextFormatter(new TextFormatter<>(doubleFilter));
        widthField.setTextFormatter(new TextFormatter<>(doubleFilter));
        depthField.setTextFormatter(new TextFormatter<>(doubleFilter));
        
        //---------------------------------------------------------------------
        
        if (templateService.isTemplateSaved()) {
            updateTemplateDisplay(templateService.loadTemplate());
        }
    }

    /**
     * Wczytuje zapisany szablon nadawcy i wypełnia nim pola formularza.
     * <p>
     * Jeśli żaden szablon nie został wcześniej zapisany, wyświetlane jest
     * ostrzeżenie i operacja jest przerywana. W przeciwnym razie dane nadawcy z
     * szablonu są wpisywane do odpowiednich pól formularza. Jeśli szablon
     * zawiera zapisane miasto, ustawiana jest również odpowiednia wartość w
     * liście wyboru regionu nadania.
     * </p>
     *
     * @param event zdarzenie akcji przycisku "Load Template"
     */
    @FXML
    void loadTemplate(ActionEvent event) {
        if (!templateService.isTemplateSaved()) {
            showAlert(Alert.AlertType.WARNING, "Brak szablonu", "Nie zapisano jeszcze żadnego szablonu.");
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
     * <p>
     * Przed zapisem sprawdzana jest kompletność danych — wszystkie pola nadawcy
     * muszą być wypełnione. Jeśli którekolwiek pole jest puste, wyświetlane
     * jest ostrzeżenie i operacja jest przerywana. Po pomyślnym zapisie panel
     * podglądu szablonu jest aktualizowany za pomocą
     * {@link #updateTemplateDisplay(SenderTemplate)}. Dane są przechowywane
     * przy użyciu {@link TemplateService} w Java Preferences API i dostępne po
     * ponownym uruchomieniu aplikacji.
     * </p>
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
            showAlert(Alert.AlertType.WARNING, "Błąd", "Uzupełnij wszystkie dane nadawcy przed zapisem szablonu.");
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
        showAlert(Alert.AlertType.INFORMATION, "Sukces", "Szablon nadawcy został zapisany.");
    }

    /**
     * Usuwa zapisany szablon nadawcy i resetuje panel podglądu do stanu
     * domyślnego.
     * <p>
     * Wywołuje {@link TemplateService#clearTemplate()} kasując wszystkie dane
     * szablonu z Java Preferences API. Następnie wszystkie pola panelu podglądu
     * ustawiane są na wartość {@code "None"}, sygnalizując brak aktywnego
     * szablonu.
     * </p>
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
     * <p>
     * Wywoływana przez przycisk „Usuń" lub automatycznie po pomyślnym dodaniu
     * paczki. Resetuje pola wymiarów, dane nadawcy, dane odbiorcy oraz wybrane
     * regiony do stanu początkowego.
     * </p>
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
    
    /**
     * Przechodzi do widoku głównego magazynu ({@code werehouseMainWindow.fxml}).
     */
    @FXML
    private void handleMagButton() {
        loadWindow("/com/mycompany/projekt_io/werehouseMainWindow.fxml");
    }

    /**
     * Przechodzi do widoku tabeli paczek ({@code packageTableWindow.fxml}).
     */
    @FXML
    private void handlePacButton() {
        loadWindow("/com/mycompany/projekt_io/packageTableWindow.fxml");
    }

    /**
     * Przechodzi do widoku zarządzania użytkownikami ({@code userManageWindow.fxml}).
     */
    @FXML
    private void handleUserButton() {
        loadWindow("/com/mycompany/projekt_io/userManageWindow.fxml");
    }

    /**
     * Przechodzi do widoku tabeli paczek ({@code packageTableWindow.fxml}).
     * Działa identycznie jak {@link #handlePacButton()}.
     */
    @FXML
    private void handleRetButton() {
        loadWindow("/com/mycompany/projekt_io/packageTableWindow.fxml");
    }
    
    /**
     * Obsługuje zdarzenie dodania nowej paczki do systemu.
     * <p>
     * Metoda wykonuje kolejno:
     * </p>
     * <ol>
     *   <li>Walidację wyboru regionów nadania i odbioru</li>
     *   <li>Walidację kompletności wszystkich pól formularza</li>
     *   <li>Wywołanie {@link PackageService#addPackageFull} z zebranymi danymi</li>
     *   <li>Wyświetlenie komunikatu o powodzeniu lub błędzie operacji</li>
     *   <li>Wyczyszczenie formularza po pomyślnym dodaniu paczki</li>
     * </ol>
     * <p>
     * W przypadku niepoprawnych danych wyświetlane jest ostrzeżenie i operacja
     * jest przerywana bez zapisu do bazy.
     * </p>
     */
    @FXML
    private void handleAddPackage() {

        if (sendRegionChoiceBox.getValue().equals("--SENDERS REGION--")
                || receiveRegionChoiceBox.getValue().equals("--RECIPIENTS REGION--")) {
            showAlert(Alert.AlertType.WARNING, "Błąd", "Wybierz region nadania i odbioru.");
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
            showAlert(Alert.AlertType.WARNING, "Błąd", "Uzupełnij wszystkie dane.");
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
            showAlert(Alert.AlertType.INFORMATION, "Sukces", "Paczka została dodana.");
            handleDeleteButton();
        } else {
            showAlert(Alert.AlertType.ERROR, "Błąd", "Nie udało się dodać paczki. Sprawdź wymiary lub połączenie z bazą.");
        }
        
        PackageDAO dao = new PackageDAO();
        SortingService algorithm = new SortingService(dao);
        algorithm.assignShelvesToPackages();
    }

    /**
     * Ładuje i wyświetla nowy widok JavaFX w tym samym oknie aplikacji.
     * <p>
     * Pobiera plik FXML z podanej ścieżki zasobów, tworzy nową scenę
     * i zastępuje nią bieżącą zawartość okna.
     * </p>
     *
     * @param fxmlPath ścieżka do pliku FXML względem classpath aplikacji
     *                 (np. {@code "/com/mycompany/projekt_io/packageTableWindow.fxml"})
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
     * @param type    typ alertu określający ikonę i styl okna
     *                (np. {@link Alert.AlertType#WARNING}, {@link Alert.AlertType#INFORMATION})
     * @param title   tytuł wyświetlany na pasku tytułu okna dialogowego
     * @param content treść komunikatu wyświetlana użytkownikowi
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