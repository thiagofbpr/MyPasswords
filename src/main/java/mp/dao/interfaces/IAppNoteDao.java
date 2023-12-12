package mp.dao.interfaces;

import javafx.collections.ObservableList;
import mp.models.App;
import mp.models.AppNote;
import java.sql.SQLException;

public interface IAppNoteDao {
    void add(AppNote appNote) throws SQLException;
    ObservableList<AppNote> getAll(App app) throws SQLException;
    void update(AppNote appNote) throws SQLException;
    void delete(AppNote appNote) throws SQLException;


}
