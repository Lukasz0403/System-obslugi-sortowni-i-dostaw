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
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.IOException;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author mateu
 */
public class PackageTableWindowController implements Initializable {

    // TIME / DATE
    @FXML
    private Label timeLabel;

    @FXML
    private Label dateLabel;

    // SIDEBAR BUTTONS
    @FXML
    private Button magButton;

    @FXML
    private Button pacButton;

    @FXML
    private Button addButton;

    // TABLE
    @FXML
    private TableView<PackageItem> packageTable;

    @FXML
    private TableColumn<PackageItem, Integer> idColumn;

    @FXML
    private TableColumn<PackageItem, String> shelfColumn;

    @FXML
    private TableColumn<PackageItem, String> serialColumn;

    @FXML
    private TableColumn<PackageItem, String> sizeColumn;

    @FXML
    private TableColumn<PackageItem, String> regionColumn;

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

        // Inicjalizacja kolumn tabeli
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        shelfColumn.setCellValueFactory(new PropertyValueFactory<>("shelf"));
        serialColumn.setCellValueFactory(new PropertyValueFactory<>("serialNumber"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
        regionColumn.setCellValueFactory(new PropertyValueFactory<>("region"));

        // przykładowe dane
        packageTable.getItems().addAll(
            new PackageItem(1, "A1", "123456", "M", "PL"),
            new PackageItem(2, "B2", "987654", "S", "DE")
        );
    }

    // SIDEBAR BUTTON HANDLERS
    @FXML
    private void handleMagButton() {
        loadWindow("/com/mycompany/projekt_io/werehouseMainWindow.fxml");
    }

    @FXML
    private void handlePacButton() {
        loadWindow("/com/mycompany/projekt_io/packageTableWindow.fxml");
    }

    @FXML
    private void handleAddButton() {
        loadWindow("/com/mycompany/projekt_io/packageAddWindow.fxml");
    }

    // Metoda pomocnicza do ładowania nowego FXML
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

    // Klasa pomocnicza do reprezentacji wiersza tabeli
    public static class PackageItem {
        private final Integer id;
        private final String shelf;
        private final String serialNumber;
        private final String size;
        private final String region;

        public PackageItem(Integer id, String shelf, String serialNumber, String size, String region) {
            this.id = id;
            this.shelf = shelf;
            this.serialNumber = serialNumber;
            this.size = size;
            this.region = region;
        }

        public Integer getId() { return id; }
        public String getShelf() { return shelf; }
        public String getSerialNumber() { return serialNumber; }
        public String getSize() { return size; }
        public String getRegion() { return region; }
    }
}