package com.kutylo.task4_7.repository;

import com.kutylo.task4_7.config.ConnectionConfig;
import com.kutylo.task4_7.mapper.UserCvMapper;
import com.kutylo.task4_7.model.UserCv;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserCvRepository {
  private ConnectionConfig config = new ConnectionConfig();
  private UserCvMapper userCvMapper = new UserCvMapper();

  private final String TABLE_NAME = "user_sv";
  private final String GET_CV_BY_USER_ID = "SELECT * FROM %s WHERE user_id = %d";
  private final String SAVE_USER_SV_QUERY = "call insert_sv(?,?,?)";

  public void insertUserSv(UserCv userCv) {
    Connection connection = config.getConnection();
    try (CallableStatement callableStatement = connection.prepareCall(SAVE_USER_SV_QUERY);
         FileInputStream inputStream = new FileInputStream(userCv.getFile())) {
      callableStatement.setInt(1, userCv.getId());
      callableStatement.setInt(2, userCv.getUser_id());
      callableStatement.setBinaryStream(3, inputStream, userCv.getFile().length());
      boolean rows = callableStatement.execute();

    } catch (SQLException | IOException e) {
      e.printStackTrace();
    }
  }

  public UserCv getSvByUserId(int id) {
    Connection connection = config.getConnection();
    try (Statement statement = connection.createStatement()) {
      ResultSet resultSet = statement.executeQuery(String.format(GET_CV_BY_USER_ID, TABLE_NAME, id));
      while (resultSet.next()) {
        return userCvMapper.mapToUserSvModel(resultSet);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

}
