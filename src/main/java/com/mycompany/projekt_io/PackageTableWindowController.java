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
    private Button userButton;
    
    // MAIN BUTTONS
    @FXML
    private Button manageButton;
        
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
    private TableColumn<PackageItem, String> sizeColumn;
    
    @FXML
    private TableColumn<PackageItem, Double> weightColumn;

    @FXML
    private TableColumn<PackageItem, String> senregionColumn;
    
    @FXML
    private TableColumn<PackageItem, String> recregionColumn;
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // TIME AND DATE
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

        // TABLE INIT
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        shelfColumn.setCellValueFactory(new PropertyValueFactory<>("shelf"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
        weightColumn.setCellValueFactory(new PropertyValueFactory<>("weight"));
        senregionColumn.setCellValueFactory(new PropertyValueFactory<>("senRegion"));
        recregionColumn.setCellValueFactory(new PropertyValueFactory<>("recRegion"));

        // TEMP DATA
        packageTable.getItems().addAll(
            new PackageItem(1, "A1", "M", 2.0, "Katowice", "Gliwice"),
            new PackageItem(2, "B2", "S", 0.5, "Gliwice", "Katowice")
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
    private void handleUserButton() {
        loadWindow("/com/mycompany/projekt_io/userManageWindow.fxml");
    }
    
    // MAIN BUTTONS
        @FXML
    private void handleAddButton() {
        loadWindow("/com/mycompany/projekt_io/packageAddWindow.fxml");
    }
    
        @FXML
    private void handlePacManButton() {
        loadWindow("/com/mycompany/projekt_io/packageManageWindow.fxml");
    }

    // FXML LOAD METHOD
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

    // TABLE REPRESENTATION CLASS
    public static class PackageItem {
        private final Integer id;
        private final String shelf;
        private final String size;
        private final Double weight;
        private final String senregion;
        private final String recregion;

        public PackageItem(Integer id, String shelf, String size, Double weight, String senregion, String recregion) {
            this.id = id;
            this.shelf = shelf;
            this.size = size;
            this.weight = weight;
            this.senregion = senregion;
            this.recregion = recregion;
        }

        public Integer getId() { return id; }
        public String getShelf() { return shelf; }
        public String getSize() { return size; }
        public Double getWeight() { return weight; }
        public String getSenRegion() { return senregion; }
        public String getRecRegion() { return recregion; }
    }
}