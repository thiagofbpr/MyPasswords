package mp.controllers.views;

import mp.constants.Strings;
import mp.constants.Views;
import mp.controllers.popups.AppNotePopupController;
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
import mp.models.App;
import mp.models.AppNote;
import mp.utils.*;

import java.sql.SQLException;

public class AppNotesController extends BaseAppController<AppNote> {

    private static ListChangeListener<AppNote> listChangeListener;

    @FXML
    private HBox HBox_appNotesAllBtn;
    @FXML
    private Label lbl_validationView;
    @FXML
    private HBox HBox_AppNotesNewBtnOnly;
    @FXML
    private VBox VBox_EmptyAppNotes;
    @FXML
    private VBox VBox_AppNotes;
    @FXML
    private TableView<AppNote> table_appNotes;
    @FXML
    private TableColumn<AppNote, Label> col_note;
    @FXML
    private TableColumn<AppNote, HBox> col_actions;


    public AppNotesController(Object model) {
        super((App)model);
    }

    @Override
    protected void runOnInitialize() {
        tableAppNotesSetup();
        switchVBoxesIfNoAppNotes();
        switchBtnHBoxesIfNeeded();
        startListListener();
    }

    @Override
    protected void runLaterOnInitialize() {
        hideAll();
    }

    @Override
    protected void hideAll() {
        for (int i = 0 ; i < table_appNotes.getItems().size() ; i++) {
            AppNote appNote = table_appNotes.getItems().get(i);
            setNewIcon(appNote, FontAwesomeIcons.EYE, table_appNotes, 1);
            hideText(appNote.getLblMaskedValue());
        }
    }

    @Override
    protected void showAll() {
        for (int i = 0 ; i < table_appNotes.getItems().size() ; i++) {
            AppNote appNote = table_appNotes.getItems().get(i);
            setNewIcon(appNote, FontAwesomeIcons.EYE_SLASH, table_appNotes, 1);
            showText(appNote.getLblMaskedValue(), appNote.getValue());
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
            switchVBoxesIfNoAppNotes();
            switchBtnHBoxesIfNeeded();
        };
    }

    private ObservableList<AppNote> getObservableList() {
        return app.appNotesProperty();
    }

    private void tableAppNotesSetup() {
        tableSetup();
        noteColumnSetup();
        actionsColumnSetup();
    }

    private void tableSetup() {
        table_appNotes.setItems(app.getAppNotes());
        table_appNotes.getColumns().forEach(column -> column.setReorderable(false));
    }

    private void noteColumnSetup() {
        col_note.setResizable(false);
        col_note.setSortable(false);
        col_note.prefWidthProperty().bind(table_appNotes.widthProperty().multiply(0.74));
        col_note.setCellValueFactory(cellData -> cellData.getValue().lblMaskedValueProperty());
        col_note.setCellFactory(column -> {
            TableCell<AppNote, Label> cell = new TableCell<>() {
                @Override
                protected void updateItem(Label label, boolean empty) {
                    super.updateItem(label, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        VBox vbox = new VBox();
                        vbox.setAlignment(Pos.CENTER);

                        AppNote selectedAppNote = getTableView().getItems().get(getIndex());

                        Label lblDescription = new Label(selectedAppNote.getDescription());
                        lblDescription.setStyle("-fx-font-weight: bold;-fx-max-width:220px;-fx-alignment:center;");

                        selectedAppNote.getLblMaskedValue().setStyle("-fx-max-width:220px;-fx-alignment:center;");

                        VBox.setMargin(lblDescription, new Insets(0, 0, 5, 0));
                        vbox.getStyleClass().add("note");
                        vbox.getChildren().addAll(lblDescription, selectedAppNote.getLblMaskedValue());

                        setGraphic(vbox);
                    }
                }
            };
            cell.setAlignment(Pos.CENTER);
            return cell;
        });
    }

