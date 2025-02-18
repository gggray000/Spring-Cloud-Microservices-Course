package com.apps.developerblog.app.ws.ui.controller;

import com.apps.developerblog.app.ws.ui.model.request.UserDetailRequestModel;
import com.apps.developerblog.app.ws.ui.model.response.UserRest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.*;

@RestController
@RequestMapping("users")
public class UserController {

    @GetMapping(path="/{userId}", produces = {MediaType.APPLICATION_XML_VALUE,
                                                MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<UserRest> getUser(@PathVariable String userId){
        UserRest returnedUser = new UserRest("John", "Doe", "test@test.com");
        return new ResponseEntity<UserRest>(returnedUser, HttpStatus.OK);
    }

    @GetMapping
    public String getUsers(@RequestParam(value="page", defaultValue = "1") int page,
                           @RequestParam(value="limit", defaultValue = "20") int limit,
                           @RequestParam(value = "sort", required = false) String sort){
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
    public UserRest createUser(@RequestBody UserDetailRequestModel userDetails){

        return new UserRest(userDetails.getFirstName(),
                            userDetails.getLastName(),
                            userDetails.getEmail()
        );
    }

    @PutMapping
    public String updateUser(){
        return "update user was called";
    }

    @DeleteMapping
    public String deleteUser(){
        return "delete user was called";
    }
}
