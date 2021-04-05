package Controllers;

import Backend.*;
import Model.PatientModel;
import View.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.util.Duration;

/**
 * This controller class is responsible for the MonitoredView and handle it's action event (button event)
 */
public class MonitoredController extends RefreshController {

    // Functional attributes
    private Double duration = 60.0;
    private PatientInfoCholesterol cholesterol = new PatientInfoCholesterol();
    private PatientInfoBP bp = new PatientInfoBP();
    private BloodPressureMonitor bpMonitor = new BloodPressureMonitor();
    private CholesterolMonitor cholesterolMonitor = new CholesterolMonitor();

    // UI Components
    private Scene tableScene;
    private Scene barChartScene;

    /**
     * Base constructor of the controller object
     * @param view Refers to the view that it will be responsible for
     */
    public MonitoredController(AbstractView view) {
        super(view);

        getHandlers().add(infoButtonHandler);
        getHandlers().add(bpButtonHandler);
        getHandlers().add(addButtonHandler);
        getHandlers().add(removeButtonHandler);
        getHandlers().add(refreshIntervalButtonHandler);
        getHandlers().add(setSbpLimit);
        getHandlers().add(setDbpLimit);
        getHandlers().add(changeToBarChartButtonHandler);
        getHandlers().add(changeToTableButtonHandler);
        getHandlers().add(toggleCholesterol);
        getHandlers().add(toggleBpValues);

        setTable(((MonitoredView)view).getMonitoredPatientsTable());
        getView().addListener(getHandlers());
    }

    /**
     * Setting the Monitor of the controller, overridden so that iterator can be set up here as well
     * @param abstractMonitor Refers to the monitor object that contains information on current monitored and unmonitored patients
     */
    @Override
    public void setAbstractMonitor(AbstractMonitor abstractMonitor) {
        super.setAbstractMonitor(abstractMonitor);
        bpMonitor.addMonitoredPatients(abstractMonitor.getMonitoredPatients());
        cholesterolMonitor.addMonitoredPatients(abstractMonitor.getMonitoredPatients());
        bootingUpIterator();
        startIterator();
    }

    //================================================================================
    // Event Handlers
    //================================================================================

