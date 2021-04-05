package Calculation;

import Model.AbstractModel;
import Model.PatientModel;
import javafx.collections.ObservableList;


/**
 * Main class used to house calculation methods
 */
public class Calculation {


    /**
     * Calculates the average cholesterol level of all the patients in the observable list
     * @param patientModels An observable list of all patient object present in the database
     * @return A double representing the average cholesterol level of all the patients
     */
    public Double calculateCholesterolAverage (ObservableList<AbstractModel> patientModels){
        double totalCholesterol = 0.0;
        int count = 0;

        for (AbstractModel patient:patientModels){
            try {
                double value = Double.parseDouble(((PatientModel)patient).getDisplayCholesterol());
                totalCholesterol += value;
                count ++;
            } catch (Exception e){
                // Ignore
            }
        }
        return (totalCholesterol / count);
    }
}
