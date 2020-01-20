package main;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class GradeLimitSet {

    private ObservableList<GradeLimit> gradeLimits = FXCollections.observableArrayList();

    public GradeLimitSet(double Aplus, double A, double Aminus, double Bplus, double B, double Bminus, double Cplus,
                         double C, double D) {
        gradeLimits.add(new GradeLimit(GradeLimit.Grade.Aplus, Aplus));
        gradeLimits.add(new GradeLimit(GradeLimit.Grade.A, A));
        gradeLimits.add(new GradeLimit(GradeLimit.Grade.Aminus, Aminus));
        gradeLimits.add(new GradeLimit(GradeLimit.Grade.Bplus, Bplus));
        gradeLimits.add(new GradeLimit(GradeLimit.Grade.B, B));
        gradeLimits.add(new GradeLimit(GradeLimit.Grade.Bminus, Bminus));
        gradeLimits.add(new GradeLimit(GradeLimit.Grade.Cplus, Cplus));
        gradeLimits.add(new GradeLimit(GradeLimit.Grade.C, C));
        gradeLimits.add(new GradeLimit(GradeLimit.Grade.D, D));
    }

    public ObservableList<GradeLimit> getGradeLimits() {
        return gradeLimits;
    }
}
