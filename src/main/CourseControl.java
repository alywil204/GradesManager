package main;

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
    private Text saveCategoryErrorText;
    @FXML
    private ChoiceBox<CourseAssignment> assignmentChoiceBox;
    @FXML
    private ChoiceBox<CourseCategory> assignmentCategoryChoiceBox;
    @FXML
    private TextField assignmentNameField;
    @FXML
    private Spinner<Double> pointsAwardedSpinner;
    @FXML
    private Spinner<Double> pointsWorthSpinner;
    @FXML
    private Spinner<Double> assignmentWeightSpinner;
    @FXML
    private Button aDeleteButton;
    @FXML
    private Text assignmentChoiceErrorText;
    @FXML
    private Text saveAssignmentErrorText;

    private Profile profile;
    private Course course;

    private final CourseCategory createNewCat = new CourseCategory(0, "Create New", 0);
    private final CourseCategory noneCat = new CourseCategory(0, "None", 0);
    private final CourseAssignment createNewA = new CourseAssignment(0, 0, "Create New", 0, 0, 0);

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
            }
            else if (t1 != null) {
                categoryNameField.setText(t1.getCatName());
                categoryWeightSpinner.getValueFactory().setValue((double)t1.getCatWeight());
                catDeleteButton.setVisible(true);
            }
        });
        categoryChoiceBox.getItems().add(createNewCat);
        categoryChoiceBox.setValue(categoryChoiceBox.getItems().get(0));
        categoryChoiceBox.getItems().addAll(course.getCategories());

        pointsAwardedSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, Double.MAX_VALUE, 0, 1));
        pointsWorthSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, Double.MAX_VALUE, 0, 1));
        assignmentWeightSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, Double.MAX_VALUE, 0, 1));

        assignmentChoiceBox.getSelectionModel().selectedItemProperty().addListener((observableValue, courseAssignment, t1) -> {
            if (t1 == createNewA) {
                assignmentCategoryChoiceBox.setValue(noneCat);
                assignmentNameField.setText("");
                assignmentWeightSpinner.getValueFactory().setValue(0.0);
                pointsWorthSpinner.getValueFactory().setValue(0.0);
                pointsAwardedSpinner.getValueFactory().setValue(0.0);
                aDeleteButton.setVisible(false);
            }
            else if (t1 != null) {
                assignmentNameField.setText(t1.getAName());
                assignmentWeightSpinner.getValueFactory().setValue((double)t1.getAWeight());
                pointsWorthSpinner.getValueFactory().setValue((double)t1.getPointsDenominator());
                pointsAwardedSpinner.getValueFactory().setValue((double)t1.getPointsNumerator());
                if (t1.getCatId() == 0) {
                    assignmentCategoryChoiceBox.setValue(noneCat);
                }
                else {
                    for (CourseCategory item : assignmentCategoryChoiceBox.getItems()) {
                        if (t1.getCatId() == item.getCatId()) {
                            assignmentCategoryChoiceBox.setValue(item);
                            break;
                        }
                    }
                }
                aDeleteButton.setVisible(true);
            }
        });
        assignmentChoiceBox.getItems().add(createNewA);
        assignmentChoiceBox.setValue(createNewA);
        assignmentChoiceBox.getItems().addAll(course.getAssignments());

        assignmentCategoryChoiceBox.getItems().add(noneCat);
        assignmentCategoryChoiceBox.setValue(noneCat);
        assignmentCategoryChoiceBox.getItems().addAll(course.getCategories());
    }

    public CourseControl(Profile profile, Course course) {
        this.profile = profile;
        this.course = course;
    }

    private void reloadCourseChange() {
        CourseCategory catPrev = categoryChoiceBox.getValue();
        CourseCategory assignmentCatPrev = assignmentCategoryChoiceBox.getValue();
        CourseAssignment assignmentPrev = assignmentChoiceBox.getValue();
        if (catPrev != createNewCat) {
            categoryChoiceBox.setValue(null);
        }
        if (assignmentCatPrev != noneCat) {
            assignmentCategoryChoiceBox.setValue(null);
        }
        if (assignmentPrev != createNewA) {
            assignmentChoiceBox.setValue(null);
        }

        categoryChoiceBox.getItems().removeAll(course.getCategories());
        assignmentCategoryChoiceBox.getItems().removeAll(course.getCategories());
        assignmentChoiceBox.getItems().removeAll(course.getAssignments());

        course = Database.getCourse(profile.getPId(), course.getCName());

        if (course == null) {
            categoryChoiceErrorText.setText("Database error");
            assignmentChoiceErrorText.setText("Database error");
        }
        else {
            categoryChoiceErrorText.setText("");
            assignmentChoiceErrorText.setText("");
            categoryChoiceBox.getItems().addAll(course.getCategories());
            assignmentCategoryChoiceBox.getItems().addAll(course.getCategories());
            assignmentChoiceBox.getItems().addAll(course.getAssignments());

        }

        for (CourseCategory item : categoryChoiceBox.getItems()) {
            if (catPrev.getCatId() == item.getCatId()) {
                categoryChoiceBox.setValue(item);
                break;
            }
        }
        if (categoryChoiceBox.getSelectionModel().getSelectedItem() == null) {
            categoryChoiceBox.setValue(createNewCat);
        }

        for (CourseCategory item : assignmentCategoryChoiceBox.getItems()) {
            if (assignmentCatPrev.getCatId() == item.getCatId()) {
                assignmentCategoryChoiceBox.setValue(item);
                break;
            }
        }
        if (assignmentCategoryChoiceBox.getSelectionModel().getSelectedItem() == null) {
            assignmentCategoryChoiceBox.setValue(noneCat);
        }

        for (CourseAssignment item : assignmentChoiceBox.getItems()) {
            if (assignmentPrev.getAId() == item.getAId()) {
                assignmentChoiceBox.setValue(item);
                break;
            }
        }
        if (assignmentChoiceBox.getSelectionModel().getSelectedItem() == null) {
            assignmentChoiceBox.setValue(createNewA);
        }
    }

    @FXML
    private void SaveAssignment(ActionEvent event) {
        CourseAssignment as = assignmentChoiceBox.getValue();
        assignmentNameField.setText(assignmentNameField.getText().trim());
        String name = assignmentNameField.getText();
        float pointsAwarded = pointsAwardedSpinner.getValue().floatValue();
        float pointsWorth = pointsWorthSpinner.getValue().floatValue();
        float assignmentWeight = assignmentWeightSpinner.getValue().floatValue();
        CourseCategory cat = assignmentCategoryChoiceBox.getValue();
        try {
            if (as.getAId() == 0) {
                Database.createCourseAssignment(course.getCId(), cat.getCatId(), name, pointsAwarded, pointsWorth, assignmentWeight);
                assignmentNameField.setText("");
                assignmentCategoryChoiceBox.setValue(noneCat);
                pointsAwardedSpinner.getValueFactory().setValue(0.0);
                pointsWorthSpinner.getValueFactory().setValue(0.0);
                assignmentWeightSpinner.getValueFactory().setValue(0.0);
            }
            else {
                Database.updateCourseAssignment(as.getAId(), cat.getCatId(), name, pointsAwarded, pointsWorth, assignmentWeight);
            }
            saveAssignmentErrorText.setText("");
        } catch (SQLIntegrityConstraintViolationException e) {
            if (pointsWorth == 0) {
                saveAssignmentErrorText.setText("Assignment points total must be greater than 0");
            }
            else if (assignmentWeight == 0) {
                saveAssignmentErrorText.setText("Assignment weight must be greater than 0");
            }
            else {
                System.out.println(e.getMessage());
                saveAssignmentErrorText.setText("Assignment name conflicts with existing");
            }
        }
        reloadCourseChange();
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
            saveCategoryErrorText.setText("");
        } catch (SQLIntegrityConstraintViolationException e) {
            if (weight == 0) {
                saveCategoryErrorText.setText("Category weight must be greater than 0");
            }
            else {
                saveCategoryErrorText.setText("Category name conflicts with existing");
            }
        }
        reloadCourseChange();
    }

    @FXML
    private void DeleteCategory(ActionEvent event) {
        CourseCategory cat = categoryChoiceBox.getValue();
        Database.deleteCourseCategory(cat.getCatId());
        reloadCourseChange();
    }

    @FXML
    private void ReturnPressed() {
        Main.returnToCourseSelect();
    }
}
