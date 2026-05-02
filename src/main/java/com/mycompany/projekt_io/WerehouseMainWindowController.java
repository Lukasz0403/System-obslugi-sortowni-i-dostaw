package com.mycompany.projekt_io;

import com.mycompany.projekt_io.core.database.PackageDAO;
import com.mycompany.projekt_io.feature.werehouse.SortingService;
import com.mycompany.projekt_io.feature.werehouse.SortingServiceInterface;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.IOException;
import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

public class WerehouseMainWindowController implements Initializable {

    @FXML private Label timeLabel, dateLabel;
    @FXML private Button magButton, pacButton, addButton;
    @FXML private AnchorPane mainPane;
    @FXML private VBox infoPanel;
    @FXML private Label infoShelfId, infoPackageCount, infoLastPackage;

    // Regały 1-20
    @FXML private Rectangle rack1, rack2, rack3, rack4, rack5, rack6, rack7, rack8, rack9, rack10,
                            rack11, rack12, rack13, rack14, rack15, rack16, rack17, rack18, rack19, rack20;

    @FXML private TableView<WarehouseSummary> warehouseTable;
    @FXML private TableColumn<WarehouseSummary, String> colTotalPackages, colZone1, colZone2, colZone3, colZone4, colZone5, colEmptyShelves, colBusiestShelf;

    private PackageDAO packageDAO = new PackageDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //TIME
        setupClock();

        // RAACKS
        Rectangle[] allRacks = {
            rack1, rack2, rack3, rack4, rack5, rack6, rack7, rack8, rack9, rack10,
            rack11, rack12, rack13, rack14, rack15, rack16, rack17, rack18, rack19, rack20
        };

        for (int i = 0; i < allRacks.length; i++) {
            setupShelfHover(allRacks[i], i + 1);
        }

        // TABLE
        setupTable();
        loadWarehouseSummary();

        // SIDEBAR
        magButton.setOnAction(e -> loadWindow("/com/mycompany/projekt_io/werehouseMainWindow.fxml"));
        pacButton.setOnAction(e -> loadWindow("/com/mycompany/projekt_io/packageTableWindow.fxml"));
        addButton.setOnAction(e -> loadWindow("/com/mycompany/projekt_io/userManageWindow.fxml"));

        Platform.runLater(() -> {
            Stage stage = (Stage) magButton.getScene().getWindow();
            WindowConstraints.applyMinSize(stage);
        });
        
        PackageDAO dao = new PackageDAO();
        SortingServiceInterface algorithm = new SortingService(dao);

        algorithm.addObserver((zoneId, occupancy, maxCapacity) -> {
            int percent = (int) ((occupancy / (double) maxCapacity) * 100);
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Strefa prawie pełna");
                alert.setHeaderText(null);
                alert.setContentText("Strefa " + zoneId + " jest zapełniona w " + percent + "%.\n"
                        + "Zajęte sloty: " + occupancy + " / " + maxCapacity);
                alert.initOwner(magButton.getScene().getWindow());
                alert.showAndWait();
            });
        });

        algorithm.assignShelvesToPackages();
    }

    private void setupShelfHover(Rectangle shelf, int rackId) {
        shelf.setOnMouseEntered(e -> {
            int count = packageDAO.getPackageCountForShelf(rackId);
            int lastId = packageDAO.getLastPackageIdForShelf(rackId);
            infoShelfId.setText("Rack ID: " + rackId);
            infoPackageCount.setText("Packages: " + count);
            infoLastPackage.setText("Last ID: " + (lastId == -1 ? "none" : lastId));
            infoPanel.setVisible(true);
            updateInfoPanelPosition(e.getSceneX(), e.getSceneY());
        });

        shelf.setOnMouseMoved(e -> updateInfoPanelPosition(e.getSceneX(), e.getSceneY()));
        shelf.setOnMouseExited(e -> infoPanel.setVisible(false));
        shelf.setOnMouseClicked(e -> openWarehouseInfo()); // Przejście do info o magazynie
        shelf.setCursor(Cursor.HAND);
    }

    private void updateInfoPanelPosition(double sceneX, double sceneY) {
        double localX = mainPane.sceneToLocal(sceneX, sceneY).getX();
        double localY = mainPane.sceneToLocal(sceneX, sceneY).getY();
        infoPanel.setLayoutX(localX + 15);
        infoPanel.setLayoutY(localY + 15);
    }

    private void openWarehouseInfo() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("werehouseInfoWindow.fxml"));
            Parent newRoot = fxmlLoader.load();
            Stage stage = (Stage) mainPane.getScene().getWindow();
            stage.getScene().setRoot(newRoot);
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void setupClock() {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            LocalDateTime now = LocalDateTime.now();
            timeLabel.setText(now.format(timeFormatter));
            dateLabel.setText(now.format(dateFormatter));
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void setupTable() {
        colTotalPackages.setCellValueFactory(new PropertyValueFactory<>("totalPackages"));
        colZone1.setCellValueFactory(new PropertyValueFactory<>("zone1"));
        colZone2.setCellValueFactory(new PropertyValueFactory<>("zone2"));
        colZone3.setCellValueFactory(new PropertyValueFactory<>("zone3"));
        colZone4.setCellValueFactory(new PropertyValueFactory<>("zone4"));
        colZone5.setCellValueFactory(new PropertyValueFactory<>("zone5"));
        colEmptyShelves.setCellValueFactory(new PropertyValueFactory<>("emptyShelves"));
        colBusiestShelf.setCellValueFactory(new PropertyValueFactory<>("busiestShelf"));
    }

    private void loadWarehouseSummary() {
        int total = packageDAO.getTotalPackageCount();
        int z1 = packageDAO.getPackageCountForZone(1);
        int z2 = packageDAO.getPackageCountForZone(2);
        int z3 = packageDAO.getPackageCountForZone(3);
        int z4 = packageDAO.getPackageCountForZone(4);
        int z5 = packageDAO.getPackageCountForZone(5);
        long empty = packageDAO.getRacks().stream().filter(r -> packageDAO.getPackageCountForShelf(r.getRack_id()) == 0).count();
        int busiest = packageDAO.getMostLoadedShelfId();
        
        warehouseTable.getItems().clear();
        warehouseTable.getItems().add(new WarehouseSummary(
            String.valueOf(total), String.valueOf(z1), String.valueOf(z2), 
            String.valueOf(z3), String.valueOf(z4), String.valueOf(z5),
            String.valueOf(empty), busiest == -1 ? "N/A" : "ID: " + busiest
        ));
    }

    private void loadWindow(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) timeLabel.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) { e.printStackTrace(); }
    }

    public static class WarehouseSummary {
        private final String totalPackages, zone1, zone2, zone3, zone4, zone5, emptyShelves, busiestShelf;
        public WarehouseSummary(String tp, String z1, String z2, String z3, String z4, String z5, String es, String bs) {
            this.totalPackages = tp; this.zone1 = z1; this.zone2 = z2; this.zone3 = z3; 
            this.zone4 = z4; this.zone5 = z5; this.emptyShelves = es; this.busiestShelf = bs;
        }
        public String getTotalPackages() { return totalPackages; }
        public String getZone1() { return zone1; }
        public String getZone2() { return zone2; }
        public String getZone3() { return zone3; }
        public String getZone4() { return zone4; }
        public String getZone5() { return zone5; }
        public String getEmptyShelves() { return emptyShelves; }
        public String getBusiestShelf() { return busiestShelf; }
    }
}
