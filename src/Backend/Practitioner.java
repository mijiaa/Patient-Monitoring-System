package Backend;
import java.util.ArrayList;

import Model.PatientModel;
import Server.ServerModule;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/***
 * Practitioner class is a subclass of User that interacts with the system
 */
public class Practitioner extends User {

	private String practitionerId;
	private ObservableList<PatientModel> allPatients = FXCollections.observableArrayList();
	public Practitioner(String practitionerId){
		this.practitionerId = practitionerId;
	}

	/***
	 * This method override base method to extract list of patients that has encounter with current practitioner
	 * @return list of instance of PatientModel that represents a patient
	 * @throws JSONException
	 */
	@Override
	public ObservableList<PatientModel> retrievePatientList() throws JSONException {

		Boolean nextPage = true;
		String encountersUrl = "https://fhir.monash.edu/hapi-fhir-jpaserver/fhir/Encounter?participant.identifier=http://hl7.org/fhir/sid/us-npi%7C" + practitionerId + "&_include=Encounter.participant.individual&_include=Encounter.patient&_format=json";
		String nextUrl = encountersUrl;
		ServerModule server = new ServerModule();
		int patientCount = 0;
		ArrayList<String> patientId = new ArrayList<>();
		JSONObject json;

		while (nextPage && patientCount <= 10) {
			json = server.getJSONObjectWUrl(nextUrl);
			nextPage = false;
			JSONArray link = (JSONArray) json.get("link");
			for (int i = 0; i < link.length(); i++) {
				JSONObject linkTemp = (JSONObject) link.get(i);
				if (linkTemp.get("relation").toString().equals("next")) {
					nextPage = true;
					nextUrl = linkTemp.get("url").toString();
				}
			}
			try {
				JSONArray entry = json.getJSONArray("entry");

				for (int j = 0; j < entry.length(); j++) {
					JSONObject temp = (JSONObject) entry.get(j);
					JSONObject resource = (JSONObject) temp.get("resource");
					JSONObject subject = (JSONObject) resource.get("subject");
					String reference = subject.get("reference").toString();
					String id = reference.substring(8);
					if (!patientId.contains(id)) {
						allPatients.add(new PatientModel(id));
						patientId.add(id);
					}
				}
				patientCount++;
			} catch (JSONException e) {
				continue;
			}
		}

		return allPatients;

	}


}
