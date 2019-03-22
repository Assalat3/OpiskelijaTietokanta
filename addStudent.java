import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class addStudent {
    public static void addStudentWindow(){

        //Setup
        Stage studentAddStage = new Stage();
        studentAddStage.initModality(Modality.APPLICATION_MODAL);

        final double windowWidth = 1000;
        final double windowHeight = 28;

        studentAddStage.setTitle("Opiskelijan lisäys");

        VBox courseWindowLayout = new VBox();

        Scene studentScene = new Scene(courseWindowLayout, windowWidth, windowHeight);

        HBox studentInfo = new HBox();

        //studentInfo:n sisältö ja mitoitus:
        double textFieldWidth = windowWidth / 4;
        TextField studentID = new TextField();
        studentID.setPromptText("ID:");
        studentID.setMaxWidth(textFieldWidth / 3);
        TextField studentFName = new TextField();
        studentFName.setPromptText("Opiskelijan etunimi:");
        studentFName.setMaxWidth(textFieldWidth / 2);
        TextField studentLName = new TextField();
        studentLName.setPromptText("Opiskelijan sukunimi:");
        studentLName.setMaxWidth(textFieldWidth / 2);
        TextField studentAdress = new TextField();
        studentAdress.setPromptText("Lähiosoite:");
        studentAdress.setMaxWidth(textFieldWidth / 2);
        TextField studentPostalZone = new TextField();
        studentPostalZone.setPromptText("Postitoimipaikka:");
        studentPostalZone.setMaxWidth(textFieldWidth / 2.0);
        TextField studentPostalNro = new TextField();
        studentPostalNro.setPromptText("Postinumero:");
        studentPostalNro.setMaxWidth(textFieldWidth / 4);
        TextField studentEmail = new TextField();
        studentEmail.setPromptText("Sähköpostiosoite:");
        studentEmail.setMaxWidth(textFieldWidth / 2);
        TextField studentPhone = new TextField();
        studentPhone.setPromptText("Puhelinnumero:");
        studentPhone.setMaxWidth(textFieldWidth / 3);

        //Tallennusnappi
        Button btnSave = new Button("Tallenna");
        btnSave.setPrefWidth(75);
        btnSave.setOnAction(e -> addStudentToDB(
                studentID.getText(),
                studentFName.getText(),
                studentLName.getText(),
                studentAdress.getText(),
                studentPostalZone.getText(),
                studentPostalNro.getText(),
                studentEmail.getText(),
                studentPhone.getText()
        ));


        //Peruutusnappi
        Button btnCancel = new Button("Sulje");
        btnCancel.setOnAction(e -> studentAddStage.close());
        btnCancel.cancelButtonProperty();
        btnCancel.setPrefWidth(75);

        //Lisätään ikkunaan napit ja tekstikentät
        studentInfo.getChildren().addAll(studentID, studentFName, studentLName,
                studentAdress, studentPostalZone, studentPostalNro, studentEmail,
                studentPhone, btnSave, btnCancel);
        courseWindowLayout.getChildren().add(studentInfo);

        studentAddStage.setScene(studentScene);
        studentAddStage.show();
    }

    //Funktio opiskelijan tallentamiselle tietokantaan
    private static void addStudentToDB(String studentID, String studentFName, String studentLName,
                                       String studentAdress, String studentPostalZone, String studentPostalNro,
                                       String studentEmail, String studentPhone) {
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
            String insertOpiskelija = "INSERT INTO opiskelija(" +
                    "opiskelija_id, etunimi, sukunimi, lahiosoite, postitoimipaikka, postinro, " +
                    "email, puhelinnro) VALUES(" +
                    studentID + ',' + "'" + studentFName + "'" + ',' + "'" + studentLName + "'" + ',' + "'" +
                    studentAdress + "'" + ',' + "'" + studentPostalZone + "'" + ',' + "'" + studentPostalNro + "'" +
                    ',' + "'" + studentEmail + "'" + ',' + "'" + studentPhone + "'" + ");";
            PreparedStatement tempExecute;

            //Suoritetaan SQL-komento
            try{
                tempExecute = connection.prepareStatement(insertOpiskelija);
                tempExecute.executeUpdate();
            } catch (SQLException se){
                se.printStackTrace();
            }

            connection.close();

            //Virheet
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
