package com.kutylo.task4_7;

import com.kutylo.task4_7.model.User;
import com.kutylo.task4_7.repository.UserCvRepository;
import com.kutylo.task4_7.repository.UserRepository;

import java.sql.SQLException;

public class Main {
  public static void main(String[] args) throws SQLException {
    UserRepository repository = new UserRepository();
    UserCvRepository userCvRepository = new UserCvRepository();
    //List<User> users = repository.getAllUser();
    //repository.deleteUserById(4);

    User user = User.newBuilder()
        .setId(8)
        .setPhone("phone")
        .setImgUrl("imgUrl")
        .setEmail("rmail@gmail")
        .setPassword("pass")
        .setUsername("user")
        .build();

//    UserCv userCv = new UserCv();
//    userCv.setUser_id(8);
//    userCv.setId(1);
//
//    userCv.setFile(new File("D:\\Mentorship\\user_cv.txt"));

    //userCvRepository.insertUserSv(userSv);
    //userCvRepository.getSvByUserId(8);
    repository.getAllStoredProcedures();
  repository.dropProcedure("uinsert_sv");
  }
}
