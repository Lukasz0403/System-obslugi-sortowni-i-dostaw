package com.mycompany.projekt_io;

//IMPORTS

import com.mycompany.projekt_io.datamodel.Format;
import com.mycompany.projekt_io.datamodel.Recipient;
import com.mycompany.projekt_io.datamodel.Region;
import com.mycompany.projekt_io.datamodel.Sender;
import com.mycompany.projekt_io.datamodel.Shelf;
import com.mycompany.projekt_io.feature.package_.PackageService;
import com.mycompany.projekt_io.datamodel.Package;
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
import javafx.scene.control.TextFormatter.Change;

//-------

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

    // COMBOBOXES---------------------------------------------------------------
    @FXML
    private ChoiceBox<String> sizeChoiceBox;
    @FXML
    private ChoiceBox<String> sendRegionChoiceBox;
    @FXML
    private ChoiceBox<String> receiveRegionChoiceBox;

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
            "Katowice",
            "Gliwice",
            "Zabrze",
            "Bytom",
            "Chorzów",
            "Ruda Śląska",
            "Tychy",
            "Dąbrowa Górnicza",
            "Sosnowiec"
        );
        
        List<String> cities2 = List.of(
            "--RECIPIENTS REGION--",
            "Katowice",
            "Gliwice",
            "Zabrze",
            "Bytom",
            "Chorzów",
            "Ruda Śląska",
            "Tychy",
            "Dąbrowa Górnicza",
            "Sosnowiec"
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
    }

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

        sizeChoiceBox.setValue("--SIZE--");
        sendRegionChoiceBox.setValue("--SENDERS REGION--");
        receiveRegionChoiceBox.setValue("--RECIPIENTS REGION--");
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
    private void handleAddPackage() {

        PackageService service = new PackageService();

        boolean success = service.addPackageFull(
                sizeChoiceBox.getValue(),
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
            System.out.println("Dodano!");
            handleDeleteButton();
        } else {
            System.out.println("Błąd!");
        }
    }

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
}