package mp.dao.impl;

import mp.dao.interfaces.IAppLoginDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mp.models.App;
import mp.models.AppLogin;
import mp.utils.DatabaseUtil;
import mp.utils.DateTimeUtil;
import java.sql.*;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.Objects;

public class AppLoginDao implements IAppLoginDao {

    @Override
    public void add(AppLogin appLogin) throws SQLException {
        Connection con =  DatabaseUtil.getConnection();
        Timestamp now = DateTimeUtil.getCurrentTimestamp();

        int lastAppLoginId = retrieveLastAppLoginId(con, appLogin.getApp().getId());
        if (lastAppLoginId != 0) {
            setEndTimeToOldAppLogin(con, lastAppLoginId, appLogin, now);
        }

        PreparedStatement stmt = con.prepareStatement("insert into applogin(username, password, createtime, updatetime, app_id) values(?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
        stmt.setString(1, appLogin.getUsername());
        stmt.setString(2, appLogin.getPassword());
        stmt.setTimestamp(3, now);
        stmt.setTimestamp(4, now);
        stmt.setInt(5, appLogin.getApp().getId());
        stmt.execute();

        appLogin.setId(extractGeneratedKey(stmt));
        appLogin.setCreateTime(DateTimeUtil.convertToZonedDateTime(now));
        appLogin.setUpdateTime(DateTimeUtil.convertToZonedDateTime(now));

        appLogin.getApp().getAppLogins().add(appLogin);
        FXCollections.sort(appLogin.getApp().getAppLogins(), Comparator.comparing(AppLogin::getId, Comparator.reverseOrder()));

        con.close();
    }

    @Override
    public AppLogin get(int id) throws SQLException {
        Connection con =  DatabaseUtil.getConnection();
        AppLogin applogin = null;

        PreparedStatement stmt = con.prepareStatement("select * from applogin where id = ?;");
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            applogin = extractFromResultSet(rs);
        }

        con.close();
        return applogin;
    }

    @Override
    public ObservableList<AppLogin> getAll(App app) throws SQLException {
        Connection con =  DatabaseUtil.getConnection();
        ObservableList<AppLogin> appLogins = FXCollections.observableArrayList();

        PreparedStatement stmt = con.prepareStatement("select * from applogin where app_id = ? order by id desc;");
        stmt.setInt(1, app.getId());
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            AppLogin applogin = extractFromResultSet(rs);
            applogin.setApp(app);
            appLogins.add(applogin);
        }

        con.close();
        return appLogins;
    }

    @Override
    public void update(AppLogin appLogin) throws SQLException {
        if (isUpdateAppLoginNeeded(appLogin)) {
            Connection con =  DatabaseUtil.getConnection();
            Timestamp now = DateTimeUtil.getCurrentTimestamp();

            PreparedStatement stmt = con.prepareStatement("update applogin set updatetime = ?, username = ?, password = ? where id = ?");
            stmt.setTimestamp(1, now);
            stmt.setString(2, appLogin.getUsername());
            stmt.setString(3, appLogin.getPassword());
            stmt.setInt(4, appLogin.getId());
            stmt.execute();

            appLogin.setUpdateTime(DateTimeUtil.convertToZonedDateTime(now));

            con.close();
        }
    }

    @Override
    public void delete(AppLogin appLogin) throws SQLException {
        Connection con =  DatabaseUtil.getConnection();

        PreparedStatement stmt = con.prepareStatement("delete from applogin where id = ?;");
        stmt.setInt(1, appLogin.getId());
        stmt.execute();

        appLogin.getApp().getAppLogins().remove(appLogin);

        if (anotherAppLoginExists(con, appLogin.getApp().getId())) {
            int precedentAppLoginId = retrievePrecedentAppLogin(con, appLogin.getApp().getId());
            if (precedentAppLoginId != 0) {
                appLogin.getApp().setEndTimeToAppLogin(precedentAppLoginId, null);
            }
        }

        con.close();
    }

    private int retrievePrecedentAppLogin(Connection con, int appId) throws SQLException {
        PreparedStatement stmt = con.prepareStatement("select max(id) from applogin where app_id = ?;");
        stmt.setInt(1, appId);

        ResultSet rs = stmt.executeQuery();
        int result = 0;

        while (rs.next()) {
            result = rs.getInt(1);
        }

        return result;
    }

    private boolean anotherAppLoginExists(Connection con, int appId) throws SQLException {
        PreparedStatement stmt = con.prepareStatement("select count(*) from applogin where app_id = ?;");
        stmt.setInt(1, appId);

        ResultSet rs = stmt.executeQuery();
        int result = 0;

        while (rs.next()) {
            result = rs.getInt(1);
        }

        return result > 0;
    }

    private void setEndTimeToOldAppLogin(Connection con, int precedentAppLoginId, AppLogin appLogin, Timestamp now) throws SQLException {
        PreparedStatement stmt = con.prepareStatement("update applogin set endtime = ? where id = ?;");
        stmt.setTimestamp(1, now);
        stmt.setInt(2, precedentAppLoginId);
        stmt.execute();

        appLogin.getApp().setEndTimeToAppLogin(precedentAppLoginId, DateTimeUtil.convertToZonedDateTime(now));
    }

    private int retrieveLastAppLoginId(Connection con, int app_id) throws SQLException {
        PreparedStatement stmt = con.prepareStatement("select max(id) as id from applogin where app_id = ?;");
        stmt.setInt(1, app_id);
        stmt.execute();

        ResultSet rs = stmt.executeQuery();
        int result = 0;
        while (rs.next()) {
            result = rs.getInt("id");
        }
        return result;
    }

    private boolean isUpdateAppLoginNeeded(AppLogin appLogin) throws SQLException {
        AppLogin existingAppLogin = get(appLogin.getId());
        return existingAppLogin != null
                && (!Objects.equals(existingAppLogin.getUsername(), appLogin.getUsername())
                    || !Objects.equals(existingAppLogin.getPassword(), appLogin.getPassword()));
    }

    private AppLogin extractFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String username = rs.getString("username");
        String password = rs.getString("password");
        ZonedDateTime createTime = DateTimeUtil.convertToZonedDateTime(rs.getTimestamp("createtime"));
        ZonedDateTime updateTime = DateTimeUtil.convertToZonedDateTime(rs.getTimestamp("updatetime"));
        ZonedDateTime endTime = DateTimeUtil.convertToZonedDateTime(rs.getTimestamp("endtime"));
        return new AppLogin(id, username, password, createTime, updateTime, endTime);
    }

    private int extractGeneratedKey(PreparedStatement stmt) throws SQLException {
        ResultSet rs = stmt.getGeneratedKeys();
        return rs.next() ? rs.getInt(1) : 0;
    }


}
