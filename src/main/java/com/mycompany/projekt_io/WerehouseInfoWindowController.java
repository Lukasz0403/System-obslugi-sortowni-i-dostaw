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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.IOException;
import javafx.application.Platform;

public class WerehouseInfoWindowController implements Initializable {

    @FXML private Label timeLabel;
    @FXML private Label dateLabel;
    @FXML private Button magButton;
    @FXML private Button pacButton;
    @FXML private Button addButton;

    // PIE CHART
    @FXML private PieChart shelfPieChart;

    // TABLES
    @FXML private TableView<TableItem> table1;
    @FXML private TableColumn<TableItem, String> table1Col1;
    @FXML private TableColumn<TableItem, String> table1Col2;

    @FXML private TableView<TableItem> table2;
    @FXML private TableColumn<TableItem, String> table2Col1;
    @FXML private TableColumn<TableItem, String> table2Col2;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // DATE/TIME
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

        // -------------------------
        // PIE CHART TEMP DATA
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
            new PieChart.Data("aaa", 30),
            new PieChart.Data("bbb", 45),
            new PieChart.Data("ccc", 25)
        );
        shelfPieChart.setData(pieChartData);
        shelfPieChart.setLegendVisible(true);
        shelfPieChart.setLabelsVisible(true);

        // -------------------------
        // INFO TABLE
        table1Col1.setCellValueFactory(new PropertyValueFactory<>("col1"));
        table1Col2.setCellValueFactory(new PropertyValueFactory<>("col2"));
        table1.getItems().addAll(
            new TableItem(" ", " "),
            new TableItem(" ", " ")
        );

        // PACKAGE TABLE
        table2Col1.setCellValueFactory(new PropertyValueFactory<>("col1"));
        table2Col2.setCellValueFactory(new PropertyValueFactory<>("col2"));
        table2.getItems().addAll(
            new TableItem(" ", " "),
            new TableItem(" ", " ")
        );

        // -------------------------
        // SIDE MENU
        magButton.setOnAction(e -> loadWindow("/com/mycompany/projekt_io/werehouseMainWindow.fxml"));
        pacButton.setOnAction(e -> loadWindow("/com/mycompany/projekt_io/packageTableWindow.fxml"));
        addButton.setOnAction(e -> loadWindow("/com/mycompany/projekt_io/userManageWindow.fxml"));
        
        //rozmiar min okna
        Platform.runLater(() -> {
        Stage stage = (Stage) magButton.getScene().getWindow();
        WindowConstraints.applyMinSize(stage);
        });
    }

    // -------------------------
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

    // -------------------------
    // TABLE CLASS
    public static class TableItem {
        private final String col1;
        private final String col2;

        public TableItem(String col1, String col2) {
            this.col1 = col1;
            this.col2 = col2;
        }

        public String getCol1() { return col1; }
        public String getCol2() { return col2; }
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
}