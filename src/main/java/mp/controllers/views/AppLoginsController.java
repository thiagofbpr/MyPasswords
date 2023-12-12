package mp.controllers.views;

import mp.constants.Strings;
import mp.constants.Views;
import mp.controllers.popups.AppLoginInfoPopupController;
import mp.controllers.popups.AppLoginPopupController;
import mp.controllers.views.base.BaseAppController;
import mp.dao.DaoProvider;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import mp.models.App;
import mp.models.AppLogin;
import mp.utils.ExceptionMessage;
import mp.utils.ImageUtil;
import mp.utils.ViewLoader;
import java.sql.SQLException;
import java.time.ZonedDateTime;

public class AppLoginsController extends BaseAppController<AppLogin> {

    private static ListChangeListener<AppLogin> listChangeListener;

    @FXML
    private HBox HBox_appLoginsAllBtn;
    @FXML
    private Label lbl_validationView;
    @FXML
    private HBox HBox_AppLoginsNewBtnOnly;
    @FXML
    private TableView<AppLogin> table_appLogins;
    @FXML
    private TableColumn<AppLogin, ZonedDateTime> col_status;
    @FXML
    private TableColumn<AppLogin, String> col_username;
    @FXML
    private TableColumn<AppLogin, String> col_copyUsername;
    @FXML
    private TableColumn<AppLogin, Label> col_password;
    @FXML
    private TableColumn<AppLogin, HBox> col_actions;
    @FXML
    private VBox VBox_EmptyAppLogins;
    @FXML
    private VBox VBox_AppLogins;

    public AppLoginsController(Object model) {
        super((App)model);
    }

    @Override
    protected void runOnInitialize() {
        tableAppLoginsSetup();
        switchVBoxesIfNoAppLogin();
        switchBtnHBoxesIfNeeded();
        startListListener();
    }

    @Override
    protected void runLaterOnInitialize() {
        hideAll();
    }

    @Override
    protected void hideAll() {
        for (int i = 0 ; i < table_appLogins.getItems().size() ; i++) {
            AppLogin appLogin = table_appLogins.getItems().get(i);
            setNewIcon(appLogin, FontAwesomeIcons.EYE, table_appLogins, 4);
            hideText(appLogin.getLblMaskedPassword());
        }
    }

    @Override
    protected void showAll() {
        for (int i = 0 ; i < table_appLogins.getItems().size() ; i++) {
            AppLogin appLogin = table_appLogins.getItems().get(i);
            setNewIcon(appLogin, FontAwesomeIcons.EYE_SLASH, table_appLogins, 4);
            showText(appLogin.getLblMaskedPassword(), appLogin.getPassword());
        }
    }

    /*************************************
     * Private methods
     **************************************/
    private void startListListener() {
        if (listChangeListener != null) {
            getObservableList().removeListener(listChangeListener);
        }
        setListChangeHandler();
        getObservableList().addListener(listChangeListener);
    }

    private void setListChangeHandler() {
        listChangeListener = change -> {
            switchVBoxesIfNoAppLogin();
            switchBtnHBoxesIfNeeded();
        };
    }

    private ObservableList<AppLogin> getObservableList() {
        return app.appLoginsListProperty();
    }

    private void tableAppLoginsSetup() {
        tableSetup();
        pwdStatusColumnSetup();
        pwdUsernameColumnSetup();
        pwdCopyUsernameColumnSetup();
        pwdColumnSetup();
        pwdActionsColumnSetup();
    }

    private void tableSetup() {
        table_appLogins.setItems(app.getAppLogins());
        table_appLogins.getColumns().forEach(column -> column.setReorderable(false));
    }

    private void pwdStatusColumnSetup() {
        col_status.setStyle("-fx-padding: 0 5 0 0;");
        col_status.setResizable(false);
        col_status.setSortable(false);
        col_status.prefWidthProperty().bind(table_appLogins.widthProperty().multiply(0.1));
        col_status.setCellValueFactory(cellData -> cellData.getValue().endTimeProperty());
        col_status.setCellFactory(column -> {
            TableCell<AppLogin, ZonedDateTime> cell = new TableCell<AppLogin, ZonedDateTime>() {
                @Override
                protected void updateItem(ZonedDateTime endTime, boolean empty) {
                    super.updateItem(endTime, empty);

                    FontAwesomeIcon statusIcon = ImageUtil.createIcon(FontAwesomeIcons.CIRCLE, false);

                    if (endTime != null) {
                        statusIcon.fillProperty().set(Color.valueOf("#aaacad"));
                    } else {
                        statusIcon.fillProperty().set(Color.valueOf("#64c987"));
                    }

                    if (!empty) {
                        setGraphic(statusIcon);
                    } else {
                        setGraphic(null);
                    }
                }
            };
            cell.setAlignment(Pos.CENTER_RIGHT);
            return cell;
        });
    }

