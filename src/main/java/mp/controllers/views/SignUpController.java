package mp.controllers.views;

import mp.constants.Strings;
import mp.constants.Views;
import mp.controllers.views.base.BaseApplicationController;
import mp.dao.DaoProvider;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.font.MFXFontIcon;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import mp.models.User;
import mp.utils.EncryptUtil;
import mp.utils.ExceptionMessage;
import mp.utils.ValidationUtil;
import mp.utils.ViewLoader;
import java.sql.SQLException;

public class SignUpController extends BaseApplicationController {

    @FXML
    private Label lbl_validationView;
    @FXML
    private MFXFontIcon btn_back;
    @FXML
    private MFXTextField txt_fullname;
    @FXML
    private Label lbl_ValidationFullName;
    @FXML
    private MFXTextField txt_username;
    @FXML
    private Label lbl_ValidationUsername;
    @FXML
    private MFXPasswordField txt_password;
    @FXML
    private Label lbl_ValidationPassword;
    @FXML
    private MFXButton btn_signUp;

    public SignUpController(Object model) {
        super();
    }

    @Override
    protected void runOnInitialize() {
        // Not implemented
    }

    @Override
    protected void runLaterOnInitialize() {
        txt_fullname.requestFocus();
    }

    @Override
    protected void validatedFieldsSetup() {
        ValidationUtil.addRequiredConstraint(txt_fullname, lbl_ValidationFullName);
        ValidationUtil.addRequiredConstraint(txt_username, lbl_ValidationUsername);
        ValidationUtil.addRequiredConstraint(txt_password, lbl_ValidationPassword);

        ValidationUtil.addNoWhiteSpaceConstraint(txt_username, lbl_ValidationUsername);
        ValidationUtil.addNoWhiteSpaceConstraint(txt_password, lbl_ValidationPassword);

        ValidationUtil.addTextLimit(txt_fullname, 45);
        ValidationUtil.addTextLimit(txt_username, 45);
        ValidationUtil.addTextLimit(txt_password, 51);

        validatedFields.put(txt_fullname, lbl_ValidationFullName);
        validatedFields.put(txt_username, lbl_ValidationUsername);
        validatedFields.put(txt_password, lbl_ValidationPassword);
    }

    /*************************************
     * FXML methods
     **************************************/
    @FXML
    void back(MouseEvent event) {
        ViewLoader.loadView(
                Views.LOGIN_VIEW,
                LoginController.class,
                null,
                lbl_validationView,
                ViewLoader.getMainFrame());
    }

    @FXML
    void signUp(ActionEvent event) {
        if (ValidationUtil.isValidationSuccessful(validatedFields)) {
            User newUser = new User(txt_fullname.getText(), txt_username.getText(), EncryptUtil.encrypt(txt_password.getText().trim()));
            try {
                DaoProvider.getUserDao().add(newUser);
            } catch (SQLException e) {
                String errorMessage = retrieveErrorMessage(e);
                new ExceptionMessage.Builder(errorMessage, lbl_validationView)
                        .withInsets(new Insets(20,0,10,0))
                        .show();
                LOGGER.severe(String.format("%s - %s", LOGGER.getName(), errorMessage));
                e.printStackTrace();
                return;
            }

            ViewLoader.loadView(
                    Views.SIGN_UP_SUCCESS_VIEW,
                    SignUpSuccessController.class,
                    null,
                    lbl_validationView,
                    ViewLoader.getMainFrame());
        }
    }

    private String retrieveErrorMessage(Exception e) {
        if (e.getMessage().contains(Strings.Constraints.UNIQUE)) {
            return Strings.Errors.EXISTING_USERNAME;
        }
        return String.format(Strings.Errors.ADDING_USER_TO_DATABASE, e.getMessage());
    }


}
