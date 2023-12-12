package mp.dao.impl;

import mp.dao.interfaces.IUserDao;
import mp.models.User;
import mp.utils.DatabaseUtil;
import mp.utils.DateTimeUtil;
import java.sql.*;
import java.time.ZonedDateTime;

public class UserDao implements IUserDao {

    @Override
    public void add(User user) throws SQLException {
        Connection con =  DatabaseUtil.getConnection();
        Timestamp updateTime = DateTimeUtil.getCurrentTimestamp();

        PreparedStatement stmt = con.prepareStatement("insert into user(fullname, username, password, updatetime) values (?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, user.getFullname());
        stmt.setString(2, user.getUsername());
        stmt.setString(3, user.getPassword());
        stmt.setTimestamp(4, updateTime);
        stmt.executeUpdate();

        int generatedId = extractGeneratedKey(stmt);
        user.setId(generatedId);
        user.setUpdatetime(DateTimeUtil.convertToZonedDateTime(updateTime));

        con.close();
    }

    @Override
    public User get(String username) throws SQLException {
        Connection con =  DatabaseUtil.getConnection();
        User user = null;

        PreparedStatement stmt = con.prepareStatement("select * from user where username = ?");
        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            user = extractFromResultSet(rs);
        }

        con.close();
        return user;
    }

    @Override
    public User get(int id) throws SQLException {
        Connection con =  DatabaseUtil.getConnection();
        User user = null;

        PreparedStatement stmt = con.prepareStatement("select * from user where id = ?");
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            user = extractFromResultSet(rs);
        }

        con.close();
        return user;
    }

    @Override
    public void update(User user, boolean onlyLastConnection) throws SQLException {
        Connection con =  DatabaseUtil.getConnection();
        if (onlyLastConnection) {
            PreparedStatement stmt = con.prepareStatement("update user set lastconnection = ? where id = ?");
            stmt.setTimestamp(1, DateTimeUtil.getCurrentTimestamp());
            stmt.setInt(2, user.getId());
            stmt.execute();
        } else {
            if (hasChanged(user)) {
                Timestamp updateTime = DateTimeUtil.getCurrentTimestamp();

                PreparedStatement stmt = con.prepareStatement("update user set updatetime = ?, fullname = ?, username = ?, password = ? where id = ?");
                stmt.setTimestamp(1, updateTime);
                stmt.setString(2, user.getFullname());
                stmt.setString(3, user.getUsername());
                stmt.setString(4, user.getPassword());
                stmt.setInt(5, user.getId());
                stmt.execute();

                user.setUpdatetime(DateTimeUtil.convertToZonedDateTime(updateTime));
            }

        }
        con.close();
    }

    @Override
    public void delete(User user) throws SQLException {
        Connection con =  DatabaseUtil.getConnection();

        PreparedStatement stmt = con.prepareStatement("delete from user where id = ?");
        stmt.setInt(1, user.getId());
        stmt.execute();

        con.close();
    }

    private User extractFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String fullname = rs.getString("fullname");
        String username = rs.getString("username");
        String password = rs.getString("password");
        ZonedDateTime updateTime = DateTimeUtil.convertToZonedDateTime(rs.getTimestamp("updatetime"));
        ZonedDateTime lastConnection = DateTimeUtil.convertToZonedDateTime(rs.getTimestamp("lastconnection"));
        return new User(id, fullname, username, password, updateTime, lastConnection);
    }

    private int extractGeneratedKey(PreparedStatement stmt) throws SQLException {
        ResultSet rs = stmt.getGeneratedKeys();
        return rs.next() ? rs.getInt(1) : 0;
    }

    private boolean hasChanged(User user) throws SQLException {
        User currentUser = get(user.getId());
        return !currentUser.equals(user);
    }


}
