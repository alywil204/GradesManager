package main;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.sql.SQLIntegrityConstraintViolationException;
import java.text.NumberFormat;
import java.util.List;

public class CourseCreateControl {
    @FXML
    private TextField courseNameField;
    @FXML
    private Text courseNameErrorText;
    @FXML
    private TableView<GradeLimit> gradeTable;
    @FXML
    private TableColumn<GradeLimit, String> gradeColumn;
    @FXML
    private TableColumn<GradeLimit, GradeLimit> limitColumn;
    @FXML
    private Text gradeTableErrorText;
    @FXML
    private Text createCourseErrorText;

    private int pId;

    private GradeLimitSet gradeLimitSet;

    private static NumberFormat nf = NumberFormat.getNumberInstance();

    @FXML
    public void initialize() {
        courseNameField.setOnAction(actionEvent -> courseNameErrorText.setText(""));

        gradeColumn.setCellValueFactory(gradeLimitStringCellDataFeatures -> new ReadOnlyStringWrapper(gradeLimitStringCellDataFeatures.getValue().getGrade().toString()));
        limitColumn.setCellValueFactory(cdf -> Bindings.createObjectBinding(cdf::getValue));
        limitColumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<GradeLimit, GradeLimit> call(TableColumn<GradeLimit, GradeLimit> param) {

                return new TableCell<>() {

                    private final EditableSpinner limitSpinner;

                    private final SpinnerValueFactory.DoubleSpinnerValueFactory valueFactory;
                    private final ChangeListener<Number> valueChangeListener;

                    {
                        limitSpinner = new EditableSpinner();
                        valueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 100);
                        limitSpinner.setValueFactory(valueFactory);
                        limitSpinner.setVisible(false);
                        setGraphic(limitSpinner);
                        valueChangeListener = (ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> valueFactory.setValue(newValue.doubleValue());
                        limitSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
                            if (getItem() != null) {
                                // write new value to table item
                                getItem().setLimit(newValue);
                            }
                        });
                    }

                    @Override
                    public void updateItem(GradeLimit gradeLimit, boolean empty) {

                        // unbind old values
                        if (getItem() != null) {
                            getItem().limitProperty().removeListener(valueChangeListener);
                        }

                        super.updateItem(gradeLimit, empty);

                        // update according to new item
                        if (empty || gradeLimit == null) {
                            limitSpinner.setVisible(false);
                        } else {
                            valueFactory.setValue(gradeLimit.getLimit());
                            gradeLimit.limitProperty().addListener(valueChangeListener);
                            limitSpinner.setVisible(true);
                        }

                    }
                };
            }
        });
        gradeTable.setItems(gradeLimitSet.getGradeLimits());
    }

    public CourseCreateControl(int profileId) {
        pId = profileId;
        gradeLimitSet = new GradeLimitSet(90, 85, 80, 75, 70, 65, 60, 55, 50);
    }

    @FXML
    private void CreateCoursePressed(ActionEvent event) {
        courseNameErrorText.setText("");
        createCourseErrorText.setText("");
        gradeTableErrorText.setText("");
        boolean anyError = false;
        String cName = courseNameField.getText().trim();
       if (cName.isEmpty()) {
            courseNameErrorText.setText("Please enter a new course name");
            anyError = true;
        }
        else {
            Boolean courseNameInUse = Database.checkCourseNameInUse(pId, cName);
            if (courseNameInUse == null) {
                createCourseErrorText.setText("Database error");
                anyError = true;
            }
            else if (courseNameInUse) {
                courseNameErrorText.setText("Course name already in use");
                anyError = true;
            }
        }
        List<GradeLimit> grades = gradeTable.getItems();
        for (int i = 0; i < grades.size() - 1; i++) {
            if (grades.get(i).getLimit() <= grades.get(i + 1).getLimit()) {
                gradeTableErrorText.setText("The limit for each grade must be higher than the limit for all lower grades");
                anyError = true;
                break;
            }
        }
        if (!anyError) {
            try {
                Database.createCourse(pId, cName, grades.get(0).getLimit(), grades.get(1).getLimit(), grades.get(2).getLimit(), grades.get(3).getLimit(), grades.get(4).getLimit(), grades.get(5).getLimit(), grades.get(6).getLimit(), grades.get(7).getLimit(), grades.get(8).getLimit());
                Main.returnToCourseSelect();
            } catch (SQLIntegrityConstraintViolationException e) {
                createCourseErrorText.setText("Database error.");
            }
        }
    }

    @FXML
    private void ReturnPressed(ActionEvent event) {
        Main.returnToCourseSelect();
    }
}
