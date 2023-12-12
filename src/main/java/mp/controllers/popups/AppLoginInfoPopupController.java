package mp.controllers.popups;

import mp.controllers.popups.base.BasePopupController;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import mp.models.AppLogin;
import mp.utils.DateTimeUtil;

public class AppLoginInfoPopupController extends BasePopupController<AppLogin> {

    @FXML
    private Label lblStatus;
    @FXML
    private FontAwesomeIcon statusIcon;
    @FXML
    private Label lblCreateTime;
    @FXML
    private Label lblUpdateTime;
    @FXML
    private Label lblEndTime;

    public AppLoginInfoPopupController(Stage popupStage, Object model) {
        super(popupStage, (AppLogin)model);
    }

    @Override
    protected void runLaterOnInitialize() {
        if (model != null) {
            lblCreateTime.setText(DateTimeUtil.getDateFormat(model.getCreateTime()));
            lblUpdateTime.setText(DateTimeUtil.getDateFormat(model.getUpdateTime()));
            if (model.getEndTime() != null) {
                statusIcon.fillProperty().set(Color.valueOf("#aaacad"));
                lblStatus.setText("Inactive");
                lblEndTime.setText(DateTimeUtil.getDateFormat(model.getEndTime()));
            } else {
                statusIcon.fillProperty().set(Color.valueOf("#64c987"));
                lblStatus.setText("Active");
                lblEndTime.setText("");
            }
            statusIcon.setVisible(true);
        }
    }

    @Override
    protected void validatedFieldsSetup() {
        // Not implemented
    }

    @Override
    protected boolean isNew() {
        return false;
    }

    @Override
    protected Label getValidationLabel() {
        return null;
    }

    @Override
    protected void rollbackChangesIfNeeded(boolean isNew) {
        // Not implemented
    }

    @Override
    protected String retrieveSaveErrorMessage(Exception e, boolean isNew) {
        return null;
    }

    @Override
    protected void retrieveValuesFromTextFields() {
        // Not implemented
    }

    @Override
    protected void save(boolean isNew) throws Exception {
        // Not implemented
    }

    /*************************************
     * FXML methods
     **************************************/
    @FXML
    void close(ActionEvent event) {
        closePopup(null);
    }

    @FXML
    void onKeyPressedHandler(KeyEvent event) {
        closePopup(event.getCode());
    }


}
