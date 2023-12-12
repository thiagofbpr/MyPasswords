package mp.controllers.views;

import mp.constants.Strings;
import mp.constants.Views;
import mp.controllers.views.base.BaseApplicationController;
import mp.dao.DaoProvider;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import mp.models.User;
import mp.utils.ExceptionMessage;
import mp.utils.UserUtil;
import mp.utils.ValidationUtil;
import mp.utils.ViewLoader;
import java.sql.SQLException;

public class UpdateUserFullNameViewController extends BaseApplicationController {

    private User oldUser;

    @FXML
    private Label lbl_validationView;
    @FXML
    private MFXTextField txt_fullname;
    @FXML
    private Label lbl_ValidationFullName;


    public UpdateUserFullNameViewController(Object model) {
        super();
    }

    @Override
    protected void runOnInitialize() {
        // Not implemented
    }

    @Override
    protected void runLaterOnInitialize() {
        txt_fullname.textProperty().set(UserUtil.CURRENT_USER.getFullname());
        txt_fullname.requestFocus();
        txt_fullname.positionCaret(0);
    }

    @Override
    protected void validatedFieldsSetup() {
        ValidationUtil.addRequiredConstraint(txt_fullname, lbl_ValidationFullName);
        ValidationUtil.addTextLimit(txt_fullname, 45);
        validatedFields.put(txt_fullname, lbl_ValidationFullName);
    }

    /*************************************
     * Private methods
     **************************************/
    private boolean saveChanges() {
        saveOldData();
        try {
            UserUtil.CURRENT_USER.setFullname(txt_fullname.getText().trim());
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
            if (saveChanges()) {
                showConfigView();
            }
        }
    }

    @FXML
    void cancel(ActionEvent event) {
        showConfigView();
    }


}
