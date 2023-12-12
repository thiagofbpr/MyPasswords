package mp.dao.interfaces;

import javafx.collections.ObservableList;
import mp.models.App;
import mp.models.AppLogin;
import java.sql.SQLException;

public interface IAppLoginDao {
    void add(AppLogin appLogin) throws SQLException;
    AppLogin get(int id) throws SQLException;
    ObservableList<AppLogin> getAll(App app) throws SQLException;
    void update(AppLogin appLogin) throws SQLException;
    void delete(AppLogin appLogin) throws SQLException;


}