    private void pwdUsernameColumnSetup() {
        col_username.setResizable(false);
        col_username.setSortable(false);
        col_username.prefWidthProperty().bind(table_appLogins.widthProperty().multiply(0.24));
        col_username.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        col_username.setCellFactory(column -> {
            TableCell<AppLogin, String> cell = new TableCell<AppLogin, String>() {
                @Override
                protected void updateItem(String username, boolean empty) {
                    super.updateItem(username, empty);
                    Label lblUsername = new Label();
                    if (username != null) {
                        lblUsername.setText(username);
                    }
                    setGraphic(lblUsername);
                }
            };
            cell.setAlignment(Pos.CENTER);
            return cell;
        });
    }

    private void pwdCopyUsernameColumnSetup() {
        col_copyUsername.setStyle("-fx-padding: 0 5 0 0;");
        col_copyUsername.setResizable(false);
        col_copyUsername.setSortable(false);
        col_copyUsername.prefWidthProperty().bind(table_appLogins.widthProperty().multiply(0.07));
        col_copyUsername.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        col_copyUsername.setCellFactory(column -> {
            TableCell<AppLogin, String> cell = new TableCell<AppLogin, String>() {
                @Override
                protected void updateItem(String username, boolean empty) {
                    super.updateItem(username, empty);

                    if (empty) {
                        setGraphic(null);
                    } else {
                        FontAwesomeIcon copyIcon = ImageUtil.createIcon(FontAwesomeIcons.COPY, true);

                        HBox.setMargin(copyIcon, new Insets(0, 7, 0, 0));

                        AppLogin selectedAppLogin = getTableView().getItems().get(getIndex());

                        copyIcon.setOnMouseClicked((MouseEvent event) -> {
                            copyUsernameToClipboard(selectedAppLogin, (FontAwesomeIcon) event.getSource());
                        });

                        setGraphic(copyIcon);
                    }
                }
            };
            cell.setAlignment(Pos.CENTER);
            return cell;
        });
    }

    private void pwdColumnSetup() {
        col_password.setResizable(false);
        col_password.setSortable(false);
        col_password.prefWidthProperty().bind(table_appLogins.widthProperty().multiply(0.28));
        col_password.setCellValueFactory(cellData -> cellData.getValue().lblMaskedPasswordProperty());
        col_password.setCellFactory(column -> {
            TableCell<AppLogin, Label> cell = new TableCell<>() {
                @Override
                protected void updateItem(Label label, boolean empty) {
                    super.updateItem(label, empty);
                    setGraphic(label);
                }
            };
            cell.setAlignment(Pos.CENTER);
            return cell;
        });
    }

