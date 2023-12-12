package mp.constants;

public class Views {

	public static View APPLICATION_START_VIEW = new View("/views/ApplicationStartView.fxml");
	public static View APPLICATION_VIEW = new View("/views/ApplicationView.fxml");
	public static View LOGIN_VIEW = new View("/views/LoginView.fxml");
	public static View SIGN_UP_VIEW = new View("/views/SignUpView.fxml");
	public static View SIGN_UP_SUCCESS_VIEW = new View("/views/SignUpSuccessView.fxml");
	public static View APP_POPUP = new View("/popups/AppPopup.fxml");
	public static View INFO_POPUP = new View("/popups/InfoPopup.fxml");
	public static View CONFIG_VIEW = new View("/views/ConfigView.fxml");
	public static View USER_FULL_NAME_UPDATE_VIEW = new View("/views/UpdateUserFullNameView.fxml");
	public static View USER_PASSWORD_UPDATE_VIEW = new View("/views/UpdateUserPasswordView.fxml");
	public static View USER_ACCOUNT_DELETION_VIEW = new View("/views/UserAccountDeletionView.fxml");
	public static View DIALOG_POPUP = new View("/popups/DialogPopup.fxml");
	public static View APP_LOGIN_POPUP = new View("/popups/AppLoginPopup.fxml");
	public static View APP_QUESTION_POPUP = new View("/popups/AppQuestionPopup.fxml");
	public static View APP_NOTE_POPUP = new View("/popups/AppNotePopup.fxml");
	public static View APPS_LIST_VIEW = new View("/views/AppsListView.fxml");
	public static View APP_VIEW = new View("/views/AppView.fxml");
	public static View APP_LOGINS_VIEW = new View("/views/AppLoginsView.fxml");
	public static View APP_LOGIN_INFO_POPUP = new View("/popups/AppLoginInfoPopup.fxml");
	public static View APP_QUESTIONS_VIEW = new View("/views/AppQuestionsView.fxml");
	public static View APP_NOTES_VIEW = new View("/views/AppNotesView.fxml");


	public static class View {
		private final String path;

		private View(String path) {
			this.path = path;
		}

		public String getPath() {
			return path;
		}

		public String getName() {
			int start = this.path.lastIndexOf("/") + 1;
			int end = this.path.indexOf(".");
			return this.path.substring(start, end);
		}
	}

	
}
