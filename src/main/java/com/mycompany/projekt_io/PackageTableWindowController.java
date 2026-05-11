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
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.IOException;
import javafx.scene.control.cell.PropertyValueFactory;
import com.mycompany.projekt_io.feature.package_.PackageTableService;
import java.util.List;
import com.mycompany.projekt_io.datamodel.Package;
import com.mycompany.projekt_io.feature.login.AppCloser;
import com.mycompany.projekt_io.feature.login.AppSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

/**
 * Kontroler okna tabeli paczek.
 * <p>
 * Odpowiada za wyświetlanie listy wszystkich paczek w tabeli JavaFX, obsługę
 * nawigacji między oknami oraz przejście do okna zarządzania wybraną paczką.
 * </p>
 *
 * @author Mateusz Gojny, Ida Wszoła
 */
public class PackageTableWindowController implements Initializable {

    @FXML private Label timeLabel;
    @FXML private Label dateLabel;
    @FXML private Button homeButton;
    @FXML private Button magButton;
    @FXML private Button pacButton;
    @FXML private Button userButton;
    @FXML private Button manageButtonMain;
    @FXML private Button addButton, logOut, closeApp;
    @FXML private TableView<PackageTableService> packageTable;
    @FXML private TableColumn<PackageTableService, Integer> idColumn;
    @FXML private TableColumn<PackageTableService, String> shelfColumn;
    @FXML private TableColumn<PackageTableService, String> sizeColumn;
    @FXML private TableColumn<PackageTableService, Integer> widthColumn;
    @FXML private TableColumn<PackageTableService, Integer> heightColumn;
    @FXML private TableColumn<PackageTableService, Integer> depthColumn;
    @FXML private TableColumn<PackageTableService, String> senregionColumn;
    @FXML private TableColumn<PackageTableService, String> recregionColumn;
    @FXML private TableColumn<PackageTableService, Double> weightColumn;
    @FXML private TextField searchField;


    private final PackageDAO dao = new PackageDAO();
    private PackageTableService selected;

    /**
     * Inicjalizuje kontroler — uruchamia zegar, konfiguruje kolumny tabeli i
     * ładuje dane paczek z bazy danych, automatycznie odświeża tabelę.
     *
     * @param url ścieżka do pliku FXML (nieużywana bezpośrednio)
     * @param rb  zasoby lokalizacyjne (nieużywane bezpośrednio)
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

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        shelfColumn.setCellValueFactory(new PropertyValueFactory<>("shelf"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
        widthColumn.setCellValueFactory(new PropertyValueFactory<>("width"));
        heightColumn.setCellValueFactory(new PropertyValueFactory<>("height"));
        depthColumn.setCellValueFactory(new PropertyValueFactory<>("depth"));
        senregionColumn.setCellValueFactory(new PropertyValueFactory<>("senderRegion"));
        recregionColumn.setCellValueFactory(new PropertyValueFactory<>("receiverRegion"));
        weightColumn.setCellValueFactory(new PropertyValueFactory<>("weight"));

        List<Package> packages = dao.getPackages();
        List<PackageTableService> tableData = packages.stream()
                .map(PackageTableService::new)
                .collect(Collectors.toList());
        packageTable.getItems().setAll(tableData);

        idColumn.setSortType(TableColumn.SortType.ASCENDING);
        packageTable.getSortOrder().clear();
        packageTable.getSortOrder().add(idColumn);
        packageTable.sort();

        Platform.runLater(() -> {
            Stage stage = (Stage) magButton.getScene().getWindow();
            WindowConstraints.applyMinSize(stage);
        });

        Timeline autoRefresh = new Timeline(
                new KeyFrame(Duration.seconds(10), event -> {
                    PackageTableService selectedItem = packageTable.getSelectionModel().getSelectedItem();
                    int selectedId = (selectedItem != null) ? selectedItem.getId() : -1;

                    List<TableColumn<PackageTableService, ?>> sortOrder = new ArrayList<>(packageTable.getSortOrder());
                    Map<TableColumn<PackageTableService, ?>, TableColumn.SortType> sortTypes = new HashMap<>();
                    for (TableColumn<PackageTableService, ?> col : sortOrder) {
                        sortTypes.put(col, col.getSortType());
                    }

                    List<Package> updated = dao.getPackages();
                    List<PackageTableService> updatedData = updated.stream()
                            .map(PackageTableService::new)
                            .collect(Collectors.toList());
                    packageTable.getItems().setAll(updatedData);

                    packageTable.getSortOrder().clear();
                    for (TableColumn<PackageTableService, ?> col : sortOrder) {
                        col.setSortType(sortTypes.get(col));
                    }
                    packageTable.getSortOrder().setAll(sortOrder);
                    packageTable.sort();

                    if (selectedId != -1) {
                        updatedData.stream()
                                .filter(p -> p.getId() == selectedId)
                                .findFirst()
                                .ifPresent(p -> {
                                    packageTable.getSelectionModel().select(p);
                                    packageTable.scrollTo(p);
                                });
                    }
                })
        );
        autoRefresh.setCycleCount(Timeline.INDEFINITE);
        autoRefresh.play();

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
        
        if (AppSession.isLoggedIn() && AppSession.getCurrentUser().getPermission().getPermission_id() == 1) {
            manageButtonMain.setDisable(true);
        }
        
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                // Pokaż wszystkie paczki
                List<Package> all = dao.getPackages();
                List<PackageTableService> allData = all.stream()
                        .map(PackageTableService::new)
                        .collect(Collectors.toList());
                packageTable.getItems().setAll(allData);
            } else {
                // Filtruj po ID
                try {
                    int searchId = Integer.parseInt(newValue);
                    List<PackageTableService> filtered = dao.getPackages().stream()
                            .map(PackageTableService::new)
                            .filter(p -> p.getId() == searchId)
                            .collect(Collectors.toList());
                    packageTable.getItems().setAll(filtered);
                } catch (NumberFormatException e) {
                    // Jeśli wpisano nie-liczbę — pokaż wszystkie
                    List<Package> all = dao.getPackages();
                    List<PackageTableService> allData = all.stream()
                            .map(PackageTableService::new)
                            .collect(Collectors.toList());
                    packageTable.getItems().setAll(allData);
                }
            }
        });
    }

    /** Przechodzi do okna głównego. */
    @FXML
    private void handleHomeButton() {
        loadWindow("/com/mycompany/projekt_io/mainWindow.fxml");
    }

