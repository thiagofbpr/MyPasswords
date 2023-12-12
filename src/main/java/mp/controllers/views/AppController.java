package mp.controllers.views;

import mp.constants.Strings;
import mp.constants.Views;
import mp.controllers.popups.AppPopupController;
import mp.controllers.views.base.BaseAppController;
import mp.dao.DaoProvider;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import mp.models.App;
import mp.utils.ExceptionMessage;
import mp.utils.ViewLoader;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;

public class AppController extends BaseAppController<App> {

    @FXML
    private HBox HBox_App;
    @FXML
    private Label lbl_validationView;
    @FXML
    private ImageView img_appLogo;
    @FXML
    private VBox VBox_AppNameOnly;
    @FXML
    private Label lbl_appNameOnly;
    @FXML
    private VBox VBox_AppNameAndUrl;
    @FXML
    private Label lbl_appName;
    @FXML
    private Label lbl_appWebsite;
    @FXML
    private VBox appLoginsFrame;
    @FXML
    private VBox appQuestionsFrame;
    @FXML
    private VBox appNotesFrame;

    public AppController(Object model) {
        super((App)model);
    }

    @Override
    protected void runOnInitialize() {
        img_appLogo.imageProperty().bind(app.logoProperty());
        switchVBoxesIfNoWebsite();
        addWebsiteChangeListener();
        loadAppLoginsList();
        loadAppQuestionsList();
        loadAppNotesList();
        initAppFramesStaticVariables();
    }

    @Override
    protected void runLaterOnInitialize() {
        // Not implemented
    }

    @Override
    protected void hideAll() {
        // Not implemented
    }

    @Override
    protected void showAll() {
        // Not implemented
    }

    /*************************************
     * Private methods
     **************************************/
    private void switchVBoxesIfNoWebsite() {
        if (app.getUrl().isEmpty()) {
            VBox_AppNameOnly.toFront();
            lbl_appNameOnly.textProperty().bind(app.nameProperty());
        } else {
            VBox_AppNameAndUrl.toFront();
            lbl_appName.textProperty().bind(app.nameProperty());
            lbl_appWebsite.textProperty().bind(app.urlProperty());
        }
    }

    private void addWebsiteChangeListener() {
        app.urlProperty().addListener((observableValue, oldValue, newValue) -> switchVBoxesIfNoWebsite());
    }

    private void loadAppLoginsList() {
        ViewLoader.loadView(
                Views.APP_LOGINS_VIEW,
                AppLoginsController.class,
                app,
                lbl_validationView,
                appLoginsFrame
        );
    }

    private void loadAppQuestionsList() {
        ViewLoader.loadView(
                Views.APP_QUESTIONS_VIEW,
                AppQuestionsController.class,
                app,
                lbl_validationView,
                appQuestionsFrame
        );
    }

    private void loadAppNotesList() {
        ViewLoader.loadView(
                Views.APP_NOTES_VIEW,
                AppNotesController.class,
                app,
                lbl_validationView,
                appNotesFrame
        );
    }

    private String formatUrlIfNeeded(String url) {
        if (!app.getUrl().contains("www") && !app.getUrl().contains("http://") && !app.getUrl().contains("https://")) {
            return "www.".concat(url);
        }
        return url;
    }

    private void deleteApp() {
        try {
            DaoProvider.getAppDao().delete(app);
        } catch (SQLException e) {
            String errorMessage = String.format(Strings.Errors.DELETING_APP, e.getMessage());
            new ExceptionMessage.Builder(errorMessage, lbl_validationView).show();
            LOGGER.severe(String.format("%s - %s", LOGGER.getName(), errorMessage));
            e.printStackTrace();
        }
    }

    private void backToAppsList() {
        ViewLoader.loadView(
                Views.APPS_LIST_VIEW,
                AppsListController.class,
                null,
                lbl_validationView,
                ViewLoader.getSecondaryFrame());
    }

    private void initAppFramesStaticVariables() {
        ViewLoader.initAppFramesStaticVariables(appLoginsFrame, appQuestionsFrame, appNotesFrame);
    }

    /*************************************
     * FXML methods
     **************************************/
    @FXML
    void browserAppWebsite(MouseEvent event) {
        try {
            String url = formatUrlIfNeeded(app.getUrl());
            Desktop.getDesktop().browse(new URI(url));
        } catch (IOException | URISyntaxException e) {
            String errorMessage = String.format(Strings.Errors.BROWSING_WEB_SITE, e.getMessage());
            new ExceptionMessage.Builder(errorMessage, lbl_validationView).show();
            LOGGER.severe(String.format("%s - %s", LOGGER.getName(), errorMessage));
            e.printStackTrace();
        }
    }

    @FXML
    void editApp(MouseEvent event) {
        ViewLoader.loadPopup(
                Views.APP_POPUP,
                AppPopupController.class,
                lbl_validationView,
                app);
    }

    @FXML
    void deleteApp(MouseEvent event) {
        ViewLoader.showDialog(
                Strings.Dialog.CONFIRM_DELETE_APP,
                () -> deleteApp(),
                lbl_validationView);
    }

    @FXML
    void back(MouseEvent event) {
        backToAppsList();
    }


}
