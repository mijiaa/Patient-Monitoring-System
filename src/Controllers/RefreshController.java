package Controllers;

import Model.AbstractModel;
import View.AbstractView;
import javafx.animation.Timeline;

/**
 * An abstract class that is meant to contain controller classes that might implement refreshes (a useful point of
 * extension for further application development)
 */
public abstract class RefreshController extends AbstractController{

    // Most controllers that deal with refreshes will contain a timeline attribute
    private Timeline timeIterator;

    /**
     * Basic constructor to be used by most controller classes
     * @param view Refers to the view that it will be responsible for
     */
    public RefreshController(AbstractView view) {
        super(view);
    }

    //================================================================================
    // Getter and Setter
    //================================================================================

    /**
     * An alternative constructor for a refresh controller that is dedicated to a singular Model object
     * @param view
     * @param model
     */
    public RefreshController(AbstractView view, AbstractModel model) {
        super(view, model);
    }

    public Timeline getTimeIterator(){
        return this.timeIterator;
    }

    // Methods mainly dealing with the timeline objects of the classes (setting, starting or stopping)
    public void setTimeIterator(Timeline timeIterator){
        this.timeIterator = timeIterator;
    }

    //================================================================================
    // Iterator functions
    //================================================================================

    public void startIterator(){
        timeIterator.play();
    }

    public void stopIterator(){
        timeIterator.stop();
    }
}
