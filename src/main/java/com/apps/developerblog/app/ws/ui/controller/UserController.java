package com.apps.developerblog.app.ws.ui.controller;

import com.apps.developerblog.app.ws.ui.model.request.UpdateUserDetailsModel;
import com.apps.developerblog.app.ws.ui.model.request.UserDetailRequestModel;
import com.apps.developerblog.app.ws.ui.model.response.UserRest;
import com.apps.developerblog.app.ws.userservice.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userServiceImpl){
        this.userService = userServiceImpl;
    }

    /*UserService userService;
    @Autowired
    public UserController(UserService userServiceImpl){
        this.userService = userServiceImpl;
    }*/

    @GetMapping(path="/{userId}",
                produces = {MediaType.APPLICATION_XML_VALUE,
                            MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<UserRest> getUser(@PathVariable String userId){
        if(this.userService.getUsers().containsKey(userId)){
            return new ResponseEntity<>(this.userService.getUsers().get(userId), HttpStatus.OK);
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
    public ResponseEntity<UserRest> createUser(
            @Valid
            @RequestBody UserDetailRequestModel userDetails){
        UserRest returnedUser = userService.createUser(userDetails);
        return new ResponseEntity<>(returnedUser, HttpStatus.OK);
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
        return userService.updateUser(userId, userDetails);
    }

    @DeleteMapping(path="/{userId}")
    public ResponseEntity deleteUser(@PathVariable String userId){
        userService.removeUser(userId);
        return ResponseEntity.noContent().build();

    }
}
