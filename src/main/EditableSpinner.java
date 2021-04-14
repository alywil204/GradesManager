package main;

import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

public class EditableSpinner extends Spinner<Double> {
    public EditableSpinner() {
        super();
        this.setEditable(true);
        getEditor().setOnAction(actionEvent -> adjustValue());
        getEditor().focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (!t1) {
                adjustValue();
            }
        });
        setOnMouseClicked(mouseEvent -> cancelEdit());
    }

    public void adjustValue() {
        String newVal = getEditor().getText().trim();
        try {
            double newDouble = Double.parseDouble(newVal);
            SpinnerValueFactory.DoubleSpinnerValueFactory vf = (SpinnerValueFactory.DoubleSpinnerValueFactory)getValueFactory();
            if (newDouble < vf.getMin()) {
                vf.setValue(vf.getMin());
            }
            else {
                vf.setValue(Math.min(newDouble, vf.getMax()));
            }
        } catch (NumberFormatException e) {
            cancelEdit();
        }
    }
}
