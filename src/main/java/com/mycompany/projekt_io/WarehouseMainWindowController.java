package com.mycompany.projekt_io;

import java.io.IOException;
import java.util.List;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import static com.mycompany.projekt_io.feature.warehouse.SortingService.MAX_RACK_CAPACITY;
import com.mycompany.projekt_io.core.database.PackageDAO;
import com.mycompany.projekt_io.datamodel.Rack;
import com.mycompany.projekt_io.feature.login.AppCloser;
import com.mycompany.projekt_io.feature.login.AppSession;
import com.mycompany.projekt_io.feature.warehouse.SortingService;
import com.mycompany.projekt_io.feature.warehouse.SortingServiceInterface;
import com.mycompany.projekt_io.feature.warehouse.WarehouseService;

/*
 * @author Łukasz Motyka, Ida Wszoła
 */
public class WarehouseMainWindowController implements Initializable {

    @FXML 
    private Label timeLabel;
    @FXML 
    private Label dateLabel;
    @FXML 
    private Button homeButton;
    @FXML 
    private Button magButton;
    @FXML 
    private Button pacButton;
    @FXML
    private Button addButton;
    @FXML 
    private Button logOut;
    @FXML 
    private Button closeApp;
    @FXML 
    private AnchorPane mainPane;
    @FXML 
    private VBox infoPanel;
    @FXML 
    private Label infoShelfId;
    @FXML
    private Label infoPackageCount;
    @FXML 
    private Label infoLastPackage;
    @FXML 
    private Text totalPackages;
    @FXML
    private Text busiestRack;
    @FXML 
    private Text emptyRack;
    @FXML 
    private Text warehouseOccupancy;
    @FXML 
    private Text topFormat;
    
    // Regały 1-20
    @FXML private Rectangle rack1, rack2, rack3, rack4, rack5, rack6, rack7, rack8, rack9, rack10,
                            rack11, rack12, rack13, rack14, rack15, rack16, rack17, rack18, rack19, rack20;
    @FXML 
    private TableView<WarehouseSummary> warehouseTable;
    @FXML 
    private TableColumn<WarehouseSummary, String> colTotalPackages, colZone1, colZone2, colZone3, colZone4, colZone5, colEmptyShelves, colBusiestShelf;
    @FXML
    private StackedBarChart<String, Number> ZoneCapacity;
    
    private PackageDAO packageDAO = new PackageDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //TIME
        setupClock();

