package sample;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class CourseSelectControl {

    @FXML
    private Text profileText;

    private Profile profile;

    public CourseSelectControl(Profile profile) {
        this.profile = profile;
    }

    @FXML
    private void initialize() {
        profileText.setText("Profile: " + profile.getPName());
    }

    @FXML
    private void Return() {
        Main.returnToProfileSelect();
    }
}
