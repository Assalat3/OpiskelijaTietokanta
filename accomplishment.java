import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.sql.*;
import java.util.ArrayList;

public class accomplishment {
    public static void accomplishmentView(String studentIdentifier) {
        //Setup
        Stage accomplishmentStage = new Stage();
        accomplishmentStage.setTitle("Suoritukset opiskelijalle ID: " + studentIdentifier);
        accomplishmentStage.initModality(Modality.APPLICATION_MODAL);

        //Ikkunan koko
        final double accomplishmentWindowWidth = 800;
        final double accomplishmentWindowHeight = 600;

        VBox accomplishmentLayout = new VBox();

        Scene accomplishmentScene = new Scene(
                accomplishmentLayout, accomplishmentWindowWidth, accomplishmentWindowHeight);

        //Suoritusten lisäystyä/muokkausta/poistoa varten
        HBox modifyAccomplishments = new HBox(5);

        //Tekstikentät syötteelle
        TextField newStudentID = new TextField(studentIdentifier);
        newStudentID.setEditable(false);
        newStudentID.setPrefWidth(50);
        TextField newCourseID = new TextField();
        newCourseID.setPrefWidth(150);
        newCourseID.setPromptText("Kurssin ID");
        TextField newGrade = new TextField();
        newGrade.setPrefWidth(100);
        newGrade.setPromptText("Arvosana");
        TextField newDate = new TextField();
        newDate.setPrefWidth(200);
        newDate.setPromptText("Päivämäärä: YYYY-MM-DD");


        //Tarvittavat napit
        Button addAccomplishment = new Button("Lisää suoritus");
        addAccomplishment.setPrefWidth(100);
        addAccomplishment.setOnAction(e -> addAccomplishmentToDB(
                newStudentID.getText(),
                newCourseID.getText(),
                newGrade.getText(),
                newDate.getText()
        ));

        //Näytetään napit ja tekstikentät
        modifyAccomplishments.getChildren().addAll(newStudentID, newCourseID, newGrade, newDate,
                addAccomplishment);
        accomplishmentLayout.getChildren().add(modifyAccomplishments);

        Label lblHeader = new Label("Opiskelijan suoritukset");
        accomplishmentLayout.getChildren().add(lblHeader);


        //lista opiskelijan suorituksille.
        ArrayList<accomplishmentStructure> DBAccomplishments = listAccomplishmentsFromDB(studentIdentifier);
        for (int i = 0; i < DBAccomplishments.size(); i++) {
            accomplishmentStructure tempAccomplishment = DBAccomplishments.get(i);
            HBox newAccomplishment = new HBox();

            TextField newAccomplishmentStudent = new TextField(tempAccomplishment.studentID);
            newAccomplishmentStudent.setEditable(false);
            newAccomplishmentStudent.setMinWidth(50);
            newAccomplishmentStudent.setPrefWidth(50);
            TextField newAccomplishmentCourse = new TextField(tempAccomplishment.courseID);
            newAccomplishmentCourse.setMinWidth(100);
            TextField newAccomplishmentGrade = new TextField(tempAccomplishment.grade);
            newAccomplishmentGrade.setMinWidth(50);
            newAccomplishmentGrade.setPrefWidth(50);
            TextField newAccomplishmentDate = new TextField(tempAccomplishment.completionDate);
            newAccomplishmentDate.setMinWidth(150);

            //Muokkausnappi
            Button alterAccomplishment = new Button("Muokkaa suoritusta");
            alterAccomplishment.setPrefWidth(150);
            String btnEditAccomplishmentStudentID = tempAccomplishment.studentID;
            String btnEditAccomplishmentCourseID = tempAccomplishment.courseID;
            alterAccomplishment.setOnAction(e -> alterAccomplishment(
                    btnEditAccomplishmentStudentID,
                    btnEditAccomplishmentCourseID,
                    newAccomplishmentStudent.getText(),
                    newAccomplishmentCourse.getText(),
                    newAccomplishmentGrade.getText(),
                    newAccomplishmentDate.getText()
            ));

            //Poistonappi
            Button removeAccomplishment = new Button("Poista suoritus");
            removeAccomplishment.setPrefWidth(100);
            removeAccomplishment.setOnAction(e -> removeAccomplishment(
                    btnEditAccomplishmentStudentID,
                    btnEditAccomplishmentCourseID
            ));

            newAccomplishment.getChildren().addAll(newAccomplishmentStudent, newAccomplishmentCourse,
                    newAccomplishmentGrade, newAccomplishmentDate, alterAccomplishment, removeAccomplishment);
            accomplishmentLayout.getChildren().add(newAccomplishment);
        }

        accomplishmentStage.setScene(accomplishmentScene);
        accomplishmentStage.setResizable(false);
        accomplishmentStage.show();
    }

