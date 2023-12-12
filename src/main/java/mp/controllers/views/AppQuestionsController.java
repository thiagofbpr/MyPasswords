package mp.controllers.views;

import mp.constants.Strings;
import mp.constants.Views;
import mp.controllers.popups.AppQuestionPopupController;
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
import mp.models.AppQuestion;
import mp.utils.ExceptionMessage;
import mp.utils.ImageUtil;
import mp.utils.ViewLoader;
import java.sql.SQLException;

public class AppQuestionsController extends BaseAppController<AppQuestion> {

    private static ListChangeListener<AppQuestion> listChangeListener;

    @FXML
    private HBox HBox_appQuestionsAllBtn;
    @FXML
    private Label lbl_validationView;
    @FXML
    private HBox HBox_AppQuestionsNewBtnOnly;
    @FXML
    private VBox VBox_EmptyAppQuestions;
    @FXML
    private VBox VBox_AppQuestions;
    @FXML
    private TableView<AppQuestion> table_appQuestions;
    @FXML
    private TableColumn<AppQuestion, Label> col_question;
    @FXML
    private TableColumn<AppQuestion, HBox> col_actions;

    public AppQuestionsController(Object model) {
        super((App)model);
    }

    @Override
    protected void runOnInitialize() {
        tableAppQuestionsSetup();
        switchVBoxesIfNoAppQuestion();
        switchBtnHBoxesIfNeeded();
        startListListener();
    }

    @Override
    protected void runLaterOnInitialize() {
        hideAll();
    }

    @Override
    protected void hideAll() {
        for (int i = 0 ; i < table_appQuestions.getItems().size() ; i++) {
            AppQuestion appQuestion = table_appQuestions.getItems().get(i);
            setNewIcon(appQuestion, FontAwesomeIcons.EYE, table_appQuestions, 1);
            hideText(appQuestion.getLblMaskedAnswer());
        }
    }

    @Override
    protected void showAll() {
        for (int i = 0 ; i < table_appQuestions.getItems().size() ; i++) {
            AppQuestion appQuestion = table_appQuestions.getItems().get(i);
            setNewIcon(appQuestion, FontAwesomeIcons.EYE_SLASH, table_appQuestions, 1);
            showText(appQuestion.getLblMaskedAnswer(), appQuestion.getAnswer());
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
            switchVBoxesIfNoAppQuestion();
            switchBtnHBoxesIfNeeded();
        };
    }

    private ObservableList<AppQuestion> getObservableList() {
        return app.appQuestionsListProperty();
    }

    private void tableAppQuestionsSetup() {
        tableSetup();
        questionColumnSetup();
        actionsColumnSetup();
    }

    private void tableSetup() {
        table_appQuestions.setItems(app.getAppQuestions());
        table_appQuestions.getColumns().forEach(column -> column.setReorderable(false));
    }

