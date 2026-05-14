package com.mycompany.projekt_io;

import com.mycompany.projekt_io.feature.login.AppCloser;
import com.mycompany.projekt_io.feature.login.AppSession;
import com.mycompany.projekt_io.datamodel.Package;
import com.mycompany.projekt_io.feature.werehouse.WarehouseService;
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
import java.util.List;
import java.util.Map;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.shape.Rectangle;

public class WarehouseInfoWindowController implements Initializable {

    @FXML private Label timeLabel;
    @FXML private Label dateLabel;
    @FXML private Button homeButton;
    @FXML private Button magButton;
    @FXML private Button pacButton;
    @FXML private Button addButton,logOut,closeApp;;

    // PIE CHART
    @FXML private PieChart shelfPieChart;
    @FXML private Label rackIdLabel;
    @FXML private Rectangle shelficon;
    @FXML private Label occupancyLabel;

    // TABLES
    /*@FXML private TableView<TableItem> table1;
    @FXML private TableColumn<TableItem, String> table1Col1;
    @FXML private TableColumn<TableItem, String> table1Col2;*/

    @FXML private TableView<TableItem> table2;
    @FXML private TableColumn<TableItem, String> colId;
    @FXML private TableColumn<TableItem, String> colFormat;
    @FXML private TableColumn<TableItem, String> colOrigin;
    @FXML private TableColumn<TableItem, String> colDest;
    @FXML private TableColumn<TableItem, String> colSender;
    @FXML private TableColumn<TableItem, String> colRecipient;
    
    private int currentRackId;
    private WarehouseService warehouseService;

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
//        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
//            new PieChart.Data("aaa", 30),
//            new PieChart.Data("bbb", 45),
//            new PieChart.Data("ccc", 25)
//        );        
//        shelfPieChart.setData(pieChartData);
//        shelfPieChart.setLegendVisible(true);
//        shelfPieChart.setLabelsVisible(true);

        // -------------------------
        // INFO TABLE
        /*table1Col1.setCellValueFactory(new PropertyValueFactory<>("col1"));
        table1Col2.setCellValueFactory(new PropertyValueFactory<>("col2"));
        table1.getItems().addAll(
            new TableItem(" ", " "),
            new TableItem(" ", " ")
        );*/

        // PACKAGE TABLE
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFormat.setCellValueFactory(new PropertyValueFactory<>("format"));
        colOrigin.setCellValueFactory(new PropertyValueFactory<>("origin"));
        colDest.setCellValueFactory(new PropertyValueFactory<>("destination"));
        colSender.setCellValueFactory(new PropertyValueFactory<>("sender"));
        colRecipient.setCellValueFactory(new PropertyValueFactory<>("recipient")
        );

        // -------------------------
        // SIDE MENU
        homeButton.setOnAction(e -> loadWindow("/com/mycompany/projekt_io/mainWindow.fxml"));
        magButton.setOnAction(e -> loadWindow("/com/mycompany/projekt_io/werehouseMainWindow.fxml"));
        pacButton.setOnAction(e -> loadWindow("/com/mycompany/projekt_io/packageTableWindow.fxml"));
        addButton.setOnAction(e -> loadWindow("/com/mycompany/projekt_io/userManageWindow.fxml"));
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
        
        //rozmiar min okna
        Platform.runLater(() -> {
        Stage stage = (Stage) magButton.getScene().getWindow();
        WindowConstraints.applyMinSize(stage);
        });
    }

        public void loadRackData(int rackId, WarehouseService service) {
        this.currentRackId = rackId;
        this.warehouseService = service;
        
        rackIdLabel.setText("REGAŁ: " + rackId);
        
        Map<String, Integer> occupancyData = warehouseService.getRackOccupancyData(rackId);
        int occupied = occupancyData.getOrDefault("Occupied", 0);
        int available = occupancyData.getOrDefault("Available", 1050);
        int totalSlots = occupied + available;

        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList(
            new PieChart.Data("Zajęte (" + occupied + ")", occupied),
            new PieChart.Data("Wolne (" + available + ")", available)
        );
        shelfPieChart.setData(pieData);
        shelfPieChart.setLegendVisible(true);
        shelfPieChart.setLabelsVisible(true);

        pieData.get(0).getNode().setStyle("-fx-pie-color: #ff0000;");
        pieData.get(1).getNode().setStyle("-fx-pie-color: #aec1d1;");

        Platform.runLater(() -> {
            Node symbol0 = shelfPieChart.lookup(".default-color0.chart-legend-item-symbol");
            Node symbol1 = shelfPieChart.lookup(".default-color1.chart-legend-item-symbol");

            if (symbol0 != null) {
                symbol0.setStyle("-fx-background-color: #ff0000;");
            }
            if (symbol1 != null) {
                symbol1.setStyle("-fx-background-color: #aec1d1;");
            }
        });

        double maxBarHeight = 249.0;
        double baseY = 108.0;
        double occupancyRatio = (double) occupied / totalSlots;
        double currentFillHeight = maxBarHeight * occupancyRatio;
        double percentage = occupancyRatio * 100;
        occupancyLabel.setText(String.format("%.1f%%", percentage));
        
        shelficon.setHeight(currentFillHeight);
        shelficon.setLayoutY(baseY + (maxBarHeight - currentFillHeight));
        shelficon.setFill(javafx.scene.paint.Color.web("#ff4d4d"));

        List<Package> packagesOnRack = warehouseService.getPackagesOnRack(rackId);
        ObservableList<TableItem> tableItems = FXCollections.observableArrayList();

        for (Package p : packagesOnRack) {
            tableItems.add(new TableItem(
                String.valueOf(p.getPackage_id()),
                p.getPackage_format().getFormat_id(),
                p.getPackage_region().getRegion_name(),
                p.getPackage_dest_region().getRegion_name(),
                p.getPackage_sender().getSender_name(),
                p.getPackage_recipient().getRecipient_name()
            ));
        }
        table2.setItems(tableItems);
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
private final String id;
    private final String format;
    private final String origin;
    private final String destination;
    private final String sender;
    private final String recipient;

    public TableItem(String id, String format, String origin, String destination, String sender, String recipient) {
        this.id = id;
        this.format = format;
        this.origin = origin;
        this.destination = destination;
        this.sender = sender;
        this.recipient = recipient;
    }

    // Gettery są NIEZBĘDNE dla TableView w JavaFX
    public String getId() { return id; }
    public String getFormat() { return format; }
    public String getOrigin() { return origin; }
    public String getDestination() { return destination; }
    public String getSender() { return sender; }
    public String getRecipient() { return recipient; }
        }
}