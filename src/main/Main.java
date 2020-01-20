package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLNonTransientConnectionException;

public class Main extends Application {

    private static Stage primaryStage;

    private static Profile selected;

    @Override
    public void start(Stage primaryStage) {
        Main.primaryStage = primaryStage;
        primaryStage.setTitle("Grades Manager");

        try {
            Database.openConnection();
            primaryStage.setOnCloseRequest(windowEvent -> Database.closeConnection());
            profileSelectUseCase();
        } catch (SQLNonTransientConnectionException e) {
            AnchorPane root = new AnchorPane();
            root.setPrefSize(800, 600);
            Text text = new Text("Database connection unavailable.");
            root.getChildren().add(text);
            AnchorPane.setTopAnchor(text, 0.0);
            AnchorPane.setLeftAnchor(text, 0.0);
            primaryStage.setScene(new Scene(root));
        }
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void profileSelectUseCase() {
        AnchorPane root;
        try {
            root = FXMLLoader.load(Main.class.getResource("ProfileSelectUI.fxml"));
        } catch (IOException e) {
            root = new AnchorPane();
            root.setPrefSize(800, 600);
            Text text = new Text("Could not load profile select ui.");
            root.getChildren().add(text);
            AnchorPane.setTopAnchor(text, 0.0);
            AnchorPane.setLeftAnchor(text, 0.0);
        }
        primaryStage.setScene(new Scene(root));
        selected = null;
    }

    public static void returnToProfileSelect() {
        profileSelectUseCase();
    }

    public static void courseSelectUseCase(Profile selected) {
        AnchorPane root;
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("CourseSelectUI.fxml"));
            loader.setControllerFactory(aClass -> new CourseSelectControl(selected));
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            root = new AnchorPane();
            root.setPrefSize(800, 600);
            Text text = new Text("Could not load course select ui.");
            root.getChildren().add(text);
            AnchorPane.setTopAnchor(text, 0.0);
            AnchorPane.setLeftAnchor(text, 0.0);
        }
        primaryStage.setScene(new Scene(root));
        Main.selected = selected;
    }

    public static void returnToCourseSelect() {
        courseSelectUseCase(selected);
    }
    
    public static void createCourseUseCase() {
        AnchorPane root;
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("CourseCreateUI.fxml"));
            loader.setControllerFactory(aClass -> new CourseCreateControl(selected.getPId()));
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            root = new AnchorPane();
            root.setPrefSize(800, 600);
            Text text = new Text("Could not load course create ui.");
            root.getChildren().add(text);
            AnchorPane.setTopAnchor(text, 0.0);
            AnchorPane.setLeftAnchor(text, 0.0);
        }
        primaryStage.setScene(new Scene(root));
    }

    public static void mainCourseUseCase(Course course) {
        AnchorPane root;
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("CourseUI.fxml"));
            loader.setControllerFactory(aClass -> new CourseControl(selected, course));
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            root = new AnchorPane();
            root.setPrefSize(800, 600);
            Text text = new Text("Could not load main course ui.");
            root.getChildren().add(text);
            AnchorPane.setTopAnchor(text, 0.0);
            AnchorPane.setLeftAnchor(text, 0.0);
        }
        primaryStage.setScene(new Scene(root));
    }
}
