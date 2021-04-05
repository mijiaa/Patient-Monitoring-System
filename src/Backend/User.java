package Backend;

import Model.PatientModel;
import javafx.collections.ObservableList;
import org.json.JSONException;

/***
 * This class acts as a base class for any user that interacts with the system
 */
public abstract class User {

	/***
	 * User that interacts with system will be able to monitor patients. This base method will be implemented in
	 * subclasses to extract list of patients that has encounter with current users
	 * @return list of instance of PatientModel that represents a patient
	 * @throws JSONException
	 */
	public ObservableList<PatientModel> retrievePatientList() throws JSONException {
		return null;
	}

}