    private void questionColumnSetup() {
        col_question.setResizable(false);
        col_question.setSortable(false);
        col_question.prefWidthProperty().bind(table_appQuestions.widthProperty().multiply(0.75));
        col_question.setCellValueFactory(cellData -> cellData.getValue().lblMaskedAnswerProperty());
        col_question.setCellFactory(column -> {
            TableCell<AppQuestion, Label> cell = new TableCell<>() {
                @Override
                protected void updateItem(Label label, boolean empty) {
                    super.updateItem(label, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        VBox vbox = new VBox();
                        vbox.setAlignment(Pos.CENTER_RIGHT);
                        vbox.setStyle("-fx-border-color:#eff1f3; -fx-border-width:0 1px 0 0; -fx-padding: 0 15px 0 0");

                        AppQuestion selectedAppQuestion = getTableView().getItems().get(getIndex());

                        Label lblQuestion = new Label(selectedAppQuestion.getText());
                        lblQuestion.getStyleClass().add("question");

                        selectedAppQuestion.getLblMaskedAnswer().getStyleClass().add("answer");

                        VBox.setMargin(lblQuestion, new Insets(0, 0, 5, 0));
                        vbox.getChildren().addAll(lblQuestion, selectedAppQuestion.getLblMaskedAnswer());

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
        col_actions.prefWidthProperty().bind(table_appQuestions.widthProperty().multiply(0.22));
        col_actions.setCellValueFactory(cellData -> cellData.getValue().actionIconsContainerProperty());
        col_actions.setCellFactory(column -> {
            TableCell<AppQuestion, HBox> cell = new TableCell<>() {
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

                            AppQuestion selectedAppQuestion = getTableView().getItems().get(getIndex());

                            eyeIcon.setOnMouseClicked((MouseEvent event) -> {
                                showOrHideAnswer(selectedAppQuestion, (FontAwesomeIcon) event.getSource());
                            });

                            copyIcon.setOnMouseClicked((MouseEvent event) -> {
                                copyAnswerToClipboard(selectedAppQuestion, (FontAwesomeIcon) event.getSource());
                            });

                            editIcon.setOnMouseClicked((MouseEvent event) -> {
                                eyeIcon.setIcon(FontAwesomeIcons.EYE);
                                hideText(selectedAppQuestion.getLblMaskedAnswer());
                                editAppQuestion(selectedAppQuestion);
                            });

                            deleteIcon.setOnMouseClicked((MouseEvent event) -> {
                                showDeleteDialog(selectedAppQuestion);
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

    private void showOrHideAnswer(AppQuestion appQuestion, FontAwesomeIcon icon) {
        showOrHideText(appQuestion.getLblMaskedAnswer(), appQuestion.getAnswer(), icon);
    }

    private void copyAnswerToClipboard(AppQuestion appQuestion, FontAwesomeIcon icon) {
        copyTextToClipboard(appQuestion.getAnswer(), true, icon);
    }

    private void switchVBoxesIfNoAppQuestion() {
        switchVBoxesIfNeeded(app.getAppQuestions().size(), VBox_AppQuestions, VBox_EmptyAppQuestions);
    }

    private void switchBtnHBoxesIfNeeded() {
        switchBtnHBoxesIfNeeded(app.getAppQuestions().size(), HBox_appQuestionsAllBtn, HBox_AppQuestionsNewBtnOnly);
    }

    private void editAppQuestion(AppQuestion appQuestion) {
        ViewLoader.loadPopup(
                Views.APP_QUESTION_POPUP,
                AppQuestionPopupController.class,
                lbl_validationView,
                appQuestion);
        table_appQuestions.refresh();
    }

    private void showDeleteDialog(AppQuestion appQuestion) {
        ViewLoader.showDialog(
                Strings.Dialog.CONFIRM_DELETE_APP_QUESTION,
                () -> deleteSelectedAppQuestion(appQuestion),
                lbl_validationView);
    }

    private void deleteSelectedAppQuestion(AppQuestion appQuestion) {
        try {
            DaoProvider.getAppQuestionDao().delete(appQuestion);
        } catch (SQLException e) {
            String errorMessage = String.format(Strings.Errors.DELETING_APP_QUESTION, e.getMessage());
            new ExceptionMessage.Builder(errorMessage, lbl_validationView).show();
            LOGGER.severe(String.format("%s - %s", LOGGER.getName(), errorMessage));
            e.printStackTrace();
        }
    }

    /*************************************
     * FXML methods
     **************************************/
    @FXML
    void newAppQuestion(MouseEvent event) {
        ViewLoader.loadPopup(
                Views.APP_QUESTION_POPUP,
                AppQuestionPopupController.class,
                lbl_validationView,
                new AppQuestion(app));
        table_appQuestions.refresh();
    }

    @FXML
    void showOrHideAllAnswers(MouseEvent event) {
        showOrHideAll((Label) event.getSource(), table_appQuestions);
    }


}
