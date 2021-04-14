package main;

public class CourseAssignment {

    private final int aId;
    private final int catId;
    private final String aName;
    private final float pointsNumerator;
    private final float pointsDenominator;
    private final float aWeight;

    public CourseAssignment(int aId, int catId, String aName, float pointsNumerator, float pointsDenominator, float aWeight) {
        this.aId = aId;
        this.catId = catId;
        this.aName = aName;
        this.pointsNumerator = pointsNumerator;
        this.pointsDenominator = pointsDenominator;
        this.aWeight = aWeight;
    }

    public int getAId() {
        return aId;
    }

    public int getCatId() {
        return catId;
    }

    public String getAName() {
        return aName;
    }

    public float getPointsNumerator() {
        return pointsNumerator;
    }

    public float getPointsDenominator() {
        return pointsDenominator;
    }

    public double getScore() {
        return getPointsNumerator()/getPointsDenominator();
    }

    public float getAWeight() {
        return aWeight;
    }

    public String toString() {
        return getAName();
    }
}
