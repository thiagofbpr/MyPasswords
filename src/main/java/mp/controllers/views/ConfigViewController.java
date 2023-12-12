package mp.controllers.views;

import mp.constants.Views;
import mp.controllers.views.base.BaseApplicationController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import mp.utils.EncryptUtil;
import mp.utils.UserUtil;
import mp.utils.ViewLoader;

public class ConfigViewController extends BaseApplicationController {

    @FXML
    private VBox VBoxConfig;
    @FXML
    private Label lbl_validationView;
    @FXML
    private Label lblUsername;
    @FXML
    private Label lblFullname;
    @FXML
    private Label lblPassword;


    public ConfigViewController(Object model) {
        super();
    }

    @Override
    protected void runOnInitialize() {
        // Not implemented
    }

    @Override
    protected void runLaterOnInitialize() {
        lblUsername.setText(UserUtil.CURRENT_USER.getUsername());
        lblFullname.setText(UserUtil.CURRENT_USER.getFullname());
        lblPassword.setText(EncryptUtil.getBlackBullets());
    }

    @Override
    protected  void validatedFieldsSetup() {
    }

    /*************************************
     * FXML methods
     **************************************/
    @FXML
    void back(MouseEvent event) {
        ViewLoader.loadView(
                Views.APPS_LIST_VIEW,
                AppsListController.class,
                null,
                lbl_validationView,
                ViewLoader.getSecondaryFrame());
    }

    @FXML
    void showConfirmUserDeletion(ActionEvent event) {
        ViewLoader.loadView(
                Views.USER_ACCOUNT_DELETION_VIEW,
                UserAccountDeletionViewController.class,
                null,
                lbl_validationView,
                ViewLoader.getSecondaryFrame());
    }

    @FXML
    void showChangeFullNameView(ActionEvent event) {
        ViewLoader.loadView(
                Views.USER_FULL_NAME_UPDATE_VIEW,
                UpdateUserFullNameViewController.class,
                null,
                lbl_validationView,
                ViewLoader.getSecondaryFrame());
    }

    @FXML
    void showChangePasswordView(ActionEvent event) {
        ViewLoader.loadView(
                Views.USER_PASSWORD_UPDATE_VIEW,
                UpdateUserPasswordViewController.class,
                null,
                lbl_validationView,
                ViewLoader.getSecondaryFrame());
    }


}
