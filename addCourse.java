import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.sql.*;

public class addCourse{
    public static void addCourseWindow(){

        //Setup
        Stage courseAddStage = new Stage();
        courseAddStage.initModality(Modality.APPLICATION_MODAL);

        final double windowWidth = 800;
        final double windowHeight = 28;

        courseAddStage.setTitle("Kurssin lisäys");

        VBox courseWindowLayout = new VBox();

        Scene courseScene = new Scene(courseWindowLayout, windowWidth, windowHeight);

        //Kysytään kurssin tiedot.
        HBox courseInfo = new HBox();

        //courseInfo:n sisältö ja mitoitus:
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

        //Napit tallentamiselle ja peruuttamiselle.
        //Tallennus
        Button btnSave = new Button("Tallenna");
        btnSave.setOnAction(e -> addCourseToDB(
                courseID.getText(),
                courseName.getText(),
                courseOP.getText(),
                courseStartDate.getText(),
                courseCompleteDate.getText(),
                courseDesc.getText()
        ));

        //Peruutus
        Button btnCancel = new Button("Sulje");
        btnCancel.setOnAction(e -> courseAddStage.close());
        btnCancel.cancelButtonProperty();

        //Lisätään ikkunaan napit ja tekstikentät
        courseInfo.getChildren().addAll(courseID, courseName, courseOP,
                courseStartDate, courseCompleteDate, courseDesc, btnSave, btnCancel);
        courseWindowLayout.getChildren().add(courseInfo);

        courseAddStage.setScene(courseScene);
        courseAddStage.show();

    }

    private static void addCourseToDB(String courseID, String courseName, String courseOP,
    String courseStartDate, String courseCompleteDate, String courseDesc){

        //DB info
        final String dburl = "jdbc:mysql://localhost:3306/opsuor";
        final String dbusername = "USERNAME";
        final String dbpass = "PASSWORD";

        try{

            //Yhdistetään tietokantaan
            Connection connection = DriverManager.getConnection
                    (dburl, dbusername, dbpass);
            System.out.println("Database connected");

            //SQL-komento
            String insertKurssi = "INSERT INTO kurssi(" +
                    "kurssi_id, nimi, opintopisteet, kuvaus, suorituspvm, paatospvm) VALUES(" +
                    courseID + ',' + "'" + courseName + "'" + ',' + courseOP + ',' + "'" +
                    courseDesc + "'" + ',' + "'" + courseStartDate + "'" + ',' + "'" + courseCompleteDate + "'" + ");";
            PreparedStatement tempExecute;

            //Suoritetaan SQL-komento
            try{
                tempExecute = connection.prepareStatement(insertKurssi);
                tempExecute.executeUpdate();
            } catch (SQLException se){
                se.printStackTrace();
            }

            connection.close();

            //Virheet
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

}
