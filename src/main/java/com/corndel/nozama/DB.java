package com.corndel.nozama;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import io.github.cdimascio.dotenv.Dotenv;

public class DB {
  public static final Dotenv dotenv = Dotenv.load();
  public static final String dbUrl = dotenv.get("DB_URL");


  public static Connection getConnection() throws SQLException {
    return DriverManager.getConnection(dbUrl);
  }
}
