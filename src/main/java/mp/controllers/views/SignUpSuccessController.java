package mp.controllers.views;

import mp.constants.Views;
import mp.controllers.views.base.BaseApplicationController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import mp.utils.ViewLoader;
import java.util.Timer;
import java.util.TimerTask;

public class SignUpSuccessController extends BaseApplicationController {

    @FXML
    private Label lbl_validationView;
    @FXML
    private Label lbl_redirecting;

    public SignUpSuccessController(Object model) {
        super();
    }

    @Override
    protected void runOnInitialize() {
        Timer timer = new Timer();
        showRedirectingLabel(timer);
        showLoginView(timer);
    }

    @Override
    protected void runLaterOnInitialize() {
        // Not implemented
    }

    @Override
    protected void validatedFieldsSetup() {
        // Not implemented
    }

    private void showRedirectingLabel(Timer timer) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> lbl_redirecting.setVisible(true));
            }
        }, 1200);
    }

    private void showLoginView(Timer timer) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() ->
                    ViewLoader.loadView(
                                    Views.LOGIN_VIEW,
                                    LoginController.class,
                                    null,
                                    lbl_validationView,
                                    ViewLoader.getMainFrame())
                );
            }
        }, 2500);
    }


}
