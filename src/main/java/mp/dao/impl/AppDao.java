package mp.dao.impl;

import mp.dao.interfaces.IAppDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import mp.models.App;
import mp.models.User;
import mp.utils.DatabaseUtil;
import mp.utils.ImageUtil;
import mp.utils.UserUtil;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

public class AppDao implements IAppDao {

    @Override
    public void add(App app) throws SQLException, IOException {
        Connection con =  DatabaseUtil.getConnection();

        PreparedStatement stmt = con.prepareStatement("insert into app(name, url, logo, user_id) values (?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, app.getName());
        stmt.setString(2, app.getUrl());
        stmt.setBytes(3, ImageUtil.imageToByteArray(app.getLogo(), "png"));
        stmt.setInt(4, UserUtil.CURRENT_USER.getId());
        stmt.execute();

        app.setId(extractGeneratedKey(stmt));
        app.setUser(UserUtil.CURRENT_USER);
        app.getUser().getApps().add(app);
        FXCollections.sort(UserUtil.CURRENT_USER.getApps());

        con.close();
    }

    @Override
    public ObservableList<App> getAll(User user) throws SQLException, IOException {
        Connection con =  DatabaseUtil.getConnection();
        ObservableList<App> userApps = FXCollections.observableArrayList();

        PreparedStatement stmt = con.prepareStatement("select * from app where user_id = ? order by name;");
        stmt.setInt(1, user.getId());
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            App app = extractFromResultSet(rs);
            app.setUser(user);
            userApps.add(app);
        }

        con.close();
        return userApps;
    }

    @Override
    public void update(App app) throws SQLException, IOException {
        Connection con =  DatabaseUtil.getConnection();

        PreparedStatement stmt = con.prepareStatement("update app set name = ?, url = ?, logo = ? where id = ?");
        stmt.setString(1, app.getName());
        stmt.setString(2, app.getUrl());
        stmt.setBytes(3, ImageUtil.imageToByteArray(app.getLogo(), "png"));
        stmt.setInt(4, app.getId());
        stmt.execute();

        FXCollections.sort(UserUtil.CURRENT_USER.getApps());
        
        con.close();
    }

    @Override
    public void delete(App app) throws SQLException {
        Connection con =  DatabaseUtil.getConnection();

        PreparedStatement stmt = con.prepareStatement("delete from app where id = ?");
        stmt.setInt(1, app.getId());
        stmt.execute();

        app.getUser().getApps().remove(app);

        con.close();
    }

    private App extractFromResultSet(ResultSet rs) throws SQLException, IOException {
        int id = rs.getInt("id");
        String name = rs.getString("name");

        InputStream is = rs.getBinaryStream("logo");
        byte[] imageBytes = ImageUtil.streamToByteArray(is);
        Image logo = new Image(new ByteArrayInputStream(imageBytes));

        String url = rs.getString("url");
        return new App(id, name, url, logo);
    }

    private int extractGeneratedKey(PreparedStatement stmt) throws SQLException {
        ResultSet rs = stmt.getGeneratedKeys();
        return rs.next() ? rs.getInt(1) : 0;
    }


}
