import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import java.util.ArrayList;

/**
 * Toni Ordning, 10.03.2019
 *
 * Pääasiallinen ikkuna. Ohjelman käynnistyessä käyttäjälle näkee listan kursseista, sekä napit kurssien ja opis-
 * kelijoiden lisäämiseen. Myös nappi opiskelijoiden listaamiseen näkyy käyttäjälle. Käyttäjän pitää lisätä opiskelija,
 * ennenkuin hän voi lisätä opiskelijalle suorituksen. Kurssien ja opiskelijoiden lisääminen sekä muokkaaminen
 * avaavat uuden ikkunan, johon käyttäjä syöttää tiedot. Kaikissa kentissä tulee olla syötettä, muuten ohjelma ei toimi.
 * Kun käyttäjiä ja kursseja on lisätty, ne näkyvät ohjelman pääikkunassa uudelleenkäynnistyksen jälkeen. En kerennyt
 * lisäämään funktiota, joka päivittäisi listaa muutosten tapahtuessa. Ohjelma lisää opiskelijoiden ja kurssien perään
 * napit, joiden avulla tietoja voidaan muokata, poistaa sekä tarkastella suorituksia. Suoritusten lisääminen tapahtuu
 * opiskelijanäkymän suoritus-napin kautta. Suoritus-napit avaavat uuden ikkunan, jossa näkyy joko kurssinäkymän kautta
 * kurssiin liittyvät suorituksen tai opiskelijanäkymän kautta opiskelijan suoritukset. Kun suorituksia on lisätty
 * opiskelijalle, ikkuna pitää sulkea ja avata uudelleen suoritusten näkymiseksi.
 */

