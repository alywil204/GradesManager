package main;

public class CourseCategory {

    private final int catId;
    private final String catName;
    private final float catWeight;

    public CourseCategory(int catId, String catName, float catWeight) {
        this.catId = catId;
        this.catName = catName;
        this.catWeight = catWeight;
    }

    public int getCatId() {
        return catId;
    }

    public String getCatName() {
        return catName;
    }

    public float getCatWeight() {
        return catWeight;
    }

    public String toString() {
        return getCatName();
    }
}
