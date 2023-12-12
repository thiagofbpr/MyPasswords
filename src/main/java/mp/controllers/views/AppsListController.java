package mp.controllers.views;

import mp.constants.Views;
import mp.controllers.views.base.BaseAppController;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import mp.models.App;
import mp.utils.UserUtil;
import mp.utils.ViewLoader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class AppsListController extends BaseAppController<App> {

    private static ListChangeListener<App> listChangeListener;
    private final FontAwesomeIcon clearTextIcon = new FontAwesomeIcon();

    @FXML
    private VBox VBox_appsList;
    @FXML
    private Label lbl_validationView;
    @FXML
    private VBox VBox_Empty;
    @FXML
    private VBox VBox_Search;
    @FXML
    private MFXTextField txt_search;
    @FXML
    private TableView<App> table_apps;
    @FXML
    private TableColumn<App, Image> col_appLogo;
    @FXML
    private TableColumn<App, String> col_appName;

    public AppsListController(Object model) {
        super((App)model);
    }

    @Override
    protected void runOnInitialize() {
        bindVBoxDimensions();
        switchVBoxIfNoApp();
        setSearchIcon();
        setClearTextIcon();
        tableAppsSetup();
        appsListSetup();
        startListListener();
    }

    @Override
    protected void runLaterOnInitialize() {
        txt_search.requestFocus();
    }

    @Override
    protected void hideAll() {
        // Not implemented
    }

    @Override
    protected void showAll() {
        // Not implemented
    }

    /*************************************
     * Private methods
     **************************************/
    private void bindVBoxDimensions() {
        VBox_appsList.prefHeightProperty().bind(ViewLoader.getSecondaryFrame().heightProperty());
    }

    private void startListListener() {
        if (listChangeListener != null) {
            getObservableList().removeListener(listChangeListener);
        }
        setListChangeHandler();
        getObservableList().addListener(listChangeListener);
    }

    private void setListChangeHandler() {
        listChangeListener = change -> handleAppsListChange(wasAppAdded(change), change.wasPermutated(), change.wasRemoved());
    }

    private ObservableList<App> getObservableList() {
        return UserUtil.CURRENT_USER.appListProperty();
    }

    private boolean wasAppAdded(ListChangeListener.Change<? extends App> change) {
        return change.next() && change.wasAdded();
    }

    private void switchVBoxIfNoApp() {
        if (appListHasElements()) {
            VBox_appsList.toFront();
        } else {
            VBox_Empty.toFront();
        }
    }

    private boolean appListHasElements() {
        return UserUtil.CURRENT_USER.getApps().size() > 0;
    }

    private void handleAppsListChange(boolean wasAppAdded, boolean wasPermutated, boolean wasAppRemoved) {
        if (wasAppAdded) {
            app = UserUtil.CURRENT_USER.getApps()
                    .stream()
                    .sorted(Comparator.comparing(App::getId, Comparator.reverseOrder()))
                    .findFirst()
                    .get();
        } else if (wasPermutated && app != null) {
            showAppView();
        } else if (wasAppRemoved) {
            showAppslist();
        }
    }

    private void showAppslist() {
        ViewLoader.loadView(
                Views.APPS_LIST_VIEW,
                AppsListController.class,
                null,
                lbl_validationView,
                ViewLoader.getSecondaryFrame());
    }

    private void setSearchIcon() {
        FontAwesomeIcon searchIcon = new FontAwesomeIcon();
        searchIcon.setIcon(FontAwesomeIcons.SEARCH);
        searchIcon.fillProperty().set(Color.rgb(74,72,72));
        txt_search.setLeadingIcon(searchIcon);
    }

    private void setClearTextIcon() {
        clearTextIcon.setIcon(FontAwesomeIcons.TIMES_CIRCLE);
        clearTextIcon.fillProperty().set(Color.rgb(74,72,72));

        clearTextIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            txt_search.clear();
            txt_search.setTrailingIcon(null);
        });
    }

    private void tableAppsSetup() {
        tableSetup();
        appNameColumnSetup();
        appLogoColumnSetup();
    }

    private void tableSetup() {
        table_apps.setPlaceholder(new Label("No items found."));
        table_apps.setRowFactory(tv -> {
            TableRow<App> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY) {
                    app = row.getItem();
                    showAppView();
                }
            });
            return row ;
        });
    }

    private void appNameColumnSetup() {
        col_appName.prefWidthProperty().bind(table_apps.widthProperty().multiply(0.73));
        col_appName.setCellValueFactory(cellData -> cellData.getValue().nameAndUrlProperty());
        col_appName.setCellFactory(column -> {
            TableCell<App, String> cell = new TableCell<App, String>() {
                @Override
                protected void updateItem(String appNameAndUrl, boolean empty) {
                    super.updateItem(appNameAndUrl, empty);
                    if (appNameAndUrl == null || empty) {
                        setGraphic(null);
                    } else {
                        VBox vbox = new VBox();
                        vbox.setAlignment(Pos.CENTER_LEFT);

                        List<String> textList = Arrays.asList(appNameAndUrl.split(";;"));

                        Label lblNom = new Label(textList.get(0));
                        lblNom.setStyle("-fx-font-size: 16px;-fx-text-fill: #4a4848;");
                        vbox.getChildren().add(lblNom);

                        if (textList.size() == 2) {
                            Label lblUrl = new Label(textList.get(1));
                            lblUrl.setStyle("-fx-text-fill: #0096FF;");
                            vbox.getChildren().add(lblUrl);
                        }

                        setGraphic(vbox);
                    }
                }
            };
            cell.setAlignment(Pos.CENTER_LEFT);
            return cell;
        });
    }

    private void appLogoColumnSetup() {
        col_appLogo.prefWidthProperty().bind(table_apps.widthProperty().multiply(0.2));
        col_appLogo.setCellValueFactory(cellData -> cellData.getValue().logoProperty());
        col_appLogo.setCellFactory(column -> {
            TableCell<App, Image> cell = new TableCell<App, Image>() {
                public void updateItem(Image logo, boolean empty) {
                    super.updateItem(logo, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        ImageView imageview = new ImageView();
                        imageview.setImage(logo);
                        imageview.setFitHeight(24);
                        imageview.setFitWidth(24);
                        setGraphic(imageview);
                    }
                }
            };
            cell.setAlignment(Pos.CENTER);
            return cell;
        });
    }

    private void appsListSetup() {
        table_apps.setItems(UserUtil.CURRENT_USER.getApps());
        FilteredList<App> filteredData = new FilteredList<>(UserUtil.CURRENT_USER.getApps(), b -> true);

        addtextSearchListenner(filteredData);
        addTextSearchEventHandler();

        SortedList<App> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table_apps.comparatorProperty());
        table_apps.setItems(sortedData);
    }

    private void addTextSearchEventHandler() {
        txt_search.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                txt_search.setText("");
            }
        });
    }

    private void addtextSearchListenner(FilteredList<App> filteredData) {
        txt_search.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(app -> {
                if (newValue.isEmpty() || newValue.isBlank()) {
                    txt_search.setTrailingIcon(null);
                    return true;
                } else {
                    txt_search.setTrailingIcon(clearTextIcon);
                    String searchKeyword = newValue.toLowerCase();
                    return app.getName().toLowerCase().contains(searchKeyword);
                }
            });
        });
    }

    private void showAppView() {
        ViewLoader.loadView(
                Views.APP_VIEW,
                AppController.class,
                app,
                lbl_validationView,
                ViewLoader.getSecondaryFrame());
    }


}
