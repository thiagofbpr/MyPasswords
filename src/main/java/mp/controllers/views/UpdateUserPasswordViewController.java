package mp.controllers.views;

import mp.constants.Strings;
import mp.constants.Views;
import mp.controllers.views.base.BaseApplicationController;
import mp.dao.DaoProvider;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import mp.models.User;
import mp.utils.*;
import java.sql.SQLException;

public class UpdateUserPasswordViewController extends BaseApplicationController {

    private User oldUser;

    @FXML
    private Label lbl_validationView;
    @FXML
    private MFXPasswordField txt_currentPassword;
    @FXML
    private Label lbl_ValidationCurrentPassword;
    @FXML
    private MFXPasswordField txt_newPassword;
    @FXML
    private Label lbl_ValidationNewPassword;


    public UpdateUserPasswordViewController(Object model) {
        super();
    }

    @Override
    protected void runOnInitialize() {
        // Not implemented
    }

    @Override
    protected void runLaterOnInitialize() {
        txt_currentPassword.requestFocus();
    }

    @Override
    protected void validatedFieldsSetup() {
        ValidationUtil.addRequiredConstraint(txt_currentPassword, lbl_ValidationCurrentPassword);
        ValidationUtil.addRequiredConstraint(txt_newPassword, lbl_ValidationNewPassword);

        ValidationUtil.addTextLimit(txt_currentPassword, 51);
        ValidationUtil.addTextLimit(txt_newPassword, 51);

        validatedFields.put(txt_currentPassword, lbl_ValidationCurrentPassword);
        validatedFields.put(txt_newPassword, lbl_ValidationNewPassword);
    }

    /*************************************
     * Private methods
     **************************************/
    private boolean saveChanges() {
        saveOldData();
        try {
            UserUtil.CURRENT_USER.setPassword(EncryptUtil.encrypt(txt_newPassword.getText().trim()));
            save();
            return true;
        } catch (SQLException e) {
            String errorMessage = retrieveErrorMessage(e);
            rollbackChangesIfNeeded();
            new ExceptionMessage.Builder(errorMessage, lbl_validationView)
                    .withInsets(new Insets(20,0,20,0))
                    .show();
            LOGGER.severe(String.format("%s - %s", LOGGER.getName(), errorMessage));
            e.printStackTrace();
            return false;
        }
    }

    private void rollbackChangesIfNeeded() {
        if (oldUser != null) {
            UserUtil.CURRENT_USER.setFullname(oldUser.getFullname());
            UserUtil.CURRENT_USER.setUsername(oldUser.getUsername());
            UserUtil.CURRENT_USER.setPassword(oldUser.getPassword());
            UserUtil.CURRENT_USER.setUpdatetime(oldUser.getUpdatetime());
            UserUtil.CURRENT_USER.setLastConnection(oldUser.getLastConnection());
        }
    }

    private void saveOldData() {
        oldUser = new User(UserUtil.CURRENT_USER);
    }

    private void save() throws SQLException {
        DaoProvider.getUserDao().update(UserUtil.CURRENT_USER, false);
    }

    private String retrieveErrorMessage(Exception e) {
        return String.format(Strings.Errors.UPDATING_USER_INFORMATION, e.getMessage());
    }

    private boolean isCurrentPasswordCorrect() {
        return txt_currentPassword.getText().equals(EncryptUtil.decrypt(UserUtil.CURRENT_USER.getPassword()));
    }

    private void showConfigView() {
        ViewLoader.loadView(
                Views.CONFIG_VIEW,
                ConfigViewController.class,
                null,
                lbl_validationView,
                ViewLoader.getSecondaryFrame());
    }

    /*************************************
     * FXML methods
     **************************************/
    @FXML
    void save(ActionEvent event) {
        if (ValidationUtil.isValidationSuccessful(validatedFields)) {
            if (isCurrentPasswordCorrect()) {
                if (saveChanges()) {
                    showConfigView();
                }
            } else {
                ValidationUtil.showCustomValidationError(txt_currentPassword, lbl_ValidationCurrentPassword, Strings.Errors.CURRENT_PASSWORD_NOT_MATCH);
            }
        }
    }

    @FXML
    void cancel(ActionEvent event) {
        showConfigView();
    }


}
