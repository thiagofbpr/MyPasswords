package mp.constants;

public class Strings {

    public static class Errors {
        public static String LOADING_VIEW = "Error loading %s view. %s";
        public static String LOADING_POPUP = "Error loading %s popup. %s";
        public static String EXISTING_USERNAME = "This username already exists. Please enter a different one.";
        public static String NON_EXISTING_USER = "Non-existing user.";
        public static String INCORRECT_PASSWORD = "Incorrect password.";
        public static String CURRENT_PASSWORD_NOT_MATCH = "Current password does not match.";
        public static String RETRIEVING_USER_FROM_DATABASE = "Error retrieving user from database. %s";
        public static String ADDING_USER_TO_DATABASE = "Error adding user to database. %s";
        public static String UPDATING_USER_INFORMATION = "Error updating user information. %s";
        public static String DELETING_USER_ACCOUNT = "Error deleting user account. %s";
        public static String UPDATING_LAST_CONNECTION = "Error updating user last connection. %s";
        public static String LOADING_USER_DATA = "Error loading user data. %s";
        public static String CREATING_NEW_INSTANCE = "Error creating a new instance of class %s. %s";
        public static String ADDING_APP = "Error adding app. %s";
        public static String UPDATING_APP = "Error updating app. %s";
        public static String DELETING_APP = "Error deleting app. %s";
        public static String EXISTING_APP = "This app already exists.";
        public static String EXISTING_APP_LOGIN = "This username/password already exists.";
        public static String EXISTING_APP_QUESTION = "This question already exists.";
        public static String EXISTING_APP_NOTE = "This note already exists.";
        public static String REQUIRED_FIELD = "This is a required field.";
        public static String INVALID_WEBSITE_FORMAT = "Invalid website format.";
        public static String NO_WHITE_SPACE = "This field must not contain white spaces.";
        public static String BROWSING_WEB_SITE = "Error browsing website. %s";
        public static String SHOWING_DIALOG = "Error showing dialog popup. %s";
        public static String ADDING_APP_LOGIN = "Error adding login. %s";
        public static String UPDATING_APP_LOGIN = "Error updating login. %s";
        public static String DELETING_APP_LOGIN = "Error deleting login. %s";
        public static String ADDING_APP_QUESTION = "Error adding question. %s";
        public static String UPDATING_APP_QUESTION = "Error updating question. %s";
        public static String DELETING_APP_QUESTION = "Error deleting question. %s";
        public static String ADDING_APP_NOTE = "Error adding note. %s";
        public static String UPDATING_APP_NOTE = "Error updating note. %s";
        public static String DELETING_APP_NOTE = "Error deleting note. %s";
    }

    public static class Dialog {
        public static String CONFIRM_DELETE_APP = "Do you really want to delete this app ?";
        public static String CONFIRM_DELETE_APP_LOGIN = "Do you really want to delete this username/password ?";
        public static String CONFIRM_DELETE_APP_QUESTION = "Do you really want to delete this question/answer ?";
        public static String CONFIRM_DELETE_APP_NOTE = "Do you really want to delete this note/value ?";
    }

    public static class Labels {
        public static final String APPLICATION_TITLE = "My Passwords";
        public static String LAST_CONNECTION = "Last connection on %s";
        public static String FIRST_CONNECTION = "This is your first connection";
        public static String NEW_APPLICATION = "New application";
        public static String EDIT_APPLICATION = "Update application";
        public static String NEW_APP_LOGIN = "New login";
        public static String EDIT_APP_LOGIN = "Update login";
        public static String NEW_APP_QUESTION = "New question";
        public static String EDIT_APP_QUESTION = "Update question";
        public static String NEW_APP_NOTE = "New note";
        public static String EDIT_APP_NOTE = "Update note";
        public static String SHOW_ALL = "Show all";
        public static String HIDE_ALL = "Hide all";
        public static String COPIED = "copied";
    }

    public static class Constraints {
        public static String UNIQUE = "SQLITE_CONSTRAINT_UNIQUE";
    }

    public static class EMAIL {
        public static String EMAIL_ADDRESS = "thiagofbpr@hotmail.com";
        public static String EMAIL_SUBJECT = "MyPasswords%20application";
        public static String ERROR_OPENING_EMAIL_CLIENT = "Error opening email client. %s";
    }

}


