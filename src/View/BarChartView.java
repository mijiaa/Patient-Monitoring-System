package View;

import Calculation.Calculation;
import Model.PatientModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.ArrayList;

public class BarChartView extends  AbstractView {


    private BarChart<String,Number> barChart;
    private XYChart.Series series;
    private Button changeDisplayButton = new Button("Go back");

    //================================================================================
    // Main functions for view
    //================================================================================

    @Override public void start(Stage stage) {

        // Creating the label
        final Label heading = new Label("Monitored Patients:");
        heading.setFont(new Font("Arial", 18));

        setupBarChart();

        LevelLegend legend = new LevelLegend();
        legend.setAlignment(Pos.CENTER);

        VBox vb = new VBox();
        vb.getChildren().addAll(heading, barChart, legend, changeDisplayButton);
        vb.setSpacing(10);
        vb.setPadding(new Insets(10, 10, 10, 10));
        vb.setVgrow(barChart, Priority.ALWAYS);
        vb.getStylesheets().add(getClass().getResource("standard-color.css").toExternalForm());

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
        changeDisplayButton.setOnAction(handlers.get(8));
    }

    //================================================================================
    // Functions for BarChart initialising
    //================================================================================

    /**
     * Creates a BarChart representing the cholesterol values of all the patient.
     */
    private void setupBarChart(){

        // Basic BarChart Initialisation (Axis, Title, Legends)
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        xAxis.setLabel("Patients");
        barChart = new BarChart<String,Number>(xAxis,yAxis);

        barChart.setTitle("Total Cholesterol mg/dl");
        barChart.setLegendVisible(false);

        // Prepares BarChart to handle data and display it
        series = new XYChart.Series();

        for (Object patient: this.getData()) {
            final XYChart.Data<String, Number> data = new XYChart.Data(((PatientModel) patient).getFirstName(), Double.parseDouble(((PatientModel) patient).getCholesterol()));
            data.nodeProperty().addListener(new ChangeListener<Node>() {
                @Override
                public void changed(ObservableValue<? extends Node> ov, Node oldNode, final Node node) {
                    if (node != null) {
                        setNodeStyle(data);
                        displayLabelForData(data);
                    }
                }
            });
            series.getData().add(data);
        }
        barChart.getData().addAll(series);
    }

    /**
     * Displays a text above each bar chart indicating the value
     * @param data Represents the numerical data in the form of a chart data
     */
    private void displayLabelForData(XYChart.Data<String, Number> data) {
        final Node node = data.getNode();
        final Text dataText = new Text(data.getYValue() + "");
        // Sets the label text for the bar
        node.parentProperty().addListener(new ChangeListener<Parent>() {
            @Override public void changed(ObservableValue<? extends Parent> ov, Parent oldParent, Parent parent) {
                Group parentGroup = (Group) parent;
                parentGroup.getChildren().add(dataText);
            }
        });
        // Resizes label bound based on bar bounds
        node.boundsInParentProperty().addListener(new ChangeListener<Bounds>() {
            @Override public void changed(ObservableValue<? extends Bounds> ov, Bounds oldBounds, Bounds bounds) {
                dataText.setLayoutX(
                        Math.round(
                                bounds.getMinX() + bounds.getWidth() / 2 - dataText.prefWidth(-1) / 2
                        )
                );
                dataText.setLayoutY(
                        Math.round(
                                bounds.getMinY() - dataText.prefHeight(-1) * 0.5
                        )
                );
            }
        });
    }

    /**
     * Sets the styling of the bar based on the value of the cholesterol
     * @param data
     */
    private void setNodeStyle(XYChart.Data<String, Number> data) {
        Node node = data.getNode();
        Double average = new Calculation().calculateCholesterolAverage(this.getData());
        if (data.getYValue().doubleValue() > average) {
            node.setStyle("-fx-bar-fill: -fx-exceeded;");
        }  else {
            node.setStyle("-fx-bar-fill: -fx-normal;");
        }
    }

    /**
     * Creates a legend based on the bar chart colors (red represents that cholesterol value exceeded original)
     */
    private class LevelLegend extends GridPane {
        LevelLegend() {
            setHgap(10);
            setVgap(10);
            addRow(0, createSymbol("-fx-exceeded"), new Label("Exceeded"));
            addRow(1, createSymbol("-fx-normal"), new Label("Normal"));

            getStyleClass().add("level-legend");
        }
    }

    /**
     * Create a custom symbol for a custom chart legend with the given fillStyle style string.
     * */
    private Node createSymbol(String fillStyle) {
        Shape symbol = new Ellipse(10, 5, 10, 5);
        symbol.setStyle("-fx-fill: " + fillStyle);
        symbol.setStroke(Color.BLACK);
        symbol.setStrokeWidth(2);

        return symbol;
    }

    //================================================================================
    // Observer function
    //================================================================================

    /**
     * Creates a new bar chart with updated data
     */
    @Override
    public void update() {
        setupBarChart();
    }


}
