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
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.scene.control.Alert;

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

    @FXML
    private Label timeLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Button magButton;
    @FXML
    private Button pacButton;
    @FXML
    private Button userButton;
    @FXML
    private Button manageButton;
    @FXML
    private Button addButton;
    @FXML
    private TableView<PackageTableService> packageTable;
    @FXML
    private TableColumn<PackageTableService, Integer> idColumn;
    @FXML
    private TableColumn<PackageTableService, String> shelfColumn;
    @FXML
    private TableColumn<PackageTableService, String> sizeColumn;
    @FXML
    private TableColumn<PackageTableService, Integer> widthColumn;
    @FXML
    private TableColumn<PackageTableService, Integer> heightColumn;
    @FXML
    private TableColumn<PackageTableService, Integer> depthColumn;
    @FXML
    private TableColumn<PackageTableService, String> senregionColumn;
    @FXML
    private TableColumn<PackageTableService, String> recregionColumn;
    @FXML
    private TableColumn<PackageTableService, Double> weightColumn;
    
    private final PackageDAO dao = new PackageDAO();
    
    private PackageTableService selected;

    /**
     * Inicjalizuje kontroler — uruchamia zegar, konfiguruje kolumny tabeli i
     * ładuje dane paczek z bazy danych, autoamtycznie odświeża tabelę.
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

        Platform.runLater(() -> {
            Stage stage = (Stage) magButton.getScene().getWindow();
            WindowConstraints.applyMinSize(stage);
        });
        
        Timeline autoRefresh = new Timeline(
                new KeyFrame(Duration.seconds(5), event -> {
                    List<Package> updated = dao.getPackages();
                    List<PackageTableService> updatedData = updated.stream()
                            .map(PackageTableService::new)
                            .collect(Collectors.toList());
                    packageTable.getItems().setAll(updatedData);
               
                })
        );
        autoRefresh.setCycleCount(Timeline.INDEFINITE);
        autoRefresh.play();
    }

    /**
     * Przechodzi do okna głównego magazynu.
     */
    @FXML
    private void handleMagButton() {
        loadWindow("/com/mycompany/projekt_io/werehouseMainWindow.fxml");
    }

    /**
     * Odświeża bieżące okno tabeli paczek.
     */
    @FXML
    private void handlePacButton() {
        loadWindow("/com/mycompany/projekt_io/packageTableWindow.fxml");
    }

    /**
     * Przechodzi do okna zarządzania użytkownikami.
     */
    @FXML
    private void handleUserButton() {
        loadWindow("/com/mycompany/projekt_io/userManageWindow.fxml");
    }

    /**
     * Przechodzi do okna dodawania nowej paczki.
     */
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
            showAlert("Błąd", "Nie wybrano żadnej paczki");
            return;
        }

        if (selected.getShelf().equals("0")) {
            showAlert("Brak regału",
                    "Nie można edytować paczki.\n\nPaczka nie została jeszcze przypisana do strefy magazynowej.");
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
     * @param title tytuł okna dialogowego
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