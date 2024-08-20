package com.academy.project.SomeProject.controllers;

import com.academy.project.SomeProject.model.User;
import com.academy.project.SomeProject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/users")
@RestController
    public class UserController {

    @Autowired
    private UserService service;

    // Create/add user in  our database

    @PostMapping("/add")
    public ResponseEntity<Optional<User>> create(@RequestBody User user){
        try {
            Optional<User> createdUser = Optional.ofNullable(service.add(user));
            if (createdUser.isEmpty()){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            return ResponseEntity.ok(createdUser);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get user by id if exists

    @GetMapping("/get/{id}")
    public ResponseEntity<User> getById(@PathVariable("id") Long id){
            Optional<User> usersId = service.findById(id);
            return usersId.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Validation and updating existing user details in our database

    @PostMapping("/update")
    public ResponseEntity<String> updateUser(@RequestBody User user){
        try {
           Optional<User> existingUser = service.findById(user.getId());
            if (user.getId() == null || user.getId() <= 0){
                return ResponseEntity.badRequest().body("Invalid id provided");
            }
            if (existingUser.isPresent()){
                User existing = existingUser.get();
                existing.setUsername(user.getUsername());
                existing.setPassword(user.getPassword());
                existing.setEmail(user.getEmail());

                service.add(existing);
                return ResponseEntity.ok("User updated successfully");
            } else {

                throw new IllegalArgumentException("User not found");
            }
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /*
    Get user by username if exist validation.
    If user is not found or its not created yet,
    we receive bad request or not found
     */

    @GetMapping("/username")
    public ResponseEntity<List<User>> getByUsername(@RequestParam(value = "username" , required = false) String username){
        try {
            List<User> userByName = service.getByUsername(username);
            if (username == null || username.isEmpty()){
                return  ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
            return userByName.isEmpty() ?
                    ResponseEntity.status(HttpStatus.NOT_FOUND).build() :
                    ResponseEntity.ok(userByName);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get all users with details from our database

    @GetMapping("/get")
    public ResponseEntity<List<User>> getAll(){
        try {
          List<User> userList = service.findAll();
            return ResponseEntity.ok(userList);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Deleting all of our users from the database

    @DeleteMapping("/deleteAll")
    public ResponseEntity<List<User>> deleteAll(){
    try{
        List<User> deleteUsers = service.deleteAll();
        return ResponseEntity.ok(deleteUsers);
    } catch (Exception e){
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    }

    // Delete a user from the database by user id

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id){
        boolean deleted = service.deleteById(id);

        if (deleted){
            return ResponseEntity.ok("User deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }
}