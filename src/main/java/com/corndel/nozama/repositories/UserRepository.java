package com.corndel.nozama.repositories;

import com.corndel.nozama.DB;
import com.corndel.nozama.createUserRequest;
import com.corndel.nozama.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
  public static List<User> findAll() throws SQLException {
    var query = "SELECT id, username, firstName, lastName, email, avatar FROM users";

    try (var con = DB.getConnection();
         var stmt = con.createStatement();
         var rs = stmt.executeQuery(query);) {

      var users = new ArrayList<User>();
      while (rs.next()) {
        var id = rs.getInt("id");
        var username = rs.getString("username");
        var firstName = rs.getString("firstName");
        var lastName = rs.getString("lastName");
        var email = rs.getString("email");
        var avatar = rs.getString("avatar");

        users.add(new User(id, username, firstName, lastName, email, avatar));
      }

      return users;
    }
  }

  public static User findById(int id) throws SQLException {
    var query = "SELECT id, username, firstName, lastName, email, avatar FROM users WHERE id = ?";

    try (var con = DB.getConnection();
         var stmt = con.prepareStatement(query)) {

      stmt.setInt(1, id);
      try (var rs = stmt.executeQuery()) {
        if (rs.next()) {
          var username = rs.getString("username");
          var firstName = rs.getString("firstName");
          var lastName = rs.getString("lastName");
          var email = rs.getString("email");
          var avatar = rs.getString("avatar");

          return new User(id, username, firstName, lastName, email, avatar);
        } else {
          return null;
        }
      }
    }
  }

  public static LoginResponse logUserIn(String username, String firstname , String lastname, String mail, String Avatar, String password) throws SQLException, Exception {
    // Check if user is null

    var query = "SELECT id, username, firstName, lastName, email, avatar, password FROM users WHERE username = ?";

    try (var con = DB.getConnection();
         var stmt = con.prepareStatement(query)) {

      stmt.setString(1, username);
      try (var rs = stmt.executeQuery()) {
        if (rs.next()) {
          var usernameDB = rs.getString("username");
          var firstName = rs.getString("firstName");
          var lastName = rs.getString("lastName");
          var email = rs.getString("email");
          var avatar = rs.getString("avatar");
          var passwordDB = rs.getString("password");
          System.out.println(passwordDB);

          if (passwordDB.equals(password)) {
            System.out.println("password are a match, Logging in");
            return new LoginResponse(username,password);
          }
          else {
            throw new Exception("password are not a match, cant log in");

       
      }
    }
  }
    }

  public static void removeUser(int id) throws SQLException, Exception {
    String findQuery = "SELECT id, username from users WHERE id = ?";

    try (Connection connection = DB.getConnection();
         PreparedStatement findStmt = connection.prepareStatement(findQuery)) {

      findStmt.setInt(1, id);

      try (ResultSet findRs = findStmt.executeQuery()) {

        if (!findRs.next()) {
          throw new Exception("User not found. Can't remove what doesn't exist.");
        }

        String deleteQuery = "DELETE FROM users WHERE id = ?";

        try (PreparedStatement deleteStmt = connection.prepareStatement(deleteQuery)) {
          deleteStmt.setInt(1, id);
          deleteStmt.executeUpdate();
        }
      }
    }

    System.out.println("No user found to log in");
    return null;
  }

  record LoginResponse(String username, String password) { }