    /**
     * The event handling of going into PatientInfo View for more patient information
     */
    EventHandler<ActionEvent> infoButtonHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            setTable(((MonitoredView)getView()).getMonitoredPatientsTable());
            setStage(getView().getStage());
            try {
                stopIterator();

                // Initialising a new view and getting the selected patient model
                PatientModel patientModelSelected = (PatientModel) getTable().getSelectionModel().getSelectedItem();
                PatientInfoView patientInfoView = new PatientInfoView();

                // Initialising the controller responsible for managing the view and model
                PatientInfoController patientInfoController = new PatientInfoController(patientInfoView,patientModelSelected);
                patientInfoController.setPrevController(MonitoredController.this);
                patientInfoController.setPreviousScene(getStage().getScene());


                // Starts the view
                getTable().getSelectionModel().clearSelection();
                patientInfoView.setPatient(patientModelSelected);
                patientInfoView.start(getStage());
            } catch (Exception e){
                // Error happens if more than one cell is selected
                System.out.println("Please ensure only one cell is selected");
            }
        }
    };

    /**
     * The event handling of going into BPObservation View for blood pressure observations
     */
    EventHandler<ActionEvent> bpButtonHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            setTable(((MonitoredView)getView()).getMonitoredPatientsTable());
            setStage(getView().getStage());
            stopIterator();

            // Initialising a new view and getting the selected patient model
            ObservableList patientsSelected = getTable().getSelectionModel().getSelectedItems();
            BPObservationView bpObservationView = new BPObservationView();

            // Initialising the controller responsible for managing the view and model
            PatientBpObservationController patientBpObservationController = new PatientBpObservationController(bpObservationView);
            patientBpObservationController.setPrevController(MonitoredController.this);
            patientBpObservationController.setPreviousScene(getStage().getScene());
            patientBpObservationController.setPatientModels(patientsSelected);
            clearObservers(patientsSelected, patientBpObservationController, bpObservationView);

            // Uses the same iterator for the bp observation views and controller as well as start it
            patientBpObservationController.setTimeIterator(getTimeIterator());
            patientBpObservationController.startIterator();

            // Starts the view
            bpObservationView.setData(patientsSelected);
            bpObservationView.start(getStage());
        }
    };

    /**
     * The event handling of going back to the page with the unmonitored patients
     */
    EventHandler<ActionEvent> addButtonHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            setTable(((MonitoredView)getView()).getMonitoredPatientsTable());
            setStage(getView().getStage());
            getTable().refresh();
            stopIterator();
            getStage().setScene(MonitoredController.this.getPreviousScene());
        }
    };

    /**
     * The event handling of removing the patient to be monitored
     */
    EventHandler<ActionEvent> removeButtonHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            stopIterator();
            setTable(((MonitoredView)getView()).getMonitoredPatientsTable());
            setStage(getView().getStage());

            // Removes the observers of selected patients and un-monitor them
            ObservableList selectedPatients = getTable().getSelectionModel().getSelectedItems();
            clearPatientObservers(selectedPatients, MonitoredController.this.getPrevController(), MonitoredController.this.getPrevController().getView());
            ((GeneralMonitor) getAbstractMonitor()).unMonitorPatients(selectedPatients);

            bootingUpIterator();
            update();
            startIterator();
        }
    };

    /**
     * The event handling of setting a new refresh interval
     */
    EventHandler<ActionEvent> refreshIntervalButtonHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            stopIterator();
            duration = Double.parseDouble(((MonitoredView) getView()).getIntervalTextField().getText());
            bootingUpIterator();
            startIterator();
            ((MonitoredView) getView()).getIndicatorLabel().setText("The iterator has been updated to refresh at: " + duration.toString() + " seconds.");
        }
    };

    /**
     * The event handling of setting a new Systolic blood pressure limit
     */
    EventHandler<ActionEvent> setSbpLimit = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            ((MonitoredView)getView()).updateSbpLimit(Double.parseDouble(((MonitoredView)getView()).getSbpLimitTextField().getText()));
        }
    };

    /**
     * The event handling of setting a new Diastolic blood pressure limit
     */
    EventHandler<ActionEvent> setDbpLimit = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            ((MonitoredView)getView()).updateDbpLimit(Double.parseDouble(((MonitoredView)getView()).getDbpLimitTextField().getText()));
        }
    };

    /**
     * The event handling of changing the data display to the form of a bar chart
     */
    EventHandler<ActionEvent> changeToBarChartButtonHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            setStage(getView().getStage());
            tableScene = getView().getStage().getScene();
            BarChartView barChartView = new BarChartView();
            barChartView.setData(cholesterolMonitor.getMonitoredPatients());
            barChartView.addListener(getHandlers());
            addViewObserver(cholesterolMonitor.getMonitoredPatients(), barChartView);

            getTable().getSelectionModel().clearSelection();
            barChartView.start(getStage());
        }
    };

    /**
     * The event handling of changing the data display to the form of a table
     */
    EventHandler<ActionEvent> changeToTableButtonHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            setStage(getView().getStage());
            barChartScene = getView().getStage().getScene();
            addViewObserver(cholesterolMonitor.getMonitoredPatients(), getView());
            getStage().setScene(tableScene);
        }
    };

    /**
     * The event handling of toggling the cholesterol observation of monitored patients
     */
    EventHandler<ActionEvent> toggleCholesterol = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            for (Object patient : getTable().getSelectionModel().getSelectedItems()){
                try {
                    if (cholesterolMonitor.getMonitoredPatients().contains(patient)){
                        cholesterolMonitor.getMonitoredPatients().remove(patient);
                    } else {
                        cholesterolMonitor.addMonitoredPatient((PatientModel) patient);
                    }

                    ((PatientModel) patient).toggleDisplayCholesterol();
                    ((PatientModel) patient).toggleDisplayChoDate();

                    getTable().refresh();

                } catch (Exception e){
                    System.out.println("Please ensure a proper cell has been selected");
                }
            }


        }
    };

    /**
     * The event handling of toggling the blood pressure observation of monitored patients
     */
    EventHandler<ActionEvent> toggleBpValues = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            for (Object patient: getTable().getSelectionModel().getSelectedItems()){
                try {
                    if (bpMonitor.getMonitoredPatients().contains(patient)){
                        bpMonitor.getMonitoredPatients().remove(patient);
                    } else {
                        bpMonitor.addMonitoredPatient((PatientModel) patient);
                    }

                    ((PatientModel) patient).toggleDisplayDbp();
                    ((PatientModel) patient).toggleDisplaySbp();
                    ((PatientModel) patient).toggleDisplayBpDate();
                    getTable().refresh();
                } catch (Exception e) {
                    System.out.println("Please ensure a proper cell has been selected");
                }
            }

        }
    };

    //================================================================================
    // Controller observer functions
    //================================================================================

    /**
     * Clears the observers for a list of patients and removes the reference of the patients in the monitored list
     * @param patients List of patients to be operated on (clear observer and remove reference)
     * @param controller Refers to the next controller to add an observer reference to
     * @param view Refers to the next view to add an observer reference to
     */
    public void clearPatientObservers(ObservableList<PatientModel> patients, AbstractController controller, AbstractView view){
        for (PatientModel patient:patients){
            if (cholesterolMonitor.getMonitoredPatients().contains(patient)){
                cholesterolMonitor.getMonitoredPatients().remove(patient);
            }

            if (bpMonitor.getMonitoredPatients().contains(patient)){
                bpMonitor.getMonitoredPatients().remove(patient);
            }

            patient.clearObservers();
            patient.addObserver(controller);
            patient.addObserver(view);
        }

    }

    /**
     * Resets the observers of each of the selected Patient Models and set their observers to the respective controller and view
     * @param patientModels Refers to the observables that their observers will be reset
     * @param controller Refers to the new controller that will be the model's observer
     * @param view Refers to the new view that will be the model's observer
     */
    public void clearObservers(ObservableList<PatientModel> patientModels, AbstractController controller, AbstractView view){
        for (PatientModel patient:patientModels){
            patient.clearObservers();
            patient.addObserver(controller);
            patient.addObserver(view);
        }

    }

    /**
     * Adds a view as an observer to the list of PatientModels
     * @param patientModels List of patient models to be added a new observer
     * @param view Observer to be added
     */
    public void addViewObserver(ObservableList<PatientModel> patientModels, AbstractView view){
        for (PatientModel patient:patientModels){
            patient.addObserver(view);
        }
    }

    //================================================================================
    // Controller update function
    //================================================================================

    /**
     * Updates the controller if a patient model is updated
     */
    @Override
    public void update() {
        getView().setData(getAbstractMonitor().getMonitoredPatients());
    }

    //================================================================================
    // Additional function for controller functionality
    //================================================================================

    /**
     * Main function used in booting up the iterator (if a change in duration is made or coming back to this view)
     */
    public void bootingUpIterator (){
        setTimeIterator(new Timeline(new KeyFrame(Duration.seconds(duration), actionEvent -> {
            getView().startLoading();
            cholesterol.updateData(getAbstractMonitor().getMonitoredPatients());
            bp.updateData(getAbstractMonitor().getMonitoredPatients());
            System.out.println("Latest total cholesterol values and blood pressur values updated");
            getView().endLoading();

        })));
        getTimeIterator().setCycleCount(Timeline.INDEFINITE);
    }

}
