package mp.utils;

import org.sqlite.SQLiteConfig;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {

	private final static String DATA_FOLDER = System.getenv("APPDATA");;
	private final static String URL = "jdbc:sqlite:" + DATA_FOLDER + "\\MyPasswords\\MyPasswords.db";

	private DatabaseUtil() {

	}

	public static Connection getConnection() throws SQLException {
		SQLiteConfig config = new SQLiteConfig();
		config.enforceForeignKeys(true);
		return DriverManager.getConnection(URL, config.toProperties());
	}
	
	
}
