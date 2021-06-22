package com.kutylo.task4_7.mapper;

import com.kutylo.task4_7.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper {

  private final String ID_COLUMN = "id";
  private final String USERNAME_COLUMN = "username";
  private final String PASSWORD_COLUMN = "password";
  private final String EMAIL_COLUMN = "email";
  private final String PHONE_COLUMN = "phone";
  private final String IMAGE_COLUMN = "image_url";

  public User mapToUserModel(ResultSet resultSet) throws SQLException {
    return User.newBuilder()
        .setId(resultSet.getInt(ID_COLUMN))
        .setUsername(resultSet.getString(USERNAME_COLUMN))
        .setPassword(resultSet.getString(PASSWORD_COLUMN))
        .setEmail(resultSet.getString(EMAIL_COLUMN))
        .setPhone(resultSet.getString(PHONE_COLUMN))
        .setImgUrl(resultSet.getString(IMAGE_COLUMN))
        .build();
  }
}
