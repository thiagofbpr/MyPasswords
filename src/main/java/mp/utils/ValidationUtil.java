package mp.utils;

import mp.constants.Strings;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.validation.Constraint;
import io.github.palexdev.materialfx.validation.Severity;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.BooleanExpression;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static io.github.palexdev.materialfx.validation.Validated.INVALID_PSEUDO_CLASS;
import static javafx.beans.binding.Bindings.createBooleanBinding;

public class ValidationUtil {

    private ValidationUtil() {

    }

    /*************************************
     * Add constraints
     **************************************/
    public static void addTextLimit(MFXTextField textField, int limit) {
        textField.setTextLimit(limit);
    }

    public static void addRequiredConstraint(MFXTextField textField, Label validationLabel) {
        setConstraintAndListeners(
                textField,
                validationLabel,
                Strings.Errors.REQUIRED_FIELD,
                textField.textProperty().length().greaterThan(0)
        );
    }

    public static void addWebsiteFormatConstraint(MFXTextField textField, Label validationLabel) {
        setConstraintAndListeners(
                textField,
                validationLabel,
                Strings.Errors.INVALID_WEBSITE_FORMAT,
                isValidWebsiteFormat(textField)
        );
    }

    public static void addNoWhiteSpaceConstraint(MFXTextField textField, Label validationLabel) {
        setConstraintAndListeners(
                textField,
                validationLabel,
                Strings.Errors.NO_WHITE_SPACE,
                hasNoWhiteSpaces(textField)
        );
    }

    /*************************************
     * Show errors
     **************************************/
    public static void showConstraintValidationError(MFXTextField textField, Label validationLabel) {
        List<Constraint> constraints = textField.validate();
        if (!constraints.isEmpty()) {
            textField.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
            validationLabel.setText(constraints.get(0).getMessage());
            validationLabel.setVisible(true);
            VBox.setMargin(validationLabel, new Insets(2, 0, 12, 0));
        }
    }

    public static void showCustomValidationError(MFXTextField textField, Label validationLabel, String validationMessage) {
        textField.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
        textField.getStyleClass().add("");
        validationLabel.setText(validationMessage);
        validationLabel.setVisible(true);
        VBox.setMargin(validationLabel, new Insets(2, 0, 12, 0));

        addValidatorListener(textField, validationLabel);
        addDelegateFocusedListener(textField, validationLabel);
    }

    /*************************************
     * Check validated fields
     **************************************/
    public static boolean isValidationSuccessful(Map<MFXTextField, Label> validatedFields) {
        boolean isValidationSuccessful = true;
        for (Map.Entry<MFXTextField, Label> entry : validatedFields.entrySet()) {
            MFXTextField textField = entry.getKey();
            Label validationLabel = entry.getValue();
            if (!textField.isValid()) {
                showConstraintValidationError(textField, validationLabel);
                isValidationSuccessful = false;
            }
        }
        return isValidationSuccessful;
    }

    /*************************************
     * Private methods
     **************************************/
    private static BooleanExpression hasNoWhiteSpaces(MFXTextField textField) {
        return createBooleanBinding(() -> !textField.textProperty().get().trim().contains(" "), textField.textProperty());
    }

    public static BooleanBinding isValidWebsiteFormat(MFXTextField textField) {
        return createBooleanBinding(() -> {
            String text = textField.textProperty().get();
            if (text != null) {
                String regex = "^(http(s)?:\\/\\/)?([A-Za-z0-9-]+\\.)+[A-Za-z]{2,6}(\\/[A-Za-z0-9-._~:/?#[\\\\]@!$&'()*+,;=%]*)?";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(text);
                return text.isEmpty() || matcher.matches();
            }
            return true;
        }, textField.textProperty());
    }

    private static void setConstraintAndListeners(MFXTextField textField, Label validationLabel, String validationMessage, BooleanExpression condition) {
        addConstraint(textField, validationMessage, condition);
        addValidatorListener(textField, validationLabel);
        addDelegateFocusedListener(textField, validationLabel);
    }

    private static void addConstraint(MFXTextField textField, String validationMessage, BooleanExpression condition) {
        Constraint constraint = Constraint.Builder.build()
                .setSeverity(Severity.ERROR)
                .setMessage(validationMessage)
                .setCondition(condition)
                .get();

        textField.getValidator()
                .constraint(constraint);
    }

    private static void addValidatorListener(MFXTextField textField, Label validationLabel) {
        textField.getValidator().validProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                clearValidationError(textField, validationLabel);
            }
        });
    }

    private static void addDelegateFocusedListener(MFXTextField textField, Label validationLabel) {
        textField.delegateFocusedProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue && !newValue) {
                showConstraintValidationError(textField, validationLabel);
            } else {
                clearValidationError(textField, validationLabel);
            }
        });
    }

    private static void clearValidationError(MFXTextField textField, Label validationLabel) {
        validationLabel.setVisible(false);
        validationLabel.setText("");
        textField.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
        VBox.setMargin(validationLabel, new Insets(2, 0, 0, 0));
    }


}
