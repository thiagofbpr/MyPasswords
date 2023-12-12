package mp.controllers.popups;

import mp.constants.Strings;
import mp.constants.Views;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import mp.utils.ExceptionMessage;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class DialogPopupController implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(DialogPopupController.class.getName());
    private final Stage dialogStage;
    private final Runnable confirmAction;
    private final String contentText;

    @FXML
    private Label lbl_validationView;
    @FXML
    private Label lbl_contentText;

    public DialogPopupController(String contentText, Runnable confirmAction, Stage dialogStage) {
        this.dialogStage = dialogStage;
        this.contentText = contentText;
        this.confirmAction = confirmAction;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> this.lbl_contentText.setText(contentText));
    }

    /*************************************
     * Private methods
     **************************************/
    private void loadDialog() {
        try {
            Stage popupStage = new Stage();

            FXMLLoader loader = new FXMLLoader(this.getClass().getResource(Views.DIALOG_POPUP.getPath()));
            loader.setController(this);
            VBox popup = loader.load();
            Scene scene = new Scene(popup);
            scene.setFill(Color.TRANSPARENT);

            popupStage.setScene(scene);
            popupStage.initStyle(StageStyle.TRANSPARENT);
            popupStage.setMaximized(true);
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.initOwner(dialogStage);
            popupStage.show();
        } catch (Exception e) {
            String errorMessage = String.format(Strings.Errors.LOADING_POPUP, Views.DIALOG_POPUP.getName(), e.getMessage());
            new ExceptionMessage.Builder(errorMessage, lbl_validationView)
                    .withInsets(new Insets(20,0,10,0))
                    .show();
            LOGGER.severe(String.format("%s - %s", LOGGER.getName(), errorMessage));
            e.printStackTrace();
        }
    }

    protected void closeDialog() {
        if (this.dialogStage != null) {
            this.dialogStage.close();
        }
    }

    private void confirm() {
        confirmAction.run();
        closeDialog();
    }

    /*************************************
     * FXML methods
     **************************************/
    @FXML
    void cancel(ActionEvent event) {
        closeDialog();
    }

    @FXML
    void onKeyPressedHandler(KeyEvent event) {
        if (event.getCode() == KeyCode.ESCAPE) {
            closeDialog();
        } else if (event.getCode() == KeyCode.SPACE || event.getCode() == KeyCode.ENTER) {
            confirm();
        }
    }

    @FXML
    void confirm(ActionEvent event) {
        confirm();
    }


}
