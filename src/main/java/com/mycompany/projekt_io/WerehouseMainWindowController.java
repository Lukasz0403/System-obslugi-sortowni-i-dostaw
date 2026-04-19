package com.mycompany.projekt_io;

import com.mycompany.projekt_io.core.database.PackageDAO;
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
import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

public class WerehouseMainWindowController implements Initializable {

    // SIDEBAR LABELS AND BUTTONS
    @FXML private Label timeLabel;
    @FXML private Label dateLabel;
    @FXML private Button magButton;
    @FXML private Button pacButton;
    @FXML private Button addButton;

    // SHELF RECTANGLES
    @FXML private Rectangle shelf1;
    @FXML private Rectangle shelf2;
    @FXML private Rectangle shelf3;
    @FXML private Rectangle shelf4;
    @FXML private Rectangle shelfex;

    @FXML private AnchorPane mainPane;

    // HOVER INFO PANEL LABELS
    @FXML private VBox infoPanel;
    @FXML private Label infoShelfId;
    @FXML private Label infoPackageCount;
    @FXML private Label infoLastPackage;
    @FXML private Label infoLastDate;
    @FXML private Label infoLastSender;

    // WAREHOUSE SUMMARY TABLE COLUMNS
    @FXML private TableView<WarehouseSummary> warehouseTable;
    @FXML private TableColumn<WarehouseSummary, String> colTotalPackages;
    @FXML private TableColumn<WarehouseSummary, String> colZone1;
    @FXML private TableColumn<WarehouseSummary, String> colZone2;
    @FXML private TableColumn<WarehouseSummary, String> colZone3;
    @FXML private TableColumn<WarehouseSummary, String> colEmptyShelves;
    @FXML private TableColumn<WarehouseSummary, String> colBusiestShelf;

