package mp.models;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.ZonedDateTime;

public class User {

    private IntegerProperty id = new SimpleIntegerProperty(this, "id");
    private StringProperty fullname= new SimpleStringProperty(this, "fullname");
    private StringProperty username= new SimpleStringProperty(this, "username");
    private StringProperty password = new SimpleStringProperty(this, "password");
    private ObjectProperty<ZonedDateTime> updatetime = new SimpleObjectProperty<>(this, "updatetime");
    private ObjectProperty<ZonedDateTime> lastConnection = new SimpleObjectProperty<>(this, "lastConnection");
    private ListProperty<App> apps = new SimpleListProperty<>(FXCollections.observableArrayList());

    public User(String fullname, String username, String password) {
        this.setFullname(fullname);
        this.setUsername(username);
        this.setPassword(password);
    }

    public User(int id, String fullname, String username, String password, ZonedDateTime updatetime, ZonedDateTime lastConnection) {
        this.setId(id);
        this.setFullname(fullname);
        this.setUsername(username);
        this.setPassword(password);
        this.setUpdatetime(updatetime);
        this.setLastConnection(lastConnection);
    }

    public User(User user) {
        this.setFullname(user.getFullname());
        this.setUsername(user.getUsername());
        this.setPassword(user.getPassword());
        this.setUpdatetime(user.getUpdatetime());
        this.setLastConnection(user.getLastConnection());
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

    public String getFullname() {
        return fullname.get();
    }
    public StringProperty fullnameProperty() {
        return fullname;
    }
    public void setFullname(String fullname) {
        this.fullname.set(fullname);
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

    public ZonedDateTime getUpdatetime() {
        return updatetime.get();
    }
    public ObjectProperty<ZonedDateTime> updatetimeProperty() {
        return updatetime;
    }
    public void setUpdatetime(ZonedDateTime updatetime) {
        this.updatetime.set(updatetime);
    }

    public ZonedDateTime getLastConnection() {
        return lastConnection.get();
    }
    public ObjectProperty<ZonedDateTime> lastConnectionProperty() {
        return lastConnection;
    }
    public void setLastConnection(ZonedDateTime lastConnection) {
        this.lastConnection.set(lastConnection);
    }

    public ObservableList<App> getApps() {
        return apps.get();
    }
    public void setApps(ObservableList<App> apps) {
        this.apps.set(apps);
    }
    public ListProperty<App> appListProperty() {
        return this.apps;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User otherUser = (User) o;
        return username.equals(otherUser.username) && password.equals(otherUser.password) && fullname.equals(otherUser.fullname);
    }
}
