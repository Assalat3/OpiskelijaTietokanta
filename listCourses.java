import java.sql.*;
import java.util.ArrayList;

public class listCourses {
    public static ArrayList<courseStructure> listCoursesFromDB(){

        //lista noudetuille kursseille
        ArrayList<courseStructure> DBCourses = new ArrayList<>();

        //DB info
        final String dburl = "jdbc:mysql://localhost:3306/opsuor";
        final String dbusername = "USERNAME";
        final String dbpass = "PASSWORD";

        try{
            //Yhdistetään tietokantaan
            Connection connection = DriverManager.getConnection
                    (dburl, dbusername, dbpass);
            System.out.println("Database connected");

            //SQL-kysely
            String courseQuery = "SELECT kurssi_id, nimi, opintopisteet, kuvaus," +
                    "suorituspvm, paatospvm " + "FROM kurssi;";

            PreparedStatement courseStatement = connection.prepareStatement(courseQuery);
            ResultSet courses = courseStatement.executeQuery();

            while (courses.next()){
                courseStructure tempCourse = new courseStructure();
                //int courseCount = courses.getRow();   //TROUBLESHOOT
                //System.out.println("Rows: " + courseCount); //TROUBLESHOOT
                tempCourse.id = courses.getString("kurssi_id");
                //System.out.println(tempCourse.id);  //TROUBLESHOOT
                tempCourse.name = courses.getString("nimi");
                tempCourse.op = courses.getString("opintopisteet");
                tempCourse.description = courses.getString("kuvaus");
                tempCourse.startDate = courses.getString("suorituspvm");
                tempCourse.completeDate = courses.getString("paatospvm");
                DBCourses.add(tempCourse);
            }

            connection.close();

            //Virheet
        } catch (Exception ex){

            ex.printStackTrace();

        }

        return DBCourses;

    }
}

class courseStructure {
    String id;
    String name;
    String op;
    String description;
    String startDate;
    String completeDate;
}
