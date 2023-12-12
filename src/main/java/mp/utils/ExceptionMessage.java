package mp.utils;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ExceptionMessage {

    private ExceptionMessage() { }

    public static class Builder {

        private final String message;
        private final Label label;
        private Insets insets;

        public Builder(String message, Label label) {
            this.message = message;
            this.label = label;
        }

        public Builder withInsets(Insets insets) {
            this.insets = insets;
            return this;
        }

        public void show() {
            if (this.label != null) {
                this.label.setText(this.message);
                this.label.setVisible(true);
                if (this.insets != null) {
                    VBox.setMargin(this.label, insets);
                }
            }
        }
    }


}
