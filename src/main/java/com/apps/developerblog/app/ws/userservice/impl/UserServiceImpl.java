package com.apps.developerblog.app.ws.userservice.impl;

import com.apps.developerblog.app.ws.ui.model.request.UpdateUserDetailsModel;
import com.apps.developerblog.app.ws.ui.model.request.UserDetailRequestModel;
import com.apps.developerblog.app.ws.ui.model.response.UserRest;
import com.apps.developerblog.app.ws.userservice.UserService;
import com.apps.developerblog.app.ws.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    Map<String, UserRest> users;
    private final Utils utils;

    public UserServiceImpl(Utils utils){
        this.utils = utils;
    }

    /*Utils utils
    @Autowired
    public UserServiceImpl(Utils utils){
        this.utils = utils;
    }*/

    @Override
    public UserRest createUser(UserDetailRequestModel userDetails) {
        UserRest returnedUser = new UserRest(
                userDetails.getFirstName(),
                userDetails.getLastName(),
                userDetails.getEmail()
        );
        String userId = utils.generateUserId();
        returnedUser.setUserId(userId);
        if(users == null) users = new HashMap<>();
        users.put(userId, returnedUser);
        return returnedUser;
    }

    public Map<String, UserRest> getUsers() {
        return users;
    }

    @Override
    public UserRest updateUser(String userId, UpdateUserDetailsModel userDetails) {
        UserRest storedUserDetails = users.get(userId);
        storedUserDetails.setFirstName(userDetails.getFirstName());
        storedUserDetails.setLastName(userDetails.getLastName());
        users.put(userId, storedUserDetails);
        return storedUserDetails;
    }

    @Override
    public void removeUser(String userId) {
        users.remove(userId);
    }
}
