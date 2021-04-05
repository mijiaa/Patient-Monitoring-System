package Controllers;

import Model.AbstractModel;
import Model.Observer;
import View.AbstractView;
import Backend.AbstractMonitor;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * This abstract class contains most attributes that Controller classes will have and their respective methods.
 */
public abstract class AbstractController implements Observer {


    // Abstract attributes
    private AbstractController prevController;
    private AbstractModel model;
    private AbstractView view;

    // UI Components
    private AbstractMonitor abstractMonitor;
    private Scene previousScene;
    private Stage stage;
    private TableView table;

    // View Event Handlers
    private ArrayList<EventHandler<ActionEvent>> handlers;

    /**
     * Base constructor of the controller object
     * @param view Refers to the view that it will be responsible for
     */
    public AbstractController(AbstractView view){
        this.view = view;
        this.stage = view.getStage();
        this.handlers = new ArrayList<EventHandler<ActionEvent>>();
    }

    /**
     * An alternative constructor of the Controller object if it's reliant on a singular Model object
     * @param view Refers to the view that it will be responsible for
     * @param model Refers to the model to be managed
     */
    public AbstractController(AbstractView view, AbstractModel model){
        this.view = view;
        this.stage = view.getStage();

        this.model = model;
        this.view.setModel(model);
        this.handlers = new ArrayList<EventHandler<ActionEvent>>();
    }


    // -> Getters
    /**
     * Getter of the reference of the previous controller
     * @return The reference to the previous controller
     */
    public AbstractController getPrevController(){
        return this.prevController;
    }

    public AbstractModel getModel() {
        return model;
    }

    public AbstractView getView() {
        return view;
    }

    public AbstractMonitor getAbstractMonitor(){
        return this.abstractMonitor;
    }

    /**
     * Method used in getting a reference to the previous scene
     */
    public Scene getPreviousScene(){
        return this.previousScene;
    }

    public Stage getStage() {
        return stage;
    }

    public TableView getTable() {
        return table;
    }

    public ArrayList<EventHandler<ActionEvent>> getHandlers() {
        return handlers;
    }

    // -> Setters
    public void setPrevController(AbstractController prevController) {
        this.prevController = prevController;
    }

    public void setModel(AbstractModel model) {
        this.model = model;
    }

    public void setAbstractMonitor(AbstractMonitor abstractMonitor){
        this.abstractMonitor = abstractMonitor;
    }

    /**
     * Method used in setting a reference to the previous scene
     * @param previousScene Refers to the previous scene that will be stored as reference for this controller
     */
    public void setPreviousScene(Scene previousScene){
        this.previousScene = previousScene;
    }

    /**
     * Method used in setting a reference to the stage used by previous controllers
     * @param stage Refers to the stage that will be set as a reference (usually for the handlers)
     */
    public void setStage(Stage stage){
        this.stage = stage;
    }

    public void setTable(TableView table) {
        this.table = table;
    }

    public void setHandlers(ArrayList<EventHandler<ActionEvent>> handlers) {
        this.handlers = handlers;
    }

    //================================================================================
    // Observer function
    //================================================================================

    /**
     * Main update function used by all observers (controller and view classes)
     */
    public void update(){ }




}


