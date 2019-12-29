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
    private static final String checkCourseExistsSql = "SELECT * FROM Course WHERE (CProfileId = ?);";
    private static PreparedStatement checkCourseNameInUseStatement;

    public static void openConnection() throws SQLNonTransientConnectionException{
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/GradesManagerDB", "root", "gmDB");
            createProfileStatement = connection.prepareStatement(createProfileSql);
            getProfileListStatement = connection.prepareStatement(getProfileListSql);
            checkCourseNameInUseStatement = connection.prepareStatement(checkCourseExistsSql);
        } catch (SQLNonTransientConnectionException e) {
            throw new SQLNonTransientConnectionException();
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

    public static boolean createProfile(String pName) throws SQLIntegrityConstraintViolationException {
        try {
            createProfileStatement.setString(1, pName);
            createProfileStatement.executeUpdate();
            return true;
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLIntegrityConstraintViolationException();
        } catch (SQLException e) {
            System.err.println("Error executing statement: createProfile");
            //e.printStackTrace();
        }
        return false;
    }

    public static List<Profile> getProfileList() {
        try {
            ArrayList<Profile> profileList = new ArrayList<>();
            ResultSet rs = getProfileListStatement.executeQuery();
            while (rs.next()) {
                int pId = rs.getInt("PId");
                String pName = rs.getString("PName");
                Profile profile = new Profile(pId, pName);
                profileList.add(profile);
            }
            return profileList;
        } catch (SQLException e) {
            System.err.println("Error executing statement: getProfileList");
            e.printStackTrace();
            return null;
        }
    }

    public static Boolean checkCourseNameInUse(int pId, String courseName) {
        try {
            checkCourseNameInUseStatement.setInt(1, pId);
            ResultSet rs = checkCourseNameInUseStatement.executeQuery();
            while (rs.next()) {
                String cName = rs.getString("CName");
                if (cName.equals(courseName)) return true;
            }
        } catch (SQLException e) {
            System.err.println("Error executing statement: checkCourseNameInUse");
            e.printStackTrace();
            return null;
        }
        return false;
    }

    /*public static boolean createCourse(int pId, String cName, double AplusGrade, double AGrade, double AminusGrade,
                                       double BplusGrade, double BGrade, double BminusGrade, double CplusGrade,
                                       double CGrade, double DGrade, List<CourseCategory> categories) {

    }*/
}
