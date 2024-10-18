package com.corndel.nozama;

public record createUserRequest(String username, String firstName, String lastName, String email, String password, String avatar) {
  createUserRequest(String username, String firstName, String lastName, String email, String password) {
    this(username, firstName, lastName, email, password, null);
  }
}
