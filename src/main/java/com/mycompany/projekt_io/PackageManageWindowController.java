package com.mycompany.projekt_io;

import com.mycompany.projekt_io.core.database.PackageDAO;
import com.mycompany.projekt_io.datamodel.Format;
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
import java.time.format.DateTimeFormatter;
import java.io.IOException;
import java.util.List;
import java.util.function.UnaryOperator;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.ChoiceBox;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import com.mycompany.projekt_io.datamodel.Package;
import com.mycompany.projekt_io.datamodel.Region;
import com.mycompany.projekt_io.datamodel.Rack;
import com.mycompany.projekt_io.feature.package_.PackageService;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * FXML Controller class
 *
 * @author mateu
 */
public class PackageManageWindowController implements Initializable {
    
    @FXML
    private Button addButton;

    @FXML
    private Label dateLabel;

    @FXML
    private Button deleteButton;

    @FXML
    private TextField depthField;

    @FXML
    private TextField heightField;

    @FXML
    private Button magButton;

    @FXML
    private Button pacButton;

    @FXML
    private ChoiceBox<String> receiveRegionChoiceBox;

    @FXML
    private TextField recipientEmailField;

    @FXML
    private TextField recipientNameField;

    @FXML
    private TextField recipientNumberField;

    @FXML
    private TextField recipientPostcodeField;

    @FXML
    private TextField recipientStreetField;

    @FXML
    private Button returnButton;

    @FXML
    private Button saveButton;

    @FXML
    private ChoiceBox<String> sendRegionChoiceBox;

    @FXML
    private TextField senderEmailField;

    @FXML
    private TextField senderNameField;

    @FXML
    private TextField senderNumberField;

    @FXML
    private TextField senderPostcodeField;

    @FXML
    private TextField senderStreetField;

    @FXML
    private ChoiceBox<String> sizeChoiceBox;

    @FXML
    private Label timeLabel;

    @FXML
    private TextField weightField;

    @FXML
    private TextField widthField;
    
    @FXML
    private Button labelButton;
    
    private Package currentPackage;
    
    private final PackageService service = new PackageService();

   

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

        // SIZE
        sizeChoiceBox.getItems().addAll("--SIZE--","A", "B", "C");
        sizeChoiceBox.setValue("--SIZE--");

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
        UnaryOperator<TextFormatter.Change> postcodeFilterAutoDash = change -> {
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
        UnaryOperator<TextFormatter.Change> numberFilter = change -> {
            String text = change.getControlNewText();
            if (text.matches("\\d{0,9}")) {
                return change;
            }
            return null;
        };
        senderNumberField.setTextFormatter(new TextFormatter<>(numberFilter));
        recipientNumberField.setTextFormatter(new TextFormatter<>(numberFilter));

        // NUMBER FILTER
        UnaryOperator<TextFormatter.Change> doubleFilter = change -> {
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
    
    @FXML
    private void handleRetButton() {
        loadWindow("/com/mycompany/projekt_io/packageTableWindow.fxml");
    }
    
        @FXML
    void generateLabel(ActionEvent event) {

    }
    
    @FXML
    void removePackage(ActionEvent event) {

        if (currentPackage == null) {
            showAlert(Alert.AlertType.WARNING, "Błąd", "Brak paczki do usunięcia.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Usuń paczkę");
        confirm.setHeaderText("Czy na pewno chcesz usunąć paczkę?");
        confirm.setContentText("Operacja jest nieodwracalna.");
        confirm.initOwner(timeLabel.getScene().getWindow());

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                boolean success = service.deletePackage(currentPackage.getPackage_id());
                if (success) {
                    loadWindow("/com/mycompany/projekt_io/packageTableWindow.fxml");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Błąd", "Nie udało się usunąć paczki.");
                }
            }
        });
    }
    
