package mp.controllers.views;

import mp.constants.Strings;
import mp.constants.Views;
import mp.controllers.popups.AppPopupController;
import mp.controllers.popups.InfoPopupController;
import mp.controllers.views.base.BaseApplicationController;
import mp.dao.DaoProvider;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import mp.models.App;
import mp.utils.DateTimeUtil;
import mp.utils.ExceptionMessage;
import mp.utils.UserUtil;
import mp.utils.ViewLoader;
import java.sql.SQLException;

public class ApplicationController extends BaseApplicationController {

    @FXML
    private Label lbl_validationView;
    @FXML
    private VBox VBoxApplication;
    @FXML
    private Label lbl_user;
    @FXML
    private Label lbl_lastConnection;
    @FXML
    private FontAwesomeIcon btn_logout;
    @FXML
    private VBox secondaryFrame;

    public ApplicationController(Object model) {
        super();
    }

    @Override
    protected void runOnInitialize() {
        bindVBoxDimensions();
        initSecondaryFrameStaticVariable();
        showAppslist();
    }

    @Override
    protected void runLaterOnInitialize() {
        lbl_user.textProperty().bind(UserUtil.CURRENT_USER.fullnameProperty());
        setLastConnectionText();
        updateLastConnectionInDataBase();
    }

    @Override
    protected void validatedFieldsSetup() {
        // Not implemented
    }

    /*************************************
     * Private methods
     **************************************/
    private void bindVBoxDimensions() {
        VBoxApplication.prefWidthProperty().bind(ViewLoader.getMainFrame().widthProperty());
        VBoxApplication.prefHeightProperty().bind(ViewLoader.getMainFrame().heightProperty());
    }

    private void showAppslist() {
        ViewLoader.loadView(
                Views.APPS_LIST_VIEW,
                AppsListController.class,
                null,
                lbl_validationView,
                secondaryFrame
        );
    }

    private void setLastConnectionText() {
        if (UserUtil.CURRENT_USER.getLastConnection() != null) {
            String lastConnection = DateTimeUtil.getDatetimeFormat(UserUtil.CURRENT_USER.getLastConnection());
            lbl_lastConnection.setText(String.format(Strings.Labels.LAST_CONNECTION, lastConnection));
        } else {
            lbl_lastConnection.setText(String.format(Strings.Labels.FIRST_CONNECTION));
        }
    }

    private void updateLastConnectionInDataBase() {
        try {
            DaoProvider.getUserDao().update(UserUtil.CURRENT_USER, true);
        } catch (SQLException e) {
            String errorMessage = String.format(Strings.Errors.UPDATING_LAST_CONNECTION, e.getMessage());
            new ExceptionMessage.Builder(errorMessage, lbl_validationView).show();
            LOGGER.severe(String.format("%s - %s", LOGGER.getName(), errorMessage));
            e.printStackTrace();
        }
    }

    private void initSecondaryFrameStaticVariable() {
        ViewLoader.initSecondaryFrameStaticVariable(secondaryFrame);
    }

    /*************************************
     * FXML methods
     **************************************/
    @FXML
    void logout(MouseEvent event) {
        UserUtil.CURRENT_USER = null;
        ViewLoader.loadView(
                Views.LOGIN_VIEW,
                LoginController.class,
                null,
                lbl_validationView,
                ViewLoader.getMainFrame());
    }

    @FXML
    void newApp(ActionEvent event) {
        ViewLoader.loadPopup(
                Views.APP_POPUP,
                AppPopupController.class,
                lbl_validationView,
                new App());
    }

    @FXML
    void showConfigView(MouseEvent event) {
        ViewLoader.loadView(
                Views.CONFIG_VIEW,
                ConfigViewController.class,
                null,
                lbl_validationView,
                secondaryFrame);
    }

    @FXML
    void showInfoPopup(MouseEvent event) {
        ViewLoader.loadPopup(
                Views.INFO_POPUP,
                InfoPopupController.class,
                lbl_validationView,
                null);
    }


}
