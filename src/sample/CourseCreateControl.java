package sample;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.text.NumberFormat;
import java.util.List;

public class CourseCreateControl {
    @FXML
    private TextField courseNameField;
    @FXML
    private Text courseNameErrorText;
    @FXML
    private TableView<CourseCategory> categoryTable;
    @FXML
    private TableColumn<CourseCategory, CourseCategory> categoryColumn;
    @FXML
    private TableColumn<CourseCategory, CourseCategory> percentageColumn;
    @FXML
    private TableColumn<CourseCategory, CourseCategory> deleteColumn;
    @FXML
    private TextField categoryNameField;
    @FXML
    private Text newCategoryErrorText;
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

    private ObservableList<CourseCategory> categories;
    private GradeLimitSet gradeLimitSet;

    private static NumberFormat nf = NumberFormat.getNumberInstance();

    @FXML
    public void initialize() {
        courseNameField.setOnAction(actionEvent -> courseNameErrorText.setText(""));
        categoryColumn.setCellValueFactory(cdf -> Bindings.createObjectBinding(cdf::getValue));
        categoryColumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<CourseCategory, CourseCategory> call(TableColumn<CourseCategory, CourseCategory> courseCategoryStringTableColumn) {
                return new TableCell<>() {
                    private final TextField categoryNameField = new TextField();

                    private void checkChangedCourseCategoryName(CourseCategory courseCategory) {
                        String newName = categoryNameField.getText().trim();
                        if (newName.isEmpty() || !checkUniqueCategoryName(newName)) {
                            categoryNameField.setText(courseCategory.getTitle());
                        } else {
                            categoryNameField.setText(newName);
                            courseCategory.setTitle(newName);
                        }
                    }

                    @Override
                    public void updateItem(CourseCategory courseCategory, boolean empty) {
                        super.updateItem(courseCategory, empty);

                        if (empty || courseCategory == null) {
                            setGraphic(null);
                        } else {
                            categoryNameField.setText(courseCategory.getTitle());
                            setGraphic(categoryNameField);
                            categoryNameField.setOnAction(actionEvent -> checkChangedCourseCategoryName(courseCategory));
                            categoryNameField.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
                                    if (!t1) {
                                        checkChangedCourseCategoryName(courseCategory);
                                    }
                            });
                        }
                    }
                };
            }
        });
        percentageColumn.setCellValueFactory(cdf -> Bindings.createObjectBinding(cdf::getValue));
        percentageColumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<CourseCategory, CourseCategory> call(TableColumn<CourseCategory, CourseCategory> param) {
                return new TableCell<>() {
                    private final Spinner<Double> percentageSpinner;

                    private final SpinnerValueFactory.DoubleSpinnerValueFactory valueFactory;
                    private final ChangeListener<Number> valueChangeListener;

                    {
                        valueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 100);
                        percentageSpinner = new Spinner<>(valueFactory);
                        percentageSpinner.setEditable(true);
                        percentageSpinner.setVisible(false);
                        setGraphic(percentageSpinner);
                        valueChangeListener = (ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> valueFactory.setValue(newValue.doubleValue());
                        percentageSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
                            if (getItem() != null) {
                                // write new value to table item
                                getItem().setPercentage(newValue);
                            }
                        });
                    }

                    private void adjustValue(CourseCategory courseCategory) {
                        String newVal = percentageSpinner.getEditor().getText().trim();
                        try {
                            double newDouble = Double.parseDouble(newVal);
                            if (newDouble < 0) {
                                percentageSpinner.getEditor().setText("0");
                            }
                            else if (newDouble > 100) {
                                percentageSpinner.getEditor().setText("100");
                            }
                            else {
                                percentageSpinner.getEditor().setText(newVal);
                            }
                        } catch (NumberFormatException e) {
                            percentageSpinner.getEditor().setText(nf.format(courseCategory.getPercentage()));
                        }
                    }

                    @Override
                    public void updateItem(CourseCategory courseCategory, boolean empty) {

                        // unbind old values
                        if (getItem() != null) {
                            getItem().percentageProperty().removeListener(valueChangeListener);
                        }

                        super.updateItem(courseCategory, empty);

                        // update according to new item
                        if (empty || courseCategory == null) {
                            percentageSpinner.setVisible(false);
                        } else {
                            valueFactory.setValue(courseCategory.getPercentage());
                            courseCategory.percentageProperty().addListener(valueChangeListener);
                            percentageSpinner.getEditor().setOnAction(actionEvent -> adjustValue(courseCategory));
                            percentageSpinner.getEditor().focusedProperty().addListener((observableValue, aBoolean, t1) -> {
                                if (!t1) {
                                    adjustValue(courseCategory);
                                }
                            });
                            percentageSpinner.setVisible(true);
                        }
                    }
                };
            }
        });
        deleteColumn.setCellValueFactory(cdf -> new ReadOnlyObjectWrapper<>(cdf.getValue()));
        deleteColumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<CourseCategory, CourseCategory> call(TableColumn<CourseCategory, CourseCategory> param) {

                return new TableCell<>() {

                    private final Button deleteButton = new Button("Delete");

                    {
                        setGraphic(deleteButton);
                    }

                    @Override
                    public void updateItem(CourseCategory courseCategory, boolean empty) {

                        super.updateItem(courseCategory, empty);

                        // update according to new item
                        if (empty || courseCategory == null) {
                            deleteButton.setVisible(false);
                        } else {
                            deleteButton.setVisible(true);
                        }

                        deleteButton.setOnAction(
                                event -> getTableView().getItems().remove(courseCategory)
                        );

                    }
                };
            }
        });
        categoryTable.setItems(categories);

        gradeColumn.setCellValueFactory(gradeLimitStringCellDataFeatures -> new ReadOnlyStringWrapper(gradeLimitStringCellDataFeatures.getValue().getGrade().toString()));
        limitColumn.setCellValueFactory(cdf -> Bindings.createObjectBinding(cdf::getValue));
        limitColumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<GradeLimit, GradeLimit> call(TableColumn<GradeLimit, GradeLimit> param) {

                return new TableCell<>() {

                    private final Spinner<Double> limitSpinner;

                    private final SpinnerValueFactory.DoubleSpinnerValueFactory valueFactory;
                    private final ChangeListener<Number> valueChangeListener;

                    {
                        valueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 100);
                        limitSpinner = new Spinner<>(valueFactory);
                        limitSpinner.setEditable(true);
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

                    private void adjustValue(GradeLimit gradeLimit) {
                        String newVal = limitSpinner.getEditor().getText().trim();
                        try {
                            double newDouble = Double.parseDouble(newVal);
                            if (newDouble < 0) {
                                limitSpinner.getEditor().setText("0");
                            }
                            else if (newDouble > 100) {
                                limitSpinner.getEditor().setText("100");
                            }
                            else {
                                limitSpinner.getEditor().setText(newVal);
                            }
                        } catch (NumberFormatException e) {
                            limitSpinner.getEditor().setText(nf.format(gradeLimit.getLimit()));
                        }
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
                            limitSpinner.getEditor().setOnAction(actionEvent -> adjustValue(gradeLimit));
                            limitSpinner.getEditor().focusedProperty().addListener((observableValue, aBoolean, t1) -> {
                                if (!t1) {
                                    adjustValue(gradeLimit);
                                }
                            });
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
        categories = FXCollections.observableArrayList();
    }

    private boolean checkUniqueCategoryName(String newCategory) {
        for (CourseCategory category : categories) {
            if (category.getTitle().equals(newCategory)) {
                return false;
            }
        }
        return true;
    }

    @FXML
    private void AddPressed(ActionEvent event) {
        String categoryName = categoryNameField.getText().trim();
        categoryNameField.setText(categoryName);
        if (categoryName.isEmpty()) {
            newCategoryErrorText.setText("Please enter a new course category name");
        }
        else if (checkUniqueCategoryName(categoryName)) {
            newCategoryErrorText.setText("");
            categories.add(new CourseCategory(categoryName));
        }
        else {
            newCategoryErrorText.setText("Name conflicts with existing");
        }
    }

    @FXML
    private void CreateCoursePressed(ActionEvent event) {
        courseNameErrorText.setText("");
        createCourseErrorText.setText("");
        gradeTableErrorText.setText("");
        newCategoryErrorText.setText("");
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
        double categorySum = 0;
        for (CourseCategory c : categories) {
            categorySum += c.getPercentage();
        }
        if (categorySum != 100) {
            newCategoryErrorText.setText("Category percentages must add to 100.  Current total: " + nf.format(categorySum));
            anyError = true;
        }
        if (!anyError) {

        }
    }

    @FXML
    private void ReturnPressed(ActionEvent event) {
        Main.returnToCourseSelect();
    }
}