public class gui extends Application {

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage defaultStage) {

        //Ikkunan alkuperäinen koko:
        final double defaultWindowWidth = 1280;
        final double defaultWindowHeight = 600;

        defaultStage.setTitle("Kurssinäkymä");

        //Layout ikkunalle
        Pane defaultWindowLayout = new Pane();

        //Luo ikkunan, koko 1100x600
        Scene defaultScene = new Scene(defaultWindowLayout, defaultWindowWidth, defaultWindowHeight);

        //Seuraavat osiot käsittelevät nappeja:
        //Luodaan pystysuuntainen valikko napeille
        VBox horizontalButtons = new VBox(5);
        horizontalButtons.setAlignment(Pos.BASELINE_CENTER);

        //Napit ja niiden koko:
        double buttonWidth = 100;
        Label lblAction = new Label("Toiminnot");
        Button btnAddCourse = new Button("Lisää kurssi");
        Button btnAddStudent = new Button("Lisää opiskelija");
        Label lblBuffer = new Label();
        Label lblView = new Label("Näkymä");
        Button btnStudentview = new Button("Opiskelijat");

        lblAction.setMinSize(16,16);

        btnAddCourse.setMinWidth(buttonWidth);
        btnAddCourse.setOnAction(e -> addCourse.addCourseWindow());
        btnAddStudent.setMaxWidth(buttonWidth);
        btnAddStudent.setOnAction(e -> addStudent.addStudentWindow());

        lblView.setMinSize(16, 16);

        btnStudentview.setMinWidth(buttonWidth);
        btnStudentview.setMaxWidth(buttonWidth);

        //Lisätään napit valikkoon vasemmalle sivulle.
        horizontalButtons.getChildren().addAll(lblAction, btnAddCourse, btnAddStudent, lblBuffer, lblView);
        defaultWindowLayout.getChildren().add(horizontalButtons);

        //Näytetään tietokannassa olevat kurssit
        VBox verticalList = new VBox();
        ArrayList<courseStructure> coursesFromDB = listCourses.listCoursesFromDB();
        for (int i = 0; i < coursesFromDB.size(); i++){
            //Kurssi
            courseStructure tempCourse = coursesFromDB.get(i);
            HBox newCourse = new HBox(5);

            //Kurssin asettelu
            TextField newCourseID = new TextField(tempCourse.id);
            newCourseID.setEditable(false);
            newCourseID.setMinWidth(75);
            newCourseID.setPrefWidth(75);
            TextField newCourseName = new TextField(tempCourse.name);
            newCourseName.setEditable(false);
            newCourseName.setMinWidth(150);
            TextField newCourseOP = new TextField(tempCourse.op);
            newCourseOP.setEditable(false);
            newCourseOP.setMinWidth(50);
            newCourseOP.setPrefWidth(50);
            TextField newCourseDesc = new TextField(tempCourse.description);
            newCourseDesc.setEditable(false);
            newCourseDesc.setMinWidth(300);
            newCourseDesc.setPrefWidth(300);
            TextField newCourseStartDate = new TextField(tempCourse.startDate);
            newCourseStartDate.setEditable(false);
            newCourseStartDate.setMinWidth(75);
            TextField newCourseCompDate = new TextField(tempCourse.completeDate);
            newCourseCompDate.setEditable(false);
            newCourseCompDate.setMinWidth(75);

            //Muokkausnappi
            Button btnEdit = new Button("Muokkaa");
            String btnEditCourseID = tempCourse.id;
            btnEdit.setMinWidth(75);
            btnEdit.setOnAction(e -> editCourse.editCourseWindow(btnEditCourseID));

            //Poistonappi
            Button btnDeleteCourse = new Button("Poista");
            btnDeleteCourse.setMinWidth(75);
            btnDeleteCourse.setOnAction(e -> editCourse.removeCourse(btnEditCourseID));

            //Kurssien suoritusten näyttämiselle nappi
            Button btnShowCourseAccomplishments = new Button("Suoritukset");
            btnShowCourseAccomplishments.setMinWidth(75);
            btnShowCourseAccomplishments.setOnAction(e -> editCourse.showCourseAccomplishments());

            //Lisätään kaikki VBoxiin ja VBox kurssilistaan
            newCourse.getChildren().addAll(newCourseID, newCourseName, newCourseOP, newCourseDesc,
                    newCourseStartDate, newCourseCompDate, btnEdit, btnDeleteCourse, btnShowCourseAccomplishments);
            verticalList.getChildren().add(newCourse);
        }
        //Lisätään verticalcourselist ikkunaan ja asetellaan paikoilleen.
        defaultWindowLayout.getChildren().add(verticalList);
        verticalList.relocate(120, 10);
        verticalList.setMaxWidth(defaultWindowWidth - 120);
        verticalList.setMaxHeight(defaultWindowHeight - 10);

        //Näytetään tietokannassa olevat opiskelijat
        VBox verticalStudentList = new VBox();
        ArrayList<studentStructure> studentsFromDB = studentView.showStudents();

        for (int i = 0; i < studentsFromDB.size(); i++){

            //Opiskelija
            studentStructure tempStudent = studentsFromDB.get(i);
            HBox newStudent = new HBox(5);

            //Opiskelijan asettelu (Kuten kurssilistassa)
            TextField newStudentID = new TextField(tempStudent.id);
            newStudentID.setEditable(false);
            newStudentID.setMinWidth(50);
            newStudentID.setPrefWidth(50);
            TextField newStudentFName = new TextField(tempStudent.fname);
            newStudentFName.setEditable(false);
            newStudentFName.setMinWidth(80);
            TextField newStudentLName = new TextField(tempStudent.lname);
            newStudentLName.setEditable(false);
            newStudentLName.setMinWidth(80);
            TextField newStudentAdress = new TextField(tempStudent.adress);
            newStudentAdress.setEditable(false);
            newStudentAdress.setMinWidth(125);
            newStudentAdress.setPrefWidth(125);
            TextField newStudentPZ = new TextField(tempStudent.postalzone);
            newStudentPZ.setEditable(false);
            newStudentPZ.setMinWidth(80);
            TextField newStudentPNumber = new TextField(tempStudent.postalnro);
            newStudentPNumber.setEditable(false);
            newStudentPNumber.setMinWidth(60);
            TextField newStudentEmail = new TextField(tempStudent.email);
            newStudentEmail.setEditable(false);
            newStudentEmail.setMinWidth(150);
            newStudentEmail.setPrefWidth(150);
            TextField newStudentPhone = new TextField(tempStudent.phonenro);
            newStudentPhone.setEditable(false);
            newStudentPhone.setMinWidth(80);

            //Muokkausnappi
            Button btnEdit = new Button("Muokkaa");
            String btnEditStudentID = tempStudent.id;
            btnEdit.setMinWidth(75);
            btnEdit.setOnAction(e -> editStudent.editStudentWindow(btnEditStudentID));

            //Poistonappi
            Button btnDeleteStudent = new Button("Poista");
            btnDeleteStudent.setMinWidth(60);
            btnDeleteStudent.setOnAction(e -> editStudent.removeStudent(btnEditStudentID));

            //Kurssisuoritusten nappi
            Button btnAccomplishment = new Button("Suoritukset");
            btnAccomplishment.setMinWidth(100);
            btnAccomplishment.setOnAction(e -> accomplishment.accomplishmentView(btnEditStudentID));

            //Lisätään kaikki VBoxiin ja VBox kurssilistaan
            newStudent.getChildren().addAll(newStudentID, newStudentFName, newStudentLName,
                    newStudentAdress, newStudentPZ, newStudentPNumber, newStudentEmail, newStudentPhone,
                    btnEdit, btnDeleteStudent, btnAccomplishment);
            verticalStudentList.getChildren().add(newStudent);
        }
        //Siirretään, mutta ei lisätä opiskelijalistaa perusnäkymään. Tehdään näkyväksi napilla.
        verticalStudentList.relocate(120, 10);
        verticalStudentList.setPrefWidth(defaultWindowWidth - 120);
        verticalStudentList.setPrefHeight(defaultWindowHeight - 10);

        //Nappi opiskelijoiden näyttämiseen.
        btnStudentview.setOnAction(e -> {
            //Määrää napin toiminnan sen mukaan, ollaanko käsittelemässä opiskelijoita vai kursseja
            if (btnStudentview.getText() == "Opiskelijat") {
                btnStudentview.setText("Kurssit");
                defaultStage.setTitle("Opiskelijanäkymä");
                defaultWindowLayout.getChildren().remove(verticalList);
                defaultWindowLayout.getChildren().add(verticalStudentList);
            } else if (btnStudentview.getText() == "Kurssit") {
                btnStudentview.setText("Opiskelijat");
                defaultStage.setTitle("Kurssinäkymä");
                defaultWindowLayout.getChildren().remove(verticalStudentList);
                defaultWindowLayout.getChildren().add(verticalList);
            }
        });
        horizontalButtons.getChildren().add(btnStudentview);
        horizontalButtons.relocate(10,10);

        //Apulinjoja ohjelman järjestelyyn, päätä lopuksi, kannattaako näitä jättää.
        Line lineVertical = new Line(115, 700, 115, 0);
        Line lineHorizontal = new Line(0,110 ,115, 110);
        defaultWindowLayout.getChildren().addAll(lineVertical, lineHorizontal);

        //Scenen asetus ja Stagen näyttäminen
        defaultStage.setScene(defaultScene);
        defaultStage.setResizable(false);
        defaultStage.show();

    }

}