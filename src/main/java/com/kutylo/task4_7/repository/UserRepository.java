package com.kutylo.task4_7.repository;

import com.kutylo.task4_7.config.ConnectionConfig;
import com.kutylo.task4_7.mapper.UserMapper;
import com.kutylo.task4_7.model.User;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
  private ConnectionConfig config = new ConnectionConfig();
  private UserMapper mapper = new UserMapper();

  private final String TABLE_NAME = "user_account";
  private final String GET_ALL_USER_QUERY = "SELECT * FROM %s";
  private final String SAVE_USER_QUERY = "INSERT INTO %s VALUES(?,?,?,?,?,?)";
  private final String UPDATE_USER_QUERY = "call update_user(?,?,?,?,?,?)";
  private final String DELETE_USER_QUERY = "DELETE FROM %S WHERE id = %d";
  private final String GET_ALL_PROCEDURES = "select * from pg_proc as p\n" +
      "join pg_namespace n on p.pronamespace = n.oid\n" +
      "where n.nspname not in ('pg_catalog', 'information_schema')\n" +
      "and p.prokind = 'p'";
  private final String DROP_PROCEDURE = "DROP PROCEDURE %s;";

  public List<User> getAllUser() {
    Connection connection = config.getConnection();
    List<User> users = new ArrayList<>();

    try (Statement statement = connection.createStatement()) {
      ResultSet resultSet = statement.executeQuery(String.format(GET_ALL_USER_QUERY, TABLE_NAME));
      while (resultSet.next()) {
        users.add(mapper.mapToUserModel(resultSet));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return users;
  }

  public User saveUser(User user) {
    Connection connection = config.getConnection();
    try (PreparedStatement preparedStatement = connection.prepareStatement(String.format(SAVE_USER_QUERY, TABLE_NAME))) {
      preparedStatement.setInt(1, user.getId());
      preparedStatement.setString(2, user.getEmail());
      preparedStatement.setString(3, user.getImgUrl());
      preparedStatement.setString(4, user.getPassword());
      preparedStatement.setString(5, user.getPhone());
      preparedStatement.setString(6, user.getUsername());
      if (preparedStatement.execute()) {
        return user;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public User updateUser(User user) {
    Connection connection = config.getConnection();
    try (CallableStatement callableStatement = connection.prepareCall(UPDATE_USER_QUERY)) {
      callableStatement.setInt(1, user.getId());
      callableStatement.setString(2, user.getEmail());
      callableStatement.setString(3, user.getUsername());
      callableStatement.setString(4, user.getPhone());
      callableStatement.setString(5, user.getImgUrl());
      callableStatement.setString(6, user.getPassword());
      boolean rows = callableStatement.execute();

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return user;
  }

  public void deleteUserById(int id) {
    Connection connection = config.getConnection();
    try (Statement statement = connection.createStatement()) {
      boolean resultSet = statement.execute(String.format(DELETE_USER_QUERY, TABLE_NAME, id));
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public List<String> getAllStoredProcedures() {
    Connection connection = config.getConnection();
    List<String> proceduresName = new ArrayList<>();

    try (Statement statement = connection.createStatement()) {
      ResultSet resultSet = statement.executeQuery(String.format(GET_ALL_PROCEDURES));
      while (resultSet.next()) {
        proceduresName.add(resultSet.getString("proname"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return proceduresName;
  }

  public boolean dropProcedure(String procedureName) {
    Connection connection = config.getConnection();

    try (Statement statement = connection.createStatement()) {
      return statement.execute((String.format(DROP_PROCEDURE, procedureName)));

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }
}
