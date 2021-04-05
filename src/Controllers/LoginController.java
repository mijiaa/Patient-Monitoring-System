package Controllers;

import Backend.PatientInfoBP;
import Backend.PatientInfoBasic;
import Model.PatientModel;
import View.AbstractView;
import View.AllPatientsView;
import Backend.PatientInfoCholesterol;
import Backend.Practitioner;
import View.LoginView;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * This controller class is responsible for the LoginView and handle it's action event (button event)
 */
public class LoginController extends AbstractController {

    /**
     * Base constructor of the controller object
     * @param view Refers to the view that it will be responsible for
     */
    public LoginController(AbstractView view) {
        super(view);
        getHandlers().add(logInButtonHandler);
        view.addListener(getHandlers());
    }

    //================================================================================
    // Event Handler
    //================================================================================

    /**
     * The event handling of logging in based on patient ID provided
     */
    EventHandler<ActionEvent> logInButtonHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {

            String doctorId = ((LoginView) getView()).getPractitionerIdTextField().getText();


            try {
                // Checks if the practitioner's ID is valid.
                Integer.parseInt(doctorId);
                Practitioner practitioner = new Practitioner(doctorId);

                try {

                    ObservableList<PatientModel> allPatients = practitioner.retrievePatientList();

                    // Retrieves patient info based on practitioner's list of patient
                    PatientInfoCholesterol cholesterol = new PatientInfoCholesterol();
                    PatientInfoBasic basic = new PatientInfoBasic();
                    PatientInfoBP bp = new PatientInfoBP();
                    basic.retrievePatientInfo(allPatients);

                    // Filters the list of patient based on available values
                    ObservableList<PatientModel> BPFilteredPatients = basic.getFilteredPatients() ;
                    ObservableList<PatientModel> ChoFilteredPatients = cholesterol.getFilteredPatients();

                    // Instantiate the view and controller objects for the next scene
                    AllPatientsView monitored = new AllPatientsView();
                    AllPatientsController allPatientsController = new AllPatientsController(monitored);
                    allPatientsController.feedPatientData(BPFilteredPatients);

                    // Starts the next scene
                    monitored.start(getView().getStage());

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                // Normally an alert will be produced but it's beyond the project scope and would take too much time.
                System.out.println("Please insert a valid practitioner ID (numbers only).");
            }

        }
    };

}
