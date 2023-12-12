package mp.dao.impl;

import mp.dao.interfaces.IAppNoteDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mp.models.App;
import mp.models.AppNote;
import mp.utils.DatabaseUtil;
import java.sql.*;

public class AppNoteDao implements IAppNoteDao {

    @Override
    public void add(AppNote appNote) throws SQLException {
        Connection con =  DatabaseUtil.getConnection();

        PreparedStatement stmt = con.prepareStatement("insert into appnote(description, value, app_id) values (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, appNote.getDescription());
        stmt.setString(2, appNote.getValue());
        stmt.setInt(3, appNote.getApp().getId());
        stmt.execute();

        appNote.setId(extractGeneratedKey(stmt));
        appNote.getApp().getAppNotes().add(appNote);

        con.close();
    }

    @Override
    public ObservableList<AppNote> getAll(App app) throws SQLException {
        Connection con =  DatabaseUtil.getConnection();
        ObservableList<AppNote> appNotes = FXCollections.observableArrayList();

        PreparedStatement stmt = con.prepareStatement("select * from appnote where app_id = ?");
        stmt.setInt(1, app.getId());
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            AppNote appNote = extractFromResultSet(rs);
            appNote.setApp(app);
            appNotes.add(appNote);
        }

        con.close();
        return appNotes;
    }

    @Override
    public void update(AppNote appNote) throws SQLException {
        Connection con =  DatabaseUtil.getConnection();

        PreparedStatement stmt = con.prepareStatement("update appnote set description = ?, value = ? where id = ?");
        stmt.setString(1, appNote.getDescription());
        stmt.setString(2, appNote.getValue());
        stmt.setInt(3, appNote.getId());
        stmt.execute();

        con.close();
    }

    @Override
    public void delete(AppNote appNote) throws SQLException {
        Connection con =  DatabaseUtil.getConnection();

        PreparedStatement stmt = con.prepareStatement("delete from appnote where id = ?");
        stmt.setInt(1, appNote.getId());
        stmt.execute();

        appNote.getApp().getAppNotes().remove(appNote);

        con.close();
    }

    private AppNote extractFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String description = rs.getString("description");
        String value = rs.getString("value");
        return new AppNote(id, description, value);
    }

    private int extractGeneratedKey(PreparedStatement stmt) throws SQLException {
        ResultSet rs = stmt.getGeneratedKeys();
        return rs.next() ? rs.getInt(1) : 0;
    }


}
