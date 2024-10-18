package com.corndel.nozama.controllers;
import com.corndel.nozama.createUserRequest;
import com.corndel.nozama.models.User;
import com.corndel.nozama.repositories.UserRepository;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import java.sql.SQLException;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.NotFoundResponse;

public class UserController {
  public static void createUser(Context ctx) throws Exception {
    createUserRequest body = ctx.bodyAsClass(createUserRequest.class);

    boolean nullUsername = body.username() == null;
    boolean nullFirstName = body.firstName() == null;
    boolean nullLastName = body.lastName() == null;
    boolean nullEmail = body.email() == null;
    boolean nullPassword = body.password() == null;

    if (nullUsername || nullFirstName || nullLastName || nullEmail || nullPassword) {
      throw new BadRequestResponse("Invalid request body.");
    }

    try {
      User user = UserRepository.insertUser(body);
      ctx.status(201);
      ctx.json(user);
    } catch (Exception e) {
      System.err.println(e.getMessage());
      ctx.status(500);
    }
  }

  public static void deleteUser(Context ctx) {
    int userId = Integer.parseInt(ctx.pathParam("userId"));
    try {
      UserRepository.removeUser(userId);
      ctx.status(204);
    } catch (Exception e) {
        System.err.println(e.getMessage());
        throw new NotFoundResponse("User not found");

    }}


        public static void userLogIn (Context ctx){
            try {

                var logInRequest = ctx.bodyAsClass(UserRequest.class);
                var user = UserRepository.logUserIn(logInRequest.username(),
                        logInRequest.firstName(), logInRequest.lastName(), logInRequest.email(),
                        logInRequest.avatar(), logInRequest.password());

                ctx.json(user);
            } catch (Exception e) {
                // test
                System.err.println(e.getMessage());
            }
        }
    }


record UserRequest(String username, String firstName , String lastName, String email, String avatar, String password) { }