package mp;

import mp.constants.Strings;
import mp.constants.Views;
import mp.controllers.views.ApplicationStartController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import mp.utils.ViewLoader;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Logger;

public class Application extends javafx.application.Application {

    private static final Logger LOGGER = Logger.getLogger(Application.class.getName());

    @Override
    public void start(Stage primaryStage) {
        try {
            ViewLoader.initPrimaryStageStaticVariable(primaryStage);
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource(Views.APPLICATION_START_VIEW.getPath()));
            loader.setController(new ApplicationStartController());
            VBox root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setTitle(Strings.Labels.APPLICATION_TITLE);
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(1024);
            primaryStage.setMinHeight(768);
            primaryStage.setMaximized(true);
            primaryStage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/images/icon.png"))));
            primaryStage.show();
        } catch (IOException e) {
            String errorMessage = String.format(Strings.Errors.LOADING_VIEW, Views.APPLICATION_START_VIEW.getName(), e.getMessage());
            LOGGER.severe(String.format("%s - %s", LOGGER.getName(), errorMessage));
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }


}