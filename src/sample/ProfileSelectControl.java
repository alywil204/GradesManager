package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
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

    @FXML
    public void SelectProfile(ActionEvent event) {
        Profile selected = profileListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            selectProfileErrorText.setText("Please select a profile to open");
            return;
        }
        Main.courseSelectUseCase(selected);
    }

    @FXML
    public void CreatePressed(ActionEvent event) {
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
