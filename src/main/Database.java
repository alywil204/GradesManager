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
    private static final String createAssignmentCategorySql = "INSERT INTO CourseAssignment (ACourseId, ACategoryId, AName, PointsNumerator, PointsDenominator, AWeight) VALUES (?, ?, ?, ?, ?, ?);";
    private static PreparedStatement createAssignmentCategoryStatement;
    private static final String updateCourseAssignmentSql = "UPDATE CourseAssignment SET ACategoryId = ?, AName = ?, PointsNumerator = ?, PointsDenominator = ?, AWeight = ? WHERE (AId = ?);";
    private static PreparedStatement updateCourseAssignmentStatement;
    private static final String deleteCourseAssignmentSql = "DELETE FROM CourseAssignment WHERE (AId = ?);";
    private static PreparedStatement deleteCourseAssignmentStatement;
    private static final String getProfileCourseListSql = "SELECT CName FROM Course WHERE (CProfileId = ?);";
    private static PreparedStatement getProfileCourseListStatement;
    private static final String getCourseSql = "SELECT * FROM Course WHERE (CProfileId = ? AND CName = ?);";
    private static PreparedStatement getCourseStatement;
    private static final String getCourseCategoryListSql = "SELECT * FROM CourseCategory WHERE (CatCourseId = ?);";
    private static PreparedStatement getCourseCategoryListStatement;
    private static final String getCourseAssignmentListSql = "SELECT * FROM CourseAssignment WHERE (ACourseId = ?);";
    private static PreparedStatement getCourseAssignmentListStatement;

    public static void openConnection() throws SQLNonTransientConnectionException{
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/GradesManagerDB",
                    "root", "gmDB");
            createProfileStatement = connection.prepareStatement(createProfileSql);
            getProfileListStatement = connection.prepareStatement(getProfileListSql);
            checkCourseNameInUseStatement = connection.prepareStatement(checkCourseNameInUseSql);
            createCourseStatement = connection.prepareStatement(createCourseSql);
            createCourseCategoryStatement = connection.prepareStatement(createCourseCategorySql);
            updateCourseCategoryStatement = connection.prepareStatement(updateCourseCategorySql);
            deleteCourseCategoryStatement = connection.prepareStatement(deleteCourseCategorySql);
            createAssignmentCategoryStatement = connection.prepareStatement(createAssignmentCategorySql);
            updateCourseAssignmentStatement = connection.prepareStatement(updateCourseAssignmentSql);
            deleteCourseAssignmentStatement = connection.prepareStatement(deleteCourseAssignmentSql);
            getProfileCourseListStatement = connection.prepareStatement(getProfileCourseListSql);
            getCourseStatement = connection.prepareStatement(getCourseSql);
            getCourseCategoryListStatement = connection.prepareStatement(getCourseCategoryListSql);
            getCourseAssignmentListStatement = connection.prepareStatement(getCourseAssignmentListSql);
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

    public static void createCourseCategory(int cId, String catName, float catWeight)
            throws SQLIntegrityConstraintViolationException{
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

    public static void updateCourseCategory(int catId, String catName, float catWeight)
            throws SQLIntegrityConstraintViolationException {
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

    public static void deleteCourseCategory(int catId) throws SQLIntegrityConstraintViolationException {
        try {
            deleteCourseCategoryStatement.setInt(1, catId);
            deleteCourseCategoryStatement.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            throw e;
        } catch (SQLException e) {
            System.err.println("Error executing statement: deleteCourseCategory");
            e.printStackTrace();
        }
    }

    public static void createCourseAssignment(int courseId, int catId, String name, float pointsNumerator,
                                              float pointsDenominator, float assignmentWeight)
            throws SQLIntegrityConstraintViolationException {
        try {
            createAssignmentCategoryStatement.setInt(1, courseId);
            if (catId > 0) {
                createAssignmentCategoryStatement.setInt(2, catId);
            }
            else {
                createAssignmentCategoryStatement.setNull(2, Types.INTEGER);
            }
            createAssignmentCategoryStatement.setString(3, name);
            createAssignmentCategoryStatement.setFloat(4, pointsNumerator);
            createAssignmentCategoryStatement.setFloat(5, pointsDenominator);
            createAssignmentCategoryStatement.setFloat(6, assignmentWeight);
            createAssignmentCategoryStatement.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            throw e;
        } catch (SQLException e) {
            System.err.println("Error executing statement: createCourseAssignment");
            e.printStackTrace();
        }
    }

    public static void updateCourseAssignment(int aId, int catId, String name, float pointsNumerator,
                                              float pointsDenominator, float assignmentWeight)
            throws SQLIntegrityConstraintViolationException {
        try {
            if (catId > 0) {
                updateCourseAssignmentStatement.setInt(1, catId);
            }
            else {
                updateCourseAssignmentStatement.setNull(1, Types.INTEGER);
            }
            updateCourseAssignmentStatement.setString(2, name);
            updateCourseAssignmentStatement.setFloat(3, pointsNumerator);
            updateCourseAssignmentStatement.setFloat(4, pointsDenominator);
            updateCourseAssignmentStatement.setFloat(5, assignmentWeight);
            updateCourseAssignmentStatement.setInt(6, aId);
            updateCourseAssignmentStatement.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            throw e;
        } catch (SQLException e) {
            System.err.println("Error executing statement: updateCourseAssignment");
            e.printStackTrace();
        }
    }

    public static void deleteCourseAssignment(int aId) {
        try {
            deleteCourseAssignmentStatement.setInt(1, aId);
            deleteCourseAssignmentStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error executing statement: deleteCourseAssignment");
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

                getCourseAssignmentListStatement.setInt(1, cId);
                ResultSet assignmentrs = getCourseAssignmentListStatement.executeQuery();
                List<CourseAssignment> assignments = new ArrayList<>();
                while (assignmentrs.next()) {
                    int aId = assignmentrs.getInt("AId");
                    int aCategoryId = assignmentrs.getInt("ACategoryId");
                    String aName = assignmentrs.getString("AName");
                    float pointsDenominator = assignmentrs.getFloat("PointsDenominator");
                    float pointsNumerator = assignmentrs.getFloat("PointsNumerator");
                    float aWeight = assignmentrs.getFloat("AWeight");
                    assignments.add(new CourseAssignment(aId, aCategoryId, aName, pointsNumerator, pointsDenominator, aWeight));
                }

                course = new Course(cId, cName, AplusGrade, AGrade, AminusGrade, BplusGrade, BGrade, BminusGrade,
                        CplusGrade, CGrade, DGrade, categories, assignments);
            }
            return course;
        } catch (SQLException e) {
            System.err.println("Error executing statement: getCourse");
            e.printStackTrace();
            return null;
        }
    }
}
