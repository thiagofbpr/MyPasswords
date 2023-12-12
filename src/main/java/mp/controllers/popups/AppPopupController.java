package mp.controllers.popups;

import mp.constants.Strings;
import mp.controllers.popups.base.BasePopupController;
import mp.dao.DaoProvider;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import mp.models.App;
import mp.utils.ImageUtil;
import mp.utils.ValidationUtil;

public class AppPopupController extends BasePopupController<App> {

    @FXML
    private VBox VBoxLogin;
    @FXML
    private Label lbl_validationView;
    @FXML
    private Label txt_title;
    @FXML
    private MFXTextField txt_name;
    @FXML
    private MFXTextField txt_website;
    @FXML
    private ImageView img_appLogo;
    @FXML
    private Label lbl_ValidationName;
    @FXML
    private Label lbl_ValidationWebsite;

    public AppPopupController(Stage popupStage, Object model) {
        super(popupStage, (App)model);
    }

    @Override
    protected void validatedFieldsSetup() {
        ValidationUtil.addRequiredConstraint(txt_name, lbl_ValidationName);
        ValidationUtil.addWebsiteFormatConstraint(txt_website, lbl_ValidationWebsite);

        ValidationUtil.addTextLimit(txt_name, 45);
        ValidationUtil.addTextLimit(txt_website, 255);

        validatedFields.put(txt_name, lbl_ValidationName);
        validatedFields.put(txt_website, lbl_ValidationWebsite);
    }

    @Override
    protected void runLaterOnInitialize() {
        txt_name.requestFocus();
        txt_name.textProperty().set(model.getName());
        txt_website.textProperty().set(model.getUrl());
        if (isNew()) {
            txt_title.setText(Strings.Labels.NEW_APPLICATION);
            txt_website.setTrailingIcon(new ImageView(ImageUtil.DEFAULT_APP_LOGO));
        } else {
            txt_title.setText(Strings.Labels.EDIT_APPLICATION);
            txt_website.setTrailingIcon(new ImageView(model.getLogo()));
        }
    }

    @Override
    protected boolean isNew() {
        return model.getId() == 0;
    }

    @Override
    protected Label getValidationLabel() {
        return lbl_validationView;
    }

    @Override
    protected void rollbackChangesIfNeeded(boolean isNew) {
        if (!isNew && oldModel != null) {
            model.setName(oldModel.getName());
            model.setUrl(oldModel.getUrl());
            model.setLogo(oldModel.getLogo());
        }
    }

    @Override
    protected String retrieveSaveErrorMessage(Exception e, boolean isNew) {
        if (e.getMessage().contains(Strings.Constraints.UNIQUE)) {
            return Strings.Errors.EXISTING_APP;
        } else {
            return isNew ?
                    String.format(Strings.Errors.ADDING_APP, e.getMessage()) :
                    String.format(Strings.Errors.UPDATING_APP, e.getMessage());
        }
    }

    @Override
    protected void retrieveValuesFromTextFields() {
        model.setName(txt_name.getText().trim());
        model.setUrl(txt_website.getText().trim());
        model.setLogo(((ImageView)txt_website.getTrailingIcon()).getImage());
    }

    @Override
    protected void save(boolean isNew) throws Exception {
        if (isNew) {
            DaoProvider.getAppDao().add(model);
        } else {
            DaoProvider.getAppDao().update(model);
        }
    }

    /*************************************
     * FXML methods
     **************************************/
    @FXML
    void cancel(ActionEvent event) {
        this.closePopup(null);
    }

    @FXML
    void cancelKey(KeyEvent event) {
        this.closePopup(event.getCode());
    }

    @FXML
    void searchAppLogo(KeyEvent event) {
        Image logo = ImageUtil.searchAppLogo(txt_website.getText());
        if (logo != null && !logo.isError()) {
            txt_website.setTrailingIcon(new ImageView(logo));
        } else {
            txt_website.setTrailingIcon(new ImageView(ImageUtil.DEFAULT_APP_LOGO));
        }
    }

    @FXML
    void save(ActionEvent event) {
        saveChanges(isNew());
    }


}
