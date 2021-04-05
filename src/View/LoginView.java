package View;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * This View class is responsible for displaying the UI of the login page
 */
public class LoginView extends AbstractView {


    private TextField practitionerIdTextField;
    private  Button loginButton = new Button("Log In");;

    //================================================================================
    // Main functions for view
    //================================================================================

    /**
     * Function is run when the display scene is first started, in this function, the basic UI components are
     * initialised.
     * @param stage Stage reference for the UI
     */
    @Override
    public void start(Stage stage) {
        setStage(stage);

        // Initialising the Vertical Box
        VBox vb = new VBox();
        vb.setPadding(new Insets(10));
        vb.setSpacing(10);
        vb.setAlignment(Pos.CENTER);

        // Creating a label and text field
        Label loginLabel = new Label("Welcome practitioner, please enter your ID...");
        practitionerIdTextField = new TextField();
        practitionerIdTextField.setMaxWidth(150);

        Label loadingLabel = new Label("Logging in will take some time, please be patient...");

        // Inserting the UI components into the VerticalBox
        vb.getChildren().addAll(loginLabel, practitionerIdTextField, loginButton, loadingLabel);

        // Creating a new Scene to be displayed
        Scene scene = new Scene(vb, 800,600);
        stage.setTitle("Log In");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Sets the event handler for the button
     * @param handlers  Refers to the handler used for the button
     */
    @Override
    public void addListener(ArrayList<EventHandler<ActionEvent>> handlers) {
        loginButton.setOnAction(handlers.get(0));
    }

    //================================================================================
    // Getter for UI Component
    //================================================================================

    public TextField getPractitionerIdTextField() {
        return practitionerIdTextField;
    }


}
