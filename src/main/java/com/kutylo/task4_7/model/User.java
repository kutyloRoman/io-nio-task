package com.kutylo.task4_7.model;

public class User {
  private int id;
  private String username;
  private String password;
  private String email;
  private String phone;
  private String imgUrl;

  public User() {
  }

  public int getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public String getEmail() {
    return email;
  }

  public String getPhone() {
    return phone;
  }

  public String getImgUrl() {
    return imgUrl;
  }

  public static Builder newBuilder() {
    return new User().new Builder();
  }


  public class Builder {
    private Builder() {
    }

    public Builder setId(int id) {
      User.this.id = id;
      return this;
    }

    public Builder setUsername(String username) {
      User.this.username = username;
      return this;
    }

    public Builder setPassword(String password) {
      User.this.password = password;
      return this;
    }

    public Builder setEmail(String email) {
      User.this.email = email;
      return this;
    }

    public Builder setPhone(String phone) {
      User.this.phone = phone;
      return this;
    }

    public Builder setImgUrl(String imgUrl) {
      User.this.imgUrl = imgUrl;
      return this;
    }

    public User build() {
      return User.this;
    }
  }
}
