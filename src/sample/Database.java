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
    private static final String checkCourseNameInUseSql = "SELECT CName FROM Course WHERE (CProfileId = ?);";
    private static PreparedStatement checkCourseNameInUseStatement;
    private static final String createCourseSql = "INSERT INTO Course (CProfileId, CName, AplusGrade, AGrade, AminusGrade, BplusGrade, BGrade, BminusGrade, CplusGrade, CGrade, DGrade) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
    private static PreparedStatement createCourseStatement;
    private static final String retrieveNewCourseIdSql = "SELECT CId FROM Course WHERE (CProfileId = ? AND CName = ?)";
    private static PreparedStatement retrieveNewCourseIdStatement;
    private static final String createCourseCategorySql = "INSERT INTO CourseCategory (CatCourseId, CatName, Percentage) VALUES (?, ?, ?);";
    private static PreparedStatement createCourseCategoryStatement;
    private static final String getProfileCourseListSql = "SELECT CName FROM Course WHERE (CProfileId = ?)";
    private static PreparedStatement getProfileCourseListStatement;

    public static void openConnection() throws SQLNonTransientConnectionException{
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/GradesManagerDB", "root", "gmDB");
            createProfileStatement = connection.prepareStatement(createProfileSql);
            getProfileListStatement = connection.prepareStatement(getProfileListSql);
            checkCourseNameInUseStatement = connection.prepareStatement(checkCourseNameInUseSql);
            createCourseStatement = connection.prepareStatement(createCourseSql);
            createCourseCategoryStatement = connection.prepareStatement(createCourseCategorySql);
            retrieveNewCourseIdStatement = connection.prepareStatement(retrieveNewCourseIdSql);
            getProfileCourseListStatement = connection.prepareStatement(getProfileCourseListSql);
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

    public static void createProfile(String pName) throws SQLIntegrityConstraintViolationException {
        try {
            createProfileStatement.setString(1, pName);
            createProfileStatement.executeUpdate();
            //return true;
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLIntegrityConstraintViolationException();
        } catch (SQLException e) {
            System.err.println("Error executing statement: createProfile");
            //e.printStackTrace();
        }
        //return false;
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

    public static boolean createCourse(int pId, String cName, double AplusGrade, double AGrade, double AminusGrade,
                                       double BplusGrade, double BGrade, double BminusGrade, double CplusGrade,
                                       double CGrade, double DGrade, List<CourseCategory> categories) {
        try {
            createCourseStatement.setInt(1, pId);
            createCourseStatement.setString(2, cName);
            createCourseStatement.setFloat(3, (float)AplusGrade);
            createCourseStatement.setFloat(4, (float)AGrade);
            createCourseStatement.setFloat(5, (float)AminusGrade);
            createCourseStatement.setFloat(6, (float)BplusGrade);
            createCourseStatement.setFloat(7, (float)BGrade);
            createCourseStatement.setFloat(8, (float)BminusGrade);
            createCourseStatement.setFloat(9, (float)CplusGrade);
            createCourseStatement.setFloat(10, (float)CGrade);
            createCourseStatement.setFloat(11, (float)DGrade);
            createCourseStatement.executeUpdate();
            retrieveNewCourseIdStatement.setInt(1, pId);
            retrieveNewCourseIdStatement.setString(2, cName);
            ResultSet rs = retrieveNewCourseIdStatement.executeQuery();
            int cId;
            if (rs.next()) {
                cId = rs.getInt("CId");
            }
            else {
                throw new SQLException();
            }
            for (CourseCategory cat : categories) {
                createCourseCategoryStatement.setInt(1, cId);
                createCourseCategoryStatement.setString(2, cat.getTitle());
                createCourseCategoryStatement.setFloat(3, (float)cat.getPercentage());
                createCourseCategoryStatement.executeUpdate();
            }
            return true;
        } catch (SQLException e) {
            System.err.println("Error executing statement: createCourse");
            //e.printStackTrace();
        }
        return false;
    }

    public static List<String> getProfileCourseList(int pId) {
        try {
            ArrayList<String> courseList = new ArrayList<>();
            getProfileCourseListStatement.setInt(1, pId);
            ResultSet rs = getProfileCourseListStatement.executeQuery();
            while (rs.next()) {
                String cName = rs.getString("CName");
                courseList.add(cName);
            }
            return courseList;
        } catch (SQLException e) {
            System.err.println("Error executing statement: getCourseList");
            //e.printStackTrace();
            return null;
        }
    }
}
