package View;

import Model.PatientModel;
import Calculation.Calculation;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.ArrayList;

/**
 * This View class is responsible for displaying the UI of the page containing the monitored patient
 */
public class MonitoredView extends AbstractView {

    private Double average;
    private Double sbpLimit = 140.0; // initial limit
    private Double dbpLimit = 80.0;

    // -> UI Components
    private TextField intervalTextField;
    private TextField sbpLimitTextField;
    private TextField dbpLimitTextField;

    private Label loadingLabel;
    private Label indicatorLabel;

    private TableView monitoredPatientsTable = new TableView<PatientModel>();;

    // -> Buttons
    private Button infoButton = new Button("Additional Patient Info");;
    private Button bpButton = new Button("Systolic Blood Pressure Log");
    private Button addButton = new Button("Add Patient(s)");
    private Button removeButton = new Button("Remove Patient(s)");
    private Button changeIntervalButton = new Button("Set refresh frequency (seconds)");
    private Button changeSbpLimitButton = new Button("Set new Systolic BP limit");
    private Button changeDbpLimitButton = new Button("Set new Diastolic BP limit");
    private Button changeDisplayButton = new Button("Total Cholesterol Values Bar Chart");
    private Button toggleCholesterolButton = new Button("Toggle Cholesterol Observation");
    private Button toggleBpValuesButton = new Button("Toggle Blood Pressure Observation");


    //================================================================================
    // Main functions for view
    //================================================================================

