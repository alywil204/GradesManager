package main;

public class Assignment {

    private final int aId;
    private final int cId;
    private final int catId;
    private final String aName;
    private final float pointsNumerator;
    private final float pointsDenominator;
    private final float aWeight;

    public Assignment(int aId, int cId, int catId, String aName, float pointsNumerator, float pointsDenominator, float aWeight) {
        this.aId = aId;
        this.cId = cId;
        this.catId = catId;
        this.aName = aName;
        this.pointsNumerator = pointsNumerator;
        this.pointsDenominator = pointsDenominator;
        this.aWeight = aWeight;
    }

    public int getAId() {
        return aId;
    }

    public int getCId() {
        return cId;
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

    public float getAWeight() {
        return aWeight;
    }
}
