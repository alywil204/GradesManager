package sample;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CourseCategory {

    private StringProperty title;
    private DoubleProperty percentage;

    public CourseCategory(String title) {
        this.title = new SimpleStringProperty(title);
        this.percentage = new SimpleDoubleProperty(0);
    }

    public void setTitle(String title) {
        this.title.set(title);
    }
    public String getTitle() {
        return title.get();
    }
    public StringProperty titleProperty() {
        return title;
    }

    public void setPercentage(double percentage) {
        this.percentage.set(percentage);
    }
    public double getPercentage() {
        return percentage.get();
    }
    public DoubleProperty percentageProperty() {
        return percentage;
    }
}
