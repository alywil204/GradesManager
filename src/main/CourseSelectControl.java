package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;

import java.util.List;

public class CourseSelectControl {

    @FXML
    private Text profileText;
    @FXML
    private ListView<String> courseNameList;
    @FXML
    private Text errorText;

    private Profile profile;

    public CourseSelectControl(Profile profile) {
        this.profile = profile;
    }

    @FXML
    private void initialize() {
        courseNameList.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2) {
                selectCourse();
            }
        });
        profileText.setText("Profile: " + profile.getPName());
        List<String> courses = Database.getProfileCourseList(profile.getPId());
        if (courses != null) {
            ObservableList<String> observableCoursesList = FXCollections.observableList(courses);
            courseNameList.setItems(observableCoursesList);
        }
        else {
            errorText.setText("Database error.");
        }
    }

    @FXML
    private void selectCourse() {
        String selected = courseNameList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            errorText.setText("Please select a course to open");
            return;
        }
        Course course = Database.getCourse(profile.getPId(), selected);
        if (course == null) {
            errorText.setText("Database error.");
        }
        Main.mainCourseUseCase(course);
    }

    @FXML
    private void SelectCourse() {
        selectCourse();
    }

    @FXML
    private void CreateCourse() {
        Main.createCourseUseCase();
    }

    @FXML
    private void ReturnPressed() {
        Main.returnToProfileSelect();
    }
}
