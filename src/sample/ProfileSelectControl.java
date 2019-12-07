package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.util.List;

public class ProfileSelectControl {

    @FXML
    private TextField newProfileNameField;
    @FXML
    private ListView<Profile> profileListView;

    @FXML
    public void initialize() {
        updateProfileList();
    }

    private void updateProfileList() {
        List<Profile> profileList = Database.getProfileList();
        if (profileList == null) return; // TODO: error handling
        ObservableList<Profile> observableProfileList = FXCollections.observableList(profileList);
        profileListView.setItems(observableProfileList);
    }

    @FXML
    public void SelectProfile(ActionEvent event) {
    }

    @FXML
    public void CreatePressed(ActionEvent event) {
        String newPName = newProfileNameField.getText();
        if (newPName.isBlank()) return; // TODO: error handling
        Database.createProfile(newPName);
        updateProfileList();
    }
}
