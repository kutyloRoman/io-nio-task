package com.kutylo.task4_7.model;

import java.io.File;

public class UserCv {
  private int id;
  private int user_id;
  private File file;

  public int getId() {
    return id;
  }

  public int getUser_id() {
    return user_id;
  }

  public File getFile() {
    return file;
  }

  public static Builder newBuilder() {
    return new UserCv().new Builder();
  }

  public class Builder {
    private Builder() {
    }

    public Builder setId(int id) {
      UserCv.this.id = id;
      return this;
    }

    public Builder setUser_id(int user_id) {
      UserCv.this.user_id = user_id;
      return this;
    }

    public Builder setFile(File file) {
      UserCv.this.file = file;
      return this;
    }

    public UserCv build() {
      return UserCv.this;
    }

  }
}
