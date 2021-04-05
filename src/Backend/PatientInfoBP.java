package Backend;

import Model.PatientModel;
import Server.ServerModule;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/***
 * This class implements PatientInfo class to represent and manage blood pressure values of patients
 * */

public class PatientInfoBP extends PatientInfo {

    private ObservableList<PatientModel> filteredPatients = FXCollections.observableArrayList();
    private ServerModule server = new ServerModule();

    public ObservableList<PatientModel> getFilteredPatients() {
        return this.filteredPatients;
    }

    /***
     * This method extract blood pressure values of patient from FHIR
     * @param patients : an instance of PatientModel that represent a list of patients
     */
    @Override
    public void updateData(ObservableList<PatientModel> patients) {

        for (int i = 0; i < patients.size(); i++) {
            PatientModel currentPatient = patients.get(i);
            Date dateTemp = null;
            JSONObject json;
            Integer index = null;
            ArrayList<String> DBPValues = new ArrayList<>();
            ArrayList<String> SBPValues = new ArrayList<>();
            Boolean nextPage = true;
            try {
                String observationUrl = "https://fhir.monash.edu/hapi-fhir-jpaserver/fhir/Observation?subject=Patient/" + currentPatient.getPatientId() + "&code=55284-4&_format=json";
                String nextUrl = observationUrl;

                while (nextPage) {
                    json = server.getJSONObjectWUrl(nextUrl);
                    nextPage = false;
                    JSONArray link = (JSONArray) json.get("link");
                    for (int z = 0; z < link.length(); z++) {
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
                            JSONArray component = resource.getJSONArray("component");
                            JSONObject dbpObject = (JSONObject) component.get(0);
                            JSONObject dbpValueQuantity = (JSONObject) dbpObject.get("valueQuantity");
                            String dbpValue = dbpValueQuantity.get("value").toString();
                            JSONObject sbpObject = (JSONObject) component.get(1);
                            JSONObject sbpValueQuantity = (JSONObject) sbpObject.get("valueQuantity");
                            String sbpValue = sbpValueQuantity.get("value").toString();
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
                            DBPValues.add(dbpValue);
                            SBPValues.add(sbpValue);


                        }

                        SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String temp = output.format(dateTemp);
                        String dbpTemp = DBPValues.get(index);
                        String sbpTemp = SBPValues.get(index);

                        if (!filteredPatients.contains(currentPatient)) {
                            filteredPatients.add(currentPatient);
                            currentPatient.setBpDate(temp);
                            currentPatient.setSbpValue(sbpTemp);
                            currentPatient.setDbpValue(dbpTemp);
                        }
                    } catch (Exception e) {
                    }
                }
            } catch (Exception e) {
            }
        }


    }

    /***
     * this method get the latest five systolic blood pressure of a specific patient, sorted according to date
     * @param patientID : an instance of PatientModel that represent a list of patients
     */
    public void getBPObservations(PatientModel patientID) {

        ArrayList<Date> dateArray = new ArrayList<>();
        JSONObject json;
        ArrayList<Double> SBPValues = new ArrayList<>();
        Boolean nextPage = true;
        try {
            String observationUrl = "https://fhir.monash.edu/hapi-fhir-jpaserver/fhir/Observation?subject=Patient/" + patientID.getPatientId() + "&code=55284-4&_format=json";
            String nextUrl = observationUrl;

            while (nextPage) {
                json = server.getJSONObjectWUrl(nextUrl);
                nextPage = false;
                JSONArray link = (JSONArray) json.get("link");
                for (int z = 0; z < link.length(); z++) {
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
                        JSONArray component = resource.getJSONArray("component");
                        JSONObject dbpObject = (JSONObject) component.get(0);
                        JSONObject sbpObject = (JSONObject) component.get(1);
                        JSONObject sbpValueQuantity = (JSONObject) sbpObject.get("valueQuantity");
                        String sbpValue = sbpValueQuantity.get("value").toString();
                        String date = (String) resource.get("effectiveDateTime");

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                        Date parseDate = dateFormat.parse(date);
                        double sbp = Double.parseDouble(sbpValue);
                        dateArray.add(parseDate);
                        SBPValues.add(sbp);
                    }

                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
        }

        //sort sbp values by date
        this.sortSBPValues(SBPValues, dateArray, patientID);
    }

    /***
     * This method sort the five latest results of patients by dates of observation
     * @param SBPList : array of values of patients systolic blood pressure
     * @param dateList : list of dates, use to sort
     * @param patient : represent actual patient
     */
    private void sortSBPValues(ArrayList<Double> SBPList, ArrayList<Date> dateList, PatientModel patient) {
        int observationLimit = 4;
        List<Date> date = new ArrayList<>(dateList);
        List<Double> sbpValues = new ArrayList<>(SBPList);
        ObservableList<Double> top5 = FXCollections.observableArrayList();
        Map<Date, Double> map = new HashMap<>();
        for (int i = 0, n = date.size(); i < n; i++) {
            map.put(date.get(i), sbpValues.get(i));
        }

        Collections.sort(date, Collections.reverseOrder());
        sbpValues.clear();
        for (Date d : date) {
            sbpValues.add(map.get(d));
        }
        ObservableList<Date> dateBp = FXCollections.observableArrayList();
        dateBp.addAll(date);
        for (int i = 0; i < sbpValues.size(); i++) {
            top5.add(sbpValues.get(i));
            if (i == observationLimit) {
                break;
            }
        }
        FXCollections.reverse(top5);
        patient.setSbpValues(top5);
        patient.setSbpDate(dateBp);
    }


}