    // DAO FOR DATABASE ACCESS
    private PackageDAO packageDAO = new PackageDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // DATE / TIME CLOCK UPDATE EVERY SECOND
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(1), e -> {
                LocalDateTime now = LocalDateTime.now();
                timeLabel.setText(now.format(timeFormatter));
                dateLabel.setText(now.format(dateFormatter));
            })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        // MAP EACH SHELF RECTANGLE TO ITS REAL SHELF ID FROM DATABASE
        // ORDER LEFT TO RIGHT: shelfex=301, shelf4=101, shelf3=102, shelf2=201, shelf1=202
        setupShelfHover(shelfex, 301);
        setupShelfHover(shelf4, 101);
        setupShelfHover(shelf3, 102);
        setupShelfHover(shelf2, 201);
        setupShelfHover(shelf1, 202);

        // SET UP WAREHOUSE SUMMARY TABLE COLUMNS
        colTotalPackages.setCellValueFactory(new PropertyValueFactory<>("totalPackages"));
        colZone1.setCellValueFactory(new PropertyValueFactory<>("zone1"));
        colZone2.setCellValueFactory(new PropertyValueFactory<>("zone2"));
        colZone3.setCellValueFactory(new PropertyValueFactory<>("zone3"));
        colEmptyShelves.setCellValueFactory(new PropertyValueFactory<>("emptyShelves"));
        colBusiestShelf.setCellValueFactory(new PropertyValueFactory<>("busiestShelf"));

        // LOAD WAREHOUSE SUMMARY DATA FROM DATABASE
        loadWarehouseSummary();

        // SIDEBAR NAVIGATION BUTTONS
        magButton.setOnAction(e -> loadWindow("/com/mycompany/projekt_io/werehouseMainWindow.fxml"));
        pacButton.setOnAction(e -> loadWindow("/com/mycompany/projekt_io/packageTableWindow.fxml"));
        addButton.setOnAction(e -> loadWindow("/com/mycompany/projekt_io/userManageWindow.fxml"));

        // APPLY MINIMUM WINDOW SIZE
        Platform.runLater(() -> {
            Stage stage = (Stage) magButton.getScene().getWindow();
            WindowConstraints.applyMinSize(stage);
        });
    }

    // SET UP HOVER AND CLICK BEHAVIOUR FOR A SHELF RECTANGLE
    private void setupShelfHover(Rectangle shelf, int shelfId) {

        // WHEN MOUSE ENTERS THE SHELF - FETCH DATA AND SHOW INFO PANEL
        shelf.setOnMouseEntered(e -> {
            int count = packageDAO.getPackageCountForShelf(shelfId);
            int lastId = packageDAO.getLastPackageIdForShelf(shelfId);

            infoShelfId.setText("Shelf: " + shelfId);
            infoPackageCount.setText("Packages: " + count);
            infoLastPackage.setText("Last Package ID: " + (lastId == -1 ? "none" : lastId));
            infoLastDate.setText("Added: 00:00  0000-00-00");
            infoLastSender.setText("By: N/A");

            infoPanel.setVisible(true);
            updateInfoPanelPosition(e.getSceneX(), e.getSceneY());
        });

        // PANEL FOLLOWS THE CURSOR WHILE HOVERING
        shelf.setOnMouseMoved(e -> updateInfoPanelPosition(e.getSceneX(), e.getSceneY()));

        // HIDE PANEL WHEN MOUSE LEAVES THE SHELF
        shelf.setOnMouseExited(e -> infoPanel.setVisible(false));

        // OPEN SHELF DETAIL WINDOW ON CLICK
        shelf.setOnMouseClicked(e -> openWarehouseInfo());
        shelf.setCursor(Cursor.HAND);
    }

    // CALCULATE POSITION OF INFO PANEL RELATIVE TO CURSOR
    private void updateInfoPanelPosition(double sceneX, double sceneY) {
        double localX = mainPane.sceneToLocal(sceneX, sceneY).getX();
        double localY = mainPane.sceneToLocal(sceneX, sceneY).getY();
        infoPanel.setLayoutX(localX + 30);
        infoPanel.setLayoutY(localY + 30);
    }

    // LOAD AND DISPLAY ONE ROW OF GENERAL WAREHOUSE STATISTICS IN THE TABLE
    private void loadWarehouseSummary() {
        int total = packageDAO.getTotalPackageCount();
        int zone1 = packageDAO.getPackageCountForZone(1);
        int zone2 = packageDAO.getPackageCountForZone(2);
        int zone3 = packageDAO.getPackageCountForZone(3);

        // COUNT EMPTY SHELVES - SHELVES WITH NO PACKAGES ASSIGNED
        long emptyShelves = packageDAO.getShelves().stream()
            .filter(s -> packageDAO.getPackageCountForShelf(s.getShelf_id()) == 0)
            .count();

        int busiestShelfId = packageDAO.getMostLoadedShelfId();
        String busiestShelf = busiestShelfId == -1 ? "N/A" : String.valueOf(busiestShelfId);

        warehouseTable.getItems().clear();
        warehouseTable.getItems().add(new WarehouseSummary(
            String.valueOf(total),
            String.valueOf(zone1),
            String.valueOf(zone2),
            String.valueOf(zone3),
            String.valueOf(emptyShelves),
            busiestShelf
        ));
    }

    // OPEN THE SHELF DETAIL WINDOW
    private void openWarehouseInfo() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("werehouseInfoWindow.fxml"));
            Parent newRoot = fxmlLoader.load();
            Stage stage = (Stage) mainPane.getScene().getWindow();
            stage.getScene().setRoot(newRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // LOAD A DIFFERENT WINDOW BY FXML PATH
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

    // DATA MODEL CLASS FOR THE WAREHOUSE SUMMARY TABLE ROW
    public static class WarehouseSummary {
        private final String totalPackages;
        private final String zone1;
        private final String zone2;
        private final String zone3;
        private final String emptyShelves;
        private final String busiestShelf;

        public WarehouseSummary(String totalPackages, String zone1, String zone2,
                                String zone3, String emptyShelves, String busiestShelf) {
            this.totalPackages = totalPackages;
            this.zone1 = zone1;
            this.zone2 = zone2;
            this.zone3 = zone3;
            this.emptyShelves = emptyShelves;
            this.busiestShelf = busiestShelf;
        }

        public String getTotalPackages() { return totalPackages; }
        public String getZone1() { return zone1; }
        public String getZone2() { return zone2; }
        public String getZone3() { return zone3; }
        public String getEmptyShelves() { return emptyShelves; }
        public String getBusiestShelf() { return busiestShelf; }
    }
}