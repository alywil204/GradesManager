package sample;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class GradeLimit {

    private Grade grade;
    private DoubleProperty limit;

    public GradeLimit(Grade grade, double limit) {
        this.grade = grade;
        this.limit = new SimpleDoubleProperty(limit);
    }

    public Grade getGrade() {
        return grade;
    }

    public double getLimit() {
        return limit.get();
    }
    public void setLimit(double limit) {
        this.limit.set(limit);
    }
    public DoubleProperty limitProperty() {
        return limit;
    }

    public enum Grade {
        Aplus, A, Aminus, Bplus, B, Bminus, Cplus, C, D, F;
        public String toString() {
            if (this == Aplus) return "A+";
            if (this == Aminus) return "A-";
            if (this == Bplus) return "B+";
            if (this == Bminus) return "B-";
            if (this == Cplus) return "C+";
            return super.toString();
        }
    }
}
