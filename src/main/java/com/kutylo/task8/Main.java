package com.kutylo.task8;

import com.kutylo.task4_7.config.ConnectionConfig;
import com.kutylo.task8.model.User;

import java.sql.Connection;

public class Main {
  public static void main(String[] args) throws Exception {
    Connection connection = new ConnectionConfig().getConnection();
    JabaTemplate template = JabaTemplate.create(User.class, connection);

    User user = User.newBuilder()
        .setId(9)
        .setPhone("0255246")
        .setImgUrl("imgUrl")
        .setEmail("test-templae@gmail")
        .setPassword("passdds")
        .setUsername("user23")
        .build();

    template.save(user);

    int updatedRows = template.update(user, User.newBuilder().setId(9)
        .setPhone("0255246")
        .setImgUrl("imgUrl")
        .setEmail("test-templae@gmail")
        .setPassword("passdds")
        .setUsername("user23update")
        .build());
    template.delete(user);
    User foundUser = (User) template.search(User.newBuilder().setId(9).build());
  }
}
