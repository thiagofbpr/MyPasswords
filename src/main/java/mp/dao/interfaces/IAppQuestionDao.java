package mp.dao.interfaces;

import javafx.collections.ObservableList;
import mp.models.App;
import mp.models.AppQuestion;
import java.sql.SQLException;

public interface IAppQuestionDao {
    void add(AppQuestion appQuestion) throws SQLException;
    ObservableList<AppQuestion> getAll(App app) throws SQLException;
    void update(AppQuestion appQuestion) throws SQLException;
    void delete(AppQuestion appQuestion) throws SQLException;


}
