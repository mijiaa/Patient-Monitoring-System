package Controllers;

import Model.PatientModel;
import View.PatientInfoView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * This controller class is responsible for the MonitoredView and handle it's action event (button event)
 */
public class PatientInfoController extends AbstractController {

    /**
     * Base constructor
     * @param view Refers to the view to be managed by the controller
     * @param model Refers to the model to be managed by the controller
     */
    public PatientInfoController(PatientInfoView view, PatientModel model) {
        super(view, model);
        getHandlers().add(goBackButtonHandler);
        getView().addListener(getHandlers());
    }

    //================================================================================
    // Event Handler
    //================================================================================
    /**
     * The event handling of going back to the page of all monitored patients
     */
    EventHandler<ActionEvent> goBackButtonHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            try {
                MonitoredController prevController = (MonitoredController) PatientInfoController.this.getPrevController();
                prevController.startIterator();
                getView().getStage().setScene(PatientInfoController.this.getPreviousScene());
            }catch (Exception e){

            }

        }
    };


}



