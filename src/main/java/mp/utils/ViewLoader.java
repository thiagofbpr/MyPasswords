package mp.utils;

import mp.constants.Strings;
import mp.constants.Views;
import mp.constants.Views.View;
import mp.controllers.popups.DialogPopupController;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

public class ViewLoader {

    private static final Logger LOGGER = Logger.getLogger(ViewLoader.class.getName());
    private static Stage PRIMARY_STAGE;
    private static VBox MAIN_FRAME;
    private static VBox SECONDARY_FRAME;
    private static VBox APP_LOGINS_FRAME;
    private static VBox APP_QUESTIONS_FRAME;
    private static VBox APP_NOTES_FRAME;

    private ViewLoader() {

    }

    public static void initPrimaryStageStaticVariable(Stage primaryStage) {
        PRIMARY_STAGE = primaryStage;
    }

    public static void initMainFrameStaticVariable(VBox mainFrame) {
        MAIN_FRAME = mainFrame;
    }

    public static void initSecondaryFrameStaticVariable(VBox secondaryFrame) {
        SECONDARY_FRAME = secondaryFrame;
    }

    public static void initAppFramesStaticVariables(VBox appLoginsFrame, VBox appQuestionsFrame, VBox appNotesFrame) {
        APP_LOGINS_FRAME = appLoginsFrame;
        APP_QUESTIONS_FRAME = appQuestionsFrame;
        APP_NOTES_FRAME = appNotesFrame;
    }

    public static Stage getPrimaryStage() {
        return PRIMARY_STAGE;
    }
    public static VBox getMainFrame() {
        return MAIN_FRAME;
    }
    public static VBox getSecondaryFrame() {
        return SECONDARY_FRAME;
    }
    public static VBox getAppLoginsFrame() {
        return APP_LOGINS_FRAME;
    }
    public static VBox getAppQuestionsFrame() {
        return APP_QUESTIONS_FRAME;
    }
    public static VBox getAppNotesFrame() {
        return APP_NOTES_FRAME;
    }

    public static <T> void loadView(View view, Class<T> viewControllerClass, Object model, Label validationLabel, VBox frame) {
        try {
            FXMLLoader loader = new FXMLLoader(ViewLoader.class.getResource(view.getPath()));
            loader.setController(createController(viewControllerClass, model));
            VBox vBox = loader.load();
            frame.getChildren().setAll(vBox);
        } catch (Exception e) {
            String errorMessage = String.format(Strings.Errors.LOADING_VIEW, view.getName(), e.getMessage());
            new ExceptionMessage.Builder(errorMessage, validationLabel)
                    .withInsets(new Insets(20,0,10,0))
                    .show();
            LOGGER.severe(String.format("%s - %s", LOGGER.getName(), errorMessage));
            e.printStackTrace();
        }
    }

    private static <T> Object createController(Class<T> viewControllerClass, Object model) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return viewControllerClass.getDeclaredConstructor(new Class[] {Object.class}).newInstance(model);
    }

    public static <T> void loadPopup(View popup, Class<T> viewControllerClass, Label errorLabel, Object model) {
        try {
            Stage popupStage = new Stage();

            FXMLLoader loader = new FXMLLoader(ViewLoader.class.getResource(popup.getPath()));
            loader.setController(viewControllerClass.getDeclaredConstructor(new Class[] {Stage.class, Object.class}).newInstance(popupStage, model));
            VBox vBox = loader.load();
            Scene scene = new Scene(vBox);
            scene.setFill(Color.TRANSPARENT);

            popupStage.setScene(scene);
            popupStage.initStyle(StageStyle.TRANSPARENT);
            popupStage.setMaximized(true);
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.initOwner(PRIMARY_STAGE);
            popupStage.showAndWait();
        } catch (Exception e) {
            String errorMessage = String.format(Strings.Errors.LOADING_POPUP, popup.getName(), e.getMessage());
            new ExceptionMessage.Builder(errorMessage, errorLabel)
                    .withInsets(new Insets(20,0,10,0))
                    .show();
            LOGGER.severe(String.format("%s - %s", LOGGER.getName(), errorMessage));
            e.printStackTrace();
        }
    }

    public static void showDialog(String dialogContentText, Runnable confirmAction, Label lbl_validationView) {
        try {
            Stage popupStage = new Stage();

            FXMLLoader loader = new FXMLLoader(ViewLoader.class.getResource(Views.DIALOG_POPUP.getPath()));
            loader.setController(new DialogPopupController(dialogContentText, confirmAction, popupStage));
            VBox dialogPopup = loader.load();
            Scene scene = new Scene(dialogPopup);
            scene.setFill(Color.TRANSPARENT);

            popupStage.setScene(scene);
            popupStage.initStyle(StageStyle.TRANSPARENT);
            popupStage.setMaximized(true);
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.initOwner(PRIMARY_STAGE);
            popupStage.show();
        } catch (IOException e) {
            String errorMessage = String.format(Strings.Errors.SHOWING_DIALOG, Views.DIALOG_POPUP.getName(), e.getMessage());
            new ExceptionMessage.Builder(errorMessage, lbl_validationView).show();
            LOGGER.severe(String.format("%s - %s", LOGGER.getName(), errorMessage));
            e.printStackTrace();
        }
    }


}
