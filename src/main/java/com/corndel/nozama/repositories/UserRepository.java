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

  public static User insertUser(createUserRequest accountDetails) throws SQLException {
    String username = accountDetails.username();
    String firstName = accountDetails.firstName();
    String lastName = accountDetails.lastName();
    String email = accountDetails.email();
    String password = accountDetails.password();

    String insertQuery = "INSERT INTO users (username, firstName, lastName, email, password) VALUES (?, ?, ?, ?, ?) RETURNING *";

    try (Connection con = DB.getConnection();
         PreparedStatement insertStmt = con.prepareStatement(insertQuery)) {

      insertStmt.setString(1, username);
      insertStmt.setString(2, firstName);
      insertStmt.setString(3, lastName);
      insertStmt.setString(4, email);
      insertStmt.setString(5, password);

      try (ResultSet insertRs = insertStmt.executeQuery()) {

        insertRs.next();
        int id = insertRs.getInt("id");

        if (accountDetails.avatar() != null) {
          String avatar = accountDetails.avatar();
          String updateQuery = "UPDATE users SET avatar = ? WHERE id = ? RETURNING *";

          try (PreparedStatement updateStmt = con.prepareStatement(updateQuery)) {

            updateStmt.setString(1, avatar);
            updateStmt.setInt(2, id);

            try (ResultSet updateRs = updateStmt.executeQuery()) {
              updateRs.next();
              return new User(id, username, firstName, lastName, email, avatar);
            }
          }
        } else {
          return new User(id, username, firstName, lastName, email, null);
        }
      }
    }
  }

  // Find all users
  public static List<User> findAll() throws SQLException {
    String query = "SELECT id, username, firstName, lastName, email, avatar FROM users";

    try (var con = DB.getConnection();
         var stmt = con.createStatement();
         var rs = stmt.executeQuery(query)) {

      List<User> users = new ArrayList<>();
      while (rs.next()) {
        int id = rs.getInt("id");
        String username = rs.getString("username");
        String firstName = rs.getString("firstName");
        String lastName = rs.getString("lastName");
        String email = rs.getString("email");
        String avatar = rs.getString("avatar");

        users.add(new User(id, username, firstName, lastName, email, avatar));
      }
      return users;
    }
  }

  // Find user by ID
  public static User findById(int id) throws SQLException {
    String query = "SELECT id, username, firstName, lastName, email, avatar FROM users WHERE id = ?";

    try (var con = DB.getConnection();
         var stmt = con.prepareStatement(query)) {

      stmt.setInt(1, id);
      try (var rs = stmt.executeQuery()) {
        if (rs.next()) {
          String username = rs.getString("username");
          String firstName = rs.getString("firstName");
          String lastName = rs.getString("lastName");
          String email = rs.getString("email");
          String avatar = rs.getString("avatar");

          return new User(id, username, firstName, lastName, email, avatar);
        } else {
          return null;
        }
      }
    }
  }

  // Log in a user
  public static LoginResponse logUserIn(String username, String firstname, String lastname, String mail, String avatar, String password) throws SQLException, Exception {
    String query = "SELECT id, username, firstName, lastName, email, avatar, password FROM users WHERE username = ?";

    try (var con = DB.getConnection();
         var stmt = con.prepareStatement(query)) {

      stmt.setString(1, username);
      try (var rs = stmt.executeQuery()) {
        if (rs.next()) {
          String usernameDB = rs.getString("username");
          String firstName = rs.getString("firstName");
          String lastName = rs.getString("lastName");
          String email = rs.getString("email");
          String avatarDB = rs.getString("avatar");
          String passwordDB = rs.getString("password");

          if (passwordDB.equals(password)) {
            System.out.println("Passwords match, logging in");
            return new LoginResponse(username, password);
          } else {
            throw new Exception("Passwords do not match, cannot log in");
          }
        } else {
          System.out.println("No user found to log in");
          return null;
        }
      }
    }
  }

  // Remove a user by ID
  public static void removeUser(int id) throws SQLException, Exception {
    String findQuery = "SELECT id, username FROM users WHERE id = ?";

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
  }

  // Login response record
  public static record LoginResponse(String username, String password) {
  }
}




