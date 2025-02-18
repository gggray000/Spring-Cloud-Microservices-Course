package com.apps.developerblog.app.ws.ui.controller;

import com.apps.developerblog.app.ws.exceptions.UserServiceException;
import com.apps.developerblog.app.ws.ui.model.request.UpdateUserDetailsModel;
import com.apps.developerblog.app.ws.ui.model.request.UserDetailRequestModel;
import com.apps.developerblog.app.ws.ui.model.response.UserRest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("users")
public class UserController {

    Map<String, UserRest> users;

    @GetMapping(path="/{userId}",
                produces = {MediaType.APPLICATION_XML_VALUE,
                            MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<UserRest> getUser(@PathVariable String userId){
        if(users.containsKey(userId)){
            return new ResponseEntity<>(users.get(userId), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping
    public String getUsers(@RequestParam(value="page", defaultValue = "1") int page,
                           @RequestParam(value="limit", defaultValue = "20") int limit,
                           @RequestParam(value = "sort", required = false) String sort){
        String name = null;
        int length = name.length();

        //throw new UserServiceException("A UserServiceException is thrown");

        return "get users was called with page = " + page + ", limit = " + limit + ", sort = " + sort;
    }

    @PostMapping(consumes = {
            MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_JSON_VALUE
    },
            produces = {
            MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_JSON_VALUE
    })
    public UserRest createUser(
            @Valid
            @RequestBody UserDetailRequestModel userDetails){
        UserRest returnedUser = new UserRest(userDetails.getFirstName(),
                userDetails.getLastName(),
                userDetails.getEmail());
        String userId = UUID.randomUUID().toString();
        returnedUser.setUserId(userId);
        if(users == null) users = new HashMap<>();
        users.put(userId, returnedUser);
        return returnedUser;
    }

    @PutMapping(path="/{userId}",
                consumes = {
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
    },
                produces = {
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            })
    public UserRest updateUser(@PathVariable String userId,
                             @Valid
                             @RequestBody UpdateUserDetailsModel userDetails){
        UserRest storedUserDetails = users.get(userId);
        storedUserDetails.setFirstName(userDetails.getFirstName());
        storedUserDetails.setLastName(userDetails.getLastName());
        users.put(userId, storedUserDetails);
        return storedUserDetails;
    }

    @DeleteMapping(path="/{userId}")
    public ResponseEntity deleteUser(@PathVariable String userId){
        users.remove(userId);
        return ResponseEntity.noContent().build();

    }
}
