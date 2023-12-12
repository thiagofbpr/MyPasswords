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
import mp.models.AppLogin;
import mp.utils.EncryptUtil;
import mp.utils.ValidationUtil;
import java.sql.SQLException;

public class AppLoginPopupController extends BasePopupController<AppLogin> {

    @FXML
    private Label lbl_validationView;
    @FXML
    private Label txt_title;
    @FXML
    private MFXTextField txt_username;
    @FXML
    private Label lbl_validationUsername;
    @FXML
    private MFXPasswordField txt_password;
    @FXML
    private Label lbl_validationPassword;


    public AppLoginPopupController(Stage popupStage, Object model) {
        super(popupStage, (AppLogin)model);
    }

    @Override
    protected void validatedFieldsSetup() {
        ValidationUtil.addRequiredConstraint(txt_username, lbl_validationUsername);
        ValidationUtil.addRequiredConstraint(txt_password, lbl_validationPassword);

        ValidationUtil.addNoWhiteSpaceConstraint(txt_username, lbl_validationUsername);
        ValidationUtil.addNoWhiteSpaceConstraint(txt_password, lbl_validationPassword);

        ValidationUtil.addTextLimit(txt_username, 45);
        ValidationUtil.addTextLimit(txt_password, 51);

        validatedFields.put(txt_username, lbl_validationUsername);
        validatedFields.put(txt_password, lbl_validationPassword);
    }

    @Override
    protected void runLaterOnInitialize() {
        txt_username.requestFocus();
        if (isNew()) {
            txt_title.setText(Strings.Labels.NEW_APP_LOGIN);
            txt_title.setVisible(true);
        } else {
            txt_title.setText(Strings.Labels.EDIT_APP_LOGIN);
            txt_title.setVisible(true);
            txt_username.textProperty().set(model.getUsername());
            txt_password.textProperty().set(EncryptUtil.decrypt(model.getPassword()));
        }
    }

    @Override
    protected boolean isNew() {
        return model.getId() == 0;
    }

    @Override
    protected Label getValidationLabel() {
        return this.lbl_validationView;
    }

    @Override
    protected String retrieveSaveErrorMessage(Exception e, boolean isNewAppLogin) {
        if (e.getMessage().contains(Strings.Errors.EXISTING_APP_LOGIN)) {
            return Strings.Errors.EXISTING_APP_LOGIN;
        } else {
            return isNewAppLogin ?
                    String.format(Strings.Errors.ADDING_APP_LOGIN, e.getMessage()) :
                    String.format(Strings.Errors.UPDATING_APP_LOGIN, e.getMessage());
        }
    }

    @Override
    protected void retrieveValuesFromTextFields() {
        model.setUsername(txt_username.getText().trim());
        model.setPassword(EncryptUtil.encrypt(txt_password.getText().trim()));
    }

    @Override
    protected void rollbackChangesIfNeeded(boolean isNew) {
        if (!isNew && oldModel != null) {
            model.setUsername(oldModel.getUsername());
            model.setPassword(oldModel.getPassword());
            model.setCreateTime(oldModel.getCreateTime());
            model.setUpdateTime(oldModel.getUpdateTime());
            model.setEndTime(oldModel.getEndTime());
            model.setLblMaskedPassword(oldModel.getLblMaskedPassword());
            model.setActionIconsContainer(oldModel.getActionIconsContainer());
        }
    }

    @Override
    protected void save(boolean isNew) throws SQLException {
        if (isNew) {
            if (isExistingLogin()) {
                throw new SQLException(Strings.Errors.EXISTING_APP_LOGIN);
            }
            DaoProvider.getAppLoginDao().add(model);
        } else {
            DaoProvider.getAppLoginDao().update(model);
        }
    }

    /*************************************
     * Private methods
     **************************************/
    private boolean isExistingLogin() {
        return model.getApp().getAppLogins().stream().anyMatch(login ->
                EncryptUtil.decrypt(login.getPassword()).equals(EncryptUtil.decrypt(model.getPassword()))
                        && login.getUsername().equals(model.getUsername())
        );
    }

    /*************************************
     * FXML methods
     **************************************/
    @FXML
    void cancel(ActionEvent event) {
        closePopup(null);
    }

    @FXML
    void onKeyPressedHandler(KeyEvent event) {
        closePopup(event.getCode());
    }

    @FXML
    void save(ActionEvent event) {
        saveChanges(isNew());
    }


}
