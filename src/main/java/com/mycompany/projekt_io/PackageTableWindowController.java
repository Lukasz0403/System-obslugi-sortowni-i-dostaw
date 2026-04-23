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
    private TableView<PackageTableService> packageTable;

    @FXML
    private TableColumn<PackageTableService, Integer> idColumn;

    @FXML
    private TableColumn<PackageTableService, String> shelfColumn;

    @FXML
    private TableColumn<PackageTableService, String> sizeColumn;

    @FXML
    private TableColumn<PackageTableService, String> senregionColumn;
    
    @FXML
    private TableColumn<PackageTableService, String> recregionColumn;
    
    @FXML
    private TableColumn<PackageTableService, Double> weightColumn;
    

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
        senregionColumn.setCellValueFactory(new PropertyValueFactory<>("senderRegion"));
        recregionColumn.setCellValueFactory(new PropertyValueFactory<>("receiverRegion"));
        weightColumn.setCellValueFactory(new PropertyValueFactory<>("weight"));

        PackageDAO dao = new PackageDAO();

        List<Package> packages = dao.getPackages();

        
        List<PackageTableService> tableData = packages.stream()
                .map(PackageTableService::new)
                .collect(Collectors.toList());

        packageTable.getItems().setAll(tableData);
        
        //rozmiar min okna
        Platform.runLater(() -> {
        Stage stage = (Stage) magButton.getScene().getWindow();
        WindowConstraints.applyMinSize(stage);
        });
        
        
        
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

        PackageTableService selected = packageTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("Błąd", "Nie wybrano żadnej paczki");
            return;
        }

        if (selected.getShelf().equals("0")) {
            showAlert(
                    "Brak regału",
                    "Nie można edytować paczki.\n\nPaczka nie została jeszcze przypisana do strefy magazynowej."
            );
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/projekt_io/packageManageWindow.fxml"));
            Parent root = loader.load();

            
            
            PackageDAO dao = new PackageDAO();
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
    
    private void showAlert(String title, String content) {
    Alert alert = new Alert(Alert.AlertType.WARNING);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(content);
    alert.initOwner(timeLabel.getScene().getWindow());
    alert.showAndWait();
}
    
    

    
}