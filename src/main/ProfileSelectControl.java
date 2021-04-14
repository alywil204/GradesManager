package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

public class ProfileSelectControl {

    @FXML
    private ListView<Profile> profileListView;
    @FXML
    private Text selectProfileErrorText;
    @FXML
    private TextField newProfileNameField;
    @FXML
    private Text createProfileErrorText;

    @FXML
    private void initialize() {
        profileListView.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2) {
                selectProfile();
            }
        });
        updateProfileList();
    }

    private void updateProfileList() {
        List<Profile> profileList = Database.getProfileList();
        if (profileList == null) {
            selectProfileErrorText.setText("Unable to retrieve profile list.");
            profileListView.setItems(null);
            return;
        }
        ObservableList<Profile> observableProfileList = FXCollections.observableList(profileList);
        profileListView.setItems(observableProfileList);
    }

    private void selectProfile() {
        Profile selected = profileListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            selectProfileErrorText.setText("Please select a profile to open");
            return;
        }
        Main.courseSelectUseCase(selected);
    }

    @FXML
    private void SelectProfilePressed() {
        selectProfile();
    }

    @FXML
    private void CreatePressed() {
        newProfileNameField.setText(newProfileNameField.getText().trim());
        String newPName = newProfileNameField.getText();
        if (newPName.isBlank()) {
            createProfileErrorText.setText("Please enter a name for the new profile");
            return;
        }
        try {
            Database.createProfile(newPName);
            createProfileErrorText.setText("");
        } catch (SQLIntegrityConstraintViolationException e) {
            createProfileErrorText.setText("Name conflicts with existing");
        }
        updateProfileList();
    }
}
