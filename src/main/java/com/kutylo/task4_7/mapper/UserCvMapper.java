package com.kutylo.task4_7.mapper;

import com.kutylo.task4_7.model.UserCv;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserCvMapper {

  private final String ID_COLUMN = "id";
  private final String USER_ID_COLUMN = "user_id";
  private final String CV_COLUMN = "cv";

  private final String CV_OUTPUT_FILE_PATH = "D:\\cv.txt";

  public UserCv mapToUserSvModel(ResultSet resultSet) throws SQLException {
    return UserCv.newBuilder()
        .setId(resultSet.getInt(ID_COLUMN))
        .setUser_id(resultSet.getInt(USER_ID_COLUMN))
        .setFile(mapBytesToFile(resultSet.getBytes(CV_COLUMN)))
        .build();
  }

  private File mapBytesToFile(byte[] cvBytesArray) {
    File file = new File(CV_OUTPUT_FILE_PATH);
    try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
      fileOutputStream.write(cvBytesArray);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return file;
  }
}
