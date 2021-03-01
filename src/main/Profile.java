package main;

public class Profile {

    private final int pId;
    private final String pName;

    public Profile(int pId, String pName) {
        this.pId = pId;
        this.pName = pName;
    }

    public int getPId() {
        return pId;
    }

    public String getPName() {
        return pName;
    }

    public String toString() {
        return getPName();
    }
}
