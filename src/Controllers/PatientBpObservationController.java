package Controllers;

import Model.PatientModel;
import View.AbstractView;
import View.BPObservationView;
import View.LineChartView;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;

public class PatientBpObservationController extends RefreshController {

    private Scene tableScene;
    private LineChartView lineChartView;
    private ObservableList<PatientModel> patientModels;

    /**
     * Base constructor
     * @param view Refers to the view to be managed by the controller
     */
    public PatientBpObservationController(BPObservationView view) {
        super(view);
        getHandlers().add(goBackButtonHandler);
        getHandlers().add(changeToLineChartButtonHandler);
        getHandlers().add(changeToTextualButtonHandler);
        getView().addListener(getHandlers());
    }


    public void setPatientModels(ObservableList<PatientModel> patientModels){
        this.patientModels = patientModels;
    }

    //================================================================================
    // Event Handlers
    //================================================================================

    /**
     * The event handling of going back to the page containing all monitored patients
     */
    EventHandler<ActionEvent> goBackButtonHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            try {
                stopIterator();
                MonitoredController prevController = (MonitoredController) PatientBpObservationController.this.getPrevController();

                clearObservers(patientModels, prevController, prevController.getView());

                prevController.startIterator();
                getView().getStage().setScene(PatientBpObservationController.this.getPreviousScene());

            }catch (Exception e){
                // Ignore if failed
            }
        }
    };

    /**
     * The event handling of changing the data display to the form of a line chart
     */
    EventHandler<ActionEvent> changeToLineChartButtonHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            setStage(getView().getStage());
            tableScene = getView().getStage().getScene();

            lineChartView = new LineChartView();
            lineChartView.setData(patientModels);
            lineChartView.addListener(getHandlers());
            addViewObserver(patientModels, lineChartView);

            lineChartView.start(getStage());
        }
    };

    /**
     * The event handling of changing the data display to a textual representation
     */
    EventHandler<ActionEvent> changeToTextualButtonHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            setStage(getView().getStage());
            addViewObserver(patientModels, getView());
            getStage().setScene(tableScene);
        }
    };

    //================================================================================
    // Controller observer functions
    //================================================================================

    /**
     * Resets the observers of each of the selected Patient Models and set their observers to the respective controller and view
     * @param patientModels Refers to the observables to be worked on
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


}
