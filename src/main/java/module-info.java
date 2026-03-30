module com.mycompany.projekt_io {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.mycompany.projekt_io to javafx.fxml;
    exports com.mycompany.projekt_io;
}
