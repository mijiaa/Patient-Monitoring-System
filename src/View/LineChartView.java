package View;

import Model.PatientModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;

public class LineChartView extends AbstractView {


    private VBox lineCharts = new VBox();
    private XYChart.Series series;
    private Button toggleDisplayButton = new Button("Textual Observation data");

    //================================================================================
    // Main functions for view
    //================================================================================

    @Override public void start(Stage stage) {

        // Creating the label
        final Label heading = new Label("Monitored Patients:");
        heading.setFont(new Font("Arial", 18));

        lineCharts.setSpacing(10);
        prepareAllLineCharts();

        VBox vb = new VBox();
        vb.getChildren().addAll(heading, lineCharts , toggleDisplayButton);

        vb.setSpacing(10);
        vb.setPadding(new Insets(10, 10, 10, 10));
        vb.setVgrow(lineCharts, Priority.ALWAYS);

        Scene scene  = new Scene(vb, 800, 600);

        stage.setScene(scene);
        stage.show();
    }

    /**
     * Sets the event handler for the button
     * @param handlers  Refers to the handlers used for the buttons
     */
    @Override
    public void addListener(ArrayList<EventHandler<ActionEvent>> handlers) {
        toggleDisplayButton.setOnAction(handlers.get(2));
    }

    //================================================================================
    // Functions for Line Chart initialising
    //================================================================================

    private void prepareAllLineCharts(){

        if (!lineCharts.getChildren().isEmpty()){
            lineCharts.getChildren().removeAll();
        }

        HBox lineChartBox = new HBox();
        int i = 0;
        boolean multipleOf3 = false;

        for (Object patient :getData()){
            i++;

            NumberAxis xAxis = new NumberAxis();
            xAxis.setLabel("Time");
            NumberAxis yAxis = new NumberAxis();
            LineChart lineChart = new LineChart<Number,Number>(xAxis,yAxis);
            generateLineChart(lineChart, (PatientModel) patient);

            lineChartBox.getChildren().add(lineChart);

            if (i == 3){
                lineCharts.getChildren().add(lineChartBox);
                i = 0;
                lineChartBox = new HBox();
                multipleOf3 = true;
            } else {
                multipleOf3 = false;
            }

        }
        if (!multipleOf3){
            lineCharts.getChildren().add(lineChartBox);
        }


    }

    private void generateLineChart(LineChart lineChart, PatientModel patient){
        lineChart.setTitle(patient.getFirstName() + " " + patient.getSurname());
        lineChart.setLegendVisible(false);

        series = new XYChart.Series();
        int i = 0;

        for (Object values: patient.getSbpValues()) {

            final XYChart.Data<Number, Number> data = new XYChart.Data(i,(Double) values );
            series.getData().add(data);
            i++;
        }
        lineChart.getData().addAll(series);
    }

    //================================================================================
    // Observer function
    //================================================================================

    @Override
    public void update() {
        prepareAllLineCharts();
    }





}