        // RACKS
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
                alert.setTitle("Zone Almost Full");
                alert.setHeaderText(null);
                alert.setContentText("Zone " + zoneId + " is " + percent + "% full.\n"
                        + "Occupied slots: " + occupancy + " / " + maxCapacity);
                alert.initOwner(magButton.getScene().getWindow());
                alert.showAndWait();
            });
        });

        algorithm.assignShelvesToPackages();
        loadZoneCapacityChart();
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

          shelf.setOnMouseClicked(e -> openWarehouseInfo(rackId));
          shelf.setCursor(Cursor.HAND);
    }

    private void loadZoneCapacityChart() {
        ZoneCapacity.getData().clear();
        int[] rackSlots = new int[21];

        for (com.mycompany.projekt_io.datamodel.Package p : packageDAO.getPackages()) {
            if (p.getPackage_rack() != null) {
                int rackId = p.getPackage_rack().getRack_id();
                if (rackId >= 1 && rackId <= 20 && p.getPackage_format() != null) {
                    rackSlots[rackId] += p.getPackage_format().getSlot_coverage();
                }
            }
        }

        XYChart.Series<String, Number> occupiedSeries = new XYChart.Series<>();
        occupiedSeries.setName("Ilość zajętych slotów");

        for (int i = 0; i < 5; i++) {
            int zoneTotal = 0;
            StringBuilder tooltipText = new StringBuilder("Strefa " + (i + 1) + ":\n");

            for (int r = 1; r <= 4; r++) {
                int rackId = (i * 4) + r;
                int slots = rackSlots[rackId];

                tooltipText.append("  • Regał ").append(rackId)
                           .append(":   ").append(slots).append(" / 1050 slotów\n");
                zoneTotal += slots;
            }

            XYChart.Data<String, Number> data = new XYChart.Data<>("Zone " + (i + 1), zoneTotal);
            String finalInfoText = tooltipText.toString().trim();

            data.nodeProperty().addListener((obs, oldNode, newNode) -> {
                if (newNode != null) {
                    newNode.setStyle("-fx-bar-fill: #aec1d1;");
                    javafx.scene.control.Tooltip tooltip = new javafx.scene.control.Tooltip(finalInfoText);
                    tooltip.setStyle("-fx-font-size: 11px; -fx-font-weight: normal;");
                    tooltip.setShowDelay(javafx.util.Duration.millis(50)); 
                    tooltip.setShowDuration(javafx.util.Duration.seconds(10));
                    javafx.scene.control.Tooltip.install(newNode, tooltip);
                }
            });
            occupiedSeries.getData().add(data);
        }
        ZoneCapacity.getData().add(occupiedSeries);

        javafx.application.Platform.runLater(() -> {
            for (javafx.scene.Node n : ZoneCapacity.lookupAll(".chart-legend-item-symbol")) {
                n.setStyle("-fx-bar-fill: #aec1d1; -fx-fill: #aec1d1;");
            }
        });
    } 
        
    private void updateInfoPanelPosition(double sceneX, double sceneY) {
        double localX = mainPane.sceneToLocal(sceneX, sceneY).getX();
        double localY = mainPane.sceneToLocal(sceneX, sceneY).getY();
        infoPanel.setLayoutX(localX + 15);
        infoPanel.setLayoutY(localY + 15);
    }

    private void openWarehouseInfo(int rackID) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("werehouseInfoWindow.fxml"));
            Parent newRoot = fxmlLoader.load();
            WarehouseInfoWindowController controller = fxmlLoader.getController();
                   WarehouseService service = new WarehouseService(this.packageDAO);
            controller.loadRackData(rackID, service);
            Stage stage = (Stage) mainPane.getScene().getWindow();
            stage.getScene().setRoot(newRoot);
        } catch (IOException e) { 
            e.printStackTrace(); 
        }
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

    private void showBasicStatistics(int total, int busiest, long empty, float occupancy, String format){
        totalPackages.setText(String.valueOf(total));
        busiestRack.setText("No. " + String.valueOf(busiest));
        emptyRack.setText(String.valueOf(empty));
        warehouseOccupancy.setText(String.format("%.2f %%", occupancy));
        topFormat.setText(format);
    }
    
    public int getTotalRackOccupancy() {
    return packageDAO.getPackages().stream()
        .filter(p -> p.getPackage_rack() != null)
        .mapToInt(p -> p.getPackage_format().getSlot_coverage())
        .sum();
    }
    
    public String getMostPopularFormat() {
    return packageDAO.getPackages().stream()
        .filter(p -> p.getPackage_format() != null && p.getPackage_format().getFormat_id() != null)
        .collect(java.util.stream.Collectors.groupingBy(
            p -> p.getPackage_format().getFormat_id(), 
            java.util.stream.Collectors.counting()
        ))
        .entrySet().stream()
        .max(java.util.Map.Entry.comparingByValue())
        .map(java.util.Map.Entry::getKey)
        .orElse("N/A");
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
        List<Rack> items = packageDAO.getRacks();
        int total = packageDAO.getTotalPackageCount();
        int z1 = packageDAO.getPackageCountForZone(1);
        int z2 = packageDAO.getPackageCountForZone(2);
        int z3 = packageDAO.getPackageCountForZone(3);
        int z4 = packageDAO.getPackageCountForZone(4);
        int z5 = packageDAO.getPackageCountForZone(5);
        int rackOccupancy = getTotalRackOccupancy();
        int busiest = packageDAO.getMostLoadedShelfId();
        int count = items.size();
        long empty = packageDAO.getRacks().stream().filter(r -> packageDAO.getPackageCountForShelf(r.getRack_id()) == 0).count();
        float occupancy = ((float) rackOccupancy / (MAX_RACK_CAPACITY * count)) * 100;        
        String format = getMostPopularFormat();

        showBasicStatistics(total, busiest, empty, occupancy, format);
        
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
        } catch (IOException e) {
            e.printStackTrace(); 
        }
    }

    public static class WarehouseSummary {
        private final String totalPackages, zone1, zone2, zone3, zone4, zone5, emptyShelves, busiestShelf;
        public WarehouseSummary(String tp, String z1, String z2, String z3, String z4, String z5, String es, String bs) {
            this.totalPackages = tp; this.zone1 = z1; this.zone2 = z2; this.zone3 = z3; 
            this.zone4 = z4; this.zone5 = z5; this.emptyShelves = es; this.busiestShelf = bs;
        }
        public String getTotalPackages() { 
            return totalPackages;
        }
        public String getZone1() { 
            return zone1; 
        }
        public String getZone2() { 
            return zone2; 
        }
        public String getZone3() { 
            return zone3; 
        }
        public String getZone4() { 
            return zone4; 
        }
        public String getZone5() { 
            return zone5; 
        }
        public String getEmptyShelves() {
            return emptyShelves;
        }
        public String getBusiestShelf() { 
            return busiestShelf; 
        }
    }
}
