package main;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.text.Text;

import java.sql.SQLIntegrityConstraintViolationException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    private EditableSpinner categoryWeightSpinner;
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
    private EditableSpinner pointsAwardedSpinner;
    @FXML
    private EditableSpinner pointsWorthSpinner;
    @FXML
    private EditableSpinner assignmentWeightSpinner;
    @FXML
    private Button aDeleteButton;
    @FXML
    private Text assignmentChoiceErrorText;
    @FXML
    private Text saveAssignmentErrorText;

    @FXML
    private TreeTableView<AssignmentTableRecord> assignmentTreeTableView;
    @FXML
    private TreeTableColumn<AssignmentTableRecord, String> assignmentColumn;
    @FXML
    private TreeTableColumn<AssignmentTableRecord, String> scoreColumn;
    @FXML
    private TreeTableColumn<AssignmentTableRecord, String> weightColumn;
    @FXML
    private Text totalWeightAchievedText;
    @FXML
    private Text gradeNeededAplusText;
    @FXML
    private Text gradeNeededAText;
    @FXML
    private Text gradeNeededAminusText;
    @FXML
    private Text gradeNeededBplusText;
    @FXML
    private Text gradeNeededBText;
    @FXML
    private Text gradeNeededBminusText;
    @FXML
    private Text gradeNeededCplusText;
    @FXML
    private Text gradeNeededCText;
    @FXML
    private Text gradeNeededDText;

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
                assignmentWeightSpinner.getValueFactory().setValue((double) t1.getAWeight());
                pointsWorthSpinner.getValueFactory().setValue((double) t1.getPointsDenominator());
                pointsAwardedSpinner.getValueFactory().setValue((double) t1.getPointsNumerator());
                if (t1.getCatId() == 0) {
                    assignmentCategoryChoiceBox.setValue(noneCat);
                } else {
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

        assignmentTreeTableView.setShowRoot(false);
        assignmentColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("name"));
        scoreColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("score"));
        weightColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("weight"));
        showResults();
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

        showResults();
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
                saveAssignmentErrorText.setText("Assignment name conflicts with existing");
            }
        }
        reloadCourseChange();
    }

    @FXML
    private void SaveCategory(ActionEvent event) {
        saveCategoryErrorText.setText("");
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
    private void DeleteAssignmentPressed(ActionEvent event) {
        deleteAssignment(assignmentChoiceBox.getValue().getAId());
        reloadCourseChange();
    }

    private void deleteAssignment(int aId) {
        Database.deleteCourseAssignment(aId);
    }

    @FXML
    private void DeleteCategory(ActionEvent event) {
        saveCategoryErrorText.setText("");
        CourseCategory cat = categoryChoiceBox.getValue();
        try {
            Database.deleteCourseCategory(cat.getCatId());
        } catch (SQLIntegrityConstraintViolationException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Deleting category \"" + cat.getCatName() + "\" will also delete all assignments belonging to the category.");
            alert.setContentText("Would you like to delete this category and all of it's assignments?");

            ButtonType confirm = new ButtonType("Confirm Delete", ButtonBar.ButtonData.YES);
            ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(confirm, cancel);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == confirm) {
                for (CourseAssignment a : course.getAssignments()) {
                    if (a.getCatId() == cat.getCatId()) {
                        deleteAssignment(a.getAId());
                    }
                }
                try {
                    Database.deleteCourseCategory(cat.getCatId());
                } catch (SQLIntegrityConstraintViolationException e2) {
                    saveCategoryErrorText.setText("Database error.");
                }
            }
            else {
                return;
            }
        }
        reloadCourseChange();
    }

    private void showResults() {
        TreeItem<AssignmentTableRecord> rootRow = new TreeItem<>(new AssignmentTableRecord("Root", "", ""));

        double possibleWeight = 0;
        double weightAchieved = 0;

        for (CourseCategory category : course.getCategories()) {
            List<TreeItem<AssignmentTableRecord>> assignmentRecords = new ArrayList<>();

            possibleWeight += category.getCatWeight();
            double catScore = 0;

            for (CourseAssignment assignment : course.getAssignments()) {
                if (assignment.getCatId() == category.getCatId()) {
                    assignmentRecords.add(new TreeItem<>(new AssignmentTableRecord(assignment.getAName(), nf.format(assignment.getScore() * 100.0), nf.format(assignment.getAWeight()))));

                    catScore += assignment.getScore()*assignment.getAWeight()/100.0;
                }
            }
            TreeItem<AssignmentTableRecord> categoryRow = new TreeItem<>(new AssignmentTableRecord(category.getCatName(), nf.format(catScore*100.0), nf.format(category.getCatWeight())));
            categoryRow.getChildren().addAll(assignmentRecords);
            rootRow.getChildren().add(categoryRow);

            categoryRow.setExpanded(true);
            weightAchieved += catScore*category.getCatWeight();
        }
        for (CourseAssignment assignment : course.getAssignments()) {
            if (assignment.getCatId() == 0) {
                rootRow.getChildren().add(new TreeItem<>(new AssignmentTableRecord(assignment.getAName(), nf.format(assignment.getScore()*100.0), nf.format(assignment.getAWeight()))));

                possibleWeight += assignment.getAWeight();
                weightAchieved += assignment.getScore()*assignment.getAWeight();
            }
        }
        assignmentTreeTableView.setRoot(rootRow);

        double examWeight = 100 - possibleWeight;
        totalWeightAchievedText.setText(nf.format(weightAchieved)+"/"+nf.format(possibleWeight));
        gradeNeededAplusText.setText(nf.format(calculateScoreNeeded(course.getAplusGrade(),weightAchieved,examWeight)*100.0));
        gradeNeededAText.setText(nf.format(calculateScoreNeeded(course.getAGrade(),weightAchieved,examWeight)*100.0));
        gradeNeededAminusText.setText(nf.format(calculateScoreNeeded(course.getAminusGrade(),weightAchieved,examWeight)*100.0));
        gradeNeededBplusText.setText(nf.format(calculateScoreNeeded(course.getBplusGrade(),weightAchieved,examWeight)*100.0));
        gradeNeededBText.setText(nf.format(calculateScoreNeeded(course.getBGrade(),weightAchieved,examWeight)*100.0));
        gradeNeededBminusText.setText(nf.format(calculateScoreNeeded(course.getBminusGrade(),weightAchieved,examWeight)*100.0));
        gradeNeededCplusText.setText(nf.format(calculateScoreNeeded(course.getCplusGrade(),weightAchieved,examWeight)*100.0));
        gradeNeededCText.setText(nf.format(calculateScoreNeeded(course.getCGrade(),weightAchieved,examWeight)*100.0));
        gradeNeededDText.setText(nf.format(calculateScoreNeeded(course.getDGrade(),weightAchieved,examWeight)*100.0));
    }

    private double calculateScoreNeeded(double maxGrade, double weightAchieved, double examWeight) {
        return Math.max(0, (maxGrade-weightAchieved)/examWeight);
    }

    @FXML
    private void ReturnPressed() {
        Main.returnToCourseSelect();
    }

    public static class AssignmentTableRecord {

        private final SimpleStringProperty name;
        private final SimpleStringProperty score;
        private final SimpleStringProperty weight;

        public AssignmentTableRecord(String name, String score, String weight) {
            this.name = new SimpleStringProperty(name);
            this.score = new SimpleStringProperty(score);
            this.weight = new SimpleStringProperty(weight);
        }

        public SimpleStringProperty nameProperty() {
            return name;
        }

        public SimpleStringProperty scoreProperty() {
            return score;
        }

        public SimpleStringProperty weightProperty() {
            return weight;
        }
    }
}
