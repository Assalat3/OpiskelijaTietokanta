import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;

public class editCourse {
    public static void editCourseWindow(String IDToFindCourse){
        Stage editCourseStage = new Stage();
        editCourseStage.initModality(Modality.APPLICATION_MODAL);

        final double windowWidth = 800;
        final double windowHeight = 28;

        editCourseStage.setTitle("Kurssin muokkaus");

        VBox courseWindowLayout = new VBox();

        Scene editCourseScene = new Scene(courseWindowLayout, windowWidth, windowHeight);

        HBox courseEdit = new HBox();

        double textFieldWidth = windowWidth / 3;
        TextField courseID = new TextField();
        courseID.setPromptText("ID:");
        courseID.setMaxWidth(textFieldWidth / 4.0);
        TextField courseName = new TextField();
        courseName.setPromptText("Kurssin nimi:");
        courseName.setMaxWidth(textFieldWidth);
        TextField courseOP = new TextField();
        courseOP.setPromptText("OP:t:");
        courseOP.setMaxWidth(textFieldWidth / 5.0);
        TextField courseStartDate = new TextField();
        courseStartDate.setPromptText("Kurssin AloitusPVM:");
        courseStartDate.setMaxWidth(textFieldWidth / 2.0);
        TextField courseCompleteDate = new TextField();
        courseCompleteDate.setPromptText("Kurssin päätösPVM:");
        courseCompleteDate.setMaxWidth(textFieldWidth / 2.0);
        TextField courseDesc = new TextField();
        courseDesc.setPromptText("Kurssin kuvaus:");
        courseDesc.setMaxWidth(textFieldWidth / 2.0);

        //Nappi tietokantaan tallentamiselle
        Button btnSaveAlterDB = new Button("Tallenna");
        btnSaveAlterDB.setOnAction(e -> alterCourseInDB(
                IDToFindCourse,
                courseID.getText(),
                courseName.getText(),
                courseOP.getText(),
                courseStartDate.getText(),
                courseCompleteDate.getText(),
                courseDesc.getText()
        ));

        //Peruutus
        Button btnCancel = new Button("Sulje");
        btnCancel.setOnAction(e -> editCourseStage.close());
        btnCancel.cancelButtonProperty();

        //Lisätään ikkunaan napit ja tekstikentät
        courseEdit.getChildren().addAll(courseID, courseName, courseOP,
                courseStartDate, courseCompleteDate, courseDesc, btnSaveAlterDB, btnCancel);
        courseWindowLayout.getChildren().add(courseEdit);

        editCourseStage.setScene(editCourseScene);
        editCourseStage.show();

    }

