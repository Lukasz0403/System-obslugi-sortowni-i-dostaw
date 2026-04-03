package com.mycompany.projekt_io;

import com.mycompany.projekt_io.core.database.PackageDAO;
import com.mycompany.projekt_io.core.database.UserDAO;
import com.mycompany.projekt_io.datamodel.Package;
import com.mycompany.projekt_io.datamodel.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("loginWindow"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        
        PackageDAO p1 = new PackageDAO();
        
        Package u = p1.getPackage(3);
        
        System.out.println(u);
        
        launch();
        
        List<Package> l1 = p1.getPackages();
        
        for(Package ul : l1) {
            System.out.println(ul);
        }
        
    }

}