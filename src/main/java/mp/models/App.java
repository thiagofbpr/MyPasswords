package mp.models;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.time.ZonedDateTime;

public class App implements Comparable<App> {
    private IntegerProperty id = new SimpleIntegerProperty(this, "id");
    private StringProperty name = new SimpleStringProperty(this, "name");
    private StringProperty url = new SimpleStringProperty(this, "url");
    private ObjectProperty<Image> logo = new SimpleObjectProperty<>(this, "logo");
    private ListProperty<AppNote> appNotes = new SimpleListProperty<>(FXCollections.observableArrayList());
    private ListProperty<AppLogin> appLogins = new SimpleListProperty<>(FXCollections.observableArrayList());
    private ListProperty<AppQuestion> appQuestions = new SimpleListProperty<>(FXCollections.observableArrayList());
    private ObjectProperty<User> user = new SimpleObjectProperty<>(this, "user");
    private StringProperty nameAndUrl = new SimpleStringProperty(this, "nameAndUrl");
    private final ObjectProperty<ImageView> logoImageView = new SimpleObjectProperty<>();

    public App() {
        setName("");
        setUrl("");
    }

    public App(int id, String name, String url, Image logo) {
        setId(id);
        setName(name);
        setUrl(url);
        setLogo(logo);
        setLogoImageView(new ImageView(getLogo()));
        getLogoImageView().imageProperty().bind(logoProperty());
    }

    public App(App app) {
        setName(app.getName());
        setUrl(app.getUrl());
        setLogo(app.getLogo());
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

    public String getName() {
        return name.get();
    }
    public StringProperty nameProperty() {
        return name;
    }
    public void setName(String name) {
        this.name.set(name);
        this.nameAndUrl.set(name + ";;" + getUrl());
    }

    public String getUrl() {
        return url.get();
    }
    public StringProperty urlProperty() {
        return url;
    }
    public void setUrl(String url) {
        this.url.set(url);
        this.nameAndUrl.set(getName() + ";;" + url);
    }

    public Image getLogo() {
        return logo.get();
    }
    public ObjectProperty<Image> logoProperty() {
        return logo;
    }
    public void setLogo(Image logo) {
        this.logo.set(logo);
    }

    public ObservableList<AppLogin> getAppLogins() {
        return appLogins.get();
    }
    public void setAppLogins(ObservableList<AppLogin> appLogins) {
        this.appLogins.set(appLogins);
    }
    public ListProperty<AppLogin> appLoginsListProperty() {
        return this.appLogins;
    }

    public ObservableList<AppQuestion> getAppQuestions() {
        return appQuestions.get();
    }
    public ListProperty<AppQuestion> appQuestionsListProperty() {
        return appQuestions;
    }
    public void setAppQuestions(ObservableList<AppQuestion> appQuestions) {
        this.appQuestions.set(appQuestions);
    }

    public ObservableList<AppNote> getAppNotes() {
        return appNotes.get();
    }
    public ListProperty<AppNote> appNotesProperty() {
        return appNotes;
    }
    public void setAppNotes(ObservableList<AppNote> appNotes) {
        this.appNotes.set(appNotes);
    }

    public User getUser() {
        return user.get();
    }
    public ObjectProperty<User> userProperty() {
        return user;
    }
    public void setUser(User user) {
        this.user.set(user);
    }

    public void setEndTimeToAppLogin(int appLoginId, ZonedDateTime endtime) {
        this.getAppLogins()
                .stream()
                .filter(app -> app.getId() == appLoginId)
                .findFirst()
                .get().setEndTime(endtime);
    }

    public StringProperty nameAndUrlProperty() {
        return nameAndUrl;
    }
    public String getNameAndUrl() {
        return nameAndUrl.get();
    }

    public ImageView getLogoImageView() {
        return logoImageView.get();
    }
    public ObjectProperty<ImageView> logoImageViewProperty() {
        return logoImageView;
    }
    public void setLogoImageView(ImageView logoImageView) {
        this.logoImageView.set(logoImageView);
    }

    @Override
    public int compareTo(App o) {
        return this.getName().compareToIgnoreCase(o.getName());
    }


}
