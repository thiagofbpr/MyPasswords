package mp.controllers.popups;

import mp.constants.Strings;
import mp.controllers.popups.base.BasePopupController;
import mp.dao.DaoProvider;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import mp.models.AppQuestion;
import mp.utils.EncryptUtil;
import mp.utils.ValidationUtil;

public class AppQuestionPopupController extends BasePopupController<AppQuestion> {

    @FXML
    private Label lbl_validationView;
    @FXML
    private Label txt_title;
    @FXML
    private MFXTextField txt_question;
    @FXML
    private Label lbl_validationQuestion;
    @FXML
    private MFXPasswordField txt_answer;
    @FXML
    private Label lbl_validationAnswer;

    public AppQuestionPopupController(Stage popupStage, Object model) {
        super(popupStage, (AppQuestion)model);
    }

    @Override
    protected void validatedFieldsSetup() {
        ValidationUtil.addRequiredConstraint(txt_question, lbl_validationQuestion);
        ValidationUtil.addRequiredConstraint(txt_answer, lbl_validationAnswer);

        ValidationUtil.addTextLimit(txt_question, 255);
        ValidationUtil.addTextLimit(txt_answer, 51);

        validatedFields.put(txt_question, lbl_validationQuestion);
        validatedFields.put(txt_answer, lbl_validationAnswer);
    }

    @Override
    protected void runLaterOnInitialize() {
        txt_question.requestFocus();
        if (isNew()) {
            txt_title.setText(Strings.Labels.NEW_APP_QUESTION);
            txt_title.setVisible(true);
        } else {
            txt_title.setText(Strings.Labels.EDIT_APP_QUESTION);
            txt_title.setVisible(true);
            txt_question.textProperty().set(model.getText());
            txt_answer.textProperty().set(EncryptUtil.decrypt(model.getAnswer()));
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
            model.setText(oldModel.getText());
            model.setAnswer(oldModel.getAnswer());
            model.setLblMaskedAnswer(oldModel.getLblMaskedAnswer());
            model.setActionIconsContainer(oldModel.getActionIconsContainer());
        }
    }

    @Override
    protected String retrieveSaveErrorMessage(Exception e, boolean isNew) {
        if (e.getMessage().contains(Strings.Constraints.UNIQUE)) {
            return Strings.Errors.EXISTING_APP_QUESTION;
        } else {
            return isNew ?
                    String.format(Strings.Errors.ADDING_APP_QUESTION, e.getMessage()) :
                    String.format(Strings.Errors.UPDATING_APP_QUESTION, e.getMessage());
        }
    }

    @Override
    protected void retrieveValuesFromTextFields() {
        model.setText(txt_question.getText().trim());
        model.setAnswer(EncryptUtil.encrypt(txt_answer.getText().trim()));
    }

    @Override
    protected void save(boolean isNew) throws Exception {
        if (isNew) {
            DaoProvider.getAppQuestionDao().add(model);
        } else {
            DaoProvider.getAppQuestionDao().update(model);
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
    void onKeyPressedHandler(KeyEvent event) {
        this.closePopup(event.getCode());
    }

    @FXML
    void save(ActionEvent event) {
        saveChanges(isNew());
    }


}
