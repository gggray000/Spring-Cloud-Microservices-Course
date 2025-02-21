package com.appsdeveloperbolg.photoapp.api.users.ui.controllers;

import com.appsdeveloperbolg.photoapp.api.users.service.UsersService;
import com.appsdeveloperbolg.photoapp.api.users.shared.UserDto;
import com.appsdeveloperbolg.photoapp.api.users.ui.model.CreateUserRequestModel;
import com.appsdeveloperbolg.photoapp.api.users.ui.model.CreateUserResponseModel;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private Environment env;

    @Autowired
    UsersService usersService;

    @GetMapping("/status/check")
    public String status(){

        return "Users Service is Working on port "
                + env.getProperty("local.server.port");
    }

    @PostMapping
    public ResponseEntity<CreateUserResponseModel> createUser(@Valid @RequestBody CreateUserRequestModel userDetail){

        UserDto userDto =
                usersService.convertUserRequestModelToUserDto(userDetail);

        UserDto createdUser = usersService.createUser(userDto);

        CreateUserResponseModel responseModel = convertUserDtoToResponseModel(createdUser);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseModel);
    }

    private CreateUserResponseModel convertUserDtoToResponseModel(UserDto userDto){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper.map(userDto, CreateUserResponseModel.class);
    }

}

