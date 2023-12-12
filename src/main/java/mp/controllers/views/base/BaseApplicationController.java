package mp.controllers.views.base;

import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public abstract class BaseApplicationController implements Initializable {

    protected static final Logger LOGGER = Logger.getLogger(BaseApplicationController.class.getName());
    protected final Map<MFXTextField, Label> validatedFields = new HashMap<>();

    protected BaseApplicationController() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        runOnInitialize();
        validatedFieldsSetup();
        Platform.runLater(this::runLaterOnInitialize);
    }

    protected abstract void runOnInitialize();

    protected abstract void runLaterOnInitialize();

    protected abstract void validatedFieldsSetup();

}
