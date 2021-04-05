package View;

import Model.PatientModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 *  This View class is responsible for displaying the UI of the patient info page,
 *  note that this view would not update based on updated patient info
 */
public class PatientInfoView extends AbstractView {

    private PatientModel patientModel;
    private Button goBackButton = new Button("Go back");

    //================================================================================
    // Main functions for view
    //================================================================================

    public void start(Stage stage) {
        setStage(stage);

        // Creating the label
        final Label title = new Label("Patient information:");
        title.setFont(new Font("Arial", 18));

        // Setting the patient info
        final Label nameLabel = new Label("Patient Name: "+ patientModel.getFirstName() + " " + patientModel.getSurname());
        final Label birthLabel = new Label("Date of Birth: " + patientModel.getBirthDate());
        final Label genderLabel = new Label("Gender: " + patientModel.getGender());
        final Label addressLabel = new Label("Address: " + patientModel.getAddress());
        final Label cityLabel = new Label("City: " + patientModel.getCity());
        final Label stateLabel = new Label("State: " + patientModel.getState());
        final Label countryLabel = new Label("Country: " + patientModel.getCountry());

        // Initialising the VerticalBox of JavaFX
        VBox vb = new VBox();
        vb.getChildren().addAll(title,nameLabel, birthLabel, genderLabel, addressLabel, cityLabel, stateLabel, countryLabel, goBackButton);
        vb.setSpacing(10);
        vb.setPadding(new Insets(10, 10, 10, 10));

        // Creating a new scene with a 800 x 600 dimension
        Scene scene = new Scene(vb, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Sets the event handler for the button
     * @param handlers  Refers to the handler used for the button
     */
    @Override
    public void addListener(ArrayList<EventHandler<ActionEvent>> handlers) {
        goBackButton.setOnAction(handlers.get(0));
    }

    //================================================================================
    // Setter for private attribute (basically receiving data from the controller)
    //================================================================================

    /**
     * Provides the patient to which the information is displayed
     * @param patientModel Patient object that holds the data of the
     */
    public void setPatient(PatientModel patientModel){
        this.patientModel = patientModel;
    }



}
