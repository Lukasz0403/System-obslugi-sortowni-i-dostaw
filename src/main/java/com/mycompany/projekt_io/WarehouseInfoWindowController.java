package com.mycompany.projekt_io;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import com.mycompany.projekt_io.datamodel.Package;
import com.mycompany.projekt_io.feature.login.AppCloser;
import com.mycompany.projekt_io.feature.login.AppSession;
import com.mycompany.projekt_io.feature.warehouse.WarehouseService;

/**
 * Kontroler okna szczegółów regału magazynowego.
 * <p>
 * Wyświetla szczegółowe informacje o wybranym regale — wykres kołowy zajętości
 * slotów, wizualizację słupkową wypełnienia, procentową zajętość oraz tabelę
 * paczek przypisanych do regału. Podwójne kliknięcie wiersza w tabeli przenosi
 * do okna tabeli paczek z zaznaczoną wybraną paczką.
 * </p>
 *
 * @author Łukasz Motyka, Ida Wszoła
 */
public class WarehouseInfoWindowController implements Initializable {

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
    private PieChart shelfPieChart;
    @FXML
    private Label rackIdLabel;
    @FXML
    private Rectangle shelficon;
    @FXML
    private Label occupancyLabel;
    @FXML
    private TableView<TableItem> table2;
    @FXML
    private TableColumn<TableItem, String> colId;
    @FXML
    private TableColumn<TableItem, String> colFormat;
    @FXML
    private TableColumn<TableItem, String> colOrigin;
    @FXML
    private TableColumn<TableItem, String> colDest;
    @FXML
    private TableColumn<TableItem, String> colSender;
    @FXML
    private TableColumn<TableItem, String> colRecipient;

    private int currentRackId;
    private WarehouseService warehouseService;

    /**
     * Inicjalizuje kontroler — uruchamia zegar, konfiguruje kolumny tabeli,
     * przyciski nawigacyjne oraz obsługę podwójnego kliknięcia w wiersz tabeli.
     *
     * @param url ścieżka do pliku FXML (nieużywana bezpośrednio)
     * @param rb zasoby lokalizacyjne (nieużywane bezpośrednio)
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
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

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFormat.setCellValueFactory(new PropertyValueFactory<>("format"));
        colOrigin.setCellValueFactory(new PropertyValueFactory<>("origin"));
        colDest.setCellValueFactory(new PropertyValueFactory<>("destination"));
        colSender.setCellValueFactory(new PropertyValueFactory<>("sender"));
        colRecipient.setCellValueFactory(new PropertyValueFactory<>("recipient"));
        table2.getColumns().forEach(col -> col.setReorderable(false));

        homeButton.setOnAction(e -> loadWindow("/com/mycompany/projekt_io/mainWindow.fxml"));
        magButton.setOnAction(e -> loadWindow("/com/mycompany/projekt_io/werehouseMainWindow.fxml"));
        pacButton.setOnAction(e -> loadWindow("/com/mycompany/projekt_io/packageTableWindow.fxml"));
        addButton.setOnAction(e -> loadWindow("/com/mycompany/projekt_io/userManageWindow.fxml"));

        logOut.setOnAction(e -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Logout");
            confirm.setHeaderText(null);
            confirm.setContentText("Are you sure you want to log out?");
            confirm.initOwner(timeLabel.getScene().getWindow());
            confirm.showAndWait().ifPresent(response -> {
                if (response == javafx.scene.control.ButtonType.OK) {
                    AppSession.logout();
                    loadWindow("/com/mycompany/projekt_io/loginWindow.fxml");
                }
            });
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

        table2.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                TableItem clickedItem = table2.getSelectionModel().getSelectedItem();
                if (clickedItem != null) {
                    goToPackageTableAndSelect(clickedItem.getId());
                }
            }
        });
    }

    /**
     * Ładuje dane wybranego regału i aktualizuje wszystkie komponenty widoku.
     * <p>
     * Pobiera zajętość slotów, aktualizuje wykres kołowy, wizualizację słupkową
     * i tabelę paczek przypisanych do wskazanego regału.
     * </p>
     *
     * @param rackId identyfikator regału którego dane mają być wyświetlone
     * @param service serwis dostarczający dane o zajętości i paczkach regału
     */
    public void loadRackData(int rackId, WarehouseService service) {
        this.currentRackId = rackId;
        this.warehouseService = service;

        rackIdLabel.setText("RACK: " + rackId);

        Map<String, Integer> occupancyData = warehouseService.getRackOccupancyData(rackId);
        int occupied = occupancyData.getOrDefault("Occupied", 0);
        int available = occupancyData.getOrDefault("Available", 1050);
        int totalSlots = occupied + available;

        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList(
                new PieChart.Data("Occupied (" + occupied + ")", occupied),
                new PieChart.Data("Free (" + available + ")", available)
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

    /**
     * Ładuje wskazane okno FXML i zastępuje nim bieżącą scenę.
     *
     * @param fxmlPath ścieżka do pliku FXML okna docelowego
     */
    private void loadWindow(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) timeLabel.getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Przechodzi do okna tabeli paczek i zaznacza paczkę o podanym
     * identyfikatorze.
     * <p>
     * Ładuje widok FXML tabeli paczek, pobiera jego kontroler i wywołuje metodę
     * odpowiedzialną za odszukanie i podświetlenie paczki o podanym ID.
     * </p>
     *
     * @param packageId identyfikator wybranej paczki pobrany z tabeli regału
     */
    private void goToPackageTableAndSelect(String packageId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com/mycompany/projekt_io/packageTableWindow.fxml"));
            Parent root = loader.load();
            PackageTableWindowController controller = loader.getController();
            controller.selectSpecificPackage(packageId);
            Stage stage = (Stage) timeLabel.getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Model danych wiersza tabeli paczek w widoku szczegółów regału.
     * <p>
     * Przechowuje uproszczone dane paczki do wyświetlenia w komponencie
     * {@code TableView} — identyfikator, gabaryt, regiony nadania i docelowy
     * oraz nazwy nadawcy i odbiorcy.
     * </p>
     */
    public static class TableItem {

        private final String id;
        private final String format;
        private final String origin;
        private final String destination;
        private final String sender;
        private final String recipient;

        /**
         * Tworzy wiersz tabeli z danymi paczki.
         *
         * @param id identyfikator paczki
         * @param format identyfikator gabarytu paczki
         * @param origin kod regionu nadania
         * @param destination kod regionu docelowego
         * @param sender nazwa nadawcy
         * @param recipient nazwa odbiorcy
         */
        public TableItem(String id, String format, String origin,
                String destination, String sender, String recipient) {
            this.id = id;
            this.format = format;
            this.origin = origin;
            this.destination = destination;
            this.sender = sender;
            this.recipient = recipient;
        }

        /**
         * @return identyfikator paczki
         */
        public String getId() {
            return id;
        }

        /**
         * @return identyfikator gabarytu paczki
         */
        public String getFormat() {
            return format;
        }

        /**
         * @return kod regionu nadania
         */
        public String getOrigin() {
            return origin;
        }

        /**
         * @return kod regionu docelowego
         */
        public String getDestination() {
            return destination;
        }

        /**
         * @return nazwa nadawcy
         */
        public String getSender() {
            return sender;
        }

        /**
         * @return nazwa odbiorcy
         */
        public String getRecipient() {
            return recipient;
        }
    }
}
