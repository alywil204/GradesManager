package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
    private void SelectCourse(ActionEvent event) {
        String selected = courseNameList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            errorText.setText("Please select a course to open");
            return;
        }
        Course course = Database.getCourse(profile.getPId(), selected);
        Main.mainCourseUseCase(course);
    }

    @FXML
    public void CreateCourse(ActionEvent event) {
        Main.createCourseUseCase();
    }

    @FXML
    private void ReturnPressed() {
        Main.returnToProfileSelect();
    }
}