    private static void alterCourseInDB(String IDOfCourse, String courseID, String courseName, String courseOP,
                                       String courseStartDate, String courseCompleteDate, String courseDesc){

        //DB info
        final String dburl = "jdbc:mysql://localhost:3306/opsuor";
        final String dbusername = "USERNAME";
        final String dbpass = "PASSWORD";
        int courseIDToEdit = Integer.parseInt(IDOfCourse);

        try{

            //Yhdistetään tietokantaan
            Connection connection = DriverManager.getConnection
                    (dburl, dbusername, dbpass);
            System.out.println("Database connected");

            //SQL-komento
            String editCourse = "UPDATE kurssi SET kurssi_id = " + courseID + ", nimi = '" + courseName + "'," +
                    "opintopisteet = " + courseOP + ", suorituspvm = '" + courseDesc + "', kuvaus = '" +
                    courseStartDate + "', paatospvm = " + courseCompleteDate + " WHERE kurssi_id = " +
                    courseIDToEdit + ';';
            PreparedStatement tempExecute;

            //Suoritetaan SQL-komento
            try{
                tempExecute = connection.prepareStatement(editCourse);
                tempExecute.executeUpdate();
            } catch (SQLException se){
                se.printStackTrace();
            }

            connection.close();

        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    protected static void removeCourse(String removeCourseByID){
        //DB info
        final String dburl = "jdbc:mysql://localhost:3306/opsuor";
        final String dbusername = "USERNAME";
        final String dbpass = "PASSWORD";
        int courseIDToEdit = Integer.parseInt(removeCourseByID);

        try{

            //Yhdistetään tietokantaan
            Connection connection = DriverManager.getConnection
                    (dburl, dbusername, dbpass);
            System.out.println("Database connected");

            //SQL-komento
            String editCourse = "DELETE FROM kurssi WHERE kurssi_id = " + courseIDToEdit + ';';
            PreparedStatement tempExecute;

            //Suoritetaan SQL-komento
            try{
                tempExecute = connection.prepareStatement(editCourse);
                tempExecute.executeUpdate();
            } catch (SQLException se){
                se.printStackTrace();
            }

            connection.close();

        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public static void showCourseAccomplishments(){
        final double courseAccomplishmentWidth = 400;
        final double courseAccomplishmentHeight = 600;

        Stage showCourseAccomplishmentsStage = new Stage();
        showCourseAccomplishmentsStage.setTitle("Kurssin suoritukset");

        VBox courseAccomplishments = new VBox();

        Scene courseAccomplishmentScene = new Scene(courseAccomplishments,
                courseAccomplishmentWidth,
                courseAccomplishmentHeight);

        ArrayList<accomplishmentStructure> CourseAccomplishmentList = CourseAccomplishmentsFromDB();
        for (int i = 0; i < CourseAccomplishmentList.size(); i++){
            accomplishmentStructure tempAccomplishment = CourseAccomplishmentList.get(i);
            HBox newCourseAccomplishment = new HBox();

            TextField newCourseAccomplishmentStudent = new TextField(tempAccomplishment.studentID);
            newCourseAccomplishmentStudent.setEditable(false);
            newCourseAccomplishmentStudent.setMinWidth(50);
            newCourseAccomplishmentStudent.setPrefWidth(50);
            TextField newCourseAccomplishmentCourse = new TextField(tempAccomplishment.courseID);
            newCourseAccomplishmentCourse.setEditable(false);
            newCourseAccomplishmentCourse.setMinWidth(100);
            TextField newCourseAccomplishmentGrade = new TextField(tempAccomplishment.grade);
            newCourseAccomplishmentGrade.setEditable(false);
            newCourseAccomplishmentGrade.setMinWidth(50);
            newCourseAccomplishmentGrade.setPrefWidth(50);
            TextField newCourseAccomplishmentDate = new TextField(tempAccomplishment.completionDate);
            newCourseAccomplishmentDate.setEditable(false);
            newCourseAccomplishmentDate.setMinWidth(150);

            newCourseAccomplishment.getChildren().addAll(newCourseAccomplishmentStudent, newCourseAccomplishmentCourse,
                    newCourseAccomplishmentGrade, newCourseAccomplishmentDate);
            courseAccomplishments.getChildren().add(newCourseAccomplishment);
        }


        showCourseAccomplishmentsStage.setScene(courseAccomplishmentScene);
        showCourseAccomplishmentsStage.show();
    }

    public static ArrayList<accomplishmentStructure> CourseAccomplishmentsFromDB() {
        ArrayList<accomplishmentStructure> listOfCourseAccomplishments = new ArrayList<>();

        //DB info
        final String dburl = "jdbc:mysql://localhost:3306/opsuor";
        final String dbusername = "USERNAME";
        final String dbpass = "PASSWORD";

        try {
            //Yhdistetään tietokantaan
            Connection connection = DriverManager.getConnection
                    (dburl, dbusername, dbpass);
            System.out.println("Database connected");

            //SQL-kysely
            String courseAccomplishmentQuery = "SELECT kurssi_id, opiskelija_id, arvosana, suoritus_pvm " +
                    "FROM opintosuoritus;";

            PreparedStatement accomplishmentStatement = connection.prepareStatement(courseAccomplishmentQuery);
            ResultSet courseAccomplishments = accomplishmentStatement.executeQuery();

            while (courseAccomplishments.next()) {
                accomplishmentStructure tempCourseAccomplishment = new accomplishmentStructure();
                tempCourseAccomplishment.studentID = courseAccomplishments.getString("opiskelija_id");
                tempCourseAccomplishment.courseID = courseAccomplishments.getString("kurssi_id");
                tempCourseAccomplishment.grade = courseAccomplishments.getString("arvosana");
                tempCourseAccomplishment.completionDate = courseAccomplishments.getString("suoritus_pvm");
                listOfCourseAccomplishments.add(tempCourseAccomplishment);
            }

            connection.close();

            //Virheet
        } catch (Exception ex) {

            ex.printStackTrace();

        }

        return listOfCourseAccomplishments;

    }

}
