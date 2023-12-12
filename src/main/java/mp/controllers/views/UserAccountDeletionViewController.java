package mp.controllers.views;

import mp.constants.Strings;
import mp.constants.Views;
import mp.controllers.views.base.BaseApplicationController;
import mp.dao.DaoProvider;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import mp.utils.*;
import java.sql.SQLException;

public class UserAccountDeletionViewController extends BaseApplicationController {

    @FXML
    private Label lbl_validationView;
    @FXML
    private Label deleteAccountTextConfirmation;
    @FXML
    private MFXPasswordField txt_password;
    @FXML
    private Label lbl_ValidationPassword;

    public UserAccountDeletionViewController(Object model) {
        super();
    }

    @Override
    protected void runOnInitialize() {
        // Not implemented
    }

    @Override
    protected void runLaterOnInitialize() {
        txt_password.requestFocus();
    }

    @Override
    protected void validatedFieldsSetup() {
        ValidationUtil.addRequiredConstraint(txt_password, lbl_ValidationPassword);
        ValidationUtil.addNoWhiteSpaceConstraint(txt_password, lbl_ValidationPassword);
        ValidationUtil.addTextLimit(txt_password, 51);
        validatedFields.put(txt_password, lbl_ValidationPassword);
    }

    /*************************************
     * Private methods
     **************************************/
    private void saveChanges() {
        if (ValidationUtil.isValidationSuccessful(validatedFields)) {
            try {
                if (!txt_password.getText().trim().equals(EncryptUtil.decrypt(UserUtil.CURRENT_USER.getPassword()))) {
                    ValidationUtil.showCustomValidationError(txt_password, lbl_ValidationPassword, Strings.Errors.INCORRECT_PASSWORD);
                } else {
                    delete();
                    showLoginView();
                    UserUtil.CURRENT_USER = null;
                }
            } catch (Exception e) {
                String errorMessage = String.format(Strings.Errors.DELETING_USER_ACCOUNT, e.getMessage());
                new ExceptionMessage.Builder(errorMessage, lbl_validationView).show();
                LOGGER.severe(String.format("%s - %s", LOGGER.getName(), errorMessage));
                e.printStackTrace();
            }
        }
    }

    private void showLoginView() {
        ViewLoader.loadView(
                Views.LOGIN_VIEW,
                LoginController.class,
                null,
                lbl_validationView,
                ViewLoader.getMainFrame()
        );
    }

    private void delete() throws SQLException {
        DaoProvider.getUserDao().delete(UserUtil.CURRENT_USER);
    }

    /*************************************
     * FXML methods
     **************************************/
    @FXML
    void save(ActionEvent event) {
        saveChanges();
    }

    @FXML
    void cancel(ActionEvent event) {
        ViewLoader.loadView(
                Views.CONFIG_VIEW,
                ConfigViewController.class,
                null,
                lbl_validationView,
                ViewLoader.getSecondaryFrame()
        );
    }

    @FXML
    void delete(ActionEvent event) {
        saveChanges();
    }


}
