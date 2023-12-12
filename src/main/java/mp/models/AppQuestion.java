package mp.models;

import javafx.beans.property.*;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import mp.utils.EncryptUtil;

public class AppQuestion {
    private IntegerProperty id = new SimpleIntegerProperty(this, "id");
    private StringProperty text = new SimpleStringProperty(this, "text");
    private StringProperty answer = new SimpleStringProperty(this, "answer");
    private ObjectProperty<App> app = new SimpleObjectProperty<>(this, "app");
    private ObjectProperty<Label> lblMaskedAnswer = new SimpleObjectProperty<>(this, "lblMaskedAnswer");
    private ObjectProperty<HBox> actionIconsContainer = new SimpleObjectProperty<>(this, "actionIconsContainer");

    public AppQuestion(String text, String answer) {
        this.setText(text);
        this.setAnswer(answer);
        this.setLblMaskedAnswer(new Label(EncryptUtil.getBlackBullets()));
        this.setActionIconsContainer(new HBox());
    }

    public AppQuestion(int id, String text, String answer) {
        this.setId(id);
        this.setText(text);
        this.setAnswer(answer);
        this.setLblMaskedAnswer(new Label(EncryptUtil.getBlackBullets()));
        this.setActionIconsContainer(new HBox());
    }

    public AppQuestion(AppQuestion appQuestion) {
        this.setText(appQuestion.getText());
        this.setAnswer(appQuestion.getAnswer());
        this.setLblMaskedAnswer(appQuestion.getLblMaskedAnswer());
        this.setActionIconsContainer(appQuestion.getActionIconsContainer());
    }

    public AppQuestion(App app) {
        this.setApp(app);
        this.setLblMaskedAnswer(new Label(EncryptUtil.getBlackBullets()));
        this.setActionIconsContainer(new HBox());
    }

    public int getId() {
        return id.get();
    }
    public IntegerProperty idProperty() {
        return id;
    }
    public void setId(int id) {
        this.id.set(id);
    }

    public String getText() {
        return text.get();
    }
    public StringProperty textProperty() {
        return text;
    }
    public void setText(String text) {
        this.text.set(text);
    }

    public String getAnswer() {
        return answer.get();
    }
    public StringProperty answerProperty() {
        return answer;
    }
    public void setAnswer(String answer) {
        this.answer.set(answer);
    }

    public App getApp() {
        return app.get();
    }
    public ObjectProperty<App> appProperty() {
        return app;
    }
    public void setApp(App app) {
        this.app.set(app);
    }

    public Label getLblMaskedAnswer() {
        return lblMaskedAnswer.get();
    }
    public ObjectProperty<Label> lblMaskedAnswerProperty() {
        return lblMaskedAnswer;
    }
    public void setLblMaskedAnswer(Label lblMaskedAnswer) {
        this.lblMaskedAnswer.set(lblMaskedAnswer);
    }

    public HBox getActionIconsContainer() {
        return actionIconsContainer.get();
    }
    public ObjectProperty<HBox> actionIconsContainerProperty() {
        return actionIconsContainer;
    }
    public void setActionIconsContainer(HBox actionIconsContainer) {
        this.actionIconsContainer.set(actionIconsContainer);
    }


}
