package mp.controllers.views;

import mp.constants.Views;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import mp.utils.ViewLoader;
import java.net.URL;
import java.util.ResourceBundle;

public class ApplicationStartController implements Initializable {

    @FXML
    private VBox mainFrame;

    public ApplicationStartController() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ViewLoader.initMainFrameStaticVariable(mainFrame);
        ViewLoader.loadView(
                Views.LOGIN_VIEW,
                LoginController.class,
                null,
                null,
                mainFrame);
    }


}
