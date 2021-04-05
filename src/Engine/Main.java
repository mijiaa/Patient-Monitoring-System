package Engine;

import Controllers.LoginController;
import View.LoginView;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    /**
     * Compile and run this class to start the program
     */
    @Override
    public void start(Stage primaryStage) {

        // Instantiate the first view needed and it's controller
        LoginView login = new LoginView();
        LoginController loginController = new LoginController(login);

        // Provides the controller a reference to the stage and starts the view.
        loginController.setStage(primaryStage);
        login.start(primaryStage);
    }
}