    @FXML
    void saveChanges(ActionEvent event) {

        if (sizeChoiceBox.getValue().equals("--SIZE--")
                || sendRegionChoiceBox.getValue().equals("--SENDERS REGION--")
                || receiveRegionChoiceBox.getValue().equals("--RECIPIENTS REGION--")) {
            showAlert(Alert.AlertType.WARNING, "Błąd", "Wybierz gabaryt oraz regiony nadania i odbioru.");
            return;
        }

        Rack shelf = currentPackage.getPackage_rack();

        boolean success = service.updatePackageFull(
                currentPackage.getPackage_id(),
                sizeChoiceBox.getValue(),
                sendRegionChoiceBox.getValue(),
                receiveRegionChoiceBox.getValue(),
                Double.parseDouble(weightField.getText()),
                Double.parseDouble(widthField.getText()),
                Double.parseDouble(heightField.getText()),
                Double.parseDouble(depthField.getText()),
                currentPackage.getPackage_sender().getSender_id(),
                senderNameField.getText(),
                currentPackage.getPackage_sender().getSender_city(),
                senderStreetField.getText(),
                senderPostcodeField.getText(),
                senderEmailField.getText(),
                senderNumberField.getText(),
                currentPackage.getPackage_recipient().getRecipient_id(),
                recipientNameField.getText(),
                currentPackage.getPackage_recipient().getRecipient_city(),
                recipientStreetField.getText(),
                recipientPostcodeField.getText(),
                recipientEmailField.getText(),
                recipientNumberField.getText(),
                shelf
        );

        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Sukces", "Zmiany zostały zapisane.");
            loadWindow("/com/mycompany/projekt_io/packageTableWindow.fxml");
        } else {
            showAlert(Alert.AlertType.ERROR, "Błąd", "Nie udało się zapisać zmian.");
        }
    }
    
    @FXML
    void handleDeleteButton(ActionEvent event) {
        
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

        sizeChoiceBox.setValue("--SIZE--");
        sendRegionChoiceBox.setValue("--SENDERS REGION--");
        receiveRegionChoiceBox.setValue("--RECIPIENTS REGION--");

    }

    // Metoda pomocnicza do ładowania nowego FXML
    private void loadWindow(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            // Pobranie aktualnego Stage z jednego z labeli
            Stage stage = (Stage) timeLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }   
    
    public void setPackage(Package p) {
    this.currentPackage = p;
    fillForm();
    }
    
    private void fillForm() {

        senderNameField.setText(currentPackage.getPackage_sender().getSender_name());
        senderStreetField.setText(currentPackage.getPackage_sender().getSender_street());
        senderPostcodeField.setText(currentPackage.getPackage_sender().getSender_postcode());
        senderEmailField.setText(currentPackage.getPackage_sender().getSender_email());
        senderNumberField.setText(currentPackage.getPackage_sender().getSender_phone());
        

        recipientNameField.setText(currentPackage.getPackage_recipient().getRecipient_name());
        recipientStreetField.setText(currentPackage.getPackage_recipient().getRecipient_street());
        recipientPostcodeField.setText(currentPackage.getPackage_recipient().getRecipient_postcode());
        recipientEmailField.setText(currentPackage.getPackage_recipient().getRecipient_email());
        recipientNumberField.setText(currentPackage.getPackage_recipient().getRecipient_phone());

        String sendRegion = mapCodeToRegionName(currentPackage.getPackage_region().getRegion_name());
        String receiveRegion = mapCodeToRegionName(currentPackage.getPackage_dest_region().getRegion_name());
        
        
        
        sizeChoiceBox.setValue(currentPackage.getPackage_format().getFormat_id());

        sendRegionChoiceBox.setValue(sendRegion);
        receiveRegionChoiceBox.setValue(receiveRegion);

        weightField.setText(String.valueOf(currentPackage.getWeight()));
        widthField.setText(String.valueOf(currentPackage.getWidth()));
        heightField.setText(String.valueOf(currentPackage.getHeight()));
        depthField.setText(String.valueOf(currentPackage.getDepth()));
    }
    
    
    private String mapCodeToRegionName(String code) {
        switch (code) {
            case "BIA":
                return "Białystok";
            case "BYD":
                return "Bydgoszcz";
            case "CZE":
                return "Częstochowa";
            case "GDA":
                return "Gdańsk";
            case "GDY":
                return "Gdynia";
            case "KAT":
                return "Katowice";
            case "KIE":
                return "Kielce";
            case "KRK":
                return "Kraków";
            case "LOD":
                return "Łódź";
            case "LUB":
                return "Lublin";
            case "OLS":
                return "Olsztyn";
            case "OPL":
                return "Opole";
            case "POZ":
                return "Poznań";
            case "RZE":
                return "Rzeszów";
            case "SOP":
                return "Sopot";
            case "SZC":
                return "Szczecin";
            case "TOR":
                return "Toruń";
            case "WAW":
                return "Warszawa";
            case "WRO":
                return "Wrocław";
            case "ZIE":
                return "Zielona Góra";
            default:
                return "--SENDERS REGION--";
        }
    }
    
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.initOwner(timeLabel.getScene().getWindow());
        alert.showAndWait();
    }
  
    
    
}
