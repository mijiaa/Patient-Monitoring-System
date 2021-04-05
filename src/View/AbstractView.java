package View;

import Model.AbstractModel;
import Model.Observer;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * This abstract class is meant to serve as a point of reference to to all Controller classes and contains the basic
 * methods and attributes that most View classes would use
 */
public abstract class AbstractView extends Application implements Observer {

    //================================================================================
    // Attributes
    //================================================================================
    private AbstractModel model;
    private Stage stage;
    private ObservableList data;

    //================================================================================
    // Getters and Setters
    //================================================================================
    // -> Getters
    public Stage getStage (){
        return this.stage;
    }

    public AbstractModel getModel() {
        return model;
    }

    public ObservableList getData() {
        return data;
    }

    // -> Setters
    public void setStage(Stage stage){
        this.stage = stage;
    }

    public void setModel(AbstractModel model){
        this.model = model;
    }

    public void setData(ObservableList data) {
        this.data = data;
    }

    //================================================================================
    // Standard view functions
    //================================================================================

    /**
     * Main function used to display UI components
     * @param stage Stage reference for the UI
     */
    @Override
    public void start(Stage stage) {
        // Do something regarding UI Design
    }

    /**
     * Used to designate the event handlers instantiated in the Controller classes
     * @param handlers  Refers to the handlers used for the buttons
     */
    public void addListener(ArrayList<EventHandler<ActionEvent>> handlers){
        // Designate some button to carry out action.
    }

    //================================================================================
    // Standard observer function
    //================================================================================

    /**
     * Main function used to update UI
     */
    public void update(){
        // Update UI
    }

    //================================================================================
    // Loading functions (useful for loading indication)
    //================================================================================

    public void startLoading(){ }

    public void endLoading(){ }






}
