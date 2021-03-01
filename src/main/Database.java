package main;

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
    private static final String createCourseCategorySql = "INSERT INTO CourseCategory (CatCourseId, CatName, CatWeight) VALUES (?, ?, ?);";
    private static PreparedStatement createCourseCategoryStatement;
    private static final String updateCourseCategorySql = "UPDATE CourseCategory SET CatName = ?, CatWeight = ? WHERE (CatId = ?);";
    private static PreparedStatement updateCourseCategoryStatement;
    private static final String deleteCourseCategorySql = "DELETE FROM CourseCategory WHERE (CatId = ?);";
    private static PreparedStatement deleteCourseCategoryStatement;
    private static final String getProfileCourseListSql = "SELECT CName FROM Course WHERE (CProfileId = ?);";
    private static PreparedStatement getProfileCourseListStatement;
    private static final String getCourseSql = "SELECT * FROM Course WHERE (CProfileId = ? AND CName = ?);";
    private static PreparedStatement getCourseStatement;
    private static final String getCourseCategoryListSql = "SELECT * FROM CourseCategory WHERE (CatCourseId = ?);";
    private static PreparedStatement getCourseCategoryListStatement;

    public static void openConnection() throws SQLNonTransientConnectionException{
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/GradesManagerDB", "root", "gmDB");
            createProfileStatement = connection.prepareStatement(createProfileSql);
            getProfileListStatement = connection.prepareStatement(getProfileListSql);
            checkCourseNameInUseStatement = connection.prepareStatement(checkCourseNameInUseSql);
            createCourseStatement = connection.prepareStatement(createCourseSql);
            createCourseCategoryStatement = connection.prepareStatement(createCourseCategorySql);
            updateCourseCategoryStatement = connection.prepareStatement(updateCourseCategorySql);
            deleteCourseCategoryStatement = connection.prepareStatement(deleteCourseCategorySql);
            getProfileCourseListStatement = connection.prepareStatement(getProfileCourseListSql);
            getCourseStatement = connection.prepareStatement(getCourseSql);
            getCourseCategoryListStatement = connection.prepareStatement(getCourseCategoryListSql);
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
        } catch (SQLIntegrityConstraintViolationException e) {
            throw e;
        } catch (SQLException e) {
            System.err.println("Error executing statement: createProfile");
            e.printStackTrace();
        }
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
                if (cName.equals(courseName)) {
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error executing statement: checkCourseNameInUse");
            e.printStackTrace();
            return null;
        }
        return false;
    }

    public static void createCourse(int pId, String cName, double AplusGrade, double AGrade, double AminusGrade,
                                       double BplusGrade, double BGrade, double BminusGrade, double CplusGrade,
                                       double CGrade, double DGrade) throws SQLIntegrityConstraintViolationException {
        try {
            createCourseStatement.setInt(1, pId);
            createCourseStatement.setString(2, cName);
            createCourseStatement.setFloat(3, (float) AplusGrade);
            createCourseStatement.setFloat(4, (float) AGrade);
            createCourseStatement.setFloat(5, (float) AminusGrade);
            createCourseStatement.setFloat(6, (float) BplusGrade);
            createCourseStatement.setFloat(7, (float) BGrade);
            createCourseStatement.setFloat(8, (float) BminusGrade);
            createCourseStatement.setFloat(9, (float) CplusGrade);
            createCourseStatement.setFloat(10, (float) CGrade);
            createCourseStatement.setFloat(11, (float) DGrade);
            createCourseStatement.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            throw e;
        } catch (SQLException e) {
            System.err.println("Error executing statement: createCourse");
            e.printStackTrace();
        }
    }

    public static void createCourseCategory(int cId, String catName, float catWeight) throws SQLIntegrityConstraintViolationException{
        try {
            createCourseCategoryStatement.setInt(1, cId);
            createCourseCategoryStatement.setString(2, catName);
            createCourseCategoryStatement.setFloat(3, catWeight);
            createCourseCategoryStatement.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            throw e;
        } catch (SQLException e) {
            System.err.println("Error executing statement: createCourseCategory");
            e.printStackTrace();
        }
    }

    public static void updateCourseCategory(int catId, String catName, float catWeight) throws SQLIntegrityConstraintViolationException {
        try {
            updateCourseCategoryStatement.setString(1, catName);
            updateCourseCategoryStatement.setFloat(2, catWeight);
            updateCourseCategoryStatement.setInt(3, catId);
            updateCourseCategoryStatement.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            throw e;
        } catch (SQLException e) {
            System.err.println("Error executing statement: updateCourseCategory");
            e.printStackTrace();
        }
    }

    public static void deleteCourseCategory(int catId) {
        try {
            deleteCourseCategoryStatement.setInt(1, catId);
            deleteCourseCategoryStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error executing statement: deleteCourseCategory");
            e.printStackTrace();
        }
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

    public static Course getCourse(int pId, String cName) {
        Course course = null;
        try {
            getCourseStatement.setInt(1, pId);
            getCourseStatement.setString(2, cName);
            ResultSet rs = getCourseStatement.executeQuery();
            while (rs.next()) {
                int cId = rs.getInt("CId");
                cName = rs.getString("CName");
                float AplusGrade = rs.getFloat("AplusGrade");
                float AGrade = rs.getFloat("AGrade");
                float AminusGrade = rs.getFloat("AminusGrade");
                float BplusGrade = rs.getFloat("BplusGrade");
                float BGrade = rs.getFloat("BGrade");
                float BminusGrade = rs.getFloat("BminusGrade");
                float CplusGrade = rs.getFloat("CplusGrade");
                float CGrade = rs.getFloat("CGrade");
                float DGrade = rs.getFloat("DGrade");
                getCourseCategoryListStatement.setInt(1, cId);
                ResultSet categoryrs = getCourseCategoryListStatement.executeQuery();
                List<CourseCategory> categories = new ArrayList<>();
                while (categoryrs.next()) {
                    int catId = categoryrs.getInt("CatId");
                    String catName = categoryrs.getString("CatName");
                    float catWeight = categoryrs.getFloat("CatWeight");
                    categories.add(new CourseCategory(catId, catName, catWeight));
                }
                course = new Course(cId, cName, AplusGrade, AGrade, AminusGrade, BplusGrade, BGrade, BminusGrade,
                        CplusGrade, CGrade, DGrade, categories);
            }
            return course;
        } catch (SQLException e) {
            System.err.println("Error executing statement: getCourse");
            e.printStackTrace();
            return null;
        }
    }
}
