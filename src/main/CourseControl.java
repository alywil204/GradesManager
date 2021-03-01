package main;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.sql.SQLIntegrityConstraintViolationException;
import java.text.NumberFormat;

public class CourseControl {

    @FXML
    private Text profileText;
    @FXML
    private Text courseText;
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
    private TextField categoryNameField;
    @FXML
    private Spinner<Double> categoryWeightSpinner;
    @FXML
    private Button catDeleteButton;
    @FXML
    private Text categoryChoiceErrorText;
    @FXML
    private Text saveCategoryText;

    private Profile profile;
    private Course course;
    private final CourseCategory createNewCat = new CourseCategory(0, "Create New", 0);

    private NumberFormat nf = NumberFormat.getNumberInstance();

    @FXML
    private void initialize() {
        profileText.setText(profile.getPName());
        courseText.setText(course.getCName());

        AplusText.setText(nf.format(course.getAplusGrade()));
        AText.setText(nf.format(course.getAGrade()));
        AminusText.setText(nf.format(course.getAminusGrade()));
        BplusText.setText(nf.format(course.getBplusGrade()));
        BText.setText(nf.format(course.getBGrade()));
        BminusText.setText(nf.format(course.getBminusGrade()));
        CplusText.setText(nf.format(course.getCplusGrade()));
        CText.setText(nf.format(course.getCGrade()));
        DText.setText(nf.format(course.getDGrade()));

        categoryWeightSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 100, 0, 1));

        categoryChoiceBox.getSelectionModel().selectedItemProperty().addListener((observableValue, courseCategory, t1) -> {
            if (t1 == createNewCat) {
                categoryNameField.setText("");
                categoryWeightSpinner.getValueFactory().setValue(0.0);
                catDeleteButton.setVisible(false);
                return;
            }
            else if (t1 != null) {
                categoryNameField.setText(t1.getCatName());
                categoryWeightSpinner.getValueFactory().setValue((double)t1.getCatWeight());
            }
            catDeleteButton.setVisible(true);
        });
        categoryChoiceBox.getItems().add(createNewCat);
        categoryChoiceBox.setValue(categoryChoiceBox.getItems().get(0));
        categoryChoiceBox.getItems().addAll(course.getCategories());
    }

    public CourseControl(Profile profile, Course course) {
        this.profile = profile;
        this.course = course;
    }

    private void reloadCourse() {
        CourseCategory cat = categoryChoiceBox.getValue();
        if (cat != createNewCat) {
            categoryChoiceBox.setValue(null);
        }

        ObservableList<CourseCategory> itemList = categoryChoiceBox.getItems();
        itemList.removeAll(course.getCategories());
        course = Database.getCourse(profile.getPId(), course.getCName());
        if (course == null) {
            categoryChoiceErrorText.setText("Database error");
        }
        else {
            categoryChoiceErrorText.setText("");
            itemList.addAll(course.getCategories());
        }

        for (CourseCategory item : categoryChoiceBox.getItems()) {
            if (cat.getCatId() == item.getCatId()) {
                categoryChoiceBox.setValue(item);
                return;
            }
        }
    }

    @FXML
    private void SaveCategory(ActionEvent event) {
        CourseCategory cat = categoryChoiceBox.getValue();
        categoryNameField.setText(categoryNameField.getText().trim());
        String name = categoryNameField.getText();
        float weight = categoryWeightSpinner.getValue().floatValue();
        try {
            if (cat.getCatId() == 0) {
                Database.createCourseCategory(course.getCId(), name, weight);
                categoryNameField.setText("");
                categoryWeightSpinner.getValueFactory().setValue(0.0);
            } else {
                Database.updateCourseCategory(cat.getCatId(), name, weight);
            }
            saveCategoryText.setText("");
        } catch (SQLIntegrityConstraintViolationException e) {
            if (weight == 0) {
                saveCategoryText.setText("Category weight must be greater than 0");
            }
            else {
                saveCategoryText.setText("Category name conflicts with existing");
            }
        }
        reloadCourse();
    }

    @FXML
    private void DeleteCategory(ActionEvent event) {
        CourseCategory cat = categoryChoiceBox.getValue();
        Database.deleteCourseCategory(cat.getCatId());
        reloadCourse();
    }

    @FXML
    private void ReturnPressed() {
        Main.returnToCourseSelect();
    }
}