    /** Przechodzi do okna głównego magazynu. */
    @FXML
    private void handleMagButton() {
        loadWindow("/com/mycompany/projekt_io/werehouseMainWindow.fxml");
    }

    /** Odświeża bieżące okno tabeli paczek. */
    @FXML
    private void handlePacButton() {
        loadWindow("/com/mycompany/projekt_io/packageTableWindow.fxml");
    }

    /** Przechodzi do okna zarządzania użytkownikami. */
    @FXML
    private void handleUserButton() {
        loadWindow("/com/mycompany/projekt_io/userManageWindow.fxml");
    }

    /** Przechodzi do okna dodawania nowej paczki. */
    @FXML
    private void handleAddButton() {
        loadWindow("/com/mycompany/projekt_io/packageAddWindow.fxml");
    }

    /**
     * Obsługuje przejście do okna zarządzania wybraną paczką.
     * <p>
     * Sprawdza czy paczka jest zaznaczona i czy ma przypisany regał. Paczki bez
     * regału nie mogą być edytowane — wymagają wcześniejszego przyjęcia na
     * magazyn. Pobiera pełne dane paczki z bazy i przekazuje je do kontrolera
     * okna zarządzania.
     * </p>
     */
    @FXML
    private void handlePacManButton() {
        selected = packageTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("Error", "No package selected.");
            return;
        }

        if (selected.getShelf().equals("0")) {
            showAlert("No Rack Assigned",
                    "Cannot edit this package.\n\nThe package has not yet been assigned to a warehouse zone.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com/mycompany/projekt_io/packageManageWindow.fxml"));
            Parent root = loader.load();

            Package fullPackage = dao.getPackage(selected.getId());

            PackageManageWindowController controller = loader.getController();
            controller.setPackage(fullPackage);

            Stage stage = (Stage) timeLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
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
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Wyświetla okno dialogowe z ostrzeżeniem.
     *
     * @param title   tytuł okna dialogowego
     * @param content treść komunikatu
     */
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.initOwner(timeLabel.getScene().getWindow());
        alert.showAndWait();
    }
}