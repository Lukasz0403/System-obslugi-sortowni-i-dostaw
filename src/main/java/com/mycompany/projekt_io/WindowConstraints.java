package com.mycompany.projekt_io;

import javafx.stage.Stage;

public class WindowConstraints {

    public static final double MIN_WIDTH = 950.0;
    public static final double MIN_HEIGHT = 750.0;

    public static void applyMinSize(Stage stage) {
        stage.setMinWidth(MIN_WIDTH);
        stage.setMinHeight(MIN_HEIGHT);
        stage.setWidth(MIN_WIDTH);
        stage.setHeight(MIN_HEIGHT);
        stage.sizeToScene();
    }
}