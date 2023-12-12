package mp.controllers.views;

import mp.constants.Strings;
import mp.constants.Views;
import mp.controllers.views.base.BaseApplicationController;
import mp.dao.DaoProvider;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import mp.models.*;
import mp.utils.*;
import java.sql.SQLException;

public class LoginController extends BaseApplicationController {

    @FXML
    private VBox VBoxLogin;
    @FXML
    private Label lbl_validationView;
    @FXML
    private MFXTextField txt_username;
    @FXML
    private Label lbl_ValidationUsername;
    @FXML
    private MFXPasswordField txt_password;
    @FXML
    private Label lbl_ValidationPassword;
    @FXML
    private MFXButton btn_login;
    @FXML
    private Label lbl_signUp;

    public LoginController(Object model) {
        super();
    }

    @Override
    protected void runOnInitialize() {
        // Not implemented
    }

    @Override
    protected void runLaterOnInitialize() {
        txt_username.requestFocus();
    }

    @Override
    protected void validatedFieldsSetup() {
        ValidationUtil.addRequiredConstraint(txt_username, lbl_ValidationUsername);
        ValidationUtil.addRequiredConstraint(txt_password, lbl_ValidationPassword);

        ValidationUtil.addNoWhiteSpaceConstraint(txt_username, lbl_ValidationUsername);
        ValidationUtil.addNoWhiteSpaceConstraint(txt_password, lbl_ValidationPassword);

        ValidationUtil.addTextLimit(txt_username, 45);
        ValidationUtil.addTextLimit(txt_password, 51);

        validatedFields.put(txt_username, lbl_ValidationUsername);
        validatedFields.put(txt_password, lbl_ValidationPassword);
    }

    /*************************************
     * Private methods
     **************************************/

    private void loadUserData() {
        try {
            ObservableList<App> userApps = DaoProvider.getAppDao().getAll(UserUtil.CURRENT_USER);
            for (App app : userApps) {
                ObservableList<AppLogin> appLogins = DaoProvider.getAppLoginDao().getAll(app);
                app.getAppLogins().addAll(appLogins);

                ObservableList<AppQuestion> appQuestions = DaoProvider.getAppQuestionDao().getAll(app);
                app.getAppQuestions().addAll(appQuestions);

                ObservableList<AppNote> appNotes = DaoProvider.getAppNoteDao().getAll(app);
                app.getAppNotes().addAll(appNotes);
            }
            UserUtil.CURRENT_USER.getApps().addAll(userApps);
        } catch (Exception e) {
            String errorMessage = String.format(Strings.Errors.LOADING_USER_DATA, e.getMessage());
            new ExceptionMessage.Builder(errorMessage, lbl_validationView)
                    .withInsets(new Insets(20,0,10,0))
                    .show();
            LOGGER.severe(String.format("%s - %s", LOGGER.getName(), errorMessage));
            e.printStackTrace();
        }
    }

    private boolean isPasswordCorrect(String userPassword) {
        String decryptedPwd = EncryptUtil.decrypt(userPassword);
        return decryptedPwd.equals(txt_password.getText().trim());
    }

    private void loadApplicationView() {
        ViewLoader.loadView(
                Views.APPLICATION_VIEW,
                ApplicationController.class,
                null,
                lbl_validationView,
                ViewLoader.getMainFrame());
    }

    private User retrieveUserFromDatabase() {
        User user = null;
        try {
            user = DaoProvider.getUserDao().get(txt_username.getText().trim());
        } catch (SQLException e) {
            String errorMessage = String.format(Strings.Errors.RETRIEVING_USER_FROM_DATABASE, e.getMessage());
            new ExceptionMessage.Builder(errorMessage, lbl_validationView)
                    .withInsets(new Insets(20,0,10,0))
                    .show();
            LOGGER.severe(String.format("%s - %s", LOGGER.getName(), errorMessage));
            e.printStackTrace();
        }
        return user;
    }

    /*************************************
     * FXML methods
     **************************************/
    @FXML
    void login(ActionEvent event) {
        if (ValidationUtil.isValidationSuccessful(validatedFields)) {
            User user = retrieveUserFromDatabase();
            if (user != null) {
                if (isPasswordCorrect(user.getPassword())) {
                    UserUtil.CURRENT_USER = user;
                    loadUserData();
                    loadApplicationView();
                } else {
                    ValidationUtil.showCustomValidationError(txt_password, lbl_ValidationPassword, Strings.Errors.INCORRECT_PASSWORD);
                }
            } else {
                ValidationUtil.showCustomValidationError(txt_username, lbl_ValidationUsername, Strings.Errors.NON_EXISTING_USER);
            }
        }
    }

    @FXML
    void signUp(MouseEvent event) {
        ViewLoader.loadView(
                Views.SIGN_UP_VIEW,
                SignUpController.class,
                null,
                lbl_validationView,
                ViewLoader.getMainFrame());
    }


}
