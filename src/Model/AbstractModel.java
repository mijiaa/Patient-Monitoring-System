package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *  This abstract class is meant to serve as a point of reference to all Controller classes and houses the basic.
 *  abstract methods and attributes of the observable models (currently PatientModel).
 */
public abstract class AbstractModel implements Observable {
    // Contains the list of observers of the current observable (usually the current controller and view, the model is being used in)
    private ObservableList<Observer> observers = FXCollections.observableArrayList();
    public ObservableList<Observer> getObservers() {
        return observers;
    }

    //================================================================================
    // Standard Model Observable functions
    //================================================================================

    /**
     * Adds an observer that will be notified of any changes made to the current observable.
     * @param observer  Observer object to be notified to update if a change occurs to this Observable.
     */
    public void addObserver(Observer observer){
        observers.add(observer);
    }

    /**
     * Removes an observer from this Observable's list
     * @param observer Observer object that was previously observing this Observable.
     */
    public void removeObserver(Observer observer){
        observers.remove(observer);
    }

    /**
     * Removes all observers
     */
    public void clearObservers(){
        observers.removeAll();
    }

}
