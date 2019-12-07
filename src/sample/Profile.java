package sample;

public class Profile {

    private String pName;

    public Profile(String pName) {
        this.pName = pName;
    }

    public String getPName() {
        return pName;
    }

    public String toString() {
        return getPName();
    }
}
