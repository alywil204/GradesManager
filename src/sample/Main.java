package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        AnchorPane root = FXMLLoader.load(getClass().getResource("ProfileSelectUI.fxml"));
        primaryStage.setTitle("Grades Manager");
        primaryStage.setScene(new Scene(root, root.getPrefWidth(), root.getPrefHeight()));
        primaryStage.setOnCloseRequest(windowEvent -> Database.closeConnection());
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
