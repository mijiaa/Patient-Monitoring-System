package Backend;

import Model.PatientModel;
import Server.ServerModule;
import javafx.collections.ObservableList;
import org.json.JSONException;

/*** This class acts as a base class for any data types of Patient
 */
public abstract class PatientInfo {

	private ServerModule server = new ServerModule();

	public ServerModule getServer() {
		return server;
	}
	public void setServer(ServerModule server) {
		this.server = server;
	}

	//================================================================================
	// Standard Patient Info functions
	//================================================================================

	/*** This method extract specific data (cholesterol, highblood etc) of patient from FHIR server
	 */
	public void updateData(ObservableList<PatientModel> filteredPatients) throws JSONException{}

	/*** This method extract the basic information of patients from FHIR server
	 */
	public void retrievePatientInfo(ObservableList<PatientModel> filteredPatients) throws JSONException { }




}
