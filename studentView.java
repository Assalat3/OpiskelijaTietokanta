import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class studentView {
    public static ArrayList<studentStructure> showStudents(){
        //lista noudetuille kursseille
        ArrayList<studentStructure> DBStudents = new ArrayList<>();

        //DB info
        final String dburl = "jdbc:mysql://localhost:3306/opsuor";
        final String dbusername = "root";
        final String dbpass = "W0tM8J0p";

        try {
            //Yhdistetään tietokantaan
            Connection connection = DriverManager.getConnection
                    (dburl, dbusername, dbpass);
            System.out.println("Database connected");

            //SQL-kysely
            String studentQuery = "SELECT opiskelija_id, etunimi, sukunimi, lahiosoite, "
                    + "postitoimipaikka, postinro, email, puhelinnro " + "FROM opiskelija;";

            PreparedStatement studentStatement = connection.prepareStatement(studentQuery);
            ResultSet students = studentStatement.executeQuery();

            //Käydään läpi kursseja ja niiden tietoja
            while (students.next()){
                studentStructure tempStudent = new studentStructure();
                tempStudent.id = students.getString("opiskelija_id");
                tempStudent.fname = students.getString("etunimi");
                tempStudent.lname = students.getString("sukunimi");
                tempStudent.adress = students.getString("lahiosoite");
                tempStudent.postalzone = students.getString("postitoimipaikka");
                tempStudent.postalnro = students.getString("postinro");
                tempStudent.email = students.getString("email");
                tempStudent.phonenro = students.getString("puhelinnro");
                DBStudents.add(tempStudent);
            }



        } catch (Exception ex) {

            ex.printStackTrace();

        }

        return DBStudents;
    }
}

class studentStructure{
    String id;
    String fname;
    String lname;
    String adress;
    String postalzone;
    String postalnro;
    String email;
    String phonenro;
}