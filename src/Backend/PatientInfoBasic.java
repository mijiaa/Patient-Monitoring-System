package Backend;

import Model.PatientModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/***
 * This class extents PatientInfo Class to represent the basic information of patients
 */
public class PatientInfoBasic extends PatientInfo {

    private ObservableList<PatientModel> filteredPatients = FXCollections.observableArrayList();
    public ObservableList<PatientModel> getFilteredPatients(){
        return this.filteredPatients;
    }

    /**
     * this method override base function to get basic information of patients that have cholesterol and blood pressure values
     * @param patients
     * @throws JSONException
     */
    @Override
    public void retrievePatientInfo(ObservableList<PatientModel> patients) throws JSONException {

        PatientInfoBP patientBP = new PatientInfoBP();
        PatientInfoCholesterol patientInfoCholesterol = new PatientInfoCholesterol();
        patientBP.updateData(patients);
        patientInfoCholesterol.updateData(patientBP.getFilteredPatients());

        filteredPatients = patientInfoCholesterol.getFilteredPatients();
        JSONObject json;

        for (int i = 0; i < filteredPatients.size(); i++) {
            PatientModel currentPatient = filteredPatients.get(i);
            json = getServer().getJSONObjectWQuery("Patient/" + currentPatient.getPatientId() + "?");

            JSONArray nameArray = json.getJSONArray("name");
            JSONObject nameTemp = (JSONObject) nameArray.get(0);
            JSONArray given = (JSONArray) nameTemp.get("given");
            String firstName = given.getString(0);
            firstName = firstName.substring(0, firstName.length() - 3);
            String surname = nameTemp.getString("family");
            surname = surname.substring(0, surname.length() - 3);


            String gender = json.get("gender").toString();
            String birthDate = json.get("birthDate").toString();

            JSONArray addArray = json.getJSONArray("address");
            JSONObject addTemp = (JSONObject) addArray.get(0);
            JSONArray lineArray = (JSONArray) addTemp.get("line");
            String line = (String) lineArray.get(0);
            String city = (String) addTemp.get("city");
            String state = (String) addTemp.get("state");
            String country = (String) addTemp.get("country");

            //creating a PatientModel of the patient
            currentPatient.setFirstName(firstName);
            currentPatient.setSurname(surname);
            currentPatient.setGender(gender);
            currentPatient.setBirthDate(birthDate);
            currentPatient.setAddress(line);
            currentPatient.setCity(city);
            currentPatient.setState(state);
            currentPatient.setCountry(country);
        }
        return;
    }



}

