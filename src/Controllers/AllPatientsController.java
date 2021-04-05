package Controllers;

import Backend.GeneralMonitor;
import Model.PatientModel;
import View.AbstractView;
import View.AllPatientsView;
import View.MonitoredView;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * This controller class is responsible for the AllPatientsView and handle it's action event (button event)
 */
public class AllPatientsController extends AbstractController {

    /**
     * Base constructor of the controller object
     * @param view Refers to the view that it will be responsible for
     */
    public AllPatientsController(AbstractView view) {
        super(view);

        getHandlers().add(monitorButtonHandler);
        view.addListener(getHandlers());

        setAbstractMonitor(new GeneralMonitor());
        setTable(((AllPatientsView)view).getAllPatientsTable());
    }

    //================================================================================
    // Event Handlers
    //================================================================================

    /**
     * The event handling of monitoring the selected patients
     */
     EventHandler<ActionEvent> monitorButtonHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            setStage(getView().getStage());
            setTable(((AllPatientsView)getView()).getAllPatientsTable());

            // Gets the selected item
            getAbstractMonitor().addMonitoredPatients(getTable().getSelectionModel().getSelectedItems());

            // Initialise the next view
            MonitoredView view = new MonitoredView();

            // Initialise the next controller and sets it's attribute accordingly
            MonitoredController monitoredController = new MonitoredController(view);
            monitoredController.setAbstractMonitor(getAbstractMonitor());
            monitoredController.update();
            monitoredController.setPrevController(AllPatientsController.this);

            // Reset the observers of all the patient models
            resetObservers(getAbstractMonitor().getMonitoredPatients(), monitoredController, view);

            // Clears the selection (UI purposes)
            getTable().getSelectionModel().clearSelection();

            // Sets a reference to the previous scene and starts the next scene
            monitoredController.setPreviousScene(getStage().getScene());
            view.start(getStage());
        }
    };


    //================================================================================
    // Additional functions for controller functionality
    //================================================================================

    /**
     * Feeds patient data to this controller (fed by loginController)
     * @param patientModels Observable list of patient models containing a list of filtered patients (possessing Cholesterol data)
     */
    public void feedPatientData (ObservableList<PatientModel> patientModels) {

        // Sets the observers of each patient model object
        for (PatientModel patient:patientModels){
            patient.addObserver(this);
            patient.addObserver(getView());
        }

        // Updates the monitor as to contain the list of filtered patients to be displayed in the AllPatientsView
        getAbstractMonitor().setUnmonitoredPatients(patientModels);
        update();
    }

    /**
     * Resets the observers of each of the selected Patient Models and set their observers to the respective controller and view
     * @param patientModels Refers to the observables to be worked on
     * @param controller Refers to the new controller that will be the models' observer
     * @param view Refers to the new view that will be the models' observer
     */
    public void resetObservers(ObservableList<PatientModel> patientModels, AbstractController controller, AbstractView view){
        for (PatientModel patient:patientModels){
            patient.clearObservers();
            patient.addObserver(controller);
            patient.addObserver(view);
        }
    }

    //================================================================================
    // Controller Observer Function
    //================================================================================

    /**
     * Standard observer update function
     */
    @Override
    public void update() {
        getView().setData(getAbstractMonitor().getUnmonitoredPatients());
    }
}
