package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.text.NumberFormat;

public class CourseControl {

    @FXML
    private Text profileText;
    @FXML
    private Text courseText;
    @FXML
    private TableView<CourseCategory> categoryTable;
    @FXML
    private TableColumn<CourseCategory, String> categoryNameColumn;
    @FXML
    private TableColumn<CourseCategory, Double> categoryWeightColumn;
    @FXML
    private Text AplusText;
    @FXML
    private Text AText;
    @FXML
    private Text AminusText;
    @FXML
    private Text BplusText;
    @FXML
    private Text BText;
    @FXML
    private Text BminusText;
    @FXML
    private Text CplusText;
    @FXML
    private Text CText;
    @FXML
    private Text DText;
    @FXML
    private ChoiceBox<CourseCategory> categoryChoiceBox;
    @FXML
    private RadioButton singleGradeRadio;
    @FXML
    private RadioButton multipleGradeRadio;
    @FXML
    private TableView assignmentTable;
    @FXML
    private Label assignmentNameLabel;
    @FXML
    private TextField assignmentNameField;
    @FXML
    private Button addAssignmentButton;
    @FXML
    private Label maxPointsLabel;
    @FXML
    private Spinner maxPointsSpinner;
    @FXML
    private Label pointsLabel;
    @FXML
    private Spinner pointsSpinner;

    private Profile profile;
    private Course course;

    private NumberFormat nf = NumberFormat.getNumberInstance();

    @FXML
    private void initialize() {
        profileText.setText(profile.getPName());
        courseText.setText(course.getCName());


        ObservableList<CourseCategory> observableCategoryList = FXCollections.observableList(course.getCategories());
        categoryNameColumn.setCellValueFactory(courseCategoryStringCellDataFeatures -> courseCategoryStringCellDataFeatures.getValue().titleProperty());
        categoryWeightColumn.setCellValueFactory(courseCategoryFloatCellDataFeatures -> courseCategoryFloatCellDataFeatures.getValue().percentageProperty().asObject());
        categoryTable.setItems(observableCategoryList);
        categoryChoiceBox.setItems(observableCategoryList);
        categoryChoiceBox.selectionModelProperty().getValue().select(0);
        AplusText.setText(nf.format(course.getAplusGrade()));
        AText.setText(nf.format(course.getAGrade()));
        AminusText.setText(nf.format(course.getAminusGrade()));
        BplusText.setText(nf.format(course.getBplusGrade()));
        BText.setText(nf.format(course.getBGrade()));
        BminusText.setText(nf.format(course.getBminusGrade()));
        CplusText.setText(nf.format(course.getCplusGrade()));
        CText.setText(nf.format(course.getCGrade()));
        DText.setText(nf.format(course.getDGrade()));
        singleGradeRadio.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
            maxPointsLabel.setVisible(t1);
            maxPointsSpinner.setVisible(t1);
            pointsLabel.setVisible(t1);
            pointsSpinner.setVisible(t1);
        });
        multipleGradeRadio.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
            assignmentTable.setVisible(t1);
            assignmentNameLabel.setVisible(t1);
            assignmentNameField.setVisible(t1);
            addAssignmentButton.setVisible(t1);
        });
    }

    public CourseControl(Profile profile, Course course) {
        this.profile = profile;
        this.course = course;
    }

    @FXML
    private void ReturnPressed() {
        Main.returnToCourseSelect();
    }
}
