package Model;

/**
 * Standard Observable interface to be used in the MVC design + Observer
 */
public interface Observable {
    public void addObserver(Observer observer);
    public void removeObserver(Observer observer);
}
