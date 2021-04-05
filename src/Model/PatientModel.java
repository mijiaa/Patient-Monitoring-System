package Model;


import javafx.collections.ObservableList;

import java.util.Date;

/**
 * Patient class responsible for holding all patient information
 */
public class PatientModel extends AbstractModel{

    private String patientId;
    private String firstName;
    private String surname;
    private String cholesterol;
    private String choDate;
    private String bpDate;
    private String birthDate;
    private String gender;
    private String address;
    private String city;
    private String state;
    private String country;
    private String sbpValue;
    private String dbpValue;
    private String displayCholesterol;
    private String displaySbp;
    private String displayDbp;
    private String displayChoDate;
    private String displayBpDate;
    private ObservableList<Double> sbpValues;
    private ObservableList<Date> sbpDate;

    /**
     * The most common way of instantiating a patient is through it's ID, where it'll be populated with it's information in the PatientInfoCholesterol Class
     * @param patientId Refers to the String containing the patient ID, which will be referred to during retrieval of info
     */
    public PatientModel(String patientId){
        this.patientId = patientId;
    }


    // -> Getters (Mostly used for JavaFX tables, charts and so on)
    public String getPatientId(){
        return patientId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSbpValue(){return sbpValue;}

    public String getDbpValue(){return dbpValue;}

    public String getSurname() {
        return surname;
    }

    public String getCholesterol() { return cholesterol; }

    public String getChoDate() { return choDate; }

    public String getBpDate() {  return bpDate; }

    public String getBirthDate() {
        return birthDate;
    }

    public String getGender() {
        return gender;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    public ObservableList<Date> getSbpDate(){
        return sbpDate;
    }

    public ObservableList<Double> getSbpValues(){
        return sbpValues;
    }

    public String getDisplayCholesterol(){
        return displayCholesterol;
    }

    public String getDisplaySbp(){
        return displaySbp;
    }

    public String getDisplayDbp(){
        return displayDbp;
    }

    public String getDisplayChoDate(){
        return displayChoDate;
    }

    public String getDisplayBpDate() {
        return displayBpDate;
    }

    // -> Setters
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setSbpDate(ObservableList<Date> sbpDate) {
        this.sbpDate = sbpDate;
    }

    //  -> Modified Setters: Responsible for updating the observers and display counterpart.
    public void setCholesterol(String cholesterol) {
        this.cholesterol = cholesterol;
        for (Observer observer : getObservers()) {
            observer.update();
        }

        if (this.displayCholesterol == null){
            this.displayCholesterol = cholesterol;
        } else if (!this.displayCholesterol.equals("-")){
            this.displayCholesterol = cholesterol;
        }
    }

    public void setSbpValue(String sbpValue) {
        this.sbpValue = sbpValue;
        for (Observer observer : getObservers()) {
            observer.update();
        }

        if (this.displaySbp == null){
            this.displaySbp = sbpValue;
        } else if (!this.displaySbp.equals("-")){
            this.displaySbp = sbpValue;
        }

    }

    public void setDbpValue(String dbpValue) {
        this.dbpValue = dbpValue;
        for (Observer observer : getObservers()) {
            observer.update();
        }
        if (this.displayDbp == null){
            this.displayDbp = dbpValue;
        } else if (!this.displayDbp.equals("-")){
            this.displayDbp = dbpValue;
        }
    }

    public void setChoDate(String choDate) {
        this.choDate = choDate;

        if (this.displayChoDate == null){
            this.displayChoDate = choDate;
        } else if (!this.displayChoDate.equals("-")){
            this.displayChoDate = choDate;
        }
    }

    public void setBpDate(String bpDate){
        this.bpDate = bpDate;
        if (this.displayBpDate == null){
            this.displayBpDate = bpDate;
        } else if (!this.displayBpDate.equals("-")){
            this.displayBpDate = bpDate;
        }
    }

    public void setSbpValues(ObservableList<Double> sbpValues) {
        this.sbpValues = sbpValues;
        for (Observer observer : getObservers()) {
            observer.update();
        }
    }

    //================================================================================
    // Toggle Functions for display values  (toggles between numerical or '-')
    //================================================================================

    public void toggleDisplayCholesterol(){
        if (this.displayCholesterol.equals("-")){
            this.displayCholesterol = this.cholesterol;
        } else {
            this.displayCholesterol = "-";
        }

    }

    public void toggleDisplaySbp(){
        if (this.displaySbp.equals("-")){
            this.displaySbp = this.sbpValue;
        } else {
            this.displaySbp = "-";
        }

    }

    public void toggleDisplayDbp(){
        if (this.displayDbp.equals("-")){
            this.displayDbp = this.dbpValue;
        } else {
            this.displayDbp = "-";
        }

    }

    public void toggleDisplayChoDate(){
        if (this.displayChoDate.equals("-")){
            this.displayChoDate = this.choDate;
        } else {
            this.displayChoDate = "-";
        }
    }

    public void toggleDisplayBpDate(){
        if (this.displayBpDate.equals("-")){
            this.displayBpDate = this.bpDate;
        } else {
            this.displayBpDate = "-";
        }
    }

}
