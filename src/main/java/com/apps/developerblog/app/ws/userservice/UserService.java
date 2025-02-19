package com.apps.developerblog.app.ws.userservice;

import com.apps.developerblog.app.ws.ui.model.request.UpdateUserDetailsModel;
import com.apps.developerblog.app.ws.ui.model.request.UserDetailRequestModel;
import com.apps.developerblog.app.ws.ui.model.response.UserRest;

import java.util.Map;

public interface UserService {
   
   UserRest createUser(UserDetailRequestModel userDetail);

   Map<String, UserRest> getUsers();

   UserRest updateUser(String userId, UpdateUserDetailsModel userDetails);

   void removeUser(String userId);

}
