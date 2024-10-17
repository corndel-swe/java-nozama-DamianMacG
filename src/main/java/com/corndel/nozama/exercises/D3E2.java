package com.corndel.nozama.exercises;

import io.javalin.http.BadRequestResponse;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.UnauthorizedResponse;

public class D3E2 {

  public class Account {
    public String username;
    public String email;

    @SuppressWarnings("unused")
    private String password;

    public Account(String username, String email, String password) {
      this.username = username;
      this.email = email;
      this.password = password;
    }

    /**
     * Updates the username of the account.
     *
     * <p>
     * This method performs validations and throws Javalin custom exceptions
     * if any of the checks fail.
     *
     * @see https://tech-docs.corndel.com/javalin/sending-errors.html
     * @param newUsername     the new username for the account
     * @param passwordAttempt the password to check against
     * @throws Exception if any of the checks fail
     */
    public void updateUsername(String newUsername, String passwordAttempt) throws Exception {
      // TODO: If newUsername is not given, throw a BadRequestResponse
      if(newUsername == null) {
        throw new BadRequestResponse("Please enter a userName");
      }

      // TODO: If passwordAttempt is not given, throw an UnauthorizedResponse
      if(passwordAttempt == null) {
        throw new UnauthorizedResponse();
      }
      // TODO: If passwordAttempt is given but not correct, throw a ForbiddenResponse
      if(passwordAttempt != password) {
      throw new ForbiddenResponse();
      }

      // TODO: If newUsername is given and passwordAttempt is correct, update the username


        this.username = newUsername;

    }
  }
}
