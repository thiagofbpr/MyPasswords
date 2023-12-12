package mp.dao.interfaces;

import javafx.collections.ObservableList;
import mp.models.App;
import mp.models.User;
import java.io.IOException;
import java.sql.SQLException;

public interface IAppDao {
    void add(App app) throws SQLException, IOException;
    ObservableList<App> getAll(User user) throws SQLException, IOException;
    void update(App app) throws SQLException, IOException;
    void delete(App app) throws SQLException;


}
