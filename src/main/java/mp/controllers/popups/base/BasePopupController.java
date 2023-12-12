package mp.controllers.popups.base;

import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import mp.utils.ExceptionMessage;
import mp.utils.ValidationUtil;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public abstract class BasePopupController<T> implements Initializable {

    protected static final Logger LOGGER = Logger.getLogger(BasePopupController.class.getName());
    protected final Map<MFXTextField, Label> validatedFields = new HashMap<>();
    protected final Stage popupStage;
    protected T model;
    protected T oldModel;

    protected BasePopupController(Stage popupStage, T model) {
        this.popupStage = popupStage;
        this.model = model;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        validatedFieldsSetup();
        Platform.runLater(this::runLaterOnInitialize);
    }

    protected void closePopup(KeyCode keyCode) {
        if (this.popupStage != null && (keyCode == null || keyCode == KeyCode.ESCAPE)) {
            this.popupStage.close();
        }
    }

    protected void saveChanges(boolean isNew) {
        if (ValidationUtil.isValidationSuccessful(validatedFields)) {
            try {
                saveOldDataIfNeeded(isNew);
                retrieveValuesFromTextFields();
                save(isNew);
                closePopup(null);
            } catch (Exception e) {
                String errorMessage = retrieveSaveErrorMessage(e, isNew);
                rollbackChangesIfNeeded(isNew);
                new ExceptionMessage.Builder(errorMessage, getValidationLabel())
                        .withInsets(new Insets(20,0,20,0))
                        .show();
                LOGGER.severe(String.format("%s - %s", LOGGER.getName(), errorMessage));
                e.printStackTrace();
            }
        }
    }

    private void saveOldDataIfNeeded(boolean isNew) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if (!isNew) {
            this.oldModel = (T) model.getClass().getDeclaredConstructor(new Class[] {model.getClass()}).newInstance(model); // oldModel = new ModelClass(model)
        }
    }

    protected abstract void validatedFieldsSetup();

    protected abstract void runLaterOnInitialize();

    protected abstract boolean isNew();

    protected abstract Label getValidationLabel();

    protected abstract void rollbackChangesIfNeeded(boolean isNew);

    protected abstract String retrieveSaveErrorMessage(Exception e, boolean isNew);

    protected abstract void retrieveValuesFromTextFields();

    protected abstract void save(boolean isNew) throws Exception;


}
