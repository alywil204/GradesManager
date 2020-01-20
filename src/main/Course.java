package main;

import java.util.List;

public class Course {

    private int cId;
    private String cName;
    private float AplusGrade;
    private float AGrade;
    private float AminusGrade;
    private float BplusGrade;
    private float BGrade;
    private float BminusGrade;
    private float CplusGrade;
    private float CGrade;
    private float DGrade;
    private List<CourseCategory> categories;

    public Course(int cId, String cName, float AplusGrade, float AGrade, float AminusGrade, float BplusGrade,
                  float BGrade, float BminusGrade, float CplusGrade, float CGrade, float DGrade,
                  List<CourseCategory> categories) {
        this.cId = cId;
        this.cName = cName;
        this.AplusGrade = AplusGrade;
        this.AGrade = AGrade;
        this.AminusGrade = AminusGrade;
        this.BplusGrade = BplusGrade;
        this.BGrade = BGrade;
        this.BminusGrade = BminusGrade;
        this.CplusGrade = CplusGrade;
        this.CGrade = CGrade;
        this.DGrade = DGrade;
        this.categories = categories;
    }

    public int getCId() {
        return cId;
    }

    public String getCName() {
        return cName;
    }

    public float getAplusGrade() {
        return AplusGrade;
    }

    public float getAGrade() {
        return AGrade;
    }

    public float getAminusGrade() {
        return AminusGrade;
    }

    public float getBplusGrade() {
        return BplusGrade;
    }

    public float getBGrade() {
        return BGrade;
    }

    public float getBminusGrade() {
        return BminusGrade;
    }

    public float getCplusGrade() {
        return CplusGrade;
    }

    public float getCGrade() {
        return CGrade;
    }

    public float getDGrade() {
        return DGrade;
    }

    public List<CourseCategory> getCategories() {
        return categories;
    }
}