    private void actionsColumnSetup() {
        col_actions.setResizable(false);
        col_actions.setSortable(false);
        col_actions.prefWidthProperty().bind(table_appNotes.widthProperty().multiply(0.20));
        col_actions.setCellValueFactory(cellData -> cellData.getValue().actionIconsContainerProperty());
        col_actions.setCellFactory(column -> {
            TableCell<AppNote, HBox> cell = new TableCell<>() {
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
                            FontAwesomeIcon deleteIcon = ImageUtil.createIcon(FontAwesomeIcons.TRASH, true);

                            HBox.setMargin(eyeIcon, new Insets(0, 7, 0, 0));
                            HBox.setMargin(copyIcon, new Insets(0, 7, 0, 0));
                            HBox.setMargin(editIcon, new Insets(0, 7, 0, 0));

                            AppNote selectedAppNote = getTableView().getItems().get(getIndex());

                            eyeIcon.setOnMouseClicked((MouseEvent event) -> {
                                showOrHideValue(selectedAppNote, (FontAwesomeIcon) event.getSource());
                            });

                            copyIcon.setOnMouseClicked((MouseEvent event) -> {
                                copyValueToClipboard(selectedAppNote, (FontAwesomeIcon) event.getSource());
                            });

                            editIcon.setOnMouseClicked((MouseEvent event) -> {
                                eyeIcon.setIcon(FontAwesomeIcons.EYE);
                                hideText(selectedAppNote.getLblMaskedValue());
                                editAppNote(selectedAppNote);
                            });

                            deleteIcon.setOnMouseClicked((MouseEvent event) -> {
                                showDeleteDialog(selectedAppNote);
                            });

                            hbox.getChildren().addAll(eyeIcon, copyIcon, editIcon, deleteIcon);
                        }
                        setGraphic(hbox);
                    }
                }
            };
            cell.setAlignment(Pos.CENTER);
            return cell;
        });
    }

    private void showOrHideValue(AppNote appNote, FontAwesomeIcon icon) {
        showOrHideText(appNote.getLblMaskedValue(), appNote.getValue(), icon);
    }

    private void copyValueToClipboard(AppNote appNote, FontAwesomeIcon icon) {
        copyTextToClipboard(appNote.getValue(), true, icon);
    }

    private void switchVBoxesIfNoAppNotes() {
        switchVBoxesIfNeeded(app.getAppNotes().size(), VBox_AppNotes, VBox_EmptyAppNotes);
    }

    private void switchBtnHBoxesIfNeeded() {
        switchBtnHBoxesIfNeeded(app.getAppNotes().size(), HBox_appNotesAllBtn, HBox_AppNotesNewBtnOnly);
    }

    private void editAppNote(AppNote appNote) {
        ViewLoader.loadPopup(
                Views.APP_NOTE_POPUP,
                AppNotePopupController.class,
                lbl_validationView,
                appNote);
        table_appNotes.refresh();
    }

    private void showDeleteDialog(AppNote appNote) {
        ViewLoader.showDialog(
                Strings.Dialog.CONFIRM_DELETE_APP_NOTE,
                () -> deleteAppNote(appNote),
                lbl_validationView);
    }

    private void deleteAppNote(AppNote appNote) {
        try {
            DaoProvider.getAppNoteDao().delete(appNote);
        } catch (SQLException e) {
            String errorMessage = String.format(Strings.Errors.DELETING_APP_NOTE, e.getMessage());
            new ExceptionMessage.Builder(errorMessage, lbl_validationView).show();
            LOGGER.severe(String.format("%s - %s", LOGGER.getName(), errorMessage));
            e.printStackTrace();
        }
    }

    /*************************************
     * FXML methods
     **************************************/
    @FXML
    void newAppNote(MouseEvent event) {
        ViewLoader.loadPopup(
                Views.APP_NOTE_POPUP,
                AppNotePopupController.class,
                lbl_validationView,
                new AppNote(app));
                table_appNotes.refresh();
    }

    @FXML
    void showOrHideAllValues(MouseEvent event) {
        showOrHideAll((Label) event.getSource(), table_appNotes);
    }


}
