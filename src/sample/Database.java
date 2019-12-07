package sample;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private static Connection connection;
    private static final String createProfileSql = "INSERT INTO Profile (PName) VALUES (?);";
    private static PreparedStatement createProfileStatement;
    private static final String getProfileListSql = "SELECT * FROM Profile;";
    private static PreparedStatement getProfileListStatement;

    static {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/GradesManagerDB", "root", "gmDB");
            createProfileStatement = connection.prepareStatement(createProfileSql);
            getProfileListStatement = connection.prepareStatement(getProfileListSql);
        } catch (SQLException e) {
            System.err.println("Database connection error");
            e.printStackTrace();
        }
    }

    public static void closeConnection() {
        if (connection == null) return;
        try {
            connection.close();
        } catch (SQLException e) {
            System.err.println("Error closing connection");
            e.printStackTrace();
        }
    }

    public static boolean createProfile(String pName) {
        try {
            createProfileStatement.setString(1, pName);
            int n = createProfileStatement.executeUpdate();
            //System.out.println("Profile Created: " + n);
            return true;
        } catch (SQLException e) {
            System.err.println("Error executing statement: createProfile");
            e.printStackTrace();
        }
        return false;
    }

    public static List<Profile> getProfileList() {
        try {
            ArrayList<Profile> profileList = new ArrayList<>();
            ResultSet rs = getProfileListStatement.executeQuery();
            while (rs.next()) {
                String pName = rs.getString("PName");
                Profile profile = new Profile(pName);
                profileList.add(profile);
            }
            return profileList;
        } catch (SQLException e) {
            System.err.println("Error executing statement: getProfileList");
            e.printStackTrace();
            return null;
        }
    }
}
