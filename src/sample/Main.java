package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.SQLNonTransientConnectionException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Grades Manager");

        AnchorPane root;
        try {
            Database.openConnection();
            primaryStage.setOnCloseRequest(windowEvent -> Database.closeConnection());
            root = FXMLLoader.load(getClass().getResource("ProfileSelectUI.fxml"));
        } catch (SQLNonTransientConnectionException e) {
            root = new AnchorPane();
            root.setPrefSize(600, 400);
            Text text = new Text("Database connection unavailable.");
            root.getChildren().add(text);
            AnchorPane.setTopAnchor(text, 0.0);
            AnchorPane.setLeftAnchor(text, 0.0);
        }
        primaryStage.setScene(new Scene(root, root.getPrefWidth(), root.getPrefHeight()));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
