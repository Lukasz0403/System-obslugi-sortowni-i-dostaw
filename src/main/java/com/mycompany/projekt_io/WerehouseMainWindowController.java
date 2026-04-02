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
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.IOException;
import javafx.scene.Cursor;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

/**
 * FXML Controller class
 */
public class WerehouseMainWindowController implements Initializable {

    @FXML private Label timeLabel;
    @FXML private Label dateLabel;

    @FXML private Button magButton;
    @FXML private Button pacButton;
    @FXML private Button addButton;

    // SHELVES
    @FXML private Rectangle shelf1;
    @FXML private Rectangle shelf2;
    @FXML private Rectangle shelf3;
    @FXML private Rectangle shelf4;
    @FXML private Rectangle shelfex;

    @FXML private AnchorPane mainPane;

    // INFO PANEL
    @FXML private VBox infoPanel;
    @FXML private Label info1;
    @FXML private Label info2;
    @FXML private Label info3;

    // TABLE
    @FXML private TableView<WarehouseItem> warehouseTable;
    @FXML private TableColumn<WarehouseItem, String> col1;
    @FXML private TableColumn<WarehouseItem, String> col2;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // DATE / TIME----------------------------------------------------------
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
        
        // TABLE INIT-----------------------------------------------------------
        col1.setCellValueFactory(new PropertyValueFactory<>("col1"));
        col2.setCellValueFactory(new PropertyValueFactory<>("col2"));
        
        // TEMP DATA------------------------------------------------------------
        warehouseTable.getItems().addAll(
            new WarehouseItem("KOCHAM", "CAŁOWAĆ"),
            new WarehouseItem("TWOJĄ", "MAMĘ")
        );

        //HOVER WINDOW TEMP DATA------------------------------------------------
        setupShelfHover(shelf1, "aaa", "bbb", "ccc");
        setupShelfHover(shelf2, "ddd", "eee", "fff");
        setupShelfHover(shelf3, "ggg", "hhh", "iii");
        setupShelfHover(shelf4, "jjj", "kkk", "lll");
        setupShelfHover(shelfex, "mmm", "nnn", "ooo");

        // SIDE MENU------------------------------------------------------------
        magButton.setOnAction(e -> loadWindow("/com/mycompany/projekt_io/werehouseMainWindow.fxml"));
        pacButton.setOnAction(e -> loadWindow("/com/mycompany/projekt_io/packageTableWindow.fxml"));
        addButton.setOnAction(e -> loadWindow("/com/mycompany/projekt_io/userManageWindow.fxml"));
    }

    /**
     * HOVER AND SHELF CLICK----------------------------------------------------
     */
    private void setupShelfHover(Rectangle shelf, String s1, String s2, String s3) {
         // HOVER FUNCTIONALITY-------------------------------------------------
        shelf.setOnMouseEntered(e -> {
             info1.setText(s1);
             info2.setText(s2);
             info3.setText(s3);
             infoPanel.setVisible(true);

             updateInfoPanelPosition(shelf, e.getSceneX(), e.getSceneY());
         });

         // WINDOW FOLLOWS CURSOR-----------------------------------------------
         shelf.setOnMouseMoved(e -> updateInfoPanelPosition(shelf, e.getSceneX(), e.getSceneY()));

         // HIDE INFO WINDOW----------------------------------------------------
         shelf.setOnMouseExited(e -> infoPanel.setVisible(false));

         // SHELF CLICK---------------------------------------------------------
         shelf.setOnMouseClicked(e -> openWarehouseInfo());
         shelf.setCursor(Cursor.HAND);
     }

     // METHOD FOR POSITIONING--------------------------------------------------
     private void updateInfoPanelPosition(Rectangle shelf, double sceneX, double sceneY) {
         double localX = mainPane.sceneToLocal(sceneX, sceneY).getX();
         double localY = mainPane.sceneToLocal(sceneX, sceneY).getY();

         // WINDOW OFFSET-------------------------------------------------------
         infoPanel.setLayoutX(localX + 30);
         infoPanel.setLayoutY(localY + 30);
     }

    /**
     * OPENING SHELF INFO-------------------------------------------------------
     */
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

    /**
     * FXML --------------------------------------------------------------------
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
    
        // TEMP TABLE CLASS-----------------------------------------------------
    public static class WarehouseItem {
        private final String col1;
        private final String col2;

        public WarehouseItem(String col1, String col2) {
            this.col1 = col1;
            this.col2 = col2;
        }

        public String getCol1() { return col1; }
        public String getCol2() { return col2; }
    }
}