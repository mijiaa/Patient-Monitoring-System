package View;

import Backend.PatientInfoBP;
import Model.PatientModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;

public class BPObservationView extends AbstractView {


    private PatientInfoBP bp = new PatientInfoBP();
    private Button goBackButton = new Button("Go back");
    private Button toggleDisplayButton = new Button("Systolic Blood Pressure Line Graph");

    //================================================================================
    // Main functions for view
    //================================================================================
    public void start(Stage stage) {
        setStage(stage);

        // Creating the label
        final Label title = new Label("Patient Systolic Blood Pressure:");
        title.setFont(new Font("Arial", 18));


        VBox mainVbox = new VBox();
        mainVbox.getChildren().addAll(title);
        mainVbox.setSpacing(10);
        mainVbox.setPadding(new Insets(10, 10, 10, 10));

        for (Object patient:getData()){

            PatientModel patientModel = (PatientModel) patient;
            bp.getBPObservations(patientModel);

            // Setting the patient info
            final Label nameLabel = new Label("Patient Name: "+ patientModel.getFirstName() + " " + patientModel.getSurname());
            final Label sbpLabel = new Label("Systolic blood pressure (mm/Hg): " + patientModel.getSbpValues().toString());

            mainVbox.getChildren().addAll(nameLabel, sbpLabel);
        }

        HBox buttons = new HBox();
        buttons.getChildren().addAll(goBackButton, toggleDisplayButton);
        buttons.setSpacing(10);

        mainVbox.getChildren().add(buttons);

        // Creating a new scene with a 800 x 600 dimension
        Scene scene = new Scene(mainVbox, 800, 600);
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
        toggleDisplayButton.setOnAction(handlers.get(1));
    }
}
