package mp.controllers.popups;

import mp.constants.Strings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import mp.utils.ExceptionMessage;
import javafx.scene.control.Label;
import java.awt.*;
import java.net.URI;

public class InfoPopupController {

    private final Stage popupStage;

    @FXML
    private VBox VBoxInfo;
    @FXML
    private Label lbl_validationView;

    public InfoPopupController(Stage popupStage, Object model) {
        this.popupStage = popupStage;
    }

    private void closePopup(KeyCode keyCode) {
        if (this.popupStage != null && (keyCode == null || keyCode == KeyCode.ESCAPE)) {
            this.popupStage.close();
        }
    }

    /*************************************
     * FXML methods
     **************************************/
    @FXML
    void close(ActionEvent event) {
        closePopup(null);
    }

    @FXML
    void closeKey(KeyEvent event) {
        closePopup(event.getCode());
    }

    @FXML
    void mailTo(MouseEvent event) {
        try {
            Desktop desktop = Desktop.getDesktop();
            String message = String.format("mailto:%s?subject=%s", Strings.EMAIL.EMAIL_ADDRESS, Strings.EMAIL.EMAIL_SUBJECT);
            URI uri = URI.create(message);
            desktop.mail(uri);
        } catch (Exception e) {
            String errorMessage = String.format(Strings.EMAIL.ERROR_OPENING_EMAIL_CLIENT, e.getMessage());
            new ExceptionMessage.Builder(errorMessage, lbl_validationView)
                    .show();
            e.printStackTrace();
        }
    }


}