    /**
     * Function is run when the display scene is first started, in this function, the basic UI components are
     * initialised.
     * @param stage Stage reference for the UI
     */
    @Override
    public void start(Stage stage){
        setStage(stage);
        /**
         * Function is run when the display scene is first started, in this function, the basic components are then
         *  initialised which are a label, a table view with 4 columns, a vertical box and the scene used to display.
         */
        // Creating the label
        final Label heading = new Label("Monitored Patients:");
        heading.setFont(new Font("Arial", 18));
        
        // Setting the columns of the table
        TableColumn<PatientModel,String> column1 = new TableColumn<>("First Name");
        column1.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        column1.setEditable(false);

        TableColumn<PatientModel, String> column2 = new TableColumn<>("Surname" +
                "");
        column2.setCellValueFactory(new PropertyValueFactory<>("surname"));
        column2.setEditable(false);

        TableColumn<PatientModel, String> column3 = new TableColumn<>("Total Cholesterol (mg/dL)");
        column3.setCellValueFactory(new PropertyValueFactory<>("displayCholesterol"));
        column3.setEditable(false);

        TableColumn<PatientModel, String> column4 = new TableColumn<>("Cholesterol Last Updated");
        column4.setCellValueFactory(new PropertyValueFactory<>("displayChoDate"));
        column4.setEditable(false);

        TableColumn<PatientModel, String> column5 = new TableColumn<>("Systolic Blood Pressure (mm/Hg)");
        column5.setCellValueFactory(new PropertyValueFactory<>("displaySbp"));
        column5.setEditable(false);

        TableColumn<PatientModel, String> column6 = new TableColumn<>("Diastolic Blood Pressure (mm/Hg)");
        column6.setCellValueFactory(new PropertyValueFactory<>("displayDbp"));
        column6.setEditable(false);

        TableColumn<PatientModel, String> column7 = new TableColumn<>("Blood Pressure Last Updated");
        column7.setCellValueFactory(new PropertyValueFactory<>("displayBpDate"));
        column7.setEditable(false);



        /**
         * Highlight text of patient cholesterol values greater than the average of all observed patients
         */
        column3.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item);
                    // Style cell text where patients with cholesterol value that exceeds the average cholesterol of all observed patients.
                    try {
                        double i = Double.parseDouble(item);
                        average = new Calculation().calculateCholesterolAverage(monitoredPatientsTable.getItems());
                        if (i >= average) {
                            TableRow currentRow = getTableRow();
                            if (currentRow != null) {
                                setTextFill(Color.RED);
                            }
                        }
                    } catch (Exception e){
                        // Ignore if failed
                    }


                } 
        }});

        /**
         * Highlight text of patient sbp values greater than sbp limit
         */
        column5.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {

                    setText(item);
                   try {
                       // Style cell text where patients with sbp value that exceeds the sbp limit
                       double i = Double.parseDouble(item);
                       if (i >= sbpLimit) {
                           TableRow currentRow = getTableRow();
                           if (currentRow != null) {
                               setTextFill(Color.GREEN);
                           }
                       }
                   } catch (Exception e) {
                       // Ignore if failed
                    }
                }
            }
        });

        /**
         * Highlight text of patient dbp values greater than dbp limit
         */
        column6.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setText(null);
                    TableRow currentRow = getTableRow();
                } else {
                    setText(item);
                    try {
                        // Style cell text where patients with dbp value that exceeds the dbp limit
                        double i = Double.parseDouble(item);
                        if (i >= dbpLimit) {
                            TableRow currentRow = getTableRow();
                            if (currentRow != null) {
                                setTextFill(Color.BLUE);
                            }
                        }
                    } catch (Exception e){
                        // Ignore if failed
                    }

                }
            }
        });

        // Inserting the data and columns into the table
        monitoredPatientsTable.getColumns().addAll(column1, column2, column3, column4,column5,column6,column7);
        monitoredPatientsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        monitoredPatientsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        monitoredPatientsTable.getSelectionModel().setCellSelectionEnabled(true);

        // Allows for multiple selection of rows
        monitoredPatientsTable.setRowFactory(new Callback<TableView<PatientModel>, TableRow<PatientModel>>() {
            @Override
            public TableRow<PatientModel> call(TableView<PatientModel> tableView2)
            {
                final TableRow<PatientModel> row = new TableRow<PatientModel>();

                row.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        final int index = row.getIndex();

                        if (index >= 0 && index < monitoredPatientsTable.getItems().size()) {
                            if (monitoredPatientsTable.getSelectionModel().isSelected(index)){
                                monitoredPatientsTable.getSelectionModel().clearSelection(index);
                            } else {
                                monitoredPatientsTable.getSelectionModel().select(index);
                            }
                            event.consume();
                        }
                    }
                });
                return row;
            }
        });
        update();

        // Creating a HBox to contain the info, bp observation, add patient and remove patient buttons
        HBox normalBox = new HBox();
        normalBox.getChildren().addAll(infoButton, bpButton, addButton, removeButton);
        normalBox.setSpacing(10);

        this.intervalTextField = new TextField();

        // Label to be used to provide visual feedback that the iterator has been updated
        loadingLabel = new Label("");
        this.indicatorLabel = new Label();

        // Creating a HBox to contain text field for interval change, it's button and a label as well as an indicator label for loading/ updating of values
        HBox iterBox = new HBox();
        iterBox.getChildren().addAll(intervalTextField, changeIntervalButton, loadingLabel, this.indicatorLabel);
        iterBox.setSpacing(10);

        // Creating a HBox to contain textfields for sbp limit and dbp limit and their respective buttons
        HBox bpBox = new HBox();
        sbpLimitTextField = new TextField();
        dbpLimitTextField = new TextField();
        bpBox.getChildren().addAll(sbpLimitTextField, changeSbpLimitButton, dbpLimitTextField, changeDbpLimitButton);

        // Creating a HBox for display buttons
        HBox displayBox = new HBox();
        displayBox.getChildren().addAll(changeDisplayButton, toggleCholesterolButton, toggleBpValuesButton);
        displayBox.setSpacing(10);

        // Initialising the main VBox for the scene
        VBox mainVBox = new VBox();
        mainVBox.getChildren().addAll(heading, monitoredPatientsTable, normalBox, iterBox, bpBox, displayBox);
        mainVBox.setSpacing(10);
        mainVBox.setPadding(new Insets(10, 10, 10, 10));

        // Creating a new scene with a 800 x 600 dimension
        Scene scene = new Scene(mainVBox, 1000, 800);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Sets the event handler for the button
     * @param handlers  Refers to the handlers used for the buttons
     */
    @Override
    public void addListener(ArrayList<EventHandler<ActionEvent>> handlers) {
        infoButton.setOnAction(handlers.get(0));
        bpButton.setOnAction(handlers.get(1));
        addButton.setOnAction(handlers.get(2));
        removeButton.setOnAction(handlers.get(3));
        changeIntervalButton.setOnAction(handlers.get(4));
        changeSbpLimitButton.setOnAction(handlers.get(5));
        changeDbpLimitButton.setOnAction(handlers.get(6));
        changeDisplayButton.setOnAction(handlers.get(7));
        toggleCholesterolButton.setOnAction(handlers.get(9));
        toggleBpValuesButton.setOnAction(handlers.get(10));
    }

    //================================================================================
    // Getter for UI Component
    //================================================================================

    public Label getIndicatorLabel() {
        return indicatorLabel;
    }

    public TextField getIntervalTextField() {
        return intervalTextField;
    }

    public TextField getSbpLimitTextField(){
        return this.sbpLimitTextField;
    }

    public TextField getDbpLimitTextField(){
        return this.dbpLimitTextField;
    }

    public TableView getMonitoredPatientsTable() {
        return  monitoredPatientsTable;
    }

    //================================================================================
    // View updater (used by controller)
    //================================================================================

    public void updateSbpLimit(double limit){
        this.sbpLimit = limit;
        this.monitoredPatientsTable.refresh();
    }

    public void updateDbpLimit(double limit){
        this.dbpLimit = limit;
        this.monitoredPatientsTable.refresh();
    }

    //================================================================================
    // Loading functions
    //================================================================================

    // Shows loading visualisation during refreshing (usually too fast to be seen)
    public void startLoading(){
        loadingLabel.setText("Loading...");
    }

    // Ends loading visualisation
    public void endLoading(){
        loadingLabel.setText("");
    }

    //================================================================================
    // Observer function
    //================================================================================

    @Override
    public void update() {
        this.monitoredPatientsTable.setItems(this.getData());
        this.monitoredPatientsTable.refresh();
    }
}
