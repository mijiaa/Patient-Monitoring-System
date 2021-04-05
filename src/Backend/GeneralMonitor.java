package Backend;

import Model.PatientModel;
import javafx.collections.ObservableList;

/**
 * Represents the monitor for general unmonitored and monitored patients
 */
public class GeneralMonitor extends AbstractMonitor {

    /**
     * Removes patient from being monitored under current practitioner.
     * @param patients	Patients to be unmonitored.
     */
    @Override
    public void unMonitorPatients(ObservableList patients){
        getUnmonitoredPatients().addAll(patients);
        getMonitoredPatients().removeAll(patients);
    }

    /**
     * Transfers the patients from it's unmonitored list to it's monitored list
     * @param patients List of patients to be monitored.
     */
    @Override
    public void addMonitoredPatients(ObservableList<PatientModel> patients) {
        super.addMonitoredPatients(patients);
        getUnmonitoredPatients().removeAll(patients);
    }

}
