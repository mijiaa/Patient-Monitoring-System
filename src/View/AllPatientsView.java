package View;

import Model.PatientModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.ArrayList;

/**
 * This View class is responsible for displaying the UI of the page containing a table of all patients
 */
public class AllPatientsView extends AbstractView {


    private TableView allPatientsTable;
    private Button monitorButton = new Button("Monitor Selected Patient/s");

    /**
     * Base constructor
     */
    public AllPatientsView (){
        // Initialising the table view
        allPatientsTable = new TableView<PatientModel>();
    }

    public TableView getAllPatientsTable(){
        return  allPatientsTable;
    }

    //================================================================================
    // Main functions for view
    //================================================================================

    /**
     * Main function used to display UI components
     * @param stage Stage reference for the UI
     */
    public void start(Stage stage){
        setStage(stage);

        // Setting the 2 columns of the table view
        TableColumn<PatientModel,String> column1 = new TableColumn<>("First Name");
        column1.setCellValueFactory(new PropertyValueFactory<>("firstName"));


        TableColumn<PatientModel, String> column2 = new TableColumn<>("Surname");
        column2.setCellValueFactory(new PropertyValueFactory<>("surname"));

        // Setting the properties of the table
        allPatientsTable.getColumns().addAll(column1, column2);
        allPatientsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        allPatientsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        allPatientsTable.getSelectionModel().setCellSelectionEnabled(true);

        // Allow for selection highlight
        allPatientsTable.setRowFactory(new Callback<TableView<PatientModel>, TableRow<PatientModel>>() {
            @Override
            public TableRow<PatientModel> call(TableView<PatientModel> tableView2)
            {
                final TableRow<PatientModel> row = new TableRow<PatientModel>();

                row.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event)
                    {
                        final int index = row.getIndex();

                        if (index >= 0 && index < allPatientsTable.getItems().size())
                        {
                            if(allPatientsTable.getSelectionModel().isSelected(index))
                                allPatientsTable.getSelectionModel().clearSelection(index);
                            else
                                allPatientsTable.getSelectionModel().select(index);

                            event.consume();
                        }
                    }
                });
                return row;
            }
        });

        update();

        // Creating the label
        final Label label = new Label("Monitored Patients:");
        label.setFont(new Font("Arial", 18));

        // Initialising the VerticalBox of JavaFX
        VBox vb = new VBox();
        vb.getChildren().addAll(label, allPatientsTable, monitorButton);
        vb.setSpacing(10);
        vb.setPadding(new Insets(10, 10, 10, 10));

        // Creating a new scene with a 800 x 600 dimension
        Scene scene = new Scene(vb, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Sets the event handler for the button
     * @param handlers  Refers to the handler used for the button
     */
    public void addListener(ArrayList<EventHandler<ActionEvent>> handlers){
        monitorButton.setOnAction(handlers.get(0));
    }

    //================================================================================
    // Observer function for view
    //================================================================================

    /**
     * Updates the table containing all patients or unmonitored patients
     */
    @Override
    public void update() {
        // If there are already patients, the table would remove all the patients and set new patients to be displayed
        if (this.allPatientsTable.getItems().size() > 0){
            this.allPatientsTable.getItems().removeAll();
        }

        this.allPatientsTable.setItems(this.getData());
        this.allPatientsTable.refresh();
    }

}
