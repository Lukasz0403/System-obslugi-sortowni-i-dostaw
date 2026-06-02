package com.mycompany.projekt_io;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Główna klasa aplikacji systemu obsługi sortowni kurierskiej.
 * <p>
 * Punkt wejścia aplikacji JavaFX inicjalizuje scenę z oknem logowania,
 * ustawia minimalne wymiary okna, tytuł oraz ikonę aplikacji.
 * </p>
 *
 */
public class App extends Application {

    private static Scene scene;

    /**
     * Uruchamia główne okno aplikacji.
     * <p>
     * Ładuje widok logowania z pliku {@code loginWindow.fxml}, ustawia
     * minimalne wymiary okna, maksymalizuje je, nadaje tytuł "Inventaro" oraz
     * ustawia ikonę aplikacji z pliku {@code logo.png}.
     * </p>
     *
     * @param stage główne okno JavaFX
     * @throws IOException jeśli nie można załadować pliku FXML
     */
    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("loginWindow"));
        stage.setScene(scene);
        WindowConstraints.applyMinSize(stage);
        stage.setMaximized(true);
        stage.setTitle("Inventaro");
        Image icon = new Image(getClass().getResourceAsStream("/logo.png"));
        stage.getIcons().add(icon);
        stage.show();
    }

    /**
     * Zastępuje korzeń bieżącej sceny widokiem załadowanym z podanego pliku
     * FXML.
     *
     * @param fxml nazwa pliku FXML bez rozszerzenia
     * @throws IOException jeśli nie można załadować pliku FXML
     */
    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    /**
     * Ładuje i zwraca korzeń widoku z podanego pliku FXML.
     *
     * @param fxml nazwa pliku FXML bez rozszerzenia
     * @return załadowany korzeń widoku
     * @throws IOException jeśli plik FXML nie istnieje lub zawiera błędy
     */
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    /**
     * Główna metoda uruchamiająca aplikację JavaFX.
     *
     * @param args argumenty wiersza poleceń (nieużywane)
     */
    public static void main(String[] args) {
        launch();
    }
}
