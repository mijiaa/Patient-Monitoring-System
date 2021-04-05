package Backend;
import Model.PatientModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/***
 * Monitor class represents real-life monitor to monitor selected patients
 */
public abstract class AbstractMonitor {

	private ObservableList<PatientModel> unmonitoredPatients = FXCollections.observableArrayList();
	private ObservableList<PatientModel> monitoredPatients = FXCollections.observableArrayList();

	/**
	 * Returns a list of all the patients to be displayed in main list of patients.
	 * @return A filtered list of patients, should contain all patients by default
	 * 			if no patient is being monitored (GUI purposes).
	 */
	public ObservableList<PatientModel> getUnmonitoredPatients(){
		return unmonitoredPatients;
	}

	/**
	 * Returns a list of monitored patients.
	 * @return A list of monitored patients (GUI purposes).
	 */
	public ObservableList<PatientModel> getMonitoredPatients(){
		return monitoredPatients;
	}

	/**
	 * Set the list of unmonitored patients.
	 * @param filteredPatients A dummy value to be used.
	 */
	public void setUnmonitoredPatients(ObservableList<PatientModel> filteredPatients) {
		this.unmonitoredPatients = filteredPatients;
	}

	//================================================================================
	// Standard Monitor function
	//================================================================================

	/**
	 * Adds a patient to be monitored
	 * @param patient PatientModel Object to be added to the monitoredPatients list
	 */
	public void addMonitoredPatient(PatientModel patient){
		this.monitoredPatients.add(patient);
	}

	/**
	 * Sets the patients to be monitored under current practitioner object.
	 * @param patients List of patients to be monitored.
	 */
	public void addMonitoredPatients(ObservableList<PatientModel> patients){
		this.monitoredPatients.addAll(patients);
	}

	/**
	 * remove patients from being monitored
	 * @param patients List of patients to be unmonitored
	 */
	public void unMonitorPatients(ObservableList patients){ }


	/**
	 * Removes a set of patients from the monitored list (useful for non-general implementation of monitor)
	 * @param patients List of patients to be removed from the monitored list.
	 */
	public void removeMonitoredPatients(ObservableList<PatientModel> patients) {
		this.monitoredPatients.removeAll(patients);
	}


}
