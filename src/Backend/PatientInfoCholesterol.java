package Backend;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import Model.PatientModel;
import Server.ServerModule;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.JSONArray;
import org.json.JSONObject;

/***
 * This class implements PatientInfo class to represent and manage cholesterol value of patients
 * */
public class PatientInfoCholesterol extends PatientInfo {

	private ObservableList<PatientModel> filteredPatients = FXCollections.observableArrayList();
	private ServerModule server = new ServerModule();

	public ObservableList<PatientModel> getFilteredPatients(){
		return this.filteredPatients;
	}

	/***
	 * This method extract cholesterol value of patient from FHIR
	 * @param patients : an instance of PatientModel that represent a list of patients
	 */
	@Override
	public void updateData(ObservableList<PatientModel> patients) {

		for (int i = 0; i< patients.size() ; i++) {
			PatientModel currentPatient = patients.get(i);
			Date dateTemp = null;
			JSONObject json;
			Integer index = null;
			ArrayList<String> cholesterolValues = new ArrayList<>();
			Boolean nextPage = true;
			try {
				String observationUrl = "https://fhir.monash.edu/hapi-fhir-jpaserver/fhir/Observation?subject=Patient/" + currentPatient.getPatientId() + "&code=2093-3&_format=json";
				String nextUrl = observationUrl;

				while (nextPage) {
					json = server.getJSONObjectWUrl(nextUrl);
					nextPage = false;
					JSONArray link = (JSONArray) json.get("link");
					for (int z = 0;z < link.length(); z++) {
						JSONObject linkTemp = (JSONObject) link.get(z);
						if (linkTemp.get("relation").toString().equals("next")) {
							nextPage = true;
							nextUrl = linkTemp.get("url").toString();
						}
					}

					try {
						JSONArray entry = json.getJSONArray("entry");
						for (int j = 0; j < entry.length(); j++) {

							JSONObject current = (JSONObject) entry.get(j);
							JSONObject resource = (JSONObject) current.get("resource");
							JSONObject valueQuantity = (JSONObject) resource.get("valueQuantity");
							String cholesterolVal = valueQuantity.get("value").toString();
							String date = (String) resource.get("effectiveDateTime");
							DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
							Date parseDate;
							try {
								parseDate = dateFormat.parse(date);
								if (dateTemp != null) {
									if (parseDate.compareTo(dateTemp) == 1) {
										dateTemp = parseDate;
										index = j;
									}
								} else {
									dateTemp = parseDate;
									index = j;
								}
							} catch (ParseException e1) {

							}
							cholesterolValues.add(cholesterolVal);

						}

						SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String temp = output.format(dateTemp);
						String choTemp = cholesterolValues.get(index);
						if (!filteredPatients.contains(currentPatient)) {
							filteredPatients.add(currentPatient);
							currentPatient.setChoDate(temp);
							currentPatient.setCholesterol(choTemp);
						}

					} catch (Exception e) { }
				}
			} catch (Exception e) { }
		}

		return;

	}
	

}