    //Suoritusten listaaminen
    public static ArrayList<accomplishmentStructure> listAccomplishmentsFromDB(String studentIdentifier) {
        ArrayList<accomplishmentStructure> listOfAccomplishments = new ArrayList<>();

        //DB info
        final String dburl = "jdbc:mysql://localhost:3306/opsuor";
        final String dbusername = "root";
        final String dbpass = "W0tM8J0p";
        int studentIDToFind = Integer.parseInt(studentIdentifier);

        try {
            //Yhdistetään tietokantaan
            Connection connection = DriverManager.getConnection
                    (dburl, dbusername, dbpass);
            System.out.println("Database connected");

            //SQL-kysely
            String accomplishmentQuery = "SELECT kurssi_id, opiskelija_id, arvosana, suoritus_pvm " +
                    "FROM opintosuoritus WHERE opiskelija_id = " + studentIDToFind + ";";

            PreparedStatement accomplishmentStatement = connection.prepareStatement(accomplishmentQuery);
            ResultSet accomplishments = accomplishmentStatement.executeQuery();

            while (accomplishments.next()) {
                accomplishmentStructure tempAccomplishment = new accomplishmentStructure();
                tempAccomplishment.studentID = accomplishments.getString("opiskelija_id");
                tempAccomplishment.courseID = accomplishments.getString("kurssi_id");
                tempAccomplishment.grade = accomplishments.getString("arvosana");
                tempAccomplishment.completionDate = accomplishments.getString("suoritus_pvm");
                listOfAccomplishments.add(tempAccomplishment);
            }

            connection.close();

            //Virheet
        } catch (Exception ex) {

            ex.printStackTrace();

        }

        return listOfAccomplishments;

    }

    //Suorituksen lisääminen
    private static void addAccomplishmentToDB(String student, String course, String grade, String date) {
        //DB info
        final String dburl = "jdbc:mysql://localhost:3306/opsuor";
        final String dbusername = "root";
        final String dbpass = "W0tM8J0p";

        try {
            //Yhdistetään tietokantaan
            Connection connection = DriverManager.getConnection
                    (dburl, dbusername, dbpass);
            System.out.println("Database connected");

            //SQL-komento
            String addAccomplishment = "INSERT INTO opintosuoritus " +
                    "VALUES( '" + student + "', '" + course + "', '" + grade + "', '" + date + "');";

            PreparedStatement tempExecute;

            try {
                tempExecute = connection.prepareStatement(addAccomplishment);
                tempExecute.executeUpdate();
            } catch (SQLException se) {
                se.printStackTrace();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //Suorituksen muuttaminen
    private static void alterAccomplishment(String studentID, String courseID,
                                            String student, String course, String grade, String date) {
        //DB info
        final String dburl = "jdbc:mysql://localhost:3306/opsuor";
        final String dbusername = "root";
        final String dbpass = "W0tM8J0p";
        int accomplishmentCourseIDToEdit = Integer.parseInt(courseID);
        int accomplishmentStudentIDToEdit = Integer.parseInt(studentID);

        try {

            //Yhdistetään tietokantaan
            Connection connection = DriverManager.getConnection
                    (dburl, dbusername, dbpass);
            System.out.println("Database connected");

            //SQL-komento
            String alterAccomplishment = "UPDATE opintosuoritus SET opiskelija_id = '" + student + "', kurssi_id = '"
                     + course + "', arvosana = '" + grade + "', suoritus_pvm = '" + date + "' " +
                    "WHERE opiskelija_id = " + accomplishmentStudentIDToEdit + " AND " +
                    "kurssi_id = " + accomplishmentCourseIDToEdit + ";";

            PreparedStatement tempExecute;

            //Suoritetaan SQL-komento
            try{
                tempExecute = connection.prepareStatement(alterAccomplishment);
                tempExecute.executeUpdate();
            } catch (SQLException se){
                se.printStackTrace();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //Suorituksen poisto
    private static void removeAccomplishment(String studentID, String courseID) {
        //DB info
        final String dburl = "jdbc:mysql://localhost:3306/opsuor";
        final String dbusername = "root";
        final String dbpass = "W0tM8J0p";
        int courseIDToEdit = Integer.parseInt(courseID);
        int studentIDToEdit = Integer.parseInt(studentID);

        try {

            //Yhdistetään tietokantaan
            Connection connection = DriverManager.getConnection
                    (dburl, dbusername, dbpass);
            System.out.println("Database connected");

            //SQL-komento
            String editAccomplishment = "DELETE FROM opintosuoritus " +
                    "WHERE opiskelija_id = '" + studentIDToEdit + "' AND kurssi_id = '" + courseIDToEdit + "';";
            PreparedStatement tempExecute;

            //Suoritetaan SQL-komento
            try {
                tempExecute = connection.prepareStatement(editAccomplishment);
                tempExecute.executeUpdate();
            } catch (SQLException se) {
                se.printStackTrace();
            }

            connection.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

//"Rakenne" suoritukselle
class accomplishmentStructure{
    String studentID;
    String courseID;
    String grade;
    String completionDate;
}