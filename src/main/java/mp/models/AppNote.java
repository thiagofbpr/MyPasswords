package mp.models;

import javafx.beans.property.*;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import mp.utils.EncryptUtil;

public class AppNote {

    private IntegerProperty id = new SimpleIntegerProperty(this, "id");
    private StringProperty description = new SimpleStringProperty(this, "description");
    private StringProperty value = new SimpleStringProperty(this, "value");
    private ObjectProperty<App> app = new SimpleObjectProperty<>(this, "app");
    private ObjectProperty<Label> lblMaskedValue = new SimpleObjectProperty<>(this, "lblMaskedValue");
    private ObjectProperty<HBox> actionIconsContainer = new SimpleObjectProperty<>(this, "actionIconsContainer");

    public AppNote(String description, String value) {
        this.setDescription(description);
        this.setValue(value);
        this.setLblMaskedValue(new Label(EncryptUtil.getBlackBullets()));
        this.setActionIconsContainer(new HBox());
    }

    public AppNote(int id, String description, String value) {
        this.setId(id);
        this.setDescription(description);
        this.setValue(value);
        this.setLblMaskedValue(new Label(EncryptUtil.getBlackBullets()));
        this.setActionIconsContainer(new HBox());
    }

    public AppNote(AppNote appNote) {
        this.setDescription(appNote.getDescription());
        this.setValue(appNote.getValue());
        this.setLblMaskedValue(appNote.getLblMaskedValue());
        this.setActionIconsContainer(appNote.getActionIconsContainer());
    }

    public AppNote(App app) {
        this.setApp(app);
        this.setLblMaskedValue(new Label(EncryptUtil.getBlackBullets()));
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

    public String getDescription() {
        return description.get();
    }
    public StringProperty descriptionProperty() {
        return description;
    }
    public void setDescription(String description) {
        this.description.set(description);
    }

    public String getValue() {
        return value.get();
    }
    public StringProperty valueProperty() {
        return value;
    }
    public void setValue(String value) {
        this.value.set(value);
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

    public Label getLblMaskedValue() {
        return lblMaskedValue.get();
    }
    public ObjectProperty<Label> lblMaskedValueProperty() {
        return lblMaskedValue;
    }
    public void setLblMaskedValue(Label lblMaskedValue) {
        this.lblMaskedValue.set(lblMaskedValue);
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
