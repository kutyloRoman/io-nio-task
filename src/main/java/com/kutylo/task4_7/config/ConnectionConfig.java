package com.kutylo.task4_7.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionConfig {
  Connection dbConnection;

  private final String DB_USER = "hbfytnzcpsvvsj";
  private final String DB_PASSWORD = "2102dd1dbd0cc69822699a75a0e1b9e06566fb2d4bd35c04c94f65a01402d7f5";
  private final String CONNECTION_URL = "jdbc:postgresql://ec2-34-240-75-196.eu-west-1.compute.amazonaws.com:5432/dc15s0tpcukaul";

  public Connection getConnection() {
    try {
      dbConnection = DriverManager.getConnection(CONNECTION_URL, DB_USER, DB_PASSWORD);
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return dbConnection;
  }

}