    private void pwdActionsColumnSetup() {
        col_actions.setResizable(false);
        col_actions.setSortable(false);
        col_actions.prefWidthProperty().bind(table_appLogins.widthProperty().multiply(0.25));
        col_actions.setCellValueFactory(cellData -> cellData.getValue().actionIconsContainerProperty());
        col_actions.setCellFactory(column -> {
            TableCell<AppLogin, HBox> cell = new TableCell<>() {
                @Override
                protected void updateItem(HBox hbox, boolean empty) {
                    super.updateItem(hbox, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        if (hbox.getChildren().size() == 0) {
                            hbox.setAlignment(Pos.CENTER);

                            FontAwesomeIcon eyeIcon = ImageUtil.createIcon(FontAwesomeIcons.EYE, true);
                            FontAwesomeIcon copyIcon = ImageUtil.createIcon(FontAwesomeIcons.COPY, true);
                            FontAwesomeIcon editIcon = ImageUtil.createIcon(FontAwesomeIcons.EDIT, true);
                            FontAwesomeIcon infoIcon = ImageUtil.createIcon(FontAwesomeIcons.INFO, true);
                            FontAwesomeIcon deleteIcon = ImageUtil.createIcon(FontAwesomeIcons.TRASH, true);

                            HBox.setMargin(eyeIcon, new Insets(0, 7, 0, 0));
                            HBox.setMargin(copyIcon, new Insets(0, 7, 0, 0));
                            HBox.setMargin(editIcon, new Insets(0, 7, 0, 0));
                            HBox.setMargin(infoIcon, new Insets(0, 7, 0, 0));

                            AppLogin selectedAppLogin = getTableView().getItems().get(getIndex());

                            eyeIcon.setOnMouseClicked((MouseEvent event) -> {
                                showOrHidePassword(selectedAppLogin, (FontAwesomeIcon) event.getSource());
                            });

                            copyIcon.setOnMouseClicked((MouseEvent event) -> {
                                copyPasswordToClipboard(selectedAppLogin, (FontAwesomeIcon) event.getSource());
                            });

                            editIcon.setOnMouseClicked((MouseEvent event) -> {
                                eyeIcon.setIcon(FontAwesomeIcons.EYE);
                                hideText(selectedAppLogin.getLblMaskedPassword());
                                editAppLogin(selectedAppLogin);
                            });

                            infoIcon.setOnMouseClicked((MouseEvent event) -> {
                                showAppLoginInfo(selectedAppLogin);
                            });

                            deleteIcon.setOnMouseClicked((MouseEvent event) -> {
                                showDeleteDialog(selectedAppLogin);
                            });

                            hbox.getChildren().addAll(eyeIcon, copyIcon, editIcon, infoIcon, deleteIcon);
                        }
                        setGraphic(hbox);
                    }
                }
            };
            cell.setAlignment(Pos.CENTER);
            return cell;
        });
    }

    private void showAppLoginInfo(AppLogin appLogin) {
        ViewLoader.loadPopup(
                Views.APP_LOGIN_INFO_POPUP,
                AppLoginInfoPopupController.class,
                lbl_validationView,
                appLogin);
    }

    private void showDeleteDialog(AppLogin appLogin) {
        ViewLoader.showDialog(
                Strings.Dialog.CONFIRM_DELETE_APP_LOGIN,
                () -> deleteAppLogin(appLogin),
                lbl_validationView);
    }

    private void deleteAppLogin(AppLogin appLogin) {
        try {
            DaoProvider.getAppLoginDao().delete(appLogin);
        } catch (SQLException e) {
            String errorMessage = String.format(Strings.Errors.DELETING_APP_LOGIN, e.getMessage());
            new ExceptionMessage.Builder(errorMessage, lbl_validationView).show();
            LOGGER.severe(String.format("%s - %s", LOGGER.getName(), errorMessage));
            e.printStackTrace();
        }
    }

    private void editAppLogin(AppLogin appLogin) {
        ViewLoader.loadPopup(
                Views.APP_LOGIN_POPUP,
                AppLoginPopupController.class,
                lbl_validationView,
                appLogin);
    }

    private void copyUsernameToClipboard(AppLogin appLogin, FontAwesomeIcon icon) {
        copyTextToClipboard(appLogin.getUsername(), false, icon);
    }

    private void showOrHidePassword(AppLogin appLogin, FontAwesomeIcon icon) {
        showOrHideText(appLogin.getLblMaskedPassword(), appLogin.getPassword(), icon);
    }

    private void copyPasswordToClipboard(AppLogin appLogin, FontAwesomeIcon icon) {
        copyTextToClipboard(appLogin.getPassword(), true, icon);
    }

    private void switchVBoxesIfNoAppLogin() {
        switchVBoxesIfNeeded(app.getAppLogins().size(), VBox_AppLogins, VBox_EmptyAppLogins);
    }

    private void switchBtnHBoxesIfNeeded() {
        switchBtnHBoxesIfNeeded(app.getAppLogins().size(), HBox_appLoginsAllBtn, HBox_AppLoginsNewBtnOnly);
    }

    /*************************************
     * FXML methods
     **************************************/
    @FXML
    void newAppLogin(MouseEvent event) {
        ViewLoader.loadPopup(
                Views.APP_LOGIN_POPUP,
                AppLoginPopupController.class,
                lbl_validationView,
                new AppLogin(app));
    }

    @FXML
    void showOrHideAllPasswords(MouseEvent event) {
        showOrHideAll((Label) event.getSource(), table_appLogins);
    }




}
