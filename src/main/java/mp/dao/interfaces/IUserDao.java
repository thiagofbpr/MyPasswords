package mp.dao.interfaces;

import mp.models.User;
import java.sql.SQLException;

public interface IUserDao {
    void add(User user) throws SQLException;
    User get(String username) throws SQLException;
    User get(int id) throws SQLException;
    void update(User user, boolean onlyLastConnection) throws SQLException;
    void delete(User user) throws SQLException;


}
