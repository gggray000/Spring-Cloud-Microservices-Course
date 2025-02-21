package com.appsdeveloperbolg.photoapp.api.users.service;

import com.appsdeveloperbolg.photoapp.api.users.shared.UserDto;
import com.appsdeveloperbolg.photoapp.api.users.ui.model.CreateUserRequestModel;

public interface UsersService {
    UserDto createUser(UserDto userDetails);
    UserDto convertUserRequestModelToUserDto(CreateUserRequestModel userDetails);
}
