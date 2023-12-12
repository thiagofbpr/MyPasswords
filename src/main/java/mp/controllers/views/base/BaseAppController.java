package mp.controllers.views.base;

import mp.constants.Strings;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import mp.models.App;
import mp.utils.EncryptUtil;
import mp.utils.ToolkitUtil;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

public abstract class BaseAppController<T> implements Initializable {

    protected static final Logger LOGGER = Logger.getLogger(BaseAppController.class.getName());
    protected App app;

    protected BaseAppController(App app) {
        this.app = app;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        runOnInitialize();
        Platform.runLater(this::runLaterOnInitialize);
    }

    protected void switchVBoxesIfNeeded(int nbElements, VBox containerWithElements, VBox containerWithNoElements) {
        if (nbElements > 0) {
            containerWithElements.toFront();
        } else {
            containerWithNoElements.toFront();
        }
    }

    protected void switchBtnHBoxesIfNeeded(int nbElements, HBox containerWithMoreThanOneElement, HBox containerWithOneOrNoElements) {
        if (nbElements > 1) {
            containerWithMoreThanOneElement.toFront();
        } else {
            containerWithOneOrNoElements.toFront();
        }
    }

    protected void showOrHideText(Label label, String encryptedText, FontAwesomeIcon icon) {
        if (icon.getGlyphName().equals(FontAwesomeIcons.EYE.name())) {
            icon.setIcon(FontAwesomeIcons.EYE_SLASH);
            showText(label, encryptedText);
        } else {
            icon.setIcon(FontAwesomeIcons.EYE);
            hideText(label);
        }
    }

    protected void showText(Label label, String encryptedText) {
        label.setText(EncryptUtil.decrypt(encryptedText));
    }

    protected void hideText(Label label) {
        label.setText(EncryptUtil.getBlackBullets());
    }

    protected void copyTextToClipboard(String text, boolean isEncryptedText, FontAwesomeIcon icon) {
        String currentSize = icon.getSize();
        String currentText = icon.getText();

        if (!currentText.equals(Strings.Labels.COPIED)) {

            ToolkitUtil.copyToClipBoard(isEncryptedText ? EncryptUtil.decrypt(text) : text);

            icon.setSize("0.8em");
            icon.setText(Strings.Labels.COPIED);

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> {
                        icon.setText(currentText);
                        icon.setSize(currentSize);
                    });
                }
            }, 800);
        }
    }

    protected void setNewIcon(T model, FontAwesomeIcons newIcon, TableView<T> table, int actionColumnIndex) {
        TableColumn actionsColumn = table.getColumns().get(actionColumnIndex);
        HBox hbox = (HBox) actionsColumn.getCellObservableValue(model).getValue();
        FontAwesomeIcon icon = (FontAwesomeIcon)hbox.getChildren().get(0);
        icon.setIcon(newIcon);
    }

    protected void showOrHideAll(Label label, TableView<T> table) {
        FontAwesomeIcon labelIcon = (FontAwesomeIcon)label.getGraphic();
        if (label.getText().equals(Strings.Labels.SHOW_ALL)) {
            label.setText(Strings.Labels.HIDE_ALL);
            labelIcon.setIcon(FontAwesomeIcons.EYE_SLASH);
            showAll();
        } else {
            label.setText(Strings.Labels.SHOW_ALL);
            labelIcon.setIcon(FontAwesomeIcons.EYE);
            hideAll();
        }
        table.refresh();
    }

    protected abstract void hideAll();

    protected abstract void showAll();

    protected abstract void runOnInitialize();

    protected abstract void runLaterOnInitialize();


}
