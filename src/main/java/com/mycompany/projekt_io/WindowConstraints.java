package com.mycompany.projekt_io;

import javafx.stage.Stage;

public class WindowConstraints {
    public static final double MIN_WIDTH = 600.0;
    public static final double MIN_HEIGHT = 450.0;

    public static void applyMinSize(Stage stage) {
        stage.setMinWidth(MIN_WIDTH);
        stage.setMinHeight(MIN_HEIGHT);
    }
}
