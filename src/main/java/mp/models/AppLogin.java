package mp.models;

import javafx.beans.property.*;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import mp.utils.EncryptUtil;
import java.time.ZonedDateTime;

public class AppLogin {
    private IntegerProperty id = new SimpleIntegerProperty(this, "id");
    private StringProperty username = new SimpleStringProperty(this, "username");
    private StringProperty password = new SimpleStringProperty(this, "password");
    private ObjectProperty<ZonedDateTime> createTime = new SimpleObjectProperty<>(this, "createTime");
    private ObjectProperty<ZonedDateTime> updateTime = new SimpleObjectProperty<>(this, "updateTime");
    private ObjectProperty<ZonedDateTime> endTime = new SimpleObjectProperty<>(this, "endTime");
    private ObjectProperty<App> app = new SimpleObjectProperty<>(this, "app");
    private ObjectProperty<Label> lblMaskedPassword = new SimpleObjectProperty<>(this, "lblMaskedPassword");
    private ObjectProperty<HBox> actionIconsContainer = new SimpleObjectProperty<>(this, "actionIconsContainer");

    public AppLogin(String username, String password) {
        this.setUsername(username);
        this.setPassword(password);
        this.setLblMaskedPassword(new Label(EncryptUtil.getBlackBullets()));
        this.setActionIconsContainer(new HBox());
    }

    public AppLogin(App app) {
        this.setApp(app);
        this.setLblMaskedPassword(new Label(EncryptUtil.getBlackBullets()));
        this.setActionIconsContainer(new HBox());
    }

    public AppLogin(AppLogin appLogin) {
        this.setUsername(appLogin.getUsername());
        this.setPassword(appLogin.getPassword());
        this.setCreateTime(appLogin.getCreateTime());
        this.setUpdateTime(appLogin.getUpdateTime());
        this.setEndTime(appLogin.getEndTime());
        this.setLblMaskedPassword(appLogin.getLblMaskedPassword());
        this.setActionIconsContainer(appLogin.getActionIconsContainer());
    }

    public AppLogin(int id, String username, String password, ZonedDateTime createTime, ZonedDateTime updateTime, ZonedDateTime endTime) {
        this.setId(id);
        this.setUsername(username);
        this.setPassword(password);
        this.setCreateTime(createTime);
        this.setUpdateTime(updateTime);
        this.setEndTime(endTime);
        this.setLblMaskedPassword(new Label(EncryptUtil.getBlackBullets()));
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

    public String getUsername() {
        return username.get();
    }
    public StringProperty usernameProperty() {
        return username;
    }
    public void setUsername(String username) {
        this.username.set(username);
    }

    public String getPassword() {
        return password.get();
    }
    public StringProperty passwordProperty() {
        return password;
    }
    public void setPassword(String password) {
        this.password.set(password);
    }

    public Label getLblMaskedPassword() {
        return lblMaskedPassword.get();
    }
    public ObjectProperty<Label> lblMaskedPasswordProperty() {
        return lblMaskedPassword;
    }
    public void setLblMaskedPassword(Label lblMaskedPassword) {
        this.lblMaskedPassword.set(lblMaskedPassword);
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

    public ZonedDateTime getCreateTime() {
        return createTime.get();
    }
    public ObjectProperty<ZonedDateTime> createTimeProperty() {
        return createTime;
    }
    public void setCreateTime(ZonedDateTime createTime) {
        this.createTime.set(createTime);
    }

    public ZonedDateTime getUpdateTime() {
        return updateTime.get();
    }
    public ObjectProperty<ZonedDateTime> updateTimeProperty() {
        return updateTime;
    }
    public void setUpdateTime(ZonedDateTime updateTime) {
        this.updateTime.set(updateTime);
    }

    public ZonedDateTime getEndTime() {
        return endTime.get();
    }
    public ObjectProperty<ZonedDateTime> endTimeProperty() {
        return endTime;
    }
    public void setEndTime(ZonedDateTime endTime) {
        this.endTime.set(endTime);
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


}
