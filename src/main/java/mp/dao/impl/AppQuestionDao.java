package mp.dao.impl;

import mp.dao.interfaces.IAppQuestionDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mp.models.App;
import mp.models.AppQuestion;
import mp.utils.DatabaseUtil;
import java.sql.*;

public class AppQuestionDao implements IAppQuestionDao {

    @Override
    public void add(AppQuestion appQuestion) throws SQLException {
        Connection con =  DatabaseUtil.getConnection();

        PreparedStatement stmt = con.prepareStatement("insert into appquestion(text, answer, app_id) values (?, ?, ?);", Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, appQuestion.getText());
        stmt.setString(2, appQuestion.getAnswer());
        stmt.setInt(3, appQuestion.getApp().getId());
        stmt.execute();

        appQuestion.setId(extractGeneratedKey(stmt));
        appQuestion.getApp().getAppQuestions().add(appQuestion);

        con.close();
    }

    @Override
    public ObservableList<AppQuestion> getAll(App app) throws SQLException {
        Connection con =  DatabaseUtil.getConnection();
        ObservableList<AppQuestion> appQuestions = FXCollections.observableArrayList();

        PreparedStatement stmt = con.prepareStatement("select * from appquestion where app_id = ?");
        stmt.setInt(1, app.getId());
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            AppQuestion appQuestion = extractFromResultSet(rs);
            appQuestion.setApp(app);
            appQuestions.add(appQuestion);
        }

        con.close();
        return appQuestions;
    }

    @Override
    public void update(AppQuestion appQuestion) throws SQLException {
        Connection con =  DatabaseUtil.getConnection();

        PreparedStatement stmt = con.prepareStatement("update appquestion set text = ?, answer = ? where id = ?");
        stmt.setString(1, appQuestion.getText());
        stmt.setString(2, appQuestion.getAnswer());
        stmt.setInt(3, appQuestion.getId());
        stmt.execute();

        con.close();
    }

    @Override
    public void delete(AppQuestion appQuestion) throws SQLException {
        Connection con =  DatabaseUtil.getConnection();

        PreparedStatement stmt = con.prepareStatement("delete from appquestion where id = ?");
        stmt.setInt(1, appQuestion.getId());
        stmt.execute();

        appQuestion.getApp().getAppQuestions().remove(appQuestion);

        con.close();
    }

    private AppQuestion extractFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String text = rs.getString("text");
        String answer = rs.getString("answer");
        return new AppQuestion(id, text, answer);
    }

    private int extractGeneratedKey(PreparedStatement stmt) throws SQLException {
        ResultSet rs = stmt.getGeneratedKeys();
        return rs.next() ? rs.getInt(1) : 0;
    }


}
