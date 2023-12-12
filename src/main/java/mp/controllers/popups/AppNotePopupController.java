package mp.controllers.popups;

import mp.constants.Strings;
import mp.controllers.popups.base.BasePopupController;
import mp.dao.DaoProvider;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import mp.models.AppNote;
import mp.utils.EncryptUtil;
import mp.utils.ValidationUtil;
import java.sql.SQLException;

public class AppNotePopupController extends BasePopupController<AppNote> {

    @FXML
    private Label lbl_validationView;
    @FXML
    private Label txt_title;
    @FXML
    private MFXTextField txt_description;
    @FXML
    private Label lbl_validationDescription;
    @FXML
    private MFXPasswordField txt_value;
    @FXML
    private Label lbl_validationValue;

    public AppNotePopupController(Stage popupStage, Object model) {
        super(popupStage, (AppNote)model);
    }

    @Override
    protected void validatedFieldsSetup() {
        ValidationUtil.addRequiredConstraint(txt_description, lbl_validationDescription);
        ValidationUtil.addRequiredConstraint(txt_value, lbl_validationValue);

        ValidationUtil.addTextLimit(txt_description, 255);
        ValidationUtil.addTextLimit(txt_value, 51);

        validatedFields.put(txt_description, lbl_validationDescription);
        validatedFields.put(txt_value, lbl_validationValue);
    }

    @Override
    protected void runLaterOnInitialize() {
        txt_description.requestFocus();
        if (isNew()) {
            txt_title.setText(Strings.Labels.NEW_APP_NOTE);
            txt_title.setVisible(true);
        } else {
            txt_title.setText(Strings.Labels.EDIT_APP_NOTE);
            txt_title.setVisible(true);
            txt_description.textProperty().set(model.getDescription());
            txt_value.textProperty().set(EncryptUtil.decrypt(model.getValue()));
        }
    }

    @Override
    protected boolean isNew() {
        return model.getId() == 0;
    }

    @Override
    protected Label getValidationLabel() {
        return lbl_validationView;
    }

    @Override
    protected void rollbackChangesIfNeeded(boolean isNew) {
        if (!isNew && oldModel != null) {
            model.setDescription(oldModel.getDescription());
            model.setValue(oldModel.getValue());
            model.setLblMaskedValue(oldModel.getLblMaskedValue());
            model.setActionIconsContainer(oldModel.getActionIconsContainer());
        }
    }

    @Override
    protected String retrieveSaveErrorMessage(Exception e, boolean isNew) {
        if (e.getMessage().contains(Strings.Constraints.UNIQUE)) {
            return Strings.Errors.EXISTING_APP_NOTE;
        } else {
            return isNew ?
                    String.format(Strings.Errors.ADDING_APP_NOTE, e.getMessage()) :
                    String.format(Strings.Errors.UPDATING_APP_NOTE, e.getMessage());
        }
    }

    @Override
    protected void retrieveValuesFromTextFields() {
        model.setDescription(txt_description.getText().trim());
        model.setValue(EncryptUtil.encrypt(txt_value.getText().trim()));
    }

    @Override
    protected void save(boolean isNew) throws SQLException {
        if (isNew) {
            DaoProvider.getAppNoteDao().add(model);
        } else {
            DaoProvider.getAppNoteDao().update(model);
        }
    }

    /*************************************
     * FXML methods
     **************************************/
    @FXML
    void cancel(ActionEvent event) {
        this.closePopup(null);
    }

    @FXML
    void onKeyPressedHandler(KeyEvent event) {
        this.closePopup(event.getCode());
    }

    @FXML
    void save(ActionEvent event) {
        saveChanges(isNew());
    }


}
